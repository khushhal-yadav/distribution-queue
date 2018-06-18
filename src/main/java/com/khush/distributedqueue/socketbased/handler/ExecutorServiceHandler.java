package com.khush.distributedqueue.socketbased.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

/**
 * Created by khush on 17/06/2018.
 */
public class ExecutorServiceHandler<S> implements  Handler<S> {

    private final Handler<S> otherHandler;
    private final ExecutorService executorService;
    private final Thread.UncaughtExceptionHandler exceptionHandler;

    public ExecutorServiceHandler(final Handler<S> otherHandler,
                                  final ExecutorService executorService,
                                  Thread.UncaughtExceptionHandler exceptionHandler) {
        this.otherHandler = otherHandler;
        this.executorService = executorService;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void handle(S s) {
        executorService
                .submit(new FutureTask<S>(() -> {
                            otherHandler.handle(s);
                            return null;
                        }) {
                            @Override
                            protected void setException(Throwable t) {
                                exceptionHandler.uncaughtException(Thread.currentThread(), t);
                            }
                        }
                );
    }
}
