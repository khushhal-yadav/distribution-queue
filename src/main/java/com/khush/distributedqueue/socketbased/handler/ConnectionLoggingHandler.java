package com.khush.distributedqueue.socketbased.handler;

import java.io.IOException;

/**
 * Created by khush on 17/06/2018.
 */
public class ConnectionLoggingHandler<S> implements Handler<S> {

    private final Handler<S> otherHandler;

    public ConnectionLoggingHandler(Handler<S> otherHandler) {
        this.otherHandler = otherHandler;
    }

    @Override
    public void handle(S s) throws IOException {
        System.out.println("Connected to " + s);
        try {
            otherHandler.handle(s);
        } finally {
            System.out.println("Disconnected from " + s);
        }
    }
}
