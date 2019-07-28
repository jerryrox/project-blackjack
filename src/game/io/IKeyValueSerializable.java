/*
 * Jerry Kim (18015036), 2019
 */
package game.io;

import game.io.serializers.KeyValueSerializer;

/**
 * Provides signatures on objects which are to be serializable using a KeyValueSerializer instance.
 * @author jerrykim
 */
public interface IKeyValueSerializable {
    
    /**
     * Store the object's data into specified serializer.
     * @param serializer 
     */
    void Serialize(KeyValueSerializer serializer);
    
    /**
     * Loads the object's data from specified serializer.
     * @param serializer 
     */
    void Deserialize(KeyValueSerializer serializer);
}
