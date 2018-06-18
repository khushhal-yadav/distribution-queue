package com.khush.checker;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by khush on 17/06/2018.
 */
public class PrimeNumberCheckSimple implements PrimeNumberCheck {

    private final Map<Integer, Boolean> primeCache = new WeakHashMap<>();

    @Override
    public boolean isPrime(int n) {
        if (n <= 1) return false;

        return primeCache.computeIfAbsent(n, k -> ifPrime(n));
    }

    private boolean ifPrime(int n) {
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }

        return true;
    }
}
