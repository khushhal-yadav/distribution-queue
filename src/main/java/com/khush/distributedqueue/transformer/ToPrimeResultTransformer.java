package com.khush.distributedqueue.transformer;

import com.khush.distributedqueue.message.PrimeResult;

/**
 * Created by khush on 17/06/2018.
 */
public class ToPrimeResultTransformer implements DataTransformer<String, PrimeResult> {

    @Override
    public PrimeResult transform(String input) {
        final String[] split = input.split(",");
        return new PrimeResult(Integer.parseInt(split[0]), split[1].equalsIgnoreCase("Y"));
    }
}
