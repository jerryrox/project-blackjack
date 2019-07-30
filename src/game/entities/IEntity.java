/*
 * Jerry Kim (18015036), 2019
 */
package game.entities;

/**
 * Provides a general signature for an entity stored in the storage.
 * Note that when serializing or deserializing the entity using IKeyValueSerializer,
 * it is not necessary to serialize/deserialize the id, since the storage should handle it safely.
 * @author jerrykim
 */
public interface IEntity {
    
    /**
     * Sets the specified value to the entity's id.
     * @param id 
     */
    void SetId(String id);
    
    /**
     * Returns the ID assigned to this entity.
     */
    String GetId();
}
