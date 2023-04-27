import java.util.*;

/**
 * A simple work queue implementation based on the IBM Developer article by
 * Brian Goetz. It is up to the user of this class to keep track of whether
 * there is any pending work remaining.
 *
 * @see <a href=
 *      "https://www.ibm.com/developerworks/library/j-jtp0730/index.html"> Java
 *      Theory and Practice: Thread Pools and Work Queues</a>
 */
public class WorkQueue {

    // DONE Modify existing methods if necessary.
    // DONE Add new methods or members if necessary.

    /**
     * Pool of worker threads that will wait in the background until work is
     * available.
     */
    private final PoolWorker[] workers;

    /** Queue of pending work requests. */
    private final LinkedList<Runnable> queue;

    /** Used to signal the queue should be shutdown. */
    private volatile boolean shutdown;

    /** The default number of threads to use when not specified. */
    public static final int DEFAULT = 5;

    /** The number of tasks awaiting completion */
    private int tasksPending;

    /**
     * Starts a work queue with the default number of threads.
     *
     * @see #WorkQueue(int)
     */
    public WorkQueue() {
        this(DEFAULT);
    }

    /**
     * Starts a work queue with the specified number of threads.
     *
     * @param threads number of worker threads; should be greater than 1
     */
    public WorkQueue(int threads) {
        this.queue = new LinkedList<Runnable>();
        this.workers = new PoolWorker[threads];

        this.shutdown = false;

        this.tasksPending = 0;

        if (threads <= 0)
        {
			threads = 5;
		}

        // start the threads so they are waiting in the background
        for (int i = 0; i < threads; i++) {
            workers[i] = new PoolWorker();
            workers[i].start();
        }
    }

    /**
     * Adds a work request to the queue. A thread will process this request when
     * available.
     *
     * @param r work request (in the form of a {@link Runnable} object)
     */
    public void execute(Runnable r)
    {
        addTasks();

        synchronized (queue) {
            queue.addLast(r);
            queue.notifyAll();
        }
    }

    /**
     * Waits for all pending work to be finished.
     *
     * @throws InterruptedException if interrupted
     */
    public void finish() throws InterruptedException
    {
        synchronized (this)
        {
            while (this.tasksPending > 0)
            {
				this.wait();
			}
        }
    }

    /**
     * Asks the queue to shutdown. Any unprocessed work will not be finished, but
     * threads in-progress will not be interrupted.
     */
    public void shutdown() {
        // safe to do unsynchronized due to volatile keyword
        shutdown = true;

        synchronized (queue) {
            queue.notifyAll();
        }
    }

    /**
     * Returns the number of worker threads being used by the work queue.
     *
     * @return number of worker threads
     */
    public int size() {
        return workers.length;
    }

    /**
     * Helper method that increases the amount of tasks that are awaiting completion
     */
    private synchronized void addTasks()
    {
        this.tasksPending++;
    }

    /**
     * Helper method that decreases the amount of tasks that are awaiting completion
    */
    private synchronized void removeTasks()
    {
        assert this.tasksPending > 0;
        
        this.tasksPending--;

        if (this.tasksPending == 0)
        {
			this.notifyAll();
		}
    }

    /**
     * Waits until work is available in the work queue. When work is found, will
     * remove the work from the queue and run it. If a shutdown is detected, will
     * exit instead of grabbing new work from the queue. These threads will continue
     * running in the background until a shutdown is requested.
     */
    private class PoolWorker extends Thread {

        @Override
        public void run() {
            Runnable r = null;

            while (true) {
                synchronized (queue) {
                    while (queue.isEmpty() && !shutdown) {
                        try {
                            queue.wait();
                        } catch (InterruptedException ex) {
                            System.err.println("Warning: Work queue interrupted.");
                            Thread.currentThread().interrupt();
                        }
                    }

                    // exit while for one of two reasons:
                    // (a) queue has work, or (b) shutdown has been called

                    if (shutdown) {
                        break;
                    } else {
                        r = queue.removeFirst();
                    }
                }

                try {
                    r.run();
                } catch (RuntimeException ex) {
                    // catch runtime exceptions to avoid leaking threads
                    System.err.println("Warning: Work queue encountered an exception while running.");
                }

                removeTasks();
            }
        }
    }
}
