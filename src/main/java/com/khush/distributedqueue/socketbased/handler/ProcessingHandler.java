package com.khush.distributedqueue.socketbased.handler;

import com.khush.distributedqueue.socketbased.transformer.Transformer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.AbstractQueue;

/**
 * Created by khush on 17/06/2018.
 */
public class ProcessingHandler<P, R> implements Handler<Socket> {

    private final AbstractQueue<P> inputQueue;
    private final AbstractQueue<R> outputQueue;
    private final Transformer<P, R> transformer;

    public ProcessingHandler(final AbstractQueue<P> inputQueue,
                             final AbstractQueue<R> outputQueue,
                             final Transformer<P, R> transformer) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
        this.transformer = transformer;
    }

    @Override
    public void handle(Socket s) throws IOException {
        try (
                Socket socket = s;
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
        ) {
            while (true) {
                P input = inputQueue.poll();
                if (input == null)
                    continue;
                R result = transformer.transform(outputStream, inputStream, input);
                outputQueue.offer(result);
            }
        }
    }
}
