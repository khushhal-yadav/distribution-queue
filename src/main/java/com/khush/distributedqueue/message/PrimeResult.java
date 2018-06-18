package com.khush.distributedqueue.message;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by khush on 17/06/2018.
 */
public class PrimeResult implements Serializable, Comparable<PrimeResult> {
    private final int number;
    private final boolean isPrime;

    public PrimeResult(int number, boolean isPrime) {
        this.number = number;
        this.isPrime = isPrime;
    }

    public int getNumber() {
        return number;
    }

    public boolean isPrime() {
        return isPrime;
    }

    @Override
    public boolean equals(Object obj) {
        return Optional.ofNullable(obj)
                .filter(PrimeResult.class::isInstance)
                .map(PrimeResult.class::cast)
                .filter(o -> Objects.equals(o.number, this.number))
                .filter(o -> Objects.equals(o.isPrime, this.isPrime))
                .isPresent();
    }

    @Override
    public int compareTo(PrimeResult other) {
        return Integer.compare(this.number, other.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, isPrime);
    }

    @Override
    public String toString() {
        return number +
                "," + (isPrime ? "Y" : "N");
    }


}
