/*
 * Jerry Kim (18015036), 2019
 */
package game.data;

import java.util.ArrayList;

/**
 * An event handler to simulate what C# provides with its event keyword.
 * @author jerrykim
 */
public class Events<T extends Object> {
    
    private ArrayList<ActionT<T>> callbacks = new ArrayList<>();
    
    public Events()
    {
    }
    
    /**
     * Adds the specified callback to listen to this event.
     */
    public void Add(ActionT<T> callback) { callbacks.add(callback); }
    
    /**
     * Removes the specified callback from this event.
     */
    public void Remove(ActionT<T> callback) { callbacks.remove(callback); }
    
    /**
     * Fires all callbacks registered to this event.
     */
    public void Invoke(T value)
    {
        for(int i=0; i<callbacks.size(); i++)
        {
            ActionT<T> callback = callbacks.get(i);
            if(callback == null)
            {
                callbacks.remove(i--);
                continue;
            }
            callback.Invoke(value);
        }
    }
}
