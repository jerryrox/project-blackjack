/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli;

import game.ui.IView;

/**
 * An abstract IView implementation for specialized View subclasses.
 * @author jerrykim
 */
public abstract class CliView extends CliDisplayer<CliView> implements IView<CliView> {

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
