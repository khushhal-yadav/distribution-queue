package com.khush.generator.sharedbased;

import com.khush.distributedqueue.SharedQueue;
import com.khush.distributedqueue.message.PrimeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Random;

/**
 * Created by khush on 17/06/2018.
 */
public class RandomNumberGenerator {

    private static final Logger log = LoggerFactory.getLogger(RandomNumberGenerator.class);

    private final int maxRandomNumber;

    private final SharedQueue<Integer, PrimeResult> sharedQueue;

    public RandomNumberGenerator(final SharedQueue<Integer, PrimeResult> sharedQueue,
                                 final int maxRandomNumber) {
        this.sharedQueue = sharedQueue;
        this.maxRandomNumber = maxRandomNumber;
    }

    public void start() {
        new Thread(new RandomNumberGeneratorRunnable()).start();
        new Thread(new PrimeResultReceiverRunnable()).start();
    }

    private class RandomNumberGeneratorRunnable implements Runnable {
        private final Random randomGenerator = new Random();

        @Override
        public void run() {
            while (true) {
                int randomInt = Math.abs(randomGenerator.nextInt(maxRandomNumber));
                sharedQueue.inputOffer(randomInt);
            }
        }
    }

    private class PrimeResultReceiverRunnable implements Runnable {
        @Override
        public void run() {
            while (true) {
                PrimeResult result = sharedQueue.resultPoll();
                if (Objects.isNull(result))
                    continue;

                log.info("{} is prime: {}", result.getNumber(), result.isPrime());
            }
        }
    }

}
