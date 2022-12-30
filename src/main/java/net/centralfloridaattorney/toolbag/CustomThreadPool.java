package net.centralfloridaattorney.toolbag;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class CustomThreadPool {
    private static final int MAX_THREADS = 500;
    private static CustomThreadPool instance;
    final BlockingQueue<Runnable> blockingQueue;
    final Worker[] workers;
    final long DELAY = 25;

    public CustomThreadPool(final int numOfThreads) {
        blockingQueue = new LinkedBlockingQueue<>();
        workers = new Worker[numOfThreads];

        for (int i = 0; i < numOfThreads; i++) {
            workers[i] = new Worker();
            workers[i].start();
        }
    }

    public static synchronized CustomThreadPool getInstance(final int numThreads) {
        if (instance == null) {
            instance = new CustomThreadPool(numThreads);
        }
        return instance;
    }

    public static synchronized CustomThreadPool getInstance() {
        if (instance == null) {
            instance = new CustomThreadPool(MAX_THREADS);
        }
        return instance;
    }

    public static void main(String[] args) throws Exception {
        final CustomThreadPool threadPool = new CustomThreadPool(5);
        for (int i = 0; i < 20; i++) {
            threadPool.execute(() -> System.out.println(Thread.currentThread().getName()));
        }
        Thread.sleep(1 * 1000);
        threadPool.shutdownImmediately();
    }

    public void execute(final Runnable task) {
        blockingQueue.add(task);
    }

    public void shutdownImmediately() {
        for (int i = 0; i < workers.length; i++) {
            workers[i].shutdownSignal = true;
            workers[i].interrupt();
            workers[i] = null;
        }
    }

    public class Worker extends Thread {
        boolean shutdownSignal = false;
        CustomThreadPool thisCustomThreadPool;
        private Runnable taskToPerform = null;

        @Override
        public void run() {
            while (true && !shutdownSignal) {
                try {
                    if (null == taskToPerform) {
                        taskToPerform = blockingQueue.poll(DELAY, TimeUnit.MILLISECONDS);
                    } else {
                        taskToPerform.run();
                        Worker worker = new Worker();
                        worker.run();
                    }
                } catch (InterruptedException e) {
                    break; // this thread is interrupted. Time to leave this loop
                }

            }
        }

        /**
         @Override public void run() {
         while(true && !shutdownSignal) {
         taskToPerform = blockingQueue.poll();
         if (taskToPerform != null) {
         taskToPerform.run();
         }
         if(shutdownSignal) {
         break;
         }
         }
         }
         **/

    }
}
