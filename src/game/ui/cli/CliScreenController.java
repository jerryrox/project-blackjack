/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli;

import game.allocation.IDependencyContainer;

/**
 * View controller implementation for screen type views.
 * Screen is a type of view where there can't be multiple screens active at the same time.
 * Views like Home or Shop or Game are some examples.
 * @author jerrykim
 */
public class CliScreenController extends CliViewController<CliView> {

    
    public CliScreenController(CliRoot root, IDependencyContainer dependencies)
    {
        super(root, dependencies);
    }
    
    protected @Override void OnPostShow(CliView view)
    {
        // Hide all other views.
        for(CliView v : views.values())
        {
            if(v.IsActive() && view != v)
                HideView(v);
        }
    }
    
    protected @Override void OnPostHide(CliView view) {}
}
