package com.khush.generator.socketbased;

import com.khush.distributedqueue.DistributedQueue;
import com.khush.distributedqueue.message.PrimeResult;
import com.khush.distributedqueue.socketbased.SocketBasedQueue;
import com.khush.distributedqueue.socketbased.transformer.ResponseTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Created by khush on 17/06/2018.
 */
public class RandomNumberGenerator {

    private static final Logger log = LoggerFactory.getLogger(RandomNumberGenerator.class);

    private final int maxRandomNumber;

    private final DistributedQueue<Integer, PrimeResult> distributedQueue;

    public RandomNumberGenerator(final int socketPort, final int maxRandomNumber) {
        this.maxRandomNumber = maxRandomNumber;
        this.distributedQueue = new SocketBasedQueue<>(new ResponseTransformer(), socketPort);
    }

    public void start() {
        new Thread(new RandomGenerator()).start();
        new Thread(new ResponseReceiver()).start();
        distributedQueue.start();
    }

    private class RandomGenerator implements Runnable {
        private final Random randomGenerator = new Random();

        @Override
        public void run() {
            while (true) {
                int randomInt = Math.abs(randomGenerator.nextInt(maxRandomNumber));
                distributedQueue.offer(randomInt);
            }
        }
    }

    private class ResponseReceiver implements Runnable {
        @Override
        public void run() {
            while (true) {
                PrimeResult result = distributedQueue.poll();
                if (result == null)
                    continue;

                log.info("{} is prime: {}", result.getNumber(), result.isPrime());
            }
        }
    }
}
