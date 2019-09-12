/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.screens;

import game.allocation.InitWithDependency;
import game.graphics.ColorPreset;
import game.rulesets.Card;
import game.ui.Pivot;
import game.ui.gui.UIScreenController;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UIScreen;
import game.ui.gui.objects.customized.UICardDisplayer;
import game.ui.gui.objects.customized.UIRoundBoxButton;
import java.awt.Desktop;
import java.net.URI;

/**
 * Help screen.
 * @author jerrykim
 */
public class UIHelpScreen extends UIScreen {
    
    private UIRoundBoxButton developerButton;
    private UIRoundBoxButton backButton;
    
    
    public UIHelpScreen()
    {
        super();
    }
    
    @InitWithDependency
    private void Init(UIScreenController screens, ColorPreset colors)
    {
        UICardDisplayer card_1 = uiObject.AddChild(new UICardDisplayer());
        {
            card_1.SetCard(new Card(1));
            card_1.GetTransform().SetLocalScale(0.25f, 0.25f);
            card_1.GetTransform().SetLocalPosition(-150, -170);
            card_1.SetSide(true, false);
        }
        UICardDisplayer card_11 = uiObject.AddChild(new UICardDisplayer());
        {
            card_11.SetCard(new Card(11));
            card_11.GetTransform().SetLocalScale(0.25f, 0.25f);
            card_11.GetTransform().SetLocalPosition(150, -170);
            card_11.SetSide(true, false);
        }
        
        UILabel title = uiObject.CreateChild().AddComponent(new UILabel());
        {
            title.SetText("How to play");
            title.SetFontSize(24);
            title.GetTransform().SetLocalPosition(0, -170);
        }
        
        UILabel content = uiObject.CreateChild().AddComponent(new UILabel());
        {
            content.SetPivot(Pivot.Top);
            content.SetText(GetInstructions());
            content.GetTransform().SetLocalPosition(0, -60);
            /*
║         The players will share the same deck, with cards ranging from 1 to 11. No doubles.       ║
║                  Players take turns drawing a card to reach 21 or close below 21.                ║
║                          Exceeding the total of 21 is considered losing.                         ║
║           When both players decide to skip turn, the phase is evaluated for the winner.          ║
║                                                                                                  ║
║          The winner inflicts their damage stat to the opponent with some critical chance.        ║
║                     If the phase ended in a draw, both players receive damage.                   ║
║                                                                                                  ║
║                  * Damaging when the card total is 21 has 200% critical chance. *                ║
║                             * Critical strike inflicts 150% damage. *
            
            */
        }
        
        uiObject.AddChild(developerButton = new UIRoundBoxButton());
        {
            developerButton.GetTransform().SetLocalPosition(-180, 280);
            developerButton.SetBgColor(colors.Neutral);
            developerButton.SetWidth(300);
            developerButton.SetLabel("Developer's Github");
            developerButton.Clicked.Add((arg) -> {
                OnDeveloperButton();
            });
        }
        uiObject.AddChild(backButton = new UIRoundBoxButton());
        {
            backButton.GetTransform().SetLocalPosition(180, 280);
            backButton.SetBgColor(colors.Warning);
            backButton.SetTextColor(colors.Dark);
            backButton.SetWidth(300);
            backButton.SetLabel("Back to main");
            backButton.Clicked.Add((arg) -> {
                screens.ShowView(UIMainScreen.class);
            });
        }
    }
    
    private String GetInstructions()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Each player starts with one random card which is not visible to the opponent.").append('\n');
        sb.append("The players will share the same deck, with cards ranging from 1 to 11. No doubles.").append('\n');
        sb.append("Players take turns drawing a card to reach 21 or close below 21.").append('\n');
        sb.append("Exceeding the total of 21 is considered losing.").append('\n');
        sb.append("When both players decide to skip turn, the phase is evaluated for the winner.").append('\n');
        sb.append("").append('\n');
        sb.append("The winner inflicts their damage stat to the opponent with some critical chance.").append('\n');
        sb.append("If the phase ended in a draw, both players receive damage.").append('\n');
        sb.append("").append('\n');
        sb.append("* Damaging when the card total is 21 has 200% critical chance. *").append('\n');
        sb.append("* Critical strike inflicts 150% damage. *").append('\n');
        return sb.toString();
    }
    
    private void OnDeveloperButton()
    {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE))
        {
            try
            {
                Desktop.getDesktop().browse(new URI("https://github.com/jerryrox"));
            }
            catch(Exception e) {}
        }
    }
}
