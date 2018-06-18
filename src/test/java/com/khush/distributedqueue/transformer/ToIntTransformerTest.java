package com.khush.distributedqueue.transformer;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by khush on 17/06/2018.
 */
public class ToIntTransformerTest {

    private final DataTransformer<String, Integer> toIntTransformer = new ToIntTransformer();

    @Test
    public void test() {
        Assert.assertEquals(Integer.valueOf(12), toIntTransformer.transform("12"));
    }

    @Test(expected = NumberFormatException.class)
    public void testNumberFormatException() {
        Assert.assertEquals(Integer.valueOf(12), toIntTransformer.transform("12N"));
    }

}