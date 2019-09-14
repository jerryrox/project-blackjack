/*
 * Jerry Kim (18015036), 2019
 */
package game.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Handles pagination of data.
 * @author jerrykim
 */
public class Paginator<T extends Object> {
    
    public final int ItemsPerPage;
    
    private ArrayList<T> items = new ArrayList<>();
    private int curPage = 1;
    
    
    public Paginator(int itemsPerPage)
    {
        ItemsPerPage = itemsPerPage;
    }
    
    /**
     * Clears all items.
     */
    public void Clear()
    {
        items.clear();
        curPage = 1;
    }
    
    /**
     * Adds the specified item to list.
     */
    public void Add(T item)
    {
        items.add(item);
    }
    
    /**
     * Removes the specified item from list.
     */
    public void Remove(T item)
    {
        items.remove(item);
    }
    
    /**
     * Sorts cells by specified comparator.
     * If null, the item must implement a Comparable<> interface.
     */
    public void Sort(Comparator<T> comparator)
    {
        items.sort(comparator);
    }
    
    /**
     * Finds the first item that matches the predicate.
     */
    public T Find(FuncT<T, Boolean> predicate)
    {
        for(int i=0; i<items.size(); i++)
        {
            if(predicate.Invoke(items.get(i)))
                return items.get(i);
        }
        return null;
    }
    
    /**
     * Sets current page number and returns it.
     */
    public int SetPage(int page)
    {
        if(page < 1 || page > GetMaxPage())
            return curPage;
        return curPage = page;
    }
    
    /**
     * Sets to next page and returns the new page number.
     */
    public int NextPage() { return SetPage(curPage + 1); }
    
    /**
     * Sets to prev page and returns the new page number.
     */
    public int PrevPage() { return SetPage(curPage - 1); }
    
    /**
     * Returns all items in current page.
     */
    public Iterable<T> GetItemsInPage()
    {
        return new Yieldable<>(yield -> {
            int start = (curPage - 1) * ItemsPerPage;
            int end = Math.min(start + ItemsPerPage, items.size());
            for(int i=start; i<end; i++)
                yield.Return(items.get(i));
        });
    }
    
    /**
     * Returns all items in the list.
     */
    public List<T> GetAllItems() { return items; }
    
    /**
     * Returns the current page number.
     */
    public int GetCurPage() { return curPage; }
    
    /**
     * Returns the max number of pages.
     */
    public int GetMaxPage() { return (items.size() - 1) / ItemsPerPage + 1; }
}
