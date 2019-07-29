/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli;

import game.allocation.IDependencyContainer;
import game.data.Yieldable;
import game.ui.IDisplayer;
import game.ui.IView;
import game.ui.IViewController;
import java.util.HashMap;

/**
 * Provides a base implementation for a the controller subclasses.
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
                yield.Return(view);
        });
    }
    
    public @Override <TView extends T> TView ShowView(Class<TView> type)
    {
        return null;
    }
    
    /*
    public T ShowView<T> () where T : TView, new()
    {
        var view = FindOrCreateView<T>();
        view.ShowView();
        OnPostShow(view);
        return view;
    }

    public T GetView<T>() where T : TView, new() { return FindOrCreateView<T>(false); }

    public bool IsActive<T> () where T : TView, new() { return ActiveViews.OfType<T>().FirstOrDefault() != null; }

    public void HideView<T> () where T : TView, new()
    {
            if(!ViewExists<T>())
                    return;

            var view = FindOrCreateView<T>();
            view.HideView();
            OnPostHide(view);
    }

    public void HideView(TView view)
    {
            view.HideView();
            OnPostHide(view);
    }

    /// <summary>
    /// Handles additional processes after showing the specified view.
    /// </summary>
    protected virtual void OnPostShow(TView view) {}

    /// <summary>
    /// Handles additional processes after hiding the specified view.
    /// </summary>
    protected virtual void OnPostHide(TView view) {}
    */
    
    
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
