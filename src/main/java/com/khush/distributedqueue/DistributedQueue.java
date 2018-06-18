package com.khush.distributedqueue;

import java.io.Serializable;

/**
 * Created by khush on 17/06/2018.
 */
public interface DistributedQueue<I extends Serializable, R extends Serializable> {

    void offer(I e);

    R poll();

    void start();

    void clear();
}
