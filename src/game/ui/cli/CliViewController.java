/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli;

import game.ui.IViewController;
import game.allocation.IDependencyContainer;
import game.data.Yieldable;
import game.debug.Debug;
import java.util.HashMap;

/**
 * Provides a base implementation for a the controller subclasses.
 * In CLI mode, views (screens and overlays) are fundamentally treated the same type.
 * @author jerrykim
 */
public abstract class CliViewController<T extends CliView> implements IViewController<T> {
    
    /**
     * Table of views that have been created and are being cached.
     */
    protected HashMap<Class, T> views = new HashMap<Class, T>();
    
    /**
     * The root displayer which contains the view objects.
     */
    protected CliRoot root;
    
    /**
     * Dependency container received from the game to initialize the views with.
     */
    protected IDependencyContainer dependencies;
    
    
    protected CliViewController(CliRoot root, IDependencyContainer dependencies)
    {
        this.root = root;
        this.dependencies = dependencies;
    }
    
    public @Override Iterable<T> GetActiveViews()
    {
        return new Yieldable<T>(yield -> {
            for(T view : views.values())
            {
                if(view.IsActive())
                    yield.Return(view);
            }
        });
    }
    
    public @Override <TView extends T> TView ShowView(Class<TView> type)
    {
        TView view = FindView(type, true);
        if(view != null)
        {
            view.ShowView();
            OnPostShow(view);
        }
        return view;
    }
    
    public @Override <TView extends T> TView GetView(Class<TView> type)
    {
        return FindView(type, false);
    }
    
    public @Override <TView extends T> boolean IsActive(Class<TView> type)
    {
        for(T view : views.values())
        {
            if(view.getClass() == type)
                return view.IsActive();
        }
        return false;
    }
    
    public @Override <TView extends T> void HideView(Class<TView> type)
    {
        TView view = FindView(type, false);
        if(view != null)
        {
            view.HideView();
            OnPostHide(view);
        }
    }
    
    public @Override void HideView(T view)
    {
        if(view != null)
        {
            view.HideView();
            OnPostHide(view);
        }
    }
    
    /**
     * Event called from showing the specified view.
     * @param view 
     */
    protected abstract void OnPostShow(T view);
    
    /**
     * Event called from hiding the specified view.
     * @param view 
     */
    protected abstract void OnPostHide(T view);
    
    /**
     * Finds and returns the view instance for specified type.
     * @param <TView>
     * @param type
     * @param createIfMissing
     */
    protected <TView extends T> TView FindView(Class<TView> type, boolean createIfMissing)
    {
        T view = views.get(type);
        if(view != null)
            return (TView)view;
        if(!createIfMissing)
            return null;
        try
        {
            TView newView = (TView)root.AddChild(type.newInstance());
            if(newView == null)
                return null;
            views.put(type, newView);
            return newView;
        }
        catch(Exception e)
        {
            // TODO: Handle error
        }
        return null;
    }
    
    /**
     * Returns whether a view of specified type exists in the controller management.
     * @param <TView>
     * @param type
     */
    protected <TView extends T> boolean ViewExists(Class<TView> type)
    {
        return views.containsKey(type);
    }
}
