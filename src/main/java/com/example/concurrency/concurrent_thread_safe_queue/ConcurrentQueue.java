package com.example.concurrency.concurrent_thread_safe_queue;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentQueue {
    private int[] queue;
    private final Lock lock = new ReentrantLock();

    public ConcurrentQueue() {
        queue = new int[0];
    }

    public void Enqueue(int item) {
        lock.lock();
        try {
            queue = Arrays.copyOf(queue, queue.length + 1);
            queue[queue.length-1] = item;
        } finally {
            lock.unlock();
        }
    }

    public int Dequeue() {
        lock.lock();
        try {
            if (queue.length == 0) {
                throw new RuntimeException("Cannot dequeue from an empty queue");
            }

            int item = queue[0];
            queue = Arrays.copyOfRange(queue, 1, queue.length);
            return item;
        } finally {
            lock.unlock();
        }
    }

    public int Size() {
        lock.lock();
        try {
            return queue.length;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ConcurrentQueue queue = new ConcurrentQueue();

        int numThreads = 100_000;
        var wgE = new CountDownLatch(numThreads);
        var wgD =  new CountDownLatch(numThreads);

        Random rand = new Random();

        for (int i = 0; i < numThreads; i++) {
            new Thread(() -> {
                queue.Enqueue(rand.nextInt());
                wgE.countDown();
            }).start();
        }
        wgE.await();
        System.out.println("Size after enqueue : " + queue.Size());

        for (int i = 0; i < numThreads; i++) {
            new Thread(() -> {
                queue.Dequeue();
                wgD.countDown();
            }).start();
        }


        wgD.await();
        System.out.println("Size after dequeue : " + queue.Size());
    }
}
