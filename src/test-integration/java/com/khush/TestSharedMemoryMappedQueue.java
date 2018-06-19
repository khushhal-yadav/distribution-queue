package com.khush;

import com.khush.distributedqueue.sharedmemorymapqueue.SharedMemoryMappedQueue;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TestSharedMemoryMappedQueue {

    //SharedMemoryMappedQueue IS YET NOT INTEGRATED with the solution, it's independent single queue test
    @Test
    public void test() throws IOException, InterruptedException {

        final SharedMemoryMappedQueue sharedMemoryMappedQueue = new SharedMemoryMappedQueue(new File("test.txt"), new File("test.txt"));
        sharedMemoryMappedQueue.start();

        for (int i =0; i < 3000; i++) {
            sharedMemoryMappedQueue.offer(i);
        }

        TimeUnit.SECONDS.sleep(2);

    }
}
