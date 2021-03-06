package com.khush;

import com.khush.checker.sharedmediabased.PrimeNumberChecker;
import com.khush.distributedqueue.DistributedQueue;
import com.khush.distributedqueue.message.PrimeResult;
import com.khush.distributedqueue.sharedmediabased.SharedMediaBasedQueue;
import com.khush.distributedqueue.transformer.ToIntTransformer;
import com.khush.distributedqueue.transformer.ToPrimeResultTransformer;
import com.khush.generator.sharedmediabased.RandomNumberGenerator;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by khush on 17/06/2018.
 */
public class TestSharedMediaBasedQueue {

    @Test
    public void test() {

        DistributedQueue<Integer, PrimeResult> inQueue = null;
        DistributedQueue<PrimeResult, Integer> outQueue = null;

        try {
            //distributedQ(shared file based broker) initialised
            inQueue
                    = new SharedMediaBasedQueue<>(new File("inputQueue.txt"), new File("resultQueue.txt"), new ToPrimeResultTransformer());

            outQueue
                    = new SharedMediaBasedQueue<>(new File("resultQueue.txt"), new File("inputQueue.txt"), new ToIntTransformer());

            //distributedQ(broker) started
            inQueue.start();
            outQueue.start();

            new RandomNumberGenerator(inQueue, 100).start();
            new PrimeNumberChecker(outQueue).start();
            //Thread.currentThread().join();
            TimeUnit.SECONDS.sleep(1);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            Objects.requireNonNull(inQueue).clear();
            Objects.requireNonNull(outQueue).clear();
        }
    }

    @Test
    public void testStream() throws IOException {
        InputStream initialStream = new FileInputStream(
                new File("inputQueue.txt"));
        //File targetFile = new File("src/main/resources/targetFile.tmp");
        //OutputStream outStream = new FileOutputStream(targetFile);

        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        while ((bytesRead = initialStream.read(buffer)) != -1) {
            System.out.println(bytesRead + " " + new String(buffer));
        }
    }
}
