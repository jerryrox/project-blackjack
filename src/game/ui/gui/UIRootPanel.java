/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui;

import game.allocation.IDependencyContainer;
import game.allocation.InitWithDependency;
import game.ui.gui.objects.UIScene;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * The root UI element which handles invocation of game logic events such as update and render.
 * @author jerrykim
 */
public class UIRootPanel extends JPanel {
    
    /**
     * Width of the game canvas.
     */
    public static final int Width = 1280;
    
    /**
     * Height of the game canvas.
     */
    public static final int Height = 720;
    
    private final int DesiredDeltaTime = (int)(1000f / 30f);
    private final int MaxDeltaTime = DesiredDeltaTime * 5;
    
    /**
     * Root object of gameobject hierarchy.
     */
    private UIScene scene;
    
    /**
     * Timer instance which enabled game loop logics.
     */
    private Timer gameLooper;
    
    /**
     * The last update time.
     */
    private long lastTime;
    
    /**
     * Input delegate.
     */
    private UIInput input;
    
    
    public UIRootPanel()
    {
        super();
        
        setPreferredSize(new Dimension(Width, Height));
        
        scene = new UIScene();
    }
    
    @InitWithDependency
    private void Init(IDependencyContainer dependencies)
    {
        // Initialize input system.
        input = new UIInput();
        input.Bind(this);
        // Cache input system as dependency.
        dependencies.Cache(input);
        
        // Init scene
        dependencies.Inject(scene);
    }
    
    /**
     * Starts the game loop logic.
     */
    public void StartLoop()
    {
        lastTime = System.currentTimeMillis();
        
        gameLooper = new Timer(DesiredDeltaTime, (e) -> {
            // Calculate delta time
            long curTime = System.currentTimeMillis();
            float deltaTime = (curTime - lastTime) / 1000f;
            lastTime = curTime;
            
            // Clamp deltaTime
            if(deltaTime > MaxDeltaTime)
                deltaTime = MaxDeltaTime;
            
            // Update
            scene.PropagateInput();
            scene.PropagateUpdate(deltaTime);
            input.Update();
            // Render
            repaint();
        });
        gameLooper.start();
    }
    
    public UIScene GetScene() { return scene; }
    
    public @Override void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        // Clear the buffer.
        g.setColor(Color.black);
        g.fillRect(0, 0, Width, Height);
        
        // Start rendering.
        scene.PropagateRender(g);
    }
}
