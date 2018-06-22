package bgu.spl.a2;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 * <p>
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class ActorThreadPool {

    private VersionMonitor monitor = new VersionMonitor();
    private Thread[] threadPool;
    private ConcurrentHashMap<String, ConcurrentLinkedQueue<Action>> queueHashMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, AtomicBoolean> lockHashMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, PrivateState> privateStateHashMap = new ConcurrentHashMap<>();

    /**
     * creates a {@link ActorThreadPool} which has nthreads. Note, threads
     * should not get started until calling to the {@link #start()} method.
     * <p>
     * Implementors note: you may not add other constructors to this class nor
     * you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param nthreads the number of threads that should be started by this thread
     *                 pool
     */
    public ActorThreadPool(int nthreads) {
        threadPool = new Thread[nthreads];
        for (int i = 0; i < nthreads; i++) {
            threadPool[i] = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    boolean foundAnUnlockQueue = false;
                    Set<String> actorIdsSet = lockHashMap.keySet();
                    for (String k : actorIdsSet) {
                        if (lockHashMap.containsKey(k)) {
                            if (lockHashMap.get(k).compareAndSet(false, true)) { //if not locked
                                if (!queueHashMap.get(k).isEmpty()) {
                                    foundAnUnlockQueue = true;

                                    queueHashMap.get(k).remove().handle(this, k, privateStateHashMap.get(k));
                                }
                                lockHashMap.get(k).compareAndSet(true, false); //unlocking
                                monitor.inc();
                            }
                        }
                    }
                    if (!foundAnUnlockQueue) {
                        try {
                            monitor.await(monitor.getVersion());
                        } catch (Exception e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            });
        }

    }

    /**
     * submits an action into an actor to be executed by a thread belongs to
     * this thread pool
     *
     * @param action     the action to execute
     * @param actorId    corresponding actor's id
     * @param actorState actor's private state (actor's information)
     */
    public void submit(Action<?> action, String actorId, PrivateState actorState) {
        if (actorState != null) {
            privateStateHashMap.putIfAbsent(actorId, actorState);
            queueHashMap.putIfAbsent(actorId, new ConcurrentLinkedQueue<>());
            lockHashMap.putIfAbsent(actorId, new AtomicBoolean(false));
        }

        if (action != null && actorId != null && lockHashMap.get(actorId) != null) {
            queueHashMap.get(actorId).add(action);
            monitor.inc();

        }
    }

    /**
     * closes the thread pool - this method interrupts all the threads and waits
     * for them to stop - it is returns *only* when there are no live threads in
     * the queue.
     * <p>
     * after calling this method - one should not use the queue anymore.
     *
     * @throws InterruptedException if the thread that shut down the threads is interrupted
     */
    public void shutdown() throws InterruptedException {
        for (int i = 0; i < threadPool.length; i++) {
            threadPool[i].interrupt();
            threadPool[i].join();
        }
    }

    /**
     * start the threads belongs to this thread pool
     */
    public void start() {
        for (int i = 0; i < threadPool.length; i++) {
            threadPool[i].start();
        }
    }

    /**
     * getter for actors
     *
     * @return actors hashmap of private state
     */
    public Map<String, PrivateState> getActors() {
        HashMap<String, PrivateState> hashMapToReturn = new HashMap<>();
        Set<String> actorIdsSet = privateStateHashMap.keySet();
        for (String k : actorIdsSet) {
            hashMapToReturn.putIfAbsent(k, privateStateHashMap.get(k));
        }
        return hashMapToReturn;
    }

    /**
     * getter for actor's private state
     *
     * @param actorId actor's id
     * @return actor's private state
     */
    public PrivateState getPrivateState(String actorId) {
        return privateStateHashMap.get(actorId);
    }
}
