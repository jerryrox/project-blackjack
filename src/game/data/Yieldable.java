/*
 * Jerry Kim (18015036), 2019
 */
package game.data;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A dirty hack to simulate C#'s yield keyword in Java.
 * @author jerrykim
 */
public class Yieldable<T> implements Iterable<T> {
    
    private IYieldAction<T> action;
    
    
    public Yieldable(IYieldAction<T> action)
    {
        this.action = action;
    }

    public @Override Iterator<T> iterator()
    {
        return new YieldIterator();
    }
    
    /**
     * Performs the actual process of simulating yield return/break.
     */
    private class YieldIterator implements Iterator<T>, IYieldHandler<T> {
        
        /**
         * Current value returned from processor thread through yielding.
         */
        private T current;
        
        /**
         * Handles atomic handling of the flag for syncing with yieldProcessor.
         */
        private AtomicBoolean boolLock = new AtomicBoolean(false);
        
        /**
         * A different thread from which the yield processing will be done.
         */
        private Thread yieldProcessor;
        
        
        public YieldIterator()
        {
            yieldProcessor = new Thread(() -> {
                try
                {
                    // Processor should be paused on beginning.
                    // Calling hasNext should trigger invocation of the actual action.
                    synchronized(yieldProcessor)
                    {
                        while(!boolLock.get())
                            this.wait();
                    }
                    action.Invoke(YieldIterator.this);
                }
                catch(InterruptedException e)
                {
                    // TODO: Do something
                    boolLock.set(false);
                }
                catch(Exception e)
                {
                    // TODO: Do something
                    boolLock.set(false);
                }
            });
            yieldProcessor.start();
        }

        public @Override boolean hasNext()
        {
            if(yieldProcessor == null)
                return false;
            
            boolLock.set(true);
            synchronized(yieldProcessor)
            {
                yieldProcessor.notify();
            }
            while(boolLock.get());
            if(yieldProcessor == null)
                return false;
            return true;
        }

        public @Override T next()
        {
            if(yieldProcessor == null)
                return null;
            return current;
        }
        
        public @Override void Return(T value)
        {
            if(yieldProcessor == null)
                return;
            synchronized(yieldProcessor)
            {
                current = value;
                boolLock.set(false);
                try
                {
                    while(!boolLock.get())
                        yieldProcessor.wait();
                }
                catch(InterruptedException e)
                {
                    // TODO: Do something
                }
            }
        }

        public @Override void Break()
        {
            synchronized(yieldProcessor)
            {
                current = null;
                
                Thread processor = yieldProcessor;
                yieldProcessor = null;
                processor.interrupt();
                
                boolLock.set(false);
            }
        }
    }
}
