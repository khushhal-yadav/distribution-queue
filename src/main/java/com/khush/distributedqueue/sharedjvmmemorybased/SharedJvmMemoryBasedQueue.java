package com.khush.distributedqueue.sharedjvmmemorybased;

import com.khush.distributedqueue.SharedQueue;

import java.util.AbstractQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by khush on 17/06/2018.
 */
public class SharedJvmMemoryBasedQueue<I, R> implements SharedQueue<I, R> {

    //ConcurrentLinkedQueue(CAS based) to support multiple concurrent connections
    //otherwise can use ArrayDequeue for single connectivity
    private final AbstractQueue<I> inQueue;
    private final AbstractQueue<R> resultQueue;

    public SharedJvmMemoryBasedQueue() {
        this.inQueue = new ConcurrentLinkedQueue<>();
        this.resultQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void inputOffer(I e) {
        this.inQueue.offer(e);
    }

    @Override
    public I inputPoll() {
        return this.inQueue.poll();
    }

    @Override
    public void resultOffer(R e) {
        this.resultQueue.offer(e);
    }

    @Override
    public R resultPoll() {
        return this.resultQueue.poll();
    }

    //Not required for memory based queue
    @Override
    public void start() {
    }

    @Override
    public void clear() {
        this.inQueue.clear();
        this.resultQueue.clear();
    }

}
