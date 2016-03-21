package com.dargueta.shared.model.player;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Kyle Cornelison
 * Singleton class for generating player IDs
 */
public class IdGenerator {
    private static IdGenerator ourInstance = new IdGenerator();

    /**
     * Get the current IdGenerator instance
     * @return Instance of IdGenerator
     */
    public static IdGenerator getInstance() {
        return ourInstance;
    }

    private AtomicInteger _counter;

    private IdGenerator() {
        _counter.set(0);
    }

    /**
     * Generate new Player ID
     * @return new Player ID
     */
    public int generate(){
        return _counter.getAndIncrement();
    }
}
