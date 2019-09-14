/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects.customized;

import game.allocation.InitWithDependency;
import game.animations.Anime;
import game.animations.Easing;
import game.data.ActionT;
import game.data.Func;
import game.rulesets.items.ItemInfo;
import game.ui.gui.components.UIAnimator;
import game.ui.gui.components.UITransform;
import game.ui.gui.objects.UIObject;
import java.util.ArrayList;

/**
 * Holds list of items as a list.
 * @author jerrykim
 */
public class UIItemList<T extends UIObject & IUIItemCell> extends UIObject {
    
    public final int ItemsPerPage;
    
    private ArrayList<T> cells = new ArrayList<>();
    
    private Func<T> createCell;
    private ActionT<ItemInfo> callback;
    
    private int curCellCount;
    
    private UIAnimator animator;
    private Anime showAni;
    
    
    public UIItemList(int itemsPerPage, Func<T> createCell)
    {
        super();
        this.ItemsPerPage = itemsPerPage;
        this.createCell = createCell;
    }
    
    @InitWithDependency
    private void Init()
    {
        animator = AddComponent(new UIAnimator());
        showAni = animator.CreateAnime("show");
        showAni.AddEvent(0, () -> {
            for(int i=0; i<ItemsPerPage; i++)
                cells.get(i).SetActive(false);
        });
        
        int height = 0;
        for(int i=0; i<ItemsPerPage; i++)
        {
            T cell = AddChild(createCell.Invoke());
            UITransform transform = cell.GetTransform();
            
            // Disable cell and add to list.
            cell.SetActive(false);
            cells.add(cell);
            
            // Position cell.
            if(height == 0)
                height = cell.GetHeight() + 4;
            else
                transform.SetLocalPosition(0, height * i);
            
            // Setup callback.
            cell.SetCallback(() -> {
                if(cell.IsActive() && cell.GetItem() != null && callback != null)
                    callback.Invoke(cell.GetItem());
            });
            
            // Build animation for show ani.
            int offset = i * 6;
            int endFrame = offset + 18;
            int index = i;
            showAni.AddEvent(offset, () -> {
                cell.SetActive(index < curCellCount);
            });
            showAni.AddSection(offset, endFrame, (progress) -> {
                float scale = Easing.BackEaseOut(progress, 0.75f, 0.25f, 0.5f);
                transform.SetLocalScale(scale, scale);
                cell.GetPanel().SetAlpha(Easing.QuadEaseOut(progress, 0, 1, 0));
            });
        }
    }
    
    /**
     * Sets callback for item selection.
     */
    public void SetCallback(ActionT<ItemInfo> callback)
    {
        this.callback = callback;
    }
    
    /**
     * Clears all cells from screen.
     */
    public void Clear()
    {
        for(int i=0; i<ItemsPerPage; i++)
            cells.get(i).SetActive(false);
        curCellCount = 0;
        showAni.Stop();
    }
    
    /**
     * Shows the next cell with specified info and value.
     */
    public void ShowCell(ItemInfo item, int value)
    {
        if(curCellCount >= ItemsPerPage)
            return;
        
        T cell = cells.get(curCellCount++);
        cell.Setup(item, value);
    }
    
    /**
     * Plays cell showing animation.
     */
    public void PlayShowAni()
    {
        showAni.PlayAt(0);
    }
}
