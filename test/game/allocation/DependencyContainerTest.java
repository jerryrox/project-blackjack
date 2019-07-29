/*
 * Jerry Kim (18015036), 2019
 */
package game.allocation;

import game.debug.ConsoleLogger;
import game.debug.ILogger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jerrykim
 */
public class DependencyContainerTest {
    
    /**
     * Test of Cache method, of class DependencyContainer.
     */
    @Test
    public void testCache() {
        DependencyContainer container = new DependencyContainer(new ConsoleLogger());
        TestDependency dependency = new TestDependency("Troll");
        
        container.Cache(dependency);
    }

    /**
     * Test of CacheAs method, of class DependencyContainer.
     */
    @Test
    public void testCacheAs() {
        DependencyContainer container = new DependencyContainer(new ConsoleLogger());
        TestDependency dependency = new TestDependency("Troll");
        
        container.CacheAs(TestInterface.class, dependency);
    }

    /**
     * Test of Get method, of class DependencyContainer.
     */
    @Test
    public void testGet() {
        DependencyContainer container = new DependencyContainer(new ConsoleLogger());
        TestDependency dependency = new TestDependency("Troll");
        TestDependency dependency2 = new TestDependency("Troll 2");
        
        container.Cache(dependency);
        container.CacheAs(TestInterface.class, dependency2);
        
        assertEquals(dependency, container.Get(TestDependency.class));
        assertEquals(dependency2, container.Get(TestInterface.class));
    }

    /**
     * Test of Inject method, of class DependencyContainer.
     */
    @Test
    public void testInject() {
        ILogger logger = new ConsoleLogger();
        DependencyContainer container = new DependencyContainer(logger);
        TestDependency dependency = new TestDependency("Troll");
        TestDependency dependency2 = new TestDependency("Troll 2");
        TestInjected injected = new TestInjected(logger);
        
        container.Cache(dependency);
        container.CacheAs(TestInterface.class, dependency2);
        
        container.Inject(injected);
        assertEquals(injected.i, dependency2);
        assertEquals(injected.d, dependency);
    }
    
    
    private interface TestInterface {
        
        String GetA();
    }
    
    private class TestDependency implements TestInterface {
        
        private String A;
        
        public TestDependency(String val)
        {
            A = val;
        }
        
        public @Override String GetA()
        {
            return A;
        }
    }
    
    private class TestInjected {
        
        public TestInterface i;
        
        @ReceivesDependency
        public TestDependency d;
        
        private ILogger logger;
        
        
        public TestInjected(ILogger logger)
        {
            this.logger = logger;
        }
        
        @InitWithDependency
        public void Init(TestInterface i)
        {
            logger.LogInfo("Invoked init. i is null? " + (i == null));
            logger.LogInfo("Received d: " + (d != null));
            this.i = i;
        }
    }
}
