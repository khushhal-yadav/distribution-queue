package com.khush.distributedqueue.socketbased.transformer;

import com.khush.distributedqueue.exception.ResponseVerificationException;
import com.khush.distributedqueue.message.PrimeResult;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by khush on 17/06/2018.
 */
public class ResponseTransformer implements Transformer<Integer, PrimeResult> {

    @Override
    public PrimeResult transform(DataOutputStream outputStream,
                                 DataInputStream inputStream,
                                 Integer input) {
        try {
            outputStream.writeInt(input);
            int number = inputStream.readInt();

            if (!Objects.equals(number, input))
                throw new ResponseVerificationException("incorrect response");

            return new PrimeResult(number, inputStream.readBoolean());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}
