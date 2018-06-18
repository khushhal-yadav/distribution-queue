package com.khush;

import com.khush.checker.sharedqueuebased.PrimeNumberChecker;
import com.khush.distributedqueue.SharedQueue;
import com.khush.distributedqueue.message.PrimeResult;
import com.khush.distributedqueue.sharedfilebased.SharedFileBasedQueue;
import com.khush.distributedqueue.sharedfilebased.transformer.ToIntTransformer;
import com.khush.distributedqueue.sharedfilebased.transformer.ToPrimeResultTransformer;
import com.khush.generator.sharedbased.RandomNumberGenerator;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by khush on 17/06/2018.
 */
public class TestSharedFileBasedQueue {

    @Test
    public void test() {

        SharedQueue<Integer, PrimeResult> sharedQueue = null;

        try {
            //sharedQueue(shared file based broker) initialised
            sharedQueue
                    = new SharedFileBasedQueue<>(new File("inputQueue.txt"), new File("resultQueue.txt"), new ToIntTransformer(), new ToPrimeResultTransformer());

            //sharedQueue(broker) started
            sharedQueue.start();

            new RandomNumberGenerator(sharedQueue, 100).start();
            new PrimeNumberChecker(sharedQueue).start();
            //Thread.currentThread().join();
            TimeUnit.SECONDS.sleep(1);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            Objects.requireNonNull(sharedQueue).clear();
        }
    }
}
