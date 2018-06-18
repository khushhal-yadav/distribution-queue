package com.khush.distributedqueue.sharedmediabased;

import com.khush.distributedqueue.DistributedQueue;
import com.khush.distributedqueue.transformer.DataTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by khush on 17/06/2018.
 */
public class SharedMediaBasedQueue<I extends Serializable, R extends Serializable> implements DistributedQueue<I, R> {

    private static final Logger log = LoggerFactory.getLogger(SharedMediaBasedQueue.class);

    //ConcurrentLinkedQueue(CAS based) to support multiple concurrent connections
    //otherwise can use ArrayDequeue for single connectivity
    private final AbstractQueue<I> inQueue;
    private final AbstractQueue<R> outQueue;

    private final DataTransformer<String, R> transformer;

    private final File inQueueFile;
    private final File outQueueFile;

    public SharedMediaBasedQueue(final File inQueueFile, final File outQueueFile, final DataTransformer<String, R> transformer)
            throws IOException {
        inQueueFile.createNewFile();
        outQueueFile.createNewFile();
        this.inQueueFile = inQueueFile;
        this.outQueueFile = outQueueFile;
        this.inQueue = new ConcurrentLinkedQueue<>();
        this.outQueue = new ConcurrentLinkedQueue<>();
        this.transformer = transformer;
    }

    public void offer(I e) {
        this.inQueue.offer(e);
    }

    public R poll() {
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
                    FileWriter fileWriter = new FileWriter(this.inQueueFile);
                    PrintWriter writer = new PrintWriter(fileWriter)
            ) {
                while (true) {
                    final I input = this.inQueue.poll();
                    if (input == null)
                        continue;
                    writer.println(input);
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
                    FileReader fileReader = new FileReader(this.outQueueFile);
                    BufferedReader br = new BufferedReader(fileReader)
            ) {
                while (true) {
                    for (String line; (line = br.readLine()) != null && !line.trim().isEmpty(); ) {
                        this.outQueue.offer(transformer.transform(line));
                    }
                }
            } catch (IOException e) {
                clear();
                throw new RuntimeException(e);
            }
        }).start();
    }

}
