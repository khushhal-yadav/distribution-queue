package com.khush.distributedqueue.socketbased.handler;

import java.io.IOException;

/**
 * Created by khush on 17/06/2018.
 */
public interface Handler<S> {

    void handle(S s) throws IOException;
}
