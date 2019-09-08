/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui;

import game.allocation.IDependencyContainer;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 * Wrapper of JFrame for customization.
 * @author jerrykim
 */
public class UIFrame extends JFrame {
    
    private UIRootPanel rootPanel;
    
    
    public UIFrame()
    {
        super("Blackjack Knights");
    }
    
    /**
     * Initializes the frame properties.
     */
    public void Initialize(IDependencyContainer dependencies)
    {
        // Setup frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Create root panel and add to frame
        rootPanel = new UIRootPanel();
        getContentPane().add(rootPanel);
        // Add the root panel as dependency since certain drawable elements may need them.
        dependencies.Cache(rootPanel);
        
        // Resize frame.
        pack();
        
        // Position frame to center of screen.
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension d = toolkit.getScreenSize();
        int width = (int)d.getWidth();
        int height = (int)d.getHeight();
        setLocation(
            width / 2 - getSize().width / 2,
            height / 2 - getSize().height / 2
        );
        
        // Display the frame.
        setVisible(true);
        
        // Initialize the scene by injection.
        dependencies.Inject(rootPanel.GetScene());
        
        // Start game loop.
        rootPanel.StartLoop();
    }
}
