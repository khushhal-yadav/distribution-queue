package com.khush.distributedqueue.sharedfilebased;

import com.khush.distributedqueue.SharedQueue;
import com.khush.distributedqueue.sharedfilebased.transformer.DataTransformer;
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
public class SharedFileBasedQueue<I extends Serializable, R extends Serializable> implements SharedQueue<I, R> {

    private static final Logger log = LoggerFactory.getLogger(SharedFileBasedQueue.class);

    //ConcurrentLinkedQueue(CAS based) to support multiple concurrent connections
    //otherwise can use ArrayDequeue for single connectivity
    private final AbstractQueue<I> inputInQueue;
    private final AbstractQueue<I> inputOutQueue;
    private final AbstractQueue<R> resultInQueue;
    private final AbstractQueue<R> resultOutQueue;

    private final DataTransformer<String, I> inputTransformer;
    private final DataTransformer<String, R> outputTransformer;

    private final File inQueueFile;
    private final File resultQueueFile;

    public SharedFileBasedQueue(final File inQueueFile, final File resultQueueFile, final DataTransformer<String, I> inputTransformer,
                                final DataTransformer<String, R> outputTransformer)
            throws IOException {
        inQueueFile.createNewFile();
        resultQueueFile.createNewFile();
        this.inQueueFile = inQueueFile;
        this.resultQueueFile = resultQueueFile;
        this.inputInQueue = new ConcurrentLinkedQueue<>();
        this.inputOutQueue = new ConcurrentLinkedQueue<>();
        this.resultInQueue = new ConcurrentLinkedQueue<>();
        this.resultOutQueue = new ConcurrentLinkedQueue<>();
        this.inputTransformer = inputTransformer;
        this.outputTransformer = outputTransformer;
    }

    public void inputOffer(I e) {
        this.inputInQueue.offer(e);
    }

    public I inputPoll() {
        return this.inputOutQueue.poll();
    }

    public void resultOffer(R e) {
        this.resultInQueue.offer(e);
    }

    public R resultPoll() {
        return this.resultOutQueue.poll();
    }

    public void start() {
        startInQueueWriter();
        startInQueueReader();
        startResultQueueWriter();
        startResultQueueReader();
        log.info("Shared File Based Queue started");
    }


    public void clear() {
        this.inputInQueue.clear();
        this.inputOutQueue.clear();
        this.resultInQueue.clear();
        this.resultOutQueue.clear();
        //log.info("Deleted input Queue file: {}", this.inQueueFile.delete());
        //log.info("Deleted result queue file: {}", this.resultQueueFile.delete());
    }

    private void startInQueueWriter() {
        new Thread(() -> {
            try (
                    FileWriter fileWriter = new FileWriter(this.inQueueFile);
                    PrintWriter writer = new PrintWriter(fileWriter)
            ) {
                while (true) {
                    final I input = this.inputInQueue.poll();
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

    private void startInQueueReader() {
        new Thread(() -> {
            try (
                    FileReader fileReader = new FileReader(this.inQueueFile);
                    BufferedReader br = new BufferedReader(fileReader)
            ) {
                while (true) {
                    for (String line; (line = br.readLine()) != null && !line.trim().isEmpty(); ) {
                        this.inputOutQueue.offer(inputTransformer.transform(line));
                    }
                }
            } catch (IOException e) {
                clear();
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void startResultQueueWriter() {
        new Thread(() -> {
            try (
                    FileWriter fileWriter = new FileWriter(this.resultQueueFile);
                    PrintWriter writer = new PrintWriter(fileWriter)
            ) {
                while (true) {
                    final R input = this.resultInQueue.poll();
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
                    FileReader fileReader = new FileReader(this.resultQueueFile);
                    BufferedReader br = new BufferedReader(fileReader)
            ) {
                while (true) {
                    for (String line; (line = br.readLine()) != null && !line.trim().isEmpty(); ) {
                        this.resultOutQueue.offer(outputTransformer.transform(line));
                    }
                }
            } catch (IOException e) {
                clear();
                throw new RuntimeException(e);
            }
        }).start();
    }

}
