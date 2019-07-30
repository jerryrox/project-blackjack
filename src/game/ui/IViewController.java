/*
 * Jerry Kim (18015036), 2019
 */
package game.ui;

/**
 * Provides a general signature for view control management.
 * @author jerrykim
 */
public interface IViewController<T> {
    
    /**
     * Returns all active views managed by this controller.
     */
    Iterable<T> GetActiveViews();
    
    /**
     * Shows the view of specified type and returns it.
     * The view is created if not already created.
     * @param <TView>
     * @param type
     */
    <TView extends T> TView ShowView(Class<TView> type);
    
    /**
     * Returns the view of specified type, if exists.
     * @param <TView>
     * @param type
     */
    <TView extends T> TView GetView(Class<TView> type);
    
    /**
     * Returns whether a view of specified type is currently active and not null.
     * @param <TView>
     * @param type
     */
    <TView extends T> boolean IsActive(Class<TView> type);
    
    /**
     * Hides the view of specified type, if exists.
     * @param <TView>
     * @param type 
     */
    <TView extends T> void HideView(Class<TView> type);
    
    /**
     * Hides the specified view instance.
     * @param <TView>
     * @param view 
     */
    void HideView(T view);
}
