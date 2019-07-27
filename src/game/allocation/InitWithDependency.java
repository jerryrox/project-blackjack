/*
 * Jerry Kim (18015036), 2019
 */
package game.allocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author jerrykim
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InitWithDependency {
    
    /**
     * Whether null dependency instance can be passed in the arguments.
     */
    public boolean AllowNulls() default true;
}
