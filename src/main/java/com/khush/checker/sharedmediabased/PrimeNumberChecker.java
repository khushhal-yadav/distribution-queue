package com.khush.checker.sharedmediabased;

import com.khush.checker.PrimeNumberCheck;
import com.khush.checker.PrimeNumberCheckSimple;
import com.khush.distributedqueue.DistributedQueue;
import com.khush.distributedqueue.message.PrimeResult;

import java.util.Objects;

/**
 * Created by khush on 17/06/2018.
 */
public class PrimeNumberChecker {

    private final DistributedQueue<PrimeResult, Integer> distributedQueue;

    private final PrimeNumberCheck primeNumberCheck;

    public PrimeNumberChecker(final DistributedQueue<PrimeResult, Integer> distributedQueue) {
        this.distributedQueue = distributedQueue;
        this.primeNumberCheck = new PrimeNumberCheckSimple();
    }

    public void start() {
        new Thread(new PrimeCheckerRunnable()).start();
    }

    private class PrimeCheckerRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                Integer number = distributedQueue.poll();
                if (Objects.isNull(number))
                    continue;

                final boolean prime = primeNumberCheck.isPrime(number);
                final PrimeResult primeResult = new PrimeResult(number, prime);
                distributedQueue.offer(primeResult);
            }
        }
    }
}
