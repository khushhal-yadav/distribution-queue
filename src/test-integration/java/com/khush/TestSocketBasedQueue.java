package com.khush;

import com.khush.checker.socketbased.PrimeNumberChecker;
import com.khush.generator.socketbased.RandomNumberGenerator;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by khush on 17/06/2018.
 */
public class TestSocketBasedQueue {

    @Test
    public void test() throws InterruptedException {
        final String hostname = "localhost";
        final int socketPort = 65432;
        final int maxRandomNumber = 100;

        new RandomNumberGenerator(socketPort, maxRandomNumber).start();
        new PrimeNumberChecker(hostname, socketPort).start();

        TimeUnit.SECONDS.sleep(2);
    }


}
