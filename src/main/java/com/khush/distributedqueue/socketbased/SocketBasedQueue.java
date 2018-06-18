package com.khush.distributedqueue.socketbased;

import com.khush.distributedqueue.DistributedQueue;
import com.khush.distributedqueue.socketbased.handler.ConnectionLoggingHandler;
import com.khush.distributedqueue.socketbased.handler.ExecutorServiceHandler;
import com.khush.distributedqueue.socketbased.handler.Handler;
import com.khush.distributedqueue.socketbased.handler.ProcessingHandler;
import com.khush.distributedqueue.socketbased.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.AbstractQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;

/**
 * Created by khush on 17/06/2018.
 */
public class SocketBasedQueue<I extends Serializable, R extends Serializable>
        implements DistributedQueue<I, R> {

    private static final Logger log = LoggerFactory.getLogger(SocketBasedQueue.class);

    //ConcurrentLinkedQueue(CAS based) to support multiple concurrent connections
    //otherwise can use ArrayDequeue for single connectivity
    private final AbstractQueue<I> inQueue;
    private final AbstractQueue<R> outQueue;
    private final Transformer<I, R> transformer;
    private final SocketQueueRunnable socketQueueRunnable;

    public SocketBasedQueue(final Transformer<I, R> transformer,
                            final int socketPort) {
        this.transformer = transformer;
        inQueue = new ConcurrentLinkedQueue<>();
        outQueue = new ConcurrentLinkedQueue<>();
        socketQueueRunnable = new SocketQueueRunnable(socketPort);
    }

    @Override
    public void offer(I e) {
        inQueue.offer(e);
    }

    @Override
    public R poll() {
        return outQueue.poll();
    }

    @Override
    public void start() {
        log.info("Starting Socket Based Queue");
        new Thread(socketQueueRunnable).start();
    }

    private class SocketQueueRunnable implements Runnable {

        private final int socketPort;

        public SocketQueueRunnable(final int socketPort) {
            this.socketPort = socketPort;
        }

        @Override
        public void run() {
            Socket socket;
            try (ServerSocket serverSocket = new ServerSocket(socketPort)) {
                while(true) {
                    socket = serverSocket.accept();
                    handle(socket);
                }
            } catch (IOException e) {
                log.error("Exception occurred while handling socket request {}", e);
                throw new RuntimeException(e);
            }
        }

        private void handle(Socket socket) throws IOException {
            Handler<Socket> handler =
                    new ExecutorServiceHandler<>(
                            new ConnectionLoggingHandler<>(
                                    new ProcessingHandler<>(inQueue, outQueue, transformer)
                            ),
                            Executors.newFixedThreadPool(10),
                            (thread, exception) -> log.error("Exception occurred from Executor pool {}", exception)
                    );

            handler.handle(socket);
        }
    }
}
