package com.khush.checker.socketbased;

import com.khush.checker.PrimeNumberCheck;
import com.khush.checker.PrimeNumberCheckSimple;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by khush on 17/06/2018.
 */
public class PrimeNumberChecker {

    private final String hostName;
    private final int port;

    private final PrimeNumberCheck primeNumberCheck;

    public PrimeNumberChecker(final String hostName, final int port) {
        this.hostName = hostName;
        this.port = port;
        this.primeNumberCheck = new PrimeNumberCheckSimple();
    }

    public void start() {
        new Thread(new PrimeNumberCheckRunnable()).start();
    }

    private class PrimeNumberCheckRunnable implements Runnable {

        @Override
        public void run() {
            try (
                    Socket socket = new Socket(hostName, port);
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
            ) {
                while (true) {
                    process(inputStream, outputStream);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void process(DataInputStream in, DataOutputStream out) {
            try {
                int testedNumber = in.readInt();
                boolean isPrime = primeNumberCheck.isPrime(testedNumber);
                out.writeInt(testedNumber);
                out.writeBoolean(isPrime);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
