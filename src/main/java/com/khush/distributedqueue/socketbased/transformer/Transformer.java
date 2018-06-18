package com.khush.distributedqueue.socketbased.transformer;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by khush on 17/06/2018.
 */
@FunctionalInterface
public interface Transformer<I, R> {

    R transform(DataOutputStream out,
                DataInputStream in,
                I input);
}
