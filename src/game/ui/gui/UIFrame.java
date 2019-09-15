/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui;

import game.IGame;
import game.allocation.IDependencyContainer;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * Wrapper of JFrame for customization.
 * @author jerrykim
 */
public class UIFrame extends JFrame {
    
    private UIRootPanel rootPanel;
    
    
    public UIFrame(int fps)
    {
        super("Project: Blackjack");
        
        rootPanel = new UIRootPanel(fps);
    }
    
    /**
     * Initializes the frame properties.
     */
    public void Initialize(IDependencyContainer dependencies)
    {
        // Setup frame
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        
        // Override close operation to gracefully exit the applicaion.
        addWindowListener(new WindowAdapter() {
            
            public @Override void windowClosing(WindowEvent windowEvent)
            {
                dependencies.Get(IGame.class).Quit();
            }
        });
        
        // Create add panel to frame
        getContentPane().add(rootPanel);
        
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
        
        // Initialize the panel by injection.
        dependencies.Inject(rootPanel);
        
        // Start game loop.
        rootPanel.StartLoop();
    }
    
    /**
     * Returns the root panel object.
     */
    public UIRootPanel GetRootPanel() { return rootPanel; }
    
}
