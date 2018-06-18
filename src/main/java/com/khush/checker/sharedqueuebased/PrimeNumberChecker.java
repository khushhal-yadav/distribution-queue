package com.khush.checker.sharedqueuebased;

import com.khush.checker.PrimeNumberCheck;
import com.khush.checker.PrimeNumberCheckSimple;
import com.khush.distributedqueue.SharedQueue;
import com.khush.distributedqueue.message.PrimeResult;

import java.util.Objects;

/**
 * Created by khush on 17/06/2018.
 */
public class PrimeNumberChecker {

    private final SharedQueue<Integer, PrimeResult> sharedQueue;

    private final PrimeNumberCheck primeNumberCheck;

    public PrimeNumberChecker(final SharedQueue<Integer, PrimeResult> sharedQueue) {
        this.sharedQueue = sharedQueue;
        this.primeNumberCheck = new PrimeNumberCheckSimple();
    }

    public void start() {
        new Thread(new PrimeCheckerRunnable()).start();
    }

    private class PrimeCheckerRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                Integer number = sharedQueue.inputPoll();
                if (Objects.isNull(number))
                    continue;

                final boolean prime = primeNumberCheck.isPrime(number);
                final PrimeResult primeResult = new PrimeResult(number, prime);
                sharedQueue.resultOffer(primeResult);
            }
        }
    }
}
