package com.khush.distributedqueue.transformer;

import com.khush.distributedqueue.message.PrimeResult;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by khush on 17/06/2018.
 */
public class ToPrimeResultTransformerTest {

    private final DataTransformer<String, PrimeResult> toPrimeResultTransformer = new ToPrimeResultTransformer();

    @Test
    public void test() {
        Assert.assertEquals(new PrimeResult(12, true), toPrimeResultTransformer.transform("12,Y"));
        Assert.assertEquals(new PrimeResult(12, false), toPrimeResultTransformer.transform("12,N"));
    }

    @Test(expected = NumberFormatException.class)
    public void testNumberFormatException() {
        toPrimeResultTransformer.transform("12N,Y");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testArrayIndexOutOfBoundsException() {
        toPrimeResultTransformer.transform("12");
    }

}