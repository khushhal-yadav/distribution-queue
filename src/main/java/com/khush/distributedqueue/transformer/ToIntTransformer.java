package com.khush.distributedqueue.transformer;

/**
 * Created by khush on 17/06/2018.
 */
public class ToIntTransformer implements DataTransformer<String, Integer> {

    @Override
    public Integer transform(String input) {
        return Integer.parseInt(input);
    }
}
