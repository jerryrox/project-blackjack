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
     * Testing dependency caching and retrieving.
     */
    @Test
    public void TestCache()
    {
        DependencyContainer container = new DependencyContainer(new ConsoleLogger());
        TestDependency dependency = new TestDependency("Troll");
        container.Cache(dependency);
        
        assertEquals(container.Get(TestDependency.class), dependency);
    }
    
    /**
     * Testing dependency caching as an interface and retrieving.
     */
    @Test
    public void TestCacheAsInterface()
    {
        DependencyContainer container = new DependencyContainer(new ConsoleLogger());
        TestDependency dependency = new TestDependency("Troll");
        container.CacheAs(TestInterface.class, dependency);
        
        // This will output an error as console message but this is expected.
        // Plus, the message only serves as an indication for the developer that the dependency was not cached for the specified type.
        assertNull(container.Get(TestDependency.class));
        
        assertNotNull(container.Get(TestInterface.class));
        
        assertEquals((TestDependency)container.Get(TestInterface.class), dependency);
    }
    
    /**
     * Testing dependency caching as a base type and retrieving.
     */
    @Test
    public void TestCacheAsBase()
    {
        DependencyContainer container = new DependencyContainer(new ConsoleLogger());
        TestDependency dependency = new TestDependency("Troll");
        container.CacheAs(TestBaseDependency.class, dependency);
        
        // This will output an error as console message but this is expected.
        // Plus, the message only serves as an indication for the developer that the dependency was not cached for the specified type.
        assertNull(container.Get(TestDependency.class));
        
        assertNotNull(container.Get(TestBaseDependency.class));
        
        assertEquals((TestDependency)container.Get(TestBaseDependency.class), dependency);
    }
    
    /**
     * Testing case where a dependency has been cached for the same type.
     */
    @Test
    public void TestDuplicateType()
    {
        DependencyContainer container = new DependencyContainer(new ConsoleLogger());
        
        TestDependency test1 = new TestDependency("Test1");
        TestBaseDependency test2 = new TestBaseDependency(11);
        
        container.Cache(test2);
        
        // Should complain in console that a duplicate type already exists.
        container.CacheAs(TestBaseDependency.class, test1);
        
        // Should complain in console that the dependency instance is null.
        assertNull(container.Get(TestDependency.class));
        
        assertEquals(container.Get(TestBaseDependency.class), test2);
    }
    
    /**
     * Test of Inject method, of class DependencyContainer.
     */
    @Test
    public void TestInject()
    {
        ILogger logger = new ConsoleLogger();
        DependencyContainer container = new DependencyContainer(logger);
        
        TestDependency dependency = new TestDependency("Troll");
        TestDependency dependency2 = new TestDependency("Trollerz");
        
        container.Cache(dependency);
        container.CacheAs(TestInterface.class, dependency2);
        
        // Object to be injected on.
        TestInjected injected = new TestInjected(logger);
        container.Inject(injected);
        
        assertEquals(injected.i, dependency2);
        assertEquals(injected.d, dependency);
    }
    
    
    private interface TestInterface {
        
        String GetA();
    }
    
    private class TestBaseDependency {
        
        public int BB;
        
        public TestBaseDependency(int bb)
        {
            BB = bb;
        }
    }
    
    private class TestDependency extends TestBaseDependency implements TestInterface {
        
        private String A;
        
        public TestDependency(String val)
        {
            super(val.length());
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
