/*
 * Jerry Kim (18015036), 2019
 */
package game.io.serializers;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * A wrapper over HashMap to provide storage of values.
 * Because the value is a String type, getting different types of values require parsing every time.
 * Therefore, this object is only intended to be used for storage/retrieval purposes.
 * @author jerrykim
 */
public class KeyValueSerializer {
    
    private HashMap<String, String> map = new HashMap<String, String>();
    
    
    /**
     * Returns the number of entries in the map.
     */
    public int GetCount() { return map.size(); }
    
    /**
     * Clears all stored entries in the map.
     */
    public void Clear() { map.clear(); }
    
    /**
     * Removes the entry associated with specified key, if exists.
     * @param key 
     */
    public void Remove(String key) { map.remove(key); }
    
    /**
     * Sets the specified value to the map, replacing if duplicate key was provided.
     * If a null value is provided, the entry matching specified key will be removed, if exists.
     * @param key
     * @param value 
     */
    public void Set(String key, Object value)
    {
        // No invalid key string.
        if(key == null || key.matches("(=|\\|)"))
            return;
        // Remove entry if null value.
        if(value == null)
        {
            Remove(key);
            return;
        }
        
        String sValue = value.toString();
        // No invalid value string.
        if(sValue.matches("(=|\\|)"))
            return;
        map.put(key, sValue);
    }
    
    /**
     * Returns whether a key of specified name exists.
     * @param key
     */
    public boolean ContainsKey(String key) { return map.containsKey(key); }
    
    /**
     * Returns a string value associated with specified key.
     * Default: null
     * @param key
     */
    public String Get(String key) { return Get(key, null); }
    
    /**
     * Returns a string value associated with specified key with default value.
     * @param key
     * @param defaultValue
     */
    public String Get(String key, String defaultValue)
    {
        String value = map.get(key);
        return value == null ? defaultValue : value;
    }
    
    /**
     * Returns an int value associated with specified key.
     * Default: 0
     * @param key
     */
    public int GetInt(String key) { return GetInt(key, 0); }
    
    /**
     * Returns an int value associated with specified key with default value.
     * @param key
     * @param defaultValue
     */
    public int GetInt(String key, int defaultValue)
    {
        try { return Integer.parseInt(Get(key)); }
        catch(Exception e) { return defaultValue; }
    }
    
    /**
     * Returns a float value associated with specified key.
     * Default: 0f
     * @param key
     */
    public float GetFloat(String key) { return GetFloat(key, 0f); }
    
    /**
     * Returns a float value associated with specified key with default value.
     * @param key
     * @param defaultValue
     */
    public float GetFloat(String key, float defaultValue)
    {
        try { return Float.parseFloat(Get(key)); }
        catch(Exception e) { return defaultValue; }
    }
    
    /**
     * Returns a boolean value associated with specified key.
     * Default: false
     * @param key
     */
    public boolean GetBool(String key) { return GetBool(key, false); }
    
    /**
     * Returns a boolean value associated with specified key with default value.
     * @param key
     * @param defaultValue
     */
    public boolean GetBool(String key, boolean defaultValue)
    {
        String sVal = Get(key);
        if(sVal == null)
            return defaultValue;
        return sVal != null && sVal.equalsIgnoreCase("true");
    }
    
    /**
     * Parses the key-value table from specified value string.
     * Additive.
     * @param value 
     */
    public void FromString(String value)
    {
        StringTokenizer tupleTokenizer = new StringTokenizer(value, "|");
        while(tupleTokenizer.hasMoreTokens())
        {
            StringTokenizer keyValueTokenizer = new StringTokenizer(tupleTokenizer.nextToken(), "=");
            
            // Key value token should only have count 2.
            if(keyValueTokenizer.countTokens() != 2)
                continue;
            
            // Set value to table.
            Set(keyValueTokenizer.nextToken(), keyValueTokenizer.nextToken());
        }
    }
    
    public @Override String toString()
    {
        StringBuilder sb = new StringBuilder();
        map.entrySet().forEach(pair -> {
            if(sb.length() > 0)
                sb.append('|');
            sb.append(pair.getKey()).append('=').append(pair.getValue());
        });
        return sb.toString();
    }
}
