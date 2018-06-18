package com.khush.checker;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by khush on 17/06/2018.
 */
public class PrimeNumberCheckSimpleTest {

    final private PrimeNumberCheck primeNumberCheck = new PrimeNumberCheckSimple();

    @Test
    public void testFew() {
        Assert.assertFalse(primeNumberCheck.isPrime(0));
        Assert.assertFalse(primeNumberCheck.isPrime(1));
        Assert.assertTrue(primeNumberCheck.isPrime(2));
        Assert.assertTrue(primeNumberCheck.isPrime(3));
        Assert.assertFalse(primeNumberCheck.isPrime(25));
        Assert.assertTrue(primeNumberCheck.isPrime(23));
        Assert.assertFalse(primeNumberCheck.isPrime(25));
    }

}