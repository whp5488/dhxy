
package com.kitty.common.utils;

import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import org.slf4j.Logger;

public class Threads
{
    private static final Logger logger;
    
    public static void sleep(final long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch (final InterruptedException e) {}
    }
    
    public static void shutdownAndAwaitTermination(final ExecutorService pool) {
        if (pool != null && !pool.isShutdown()) {
            pool.shutdown();
            try {
                if (!pool.awaitTermination(120L, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                    if (!pool.awaitTermination(120L, TimeUnit.SECONDS)) {
                        Threads.logger.info("Pool did not terminate");
                    }
                }
            }
            catch (final InterruptedException ie) {
                pool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public static void printException(final Runnable r, Throwable t) {
        if (t == null && r instanceof Future) {
            try {
                final Future<?> future = (Future<?>)r;
                if (future.isDone()) {
                    future.get();
                }
            }
            catch (final CancellationException ce) {
                t = ce;
            }
            catch (final ExecutionException ee) {
                t = ee.getCause();
            }
            catch (final InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        if (t != null) {
            Threads.logger.error(t.getMessage(), t);
        }
    }
    
    static {
        logger = LoggerFactory.getLogger(Threads.class);
    }
}
