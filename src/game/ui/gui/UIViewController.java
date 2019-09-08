/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui;

import game.allocation.IDependencyContainer;
import game.data.Yieldable;
import game.debug.Debug;
import game.ui.IViewController;
import game.ui.gui.components.ui.UIView;
import game.ui.gui.objects.UIObject;
import java.util.HashMap;

/**
 * Controls the way which UIView objects are shown and hidden.
 * @author jerrykim
 */
public abstract class UIViewController<T extends UIView> implements IViewController<T> {

    /**
     * Table of view instances mapped to their specific types.
     */
    protected HashMap<Class, T> views = new HashMap<>();
    
    /**
     * The UIObject instance where views will be created.
     */
    protected UIObject root;
    
    
    protected UIViewController(UIObject root)
    {
        this.root = root;
    }
    
    public @Override Iterable<T> GetActiveViews()
    {
        return new Yieldable<T>(yield -> {
            views.values().forEach(v -> {
                if(v.IsShowing())
                    yield.Return(v);
            });
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
    
    public @Override <TView extends T> TView GetView(Class<TView> type) { return FindView(type, false); }
    
    public @Override <TView extends T> boolean IsActive(Class<TView> type)
    {
        for(T view : views.values())
        {
            if(view.getClass() == type)
                return view.IsShowing();
        }
        return false;
    }
    
    public @Override <TView extends T> void HideView(Class<TView> type)
    {
        HideView(FindView(type, false));
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
     * Event called after showing the specified view.
     */
    protected abstract void OnPostShow(T view);
    
    /**
     * Event called after hiding the specified view.
     */
    protected abstract void OnPostHide(T view);
    
    /**
     * Finds and returns the view instance for specified type.
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
            TView newView = (TView)root.CreateChild(type.getName()).AddComponent(type.newInstance());
            if(newView == null)
                return null;
            views.put(type, newView);
            return newView;
        }
        catch(Exception e)
        {
            Debug.LogError("UIViewController.FindView - Failed to create view for type: " + type.getName());
        }
        return null;
    }
}
