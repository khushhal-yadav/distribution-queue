package com.khush;

import com.khush.checker.sharedqueuebased.PrimeNumberChecker;
import com.khush.distributedqueue.SharedQueue;
import com.khush.distributedqueue.message.PrimeResult;
import com.khush.distributedqueue.sharedjvmmemorybased.SharedJvmMemoryBasedQueue;
import com.khush.generator.sharedbased.RandomNumberGenerator;
import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by khush on 17/06/2018.
 */
public class TestSharedJvmMemoryBasedQueue {

    @Test
    public void test() {

        SharedQueue<Integer, PrimeResult> sharedQueue = null;

        try {
            //sharedQueue(shared memory based broker) initialised
            sharedQueue = new SharedJvmMemoryBasedQueue<>();

            new RandomNumberGenerator(sharedQueue, 100).start();
            new PrimeNumberChecker(sharedQueue).start();
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            Objects.requireNonNull(sharedQueue).clear();
        }
    }
}
