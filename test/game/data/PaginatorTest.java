/*
 * Jerry Kim (18015036), 2019
 */
package game.data;

import java.util.Iterator;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jerrykim
 */
public class PaginatorTest {
    
    
    @Test
    public void TestInit()
    {
        // 2 items per page
        Paginator<Integer> paginator = new Paginator<>(2);
        
        // No items yet!
        List<Integer> items = paginator.GetAllItems();
        assertEquals(items.size(), 0);
        
        // Check pages
        assertEquals(paginator.GetCurPage(), 1);
        assertEquals(paginator.NextPage(), 1);
        assertEquals(paginator.PrevPage(), 1);
        assertEquals(paginator.GetMaxPage(), 1);
    }
    
    /**
     * Testing add and simple pagination.
     */
    @Test
    public void TestAdd()
    {
        Paginator<Integer> paginator = new Paginator<>(2);
        paginator.Add(1);
        paginator.Add(2);
        paginator.Add(3);
        paginator.Add(4);
        paginator.Add(5);
        
        assertEquals(paginator.GetItemsCount(), 5);
        assertEquals(paginator.GetMaxPage(), 3);
        
        Iterator<Integer> iterator = paginator.GetItemsInPage().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next().intValue(), 1);
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next().intValue(), 2);
        assertFalse(iterator.hasNext());
        
        // Next page
        assertEquals(paginator.NextPage(), 2);
        
        iterator = paginator.GetItemsInPage().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next().intValue(), 3);
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next().intValue(), 4);
        assertFalse(iterator.hasNext());
        
        // Next page
        assertEquals(paginator.NextPage(), 3);
        
        iterator = paginator.GetItemsInPage().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next().intValue(), 5);
        assertFalse(iterator.hasNext());
        
        // There shouldn't be a next page.
        assertEquals(paginator.NextPage(), 3);
        
        // Prev page
        assertEquals(paginator.PrevPage(), 2);
        
        iterator = paginator.GetItemsInPage().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next().intValue(), 3);
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next().intValue(), 4);
        assertFalse(iterator.hasNext());
        
        // Set page
        assertEquals(paginator.SetPage(1), 1);
        
        iterator = paginator.GetItemsInPage().iterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next().intValue(), 1);
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next().intValue(), 2);
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void TestSort()
    {
        Paginator<Integer> paginator = new Paginator<>(2);
        paginator.Add(4);
        paginator.Add(2);
        paginator.Add(3);
        paginator.Add(1);
        paginator.Add(5);
        
        paginator.Sort((x, y) -> Integer.compare(x, y));
        
        List<Integer> items = paginator.GetAllItems();
        assertEquals(items.size(), 5);
        for(int i=0; i<5; i++)
            assertEquals(items.get(i).intValue(), i + 1);
    }
    
    /**
     * Testing individual removing and clearing.
     */
    @Test
    public void TestRemove()
    {
        Paginator<Integer> paginator = new Paginator<>(2);
        for(int i=0; i<5; i++)
            paginator.Add(i);
        
        // Remove last entry.
        paginator.Remove(4);
        
        List<Integer> items = paginator.GetAllItems();
        assertEquals(items.size(), 4);
        for(int i=0; i<4; i++)
            assertEquals(items.get(i).intValue(), i);
        
        // Set page to some other value.
        paginator.SetPage(2);
        // Then clear all items.
        paginator.Clear();
        assertEquals(paginator.GetItemsCount(), 0);
        assertEquals(paginator.GetCurPage(), 1);
    }
    
    @Test
    public void TestFind()
    {
        Paginator<Integer> paginator = new Paginator<>(2);
        for(int i=0; i<5; i++)
            paginator.Add(i);
        
        assertEquals(paginator.Find(v -> v.intValue() == 3).intValue(), 3);
    }
}
