package com.khush.distributedqueue.sharedfilebased.transformer;

import java.io.Serializable;

/**
 * Created by khush on 17/06/2018.
 */
@FunctionalInterface
public interface DataTransformer<I extends Serializable, R extends Serializable> {

    R transform(I input);
}
