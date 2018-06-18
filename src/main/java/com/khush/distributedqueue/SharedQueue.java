package com.khush.distributedqueue;

/**
 * Created by khush on 17/06/2018.
 */
public interface SharedQueue<I, R> {

    void inputOffer(I e);

    I inputPoll();

    void resultOffer(R e);

    R resultPoll();

    void start();

    void clear();
}
