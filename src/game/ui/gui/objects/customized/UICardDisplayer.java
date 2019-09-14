/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects.customized;

import game.allocation.InitWithDependency;
import game.animations.Anime;
import game.animations.EaseType;
import game.animations.Easing;
import game.data.Action;
import game.data.Vector2;
import game.debug.Debug;
import game.graphics.ColorPreset;
import game.rulesets.Card;
import game.ui.gui.components.UIAnimator;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.objects.UIObject;

/**
 * Displays a card.
 * @author jerrykim
 */
public class UICardDisplayer extends UIObject {
    
    public static final int CardWidth = 280;
    
    private UIObject scaler;
    private UIObject frontSide;
    private UIObject backSide;
    
    private UISprite[] patterns = new UISprite[11];
    private UILabel numberLabel;
    
    private Anime flipToFrontAni;
    private Anime flipToBackAni;
    
    private Action onFrontAni;
    private Action onBackAni;
    
    private Card card;
    
    
    public UICardDisplayer()
    {
        super();
    }
    
    @InitWithDependency
    private void Init(ColorPreset colors)
    {
        scaler = CreateChild();
        
        UISprite frame = scaler.AddComponent(new UISprite());
        {
            frame.SetSpritename("card-frame");
            frame.SetWrapMode(UISprite.WrapModes.Sliced);
            frame.SetSize(CardWidth, 400);
        }
        
        frontSide = scaler.CreateChild();
        {
            for(int i=0; i<patterns.length; i++)
            {
                UISprite pattern = patterns[i] = frontSide.CreateChild().AddComponent(new UISprite());
                {
                    pattern.SetSpritename("pattern");
                    pattern.ResetSize();
                    pattern.SetColor(colors.CardPattern);
                    pattern.SetEnabled(false);
                }
            }

            numberLabel = frontSide.CreateChild().AddComponent(new UILabel());
            {
                numberLabel.SetFontSize(36);
                numberLabel.SetColor(colors.Darker);
                numberLabel.GetTransform().SetLocalPosition(0, 150);
            }
        }
        
        backSide = scaler.CreateChild();
        {
            UISprite back = backSide.CreateChild().AddComponent(new UISprite());
            {
                back.SetSpritename("card-back");
                back.ResetSize();
            }
        }
        
        UIAnimator animator = AddComponent(new UIAnimator());
        flipToFrontAni = animator.CreateAnime("fliptofront");
        {
            flipToFrontAni.AddSection(0, 9, (progress) -> {
                GetTransform().SetLocalScale(Easing.QuadEaseOut(progress, 1, -1, 0), 1);
            });
            flipToFrontAni.AddEvent(9, () -> {
                SetSide(true, false);
            });
            flipToFrontAni.AddSection(9, 18, (progress) -> {
                GetTransform().SetLocalScale(Easing.QuadEaseIn(progress, 0, 1, 0), 1);
            });
            flipToFrontAni.AddEvent(18, () -> {
                if(onFrontAni != null)
                    onFrontAni.Invoke();
                onFrontAni = null;
            });
        }
        flipToBackAni = animator.CreateAnime("cliptoback");
        {
            flipToBackAni.AddSection(0, 9, (progress) -> {
                GetTransform().SetLocalScale(Easing.QuadEaseOut(progress, 1, -1, 0), 1);
            });
            flipToBackAni.AddEvent(9, () -> {
                SetSide(false, false);
            });
            flipToBackAni.AddSection(9, 18, (progress) -> {
                GetTransform().SetLocalScale(Easing.QuadEaseIn(progress, 0, 1, 0), 1);
            });
            flipToBackAni.AddEvent(18, () -> {
                if(onBackAni != null)
                    onBackAni.Invoke();
                onBackAni = null;
            });
        }
        
        // Start from back side.
        SetSide(false, false);
    }
    
    /**
     * Sets scale of the card displayer.
     */
    public void SetScale(float scale)
    {
        scaler.GetTransform().SetLocalScale(scale, scale);
    }
    
    /**
     * Whether the card flip animation is currently playing.
     */
    public boolean IsAnimating() { return flipToFrontAni.IsPlaying() || flipToBackAni.IsPlaying(); }
    
    /**
     * Returns whether the card is displaying the front side.
     * @return 
     */
    public boolean IsFrontSide() { return frontSide.IsActive(); }
    
    /**
     * Sets callback functions for animation end event.
     */
    public void SetAnimationCallbacks(Action onFront, Action onBack)
    {
        onFrontAni = onFront;
        onBackAni = onBack;
    }
    
    /**
     * Sets the side to be displayed.
     */
    public void SetSide(boolean isFront, boolean animate)
    {
        if(!animate)
        {
            frontSide.SetActive(isFront);
            backSide.SetActive(!isFront);
            return;
        }
        
        if(isFront)
        {
            if(!IsFrontSide())
            {
                flipToBackAni.Stop();
                flipToFrontAni.PlayAt(0);
            }
        }
        else
        {
            if(IsFrontSide())
            {
                flipToFrontAni.Stop();
                flipToBackAni.PlayAt(0);
            }
        }
    }
    
    /**
     * Sets the card instance to be displayed.
     */
    public void SetCard(Card card)
    {
        this.card = card;
        SetupCard();
    }
    
    private void SetupCard()
    {
        if(card == null)
            return;
        
        // Setup patterns
        Vector2[] patternPos = CalculatePatternPos(card.GetNumber());
        for(int i=0; i<patterns.length; i++)
        {
            UISprite pattern = patterns[i];
            if(i >= card.GetNumber())
            {
                pattern.SetEnabled(false);
            }
            else
            {
                pattern.SetEnabled(true);
                pattern.GetTransform().SetLocalPosition(patternPos[i]);
            }
        }
        
        // Setup label
        numberLabel.SetText(String.valueOf(card.GetNumber()));
    }
    
    private Vector2[] CalculatePatternPos(int number)
    {
        Vector2[] positions = new Vector2[number];
        
        // Find out how many rows are needed.
        int cumulativeNum = number;
        int rowCount = 1;
        while(true)
        {
            int patternsInRow = rowCount % 2 == 1 ? 3 : 2;
            cumulativeNum -= patternsInRow;
            if(cumulativeNum <= 0)
                break;
            rowCount ++;
        }
        
        // Calculate top row position.
        final float centerRowPos = -30f;
        final float rowDistance = 60;
        final float colDistance = 76;
        float topRowPos = centerRowPos - ((rowCount - 1) * rowDistance / 2);
        
        int posInx = 0;
        for(int i=0; i<rowCount; i++)
        {
            int patternsInRow = i % 2 == 0 ? 3 : 2;
            if(rowCount == 1)
                patternsInRow = positions.length;
            else if(i % 2 == 0 && positions.length - posInx == 1)
                patternsInRow = 1;
            
            float leftPos = -((patternsInRow - 1) * colDistance / 2);
            for(int c=0; c<patternsInRow; c++)
            {
                if(posInx >= positions.length)
                    break;
                
                positions[posInx] = new Vector2(leftPos + c * colDistance, topRowPos + i * rowDistance);
                posInx ++;
            }
        }
        
        return positions;
    }
}
