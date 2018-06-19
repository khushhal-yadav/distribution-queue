package com.khush.distributedqueue.sharedmemorymapqueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.AbstractQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by khush on 17/06/2018.
 */
public class SharedMemoryMappedQueue {

    private static final Logger log = LoggerFactory.getLogger(SharedMemoryMappedQueue.class);

    //ConcurrentLinkedQueue(CAS based) to support multiple concurrent connections
    //otherwise can use ArrayDequeue for single connectivity
    private final AbstractQueue<Integer> inQueue;
    private final AbstractQueue<Integer> outQueue;

    //private final DataTransformer<String, R> transformer;

    private final File inQueueFile;
    private final File outQueueFile;

    public SharedMemoryMappedQueue(final File inQueueFile, final File outQueueFile)
            throws IOException {
        inQueueFile.createNewFile();
        outQueueFile.createNewFile();
        this.inQueueFile = inQueueFile;
        this.outQueueFile = outQueueFile;
        this.inQueue = new ConcurrentLinkedQueue<>();
        this.outQueue = new ConcurrentLinkedQueue<>();
    }

    public void offer(Integer e) {
        this.inQueue.offer(e);
    }

    public Integer poll() {
        return this.outQueue.poll();
    }

    public void start() {
        startInQueueWriter();
        startResultQueueReader();
        log.info("Shared File Based Queue started");
    }


    public void clear() {
        this.inQueue.clear();
        this.outQueue.clear();
        //log.info("Deleted input Queue file: {}", this.inQueueFile.delete());
        //log.info("Deleted result queue file: {}", this.outQueueFile.delete());
    }

    private void startInQueueWriter() {
        new Thread(() -> {
            try (
                    RandomAccessFile aFile = new RandomAccessFile(inQueueFile, "rw");
                    FileChannel inChannel = aFile.getChannel()
            ) {
                ByteBuffer buf = ByteBuffer.allocate(2);
                while (true) {
                    final Integer input = this.inQueue.poll();
                    if (input == null)
                        continue;
                    buf.clear();
                    buf.putShort((short) input.intValue());
                    buf.flip();
                    while (buf.hasRemaining()) {
                        inChannel.write(buf);
                    }
                }
            } catch (IOException e) {
                clear();
                throw new RuntimeException(e);
            }
        }).start();

    }

    private void startResultQueueReader() {
        new Thread(() -> {
            try (
                    RandomAccessFile aFile = new RandomAccessFile(outQueueFile, "r");
                    FileChannel inChannel = aFile.getChannel()
            ) {
                ByteBuffer buffer = ByteBuffer.allocate(2);
                int bytesRead = inChannel.read(buffer);

                byte[] bytes;

                while (bytesRead > 0) {
                    buffer.flip();
                    bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    log.info("{}", (int) bytes[1]);
                    buffer.compact();
                    bytesRead = inChannel.read(buffer);
                }
            } catch (IOException e) {
                clear();
                throw new RuntimeException(e);
            }
        }).start();
    }

}
