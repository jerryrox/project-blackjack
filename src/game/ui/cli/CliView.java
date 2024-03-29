/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli;

/**
 * An abstract IView implementation for specialized View subclasses.
 * @author jerrykim
 */
public abstract class CliView<T extends CliView> extends CliDisplayer<T> implements IView<T> {

    protected CliView()
    {
        isActive = false;
    }
    
    public @Override void ShowView()
    {
        if(isActive)
            return;
        SetActive(true);
    }
    
    public @Override void HideView()
    {
        if(!isActive)
            return;
        SetActive(false);
    }
}
