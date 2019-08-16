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
        return new YieldIterator(action);
    }
    
    /**
     * Performs the actual process of simulating yield return/break.
     */
    private class YieldIterator implements Iterator<T> {
        
        /**
         * Current value returned from processor thread through yielding.
         */
        private T current;
        
        /**
         * A different thread from which the yield processing will be done.
         */
        private Thread yieldProcessor;
        
        /**
         * Runnable instance to be performed by thread.
         */
        private YieldRunnable runnable;
        
        
        public YieldIterator(IYieldAction<T> action)
        {
            runnable = new YieldRunnable(this, action);
            
            yieldProcessor = new Thread(runnable);
            yieldProcessor.start();
        }
        
        /**
         * Sets the current yield return value.
         */
        public synchronized void SetCurrent(T value)
        {
            current = value;
        }
        
        /**
         * Signals to stop processing the thread.
         * shouldInterrupt must be set true if Break() was called.
         */
        public synchronized void StopThread(boolean shouldInterrupt)
        {
            if(yieldProcessor == null)
                return;
            
            SetCurrent(null);
            if(shouldInterrupt)
                yieldProcessor.interrupt();
            yieldProcessor = null;
        }

        public @Override boolean hasNext()
        {
            if(yieldProcessor == null)
                return false;
            
            runnable.SetRun();
            while(runnable.IsRunning()) {}
            return yieldProcessor != null;
        }

        public @Override T next()
        {
            if(yieldProcessor == null)
                return null;
            return current;
        }
    }
    
    private class YieldRunnable implements Runnable, IYieldHandler<T> {
        
        /**
         * Max amount of time before the wait() method automatically breaks out of wait state.
         */
        private final long MaxWaitThreshold = 3000;
        
        /**
         * Iterator instance which is running this runnable through thread.
         */
        private YieldIterator parent;
        
        /**
         * Whether the runnable should continue processing.
         */
        private AtomicBoolean shouldRun = new AtomicBoolean(false);
        
        /**
         * Action which handles yield returning of values.
         */
        private IYieldAction action;
        
        /**
         * Whether the runnable has finished its process.
         */
        private boolean isFinished = false;
        

        public YieldRunnable(YieldIterator parent, IYieldAction<T> action)
        {
            this.parent = parent;
            this.action = action;
        }
        
        /**
         * Sets the runnable to running state.
         */
        public synchronized void SetRun()
        {
            if(isFinished)
                return;
            shouldRun.set(true);
            notify();
        }
        
        /**
         * Returns whether the runnable is running.
         */
        public synchronized boolean IsRunning() { return shouldRun.get(); }
        
        @Override
        public void run()
        {
            try
            {
                // Processor should be paused on beginning.
                // Calling hasNext should trigger invocation of the actual action.
                while(!shouldRun.get())
                {
                    synchronized(this)
                    {
                        wait(MaxWaitThreshold);
                        DetectDereference();
                    }
                }
                // Perform action if not dereferenced.
                if(action != null)
                    action.Invoke(this);
            }
            catch(InterruptedException e)
            {
                // TODO: Do something
            }
            catch(Exception e)
            {
                // TODO: Do something
            }

            // Finalize process.
            synchronized(this)
            {
                parent.StopThread(false);
                shouldRun.set(false);
                isFinished = true;
            }
        }
        
        public @Override void Return(T value)
        {
            if(isFinished)
                return;
            synchronized(this)
            {
                parent.SetCurrent(value);
                shouldRun.set(false);
                try
                {
                    while(!shouldRun.get())
                    {
                        wait(MaxWaitThreshold);
                        DetectDereference();
                    }
                }
                catch(InterruptedException e)
                {
                    // TODO: Do something
                }
            }
        }

        public @Override void Break()
        {
            synchronized(this)
            {
                parent.StopThread(true);
            }
        }
        
        /**
         * If the wait has ended when shouldRun flag is false, the parent iterable has probably been de-referenced during
         * foreach loop due to exception or breaking out of loop.
         * If this is the case, we must dispose it to prevent leaks.
         * This method must be called after wait() within the same synchronized body.
         */
        private boolean DetectDereference()
        {
            if(shouldRun.get())
                return false;
            
            shouldRun.set(true);
            parent.StopThread(true);
            action = null;
            return true;
        }
    }
}
