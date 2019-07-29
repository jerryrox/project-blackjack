/*
 * Jerry Kim (18015036), 2019
 */
package game.ui;

/**
 * Provides a general signature for View type displayers.
 * @author jerrykim
 */
public interface IView<T> extends IDisplayer<T> {
    
    /**
     * Starts showing the view.
     */
    void ShowView();
    
    /**
     * Starts hiding the view.
     */
    void HideView();
}
