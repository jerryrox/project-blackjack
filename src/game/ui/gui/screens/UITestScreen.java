/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.screens;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.animations.AnimationWrap;
import game.animations.Anime;
import game.animations.Easing;
import game.data.Action;
import game.data.ActionT;
import game.debug.Debug;
import game.ui.gui.UIInput;
import game.ui.gui.UIOverlayController;
import game.ui.gui.components.UIAnimator;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UIScreen;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.overlays.UIDialogOverlay;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

/**
 * Screen implementation dedicated for testing purposes.
 * @author jerrykim
 */
public class UITestScreen extends UIScreen {
    
    private ActionT<Float> updateHandler;
    private Action inputHandler;
    
    
    @ReceivesDependency
    private UIInput input;
    
    @ReceivesDependency
    private UIOverlayController overlays;
    
    
    public UITestScreen()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
//        InitForWidgetDisplayTest();
//        InitForInputTest();
        InitForDialogOverlayTest();
    }
    
    public @Override void Update(float deltaTime)
    {
        if(updateHandler != null)
            updateHandler.Invoke(deltaTime);
    }
    
    public @Override boolean UpdateInput()
    {
        if(inputHandler != null)
            inputHandler.Invoke();
        return super.UpdateInput() && false;
    }
    
    private void InitForWidgetDisplayTest()
    {
        UISprite sprite = uiObject.CreateChild().AddComponent(new UISprite());
        sprite.SetSpritename("round-box");
        sprite.SetSize(250, 500);
        sprite.SetColor(Color.blue);
        sprite.SetWrapMode(UISprite.WrapModes.Sliced);
        sprite.SetAlpha(0.5f);
        
        UISprite sprite2 = uiObject.CreateChild().AddComponent(new UISprite());
        sprite2.SetSpritename("icon-arrow-left");
        sprite2.ResetSize();
        sprite2.SetColor(Color.yellow);
        sprite2.SetAlpha(0.75f);
        sprite2.GetTransform().SetLocalPosition(100, 100);
        sprite2.SetSize(64, 64);
        
        UILabel label = uiObject.CreateChild().AddComponent(new UILabel());
        label.SetText("My text");
        label.SetColor(Color.red);
        label.SetAlpha(0.5f);
        label.GetObject().SetDepth(10);
        label.GetTransform().SetLocalPosition(100, 100);
        
        UIAnimator animator = uiObject.AddComponent(new UIAnimator());
        Anime labelAni = animator.CreateAnime("label");
        {
            labelAni.AddSection(0, 30, (progress) -> {
                label.GetTransform().SetLocalPosition(0, Easing.QuadEaseOut(progress, 0, 50, 0));
            });
            labelAni.AddSection(30, 60, (progress) -> {
                label.GetTransform().SetLocalPosition(0, Easing.QuadEaseIn(progress, 50, -50, 0));
            });
            labelAni.PlayAt(0);
            labelAni.SetWrapMode(AnimationWrap.Loop);
        }
        
        Anime sprite2Ani = animator.CreateAnime("sprite2");
        {
            sprite2Ani.AddSection(0, 20, (progress) -> {
                sprite2.SetColor(new Color(
                    Easing.QuadEaseOut(progress, 0.5f, 0.5f, 0),
                    0.5f,
                    0.5f
                ));
            });
            sprite2Ani.AddSection(20, 40, (progress) -> {
                sprite2.SetColor(new Color(
                    Easing.QuadEaseOut(progress, 1f, -0.5f, 0),
                    Easing.QuadEaseOut(progress, 0.5f, 0.5f, 0),
                    0.5f
                ));
            });
            sprite2Ani.AddSection(40, 60, (progress) -> {
                sprite2.SetColor(new Color(
                    0.5f,
                    Easing.QuadEaseOut(progress, 1f, -0.5f, 0),
                    Easing.QuadEaseOut(progress, 0.5f, 0.5f, 0)
                ));
            });
            sprite2Ani.AddSection(60, 80, (progress) -> {
                sprite2.SetColor(new Color(
                    0.5f,
                    0.5f,
                    Easing.QuadEaseOut(progress, 1f, -0.5f, 0)
                ));
            });
            sprite2Ani.PlayAt(0);
            sprite2Ani.SetWrapMode(AnimationWrap.Loop);
        }
    }
    
    private void InitForInputTest()
    {
        UILabel mouseIndicator = uiObject.CreateChild().AddComponent(new UILabel());
        {
            mouseIndicator.GetTransform().SetLocalPosition(0, -300);
            mouseIndicator.SetFontSize(24);
        }
        UILabel keyIndicator = uiObject.CreateChild().AddComponent(new UILabel());
        {
            keyIndicator.GetTransform().SetLocalPosition(0, -200);
            keyIndicator.SetFont("Tahoma", Font.BOLD | Font.ITALIC, 16);
        }
        
        StringBuilder sb = new StringBuilder();
        
        inputHandler = () -> {
            if(input.IsMouseDown())
            {
                mouseIndicator.SetColor(Color.yellow);
                mouseIndicator.SetText("Mouse down!");
            }
            else if(input.IsMouseHold())
            {
                mouseIndicator.SetText("Mouse hold!");
            }
            else if(input.IsMouseUp())
            {
                mouseIndicator.SetColor(Color.white);
                mouseIndicator.SetText("Mouse Up!");
            }
            
            sb.append(input.GetInputString());
            keyIndicator.SetText(sb.toString());
        };
    }
    
    private void InitForDialogOverlayTest()
    {
        inputHandler = () -> {
            if(input.IsKeyDown(KeyEvent.VK_A))
            {
                UIDialogOverlay dialog = overlays.ShowView(UIDialogOverlay.class);
                dialog.SetYesMode("Test message dialog with Yes button only?", () -> {
                    Debug.Log("Pressed Yes");
                });
            }
            else if(input.IsKeyDown(KeyEvent.VK_S))
            {
                UIDialogOverlay dialog = overlays.ShowView(UIDialogOverlay.class);
                dialog.SetYesNoMode("Test message dialog with Yes and No buttons?",
                    () -> {
                        Debug.Log("Pressed Yes");
                    },
                    () -> {
                        Debug.Log("Pressed No");
                    }
                );
            }
        };
    }
    
}
