 /*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects;

import game.allocation.InitWithDependency;
import game.debug.Debug;
import game.ui.Pivot;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UISprite;
import java.awt.Color;

/**
 * Interactive object for text input via keyboard.
 * @author jerrykim
 */
public class UITextBox extends UICursorInteractive {
    
    public static final String AlphanumericRestriction = "[^a-zA-Z0-9]";
    
    private final float CaretDuration = 0.3f;
    
    /**
     * Whether the textbox has focus and can be typed on.
     */
    protected boolean isFocused = false;
    
    private int maxChars = 16;
    private float caretTime = CaretDuration;
    
    private UISprite bg;
    private UISprite caret;
    private UILabel label;
    
    private String restriction = null;
    
    /**
     * A temporary string builder used for receiving character inputs
     * and displaying it on the label.
     */
    private StringBuilder tempBuilder = new StringBuilder();
    
    
    public UITextBox()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        bg = CreateChild().AddComponent(new UISprite());
        {
            bg.SetSpritename("round-box");
            bg.SetWrapMode(UISprite.WrapModes.Sliced);
        }
        caret = CreateChild().AddComponent(new UISprite());
        {
            caret.SetSpritename("box");
            caret.SetSize(2, 24);
        }
        label = CreateChild().AddComponent(new UILabel());
        {
        }
        
        SetTarget(bg);
        SetWidth(200);
    }
    
    /**
     * Sets the color of bg sprite.
     */
    public void SetBgColor(Color color) { bg.SetColor(color); }
    
    /**
     * Sets the color of label.
     */
    public void SetTextColor(Color color)
    {
        label.SetColor(color);
        caret.SetColor(color);
    }
    
    /**
     * Sets the max possible number of characters inputtable on this text box.
     * If 0, it is unlimited.
     */
    public void SetMaxCharacters(int chars)
    {
        if(chars > 0 && chars < maxChars)
            SetValue(label.GetText());
        maxChars = chars;
    }
    
    /**
     * Sets the width of the input box.
     * @param width 
     */
    public void SetWidth(int width)
    {
        bg.SetSize(width, 36);
    }
    
    /**
     * Sets currently inputted value.
     */
    public void SetValue(String value)
    {
        if(value == null)
            value = "";
        // Apply max characters.
        if(maxChars > 0)
            value = value.substring(0, Math.min(value.length(), maxChars));
        // Apply restriction pattern.
        label.SetText(value.replaceAll(restriction, ""));
    }
    
    /**
     * Returns the text value of the input.
     */
    public String GetValue() { return label.GetText(); }
    
    /**
     * Sets a regular expression to limit input to certain characters only.
     */
    public void SetRestriction(String restriction)
    {
        this.restriction = restriction;
        if(restriction != null)
            SetValue(label.GetText());
    }
    
    /**
     * Sets focused state of the text box.
     */
    public void SetFocus(boolean focus)
    {
        if(isFocused != focus)
        {
            isFocused = focus;
            
            if(focus)
                OnFocus();
            else
                OnUnfocus();
        }
    }
    
    public void OnFocus() {}
    
    public void OnUnfocus() {}
    
    public @Override void Update(float deltaTime)
    {
        super.Update(deltaTime);
        
        if(isFocused)
        {
            caretTime -= deltaTime;
            if(caretTime <= 0)
            {
                caretTime = CaretDuration;
                caret.SetEnabled(!caret.isEnabled);
            }
            
            caret.GetTransform().SetLocalPosition(
                label.GetWidth() / 2 + 1,
                0
            );
        }
        else
        {
            caret.SetEnabled(false);
        }
    }
    
    public @Override boolean UpdateInput()
    {
        boolean superUpdateInput = super.UpdateInput();
        
        if(input.IsMouseDown())
        {
            if(IsCursorOver(input.GetMousePosition()))
                SetFocus(true);
            else
                SetFocus(false);
        }
        
        if(isFocused)
        {
            // Get input buffer
            String text = input.GetInputString()
                    .replace('\n', (char)0)
                    .replace('\r', (char)0)
                    .replace('\t', (char)0);
            if(text.length() > 0)
            {
                tempBuilder.append(label.GetText());
                for(int i=0; i<text.length(); i++)
                {
                    // Check escaped character sequence
                    if(i < text.length()-1)
                    {
                        if(text.charAt(i) == '\\')
                        {
                            boolean consumedSequence = false;
                            switch(text.charAt(i+1))
                            {
                            // Backspace
                            case 'b':
                                consumedSequence = true;
                                if(tempBuilder.length() > 0)
                                    tempBuilder.setLength(tempBuilder.length() - 1);
                                break;
                            }
                            
                            if(consumedSequence)
                            {
                                i++;
                                break;
                            }
                        }
                    }
                    // Append normally.
                    tempBuilder.append(text.charAt(i));
                }
                
                // Set final value.
                SetValue(tempBuilder.toString());
                
                // Reset string builder.
                tempBuilder.setLength(0);
            }
        }
        
        return superUpdateInput;
    }
}
