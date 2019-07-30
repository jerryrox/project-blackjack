/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli;

import game.allocation.IDependencyContainer;

/**
 * View controller implementation for overlay type views.
 * Overlay is a type of view where there may be multiple, different types of views
 * displayed on the window simultaneously.
 * Views like a Dialog is an example.
 * @author jerrykim
 */
public class CliOverlayController extends CliViewController<CliView> {

    public CliOverlayController(CliRoot root, IDependencyContainer dependencies)
    {
        super(root, dependencies);
    }

    protected @Override void OnPostShow(CliView view) {}

    protected @Override void OnPostHide(CliView view) {}
}
