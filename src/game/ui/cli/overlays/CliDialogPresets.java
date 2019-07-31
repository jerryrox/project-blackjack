/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.overlays;

import game.ui.cli.commands.CommandInfo;

/**
 * Static presets for CliDialogOverlays.
 * @author jerrykim
 */
public final class CliDialogPresets {
    
    /**
     * Quit dialog preset.
     * @param overlay
     * @param onYes
     * @param onNo 
     */
    public static void SetQuit(CliDialogOverlay overlay, CommandInfo.CallbackHandler onYes, CommandInfo.CallbackHandler onNo)
    {
        SetYesNo(overlay, "Are you sure you want to quit?", onYes, onNo);
    }
    
    /**
     * Game exist dialog preset.
     * @param overlay
     * @param onYes
     * @param onNo 
     */
    public static void SetExitGame(CliDialogOverlay overlay, CommandInfo.CallbackHandler onYes, CommandInfo.CallbackHandler onNo)
    {
        SetYesNo(overlay, "Are you sure you want to exit the game play? Your progress will not be saved!", onYes, onNo);
    }
    
    /**
     * A general yes or no dialog preset.
     * @param overlay
     * @param message
     * @param onYes
     * @param onNo
     */
    public static void SetYesNo(CliDialogOverlay overlay, String message, CommandInfo.CallbackHandler onYes, CommandInfo.CallbackHandler onNo)
    {
        overlay.SetDialog(message, new CommandInfo("yes", onYes), new CommandInfo("no", onNo));
    }
}
