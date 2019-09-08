/*
 * Jerry Kim (18015036), 2019
 */
package game.animations;

import java.util.HashMap;

/**
 * Java port of my C# Easing library.
 * https://github.com/jerryrox/Renko-L/blob/master/Utility/Easing.cs
 * 
 * Provides a series of functions that calculate interpolation values.
 * @author jerrykim
 */
public class Easing {
    
    /**
     * Table of easing handlers mapped to ease types.
     */
    private static final HashMap<EaseType, EaseHandler> Handlers;
    
    
    static
    {
        Handlers = new HashMap<>();
        Handlers.put(EaseType.BackEaseIn, (t, b, c, d) -> BackEaseIn(t, b, c, d));
        Handlers.put(EaseType.BackEaseInOut, (t, b, c, d) -> BackEaseInOut(t, b, c, d));
        Handlers.put(EaseType.BackEaseOut, (t, b, c, d) -> BackEaseOut(t, b, c, d));
        Handlers.put(EaseType.BackEaseOutIn, (t, b, c, d) -> BackEaseOutIn(t, b, c, d));
        Handlers.put(EaseType.BounceEaseIn, (t, b, c, d) -> BounceEaseIn(t, b, c, d));
        Handlers.put(EaseType.BounceEaseInOut, (t, b, c, d) -> BounceEaseInOut(t, b, c, d));
        Handlers.put(EaseType.BounceEaseOut, (t, b, c, d) -> BounceEaseOut(t, b, c, d));
        Handlers.put(EaseType.BounceEaseOutIn, (t, b, c, d) -> BounceEaseOutIn(t, b, c, d));
        Handlers.put(EaseType.CircEaseIn, (t, b, c, d) -> CircEaseIn(t, b, c, d));
        Handlers.put(EaseType.CircEaseInOut, (t, b, c, d) -> CircEaseInOut(t, b, c, d));
        Handlers.put(EaseType.CircEaseOut, (t, b, c, d) -> CircEaseOut(t, b, c, d));
        Handlers.put(EaseType.CircEaseOutIn, (t, b, c, d) -> CircEaseOutIn(t, b, c, d));
        Handlers.put(EaseType.CubicEaseIn, (t, b, c, d) -> CubicEaseIn(t, b, c, d));
        Handlers.put(EaseType.CubicEaseInOut, (t, b, c, d) -> CubicEaseInOut(t, b, c, d));
        Handlers.put(EaseType.CubicEaseOut, (t, b, c, d) -> CubicEaseOut(t, b, c, d));
        Handlers.put(EaseType.CubicEaseOutIn, (t, b, c, d) -> CubicEaseOutIn(t, b, c, d));
        Handlers.put(EaseType.EaseIn, (t, b, c, d) -> EaseIn(t, b, c, d));
        Handlers.put(EaseType.EaseOut, (t, b, c, d) -> EaseOut(t, b, c, d));
        Handlers.put(EaseType.ElasticEaseIn, (t, b, c, d) -> ElasticEaseIn(t, b, c, d));
        Handlers.put(EaseType.ElasticEaseInOut, (t, b, c, d) -> ElasticEaseInOut(t, b, c, d));
        Handlers.put(EaseType.ElasticEaseOut, (t, b, c, d) -> ElasticEaseOut(t, b, c, d));
        Handlers.put(EaseType.ElasticEaseOutIn, (t, b, c, d) -> ElasticEaseOutIn(t, b, c, d));
        Handlers.put(EaseType.ExpoEaseIn, (t, b, c, d) -> ExpoEaseIn(t, b, c, d));
        Handlers.put(EaseType.ExpoEaseInOut, (t, b, c, d) -> ExpoEaseInOut(t, b, c, d));
        Handlers.put(EaseType.ExpoEaseOut, (t, b, c, d) -> ExpoEaseOut(t, b, c, d));
        Handlers.put(EaseType.ExpoEaseOutIn, (t, b, c, d) -> ExpoEaseOutIn(t, b, c, d));
        Handlers.put(EaseType.Linear, (t, b, c, d) -> Linear(t, b, c, d));
        Handlers.put(EaseType.QuadEaseIn, (t, b, c, d) -> QuadEaseIn(t, b, c, d));
        Handlers.put(EaseType.QuadEaseInOut, (t, b, c, d) -> QuadEaseInOut(t, b, c, d));
        Handlers.put(EaseType.QuadEaseOut, (t, b, c, d) -> QuadEaseOut(t, b, c, d));
        Handlers.put(EaseType.QuadEaseOutIn, (t, b, c, d) -> QuadEaseOutIn(t, b, c, d));
        Handlers.put(EaseType.QuartEaseIn, (t, b, c, d) -> QuartEaseIn(t, b, c, d));
        Handlers.put(EaseType.QuartEaseInOut, (t, b, c, d) -> QuartEaseInOut(t, b, c, d));
        Handlers.put(EaseType.QuartEaseOut, (t, b, c, d) -> QuartEaseOut(t, b, c, d));
        Handlers.put(EaseType.QuartEaseOutIn, (t, b, c, d) -> QuartEaseOutIn(t, b, c, d));
        Handlers.put(EaseType.QuintEaseIn, (t, b, c, d) -> QuintEaseIn(t, b, c, d));
        Handlers.put(EaseType.QuintEaseInOut, (t, b, c, d) -> QuintEaseInOut(t, b, c, d));
        Handlers.put(EaseType.QuintEaseOut, (t, b, c, d) -> QuintEaseOut(t, b, c, d));
        Handlers.put(EaseType.QuintEaseOutIn, (t, b, c, d) -> QuintEaseOutIn(t, b, c, d));
        Handlers.put(EaseType.SineEaseIn, (t, b, c, d) -> SineEaseIn(t, b, c, d));
        Handlers.put(EaseType.SineEaseInOut, (t, b, c, d) -> SineEaseInOut(t, b, c, d));
        Handlers.put(EaseType.SineEaseOut, (t, b, c, d) -> SineEaseOut(t, b, c, d));
        Handlers.put(EaseType.SineEaseOutIn, (t, b, c, d) -> SineEaseOutIn(t, b, c, d));
    }
    
    /**
     * Handles easing of specified ease type.
     */
    public static float Ease(EaseType type, float t, float b, float c, float d) { return Handlers.get(type).Invoke(t, b, c, d); }
    
    /// <summary>
    /// Linear change in value.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <returns>The correct value.</returns>
    public static float Linear( float t, float b, float c, float d ) {
            return c * t + b;
    }

    /// <summary>
    /// Same easing effect as "QuadEaseOut".
    /// </summary>
    /// <returns>The out.</returns>
    /// <param name="t">T.</param>
    /// <param name="b">The blue component.</param>
    /// <param name="c">C.</param>
    /// <param name="d">D.</param>
    public static float EaseOut( float t, float b, float c, float d ) {
            return QuadEaseOut(t, b, c, d);
    }

    /// <summary>
    /// Same easing effect as "QuadEaseIn".
    /// </summary>
    /// <returns>The in.</returns>
    /// <param name="t">T.</param>
    /// <param name="b">The blue component.</param>
    /// <param name="c">C.</param>
    /// <param name="d">D.</param>
    public static float EaseIn( float t, float b, float c, float d ) {
            return QuadEaseIn(t, b, c, d);
    }

    /// <summary>
    /// Easing equation function for an exponential (2^t) easing out: 
    /// decelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <returns>The correct value.</returns>
    public static float ExpoEaseOut( float t, float b, float c, float d ) {
            return ( t == 1 ) ? b + c : c * ( -(float)Math.pow( 2, -10 * t ) + 1 ) + b;
    }

    /// <summary>
    /// Easing equation function for an exponential (2^t) easing in: 
    /// accelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float ExpoEaseIn( float t, float b, float c, float d ) {
            return ( t == 0 ) ? b : c * (float)Math.pow( 2, 10 * ( t - 1 ) ) + b;
    }

    /// <summary>
    /// Easing equation function for an exponential (2^t) easing in/out: 
    /// acceleration until halfway, then deceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float ExpoEaseInOut( float t, float b, float c, float d ) {
            if ( t == 0 )
                    return b;

            if ( t == 1 )
                    return b + c;

            if ( (t *= 2) < 1 )
                    return c  * 0.5f * (float)Math.pow( 2, 10 * ( t - 1 ) ) + b;

            return c  * 0.5f * ( -(float)Math.pow( 2, -10 * --t ) + 2 ) + b;
    }

    /// <summary>
    /// Easing equation function for an exponential (2^t) easing out/in: 
    /// deceleration until halfway, then acceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float ExpoEaseOutIn( float t, float b, float c, float d ) {
            if ( t < 0.5f )
                    return ExpoEaseOut( t * 2, b, c  * 0.5f, d );

            return ExpoEaseIn( ( t * 2 ) - 1f, b + c * 0.5f, c * 0.5f, d );
    }

    /// <summary>
    /// Easing equation function for a circular (sqrt(1-t^2)) easing out: 
    /// decelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float CircEaseOut( float t, float b, float c, float d ) {
            return c * (float)Math.sqrt( 1 - ( t = t - 1 ) * t ) + b;
    }

    /// <summary>
    /// Easing equation function for a circular (sqrt(1-t^2)) easing in: 
    /// accelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float CircEaseIn( float t, float b, float c, float d ) {
            return -c * ( (float)Math.sqrt( 1 - t * t ) - 1 ) + b;
    }

    /// <summary>
    /// Easing equation function for a circular (sqrt(1-t^2)) easing in/out: 
    /// acceleration until halfway, then deceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float CircEaseInOut( float t, float b, float c, float d ) {
            if ( (t *= 2f) < 1 )
                    return -c * 0.5f * ( (float)Math.sqrt( 1 - t * t ) - 1 ) + b;

            return c * 0.5f * ( (float)Math.sqrt( 1 - ( t -= 2 ) * t ) + 1 ) + b;
    }

    /// <summary>
    /// Easing equation function for a circular (sqrt(1-t^2)) easing in/out: 
    /// acceleration until halfway, then deceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float CircEaseOutIn( float t, float b, float c, float d ) {
            if ( t < 0.5f )
                    return CircEaseOut( t * 2, b, c * 0.5f, d );

            return CircEaseIn( ( t * 2 ) - 1f, b + c * 0.5f, c * 0.5f, d );
    }

    /// <summary>
    /// Easing equation function for a quadratic (t^2) easing out: 
    /// decelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float QuadEaseOut( float t, float b, float c, float d ) {
            return -c * t * ( t - 2 ) + b;
    }

    /// <summary>
    /// Easing equation function for a quadratic (t^2) easing in: 
    /// accelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float QuadEaseIn( float t, float b, float c, float d ) {
            return c * t * t + b;
    }

    /// <summary>
    /// Easing equation function for a quadratic (t^2) easing in/out: 
    /// acceleration until halfway, then deceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float QuadEaseInOut( float t, float b, float c, float d ) {
            if ( (t *= 2f) < 1 )
                    return c * 0.5f * t * t + b;

            return -c * 0.5f * ( ( --t ) * ( t - 2 ) - 1 ) + b;
    }

    /// <summary>
    /// Easing equation function for a quadratic (t^2) easing out/in: 
    /// deceleration until halfway, then acceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float QuadEaseOutIn( float t, float b, float c, float d ) {
            if ( t < 0.5f )
                    return QuadEaseOut( t * 2, b, c * 0.5f, d );

            return QuadEaseIn( ( t * 2 ) - 1f, b + c * 0.5f, c * 0.5f, d );
    }

    /// <summary>
    /// Easing equation function for a sinusoidal (sin(t)) easing out: 
    /// decelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float SineEaseOut( float t, float b, float c, float d ) {
            return c * (float)Math.sin( t * ( Math.PI * 0.5f ) ) + b;
    }

    /// <summary>
    /// Easing equation function for a sinusoidal (sin(t)) easing in: 
    /// accelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float SineEaseIn( float t, float b, float c, float d ) {
            return -c * (float)Math.cos( t * ( Math.PI * 0.5f ) ) + c + b;
    }

    /// <summary>
    /// Easing equation function for a sinusoidal (sin(t)) easing in/out: 
    /// acceleration until halfway, then deceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float SineEaseInOut( float t, float b, float c, float d ) {
            if ((t *= 2f) < 1)
                    return SineEaseIn(t, b, c*0.5f, d);
            return SineEaseOut(t-1, b + c*0.5f, c*0.5f, d);
    }

    /// <summary>
    /// Easing equation function for a sinusoidal (sin(t)) easing in/out: 
    /// deceleration until halfway, then acceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float SineEaseOutIn( float t, float b, float c, float d ) {
            if (t < 0.5f)
                    return SineEaseOut(t * 2, b, c * 0.5f, d);

            return SineEaseIn((t * 2) - 1f, b + c * 0.5f, c * 0.5f, d);
    }

    /// <summary>
    /// Easing equation function for a cubic (t^3) easing out: 
    /// decelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float CubicEaseOut( float t, float b, float c, float d ) {
            return c * ( ( t = t - 1 ) * t * t + 1 ) + b;
    }

    /// <summary>
    /// Easing equation function for a cubic (t^3) easing in: 
    /// accelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float CubicEaseIn( float t, float b, float c, float d ) {
            return c * t * t * t + b;
    }

    /// <summary>
    /// Easing equation function for a cubic (t^3) easing in/out: 
    /// acceleration until halfway, then deceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float CubicEaseInOut( float t, float b, float c, float d ) {
            if ( (t *= 2f) < 1 )
                    return c * 0.5f * t * t * t + b;

            return c * 0.5f * ( ( t -= 2 ) * t * t + 2 ) + b;
    }

    /// <summary>
    /// Easing equation function for a cubic (t^3) easing out/in: 
    /// deceleration until halfway, then acceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float CubicEaseOutIn( float t, float b, float c, float d ) {
            if ( t < 0.5f )
                    return CubicEaseOut( t * 2, b, c * 0.5f, d );

            return CubicEaseIn( ( t * 2 ) - 1f, b + c * 0.5f, c * 0.5f, d );
    }

    /// <summary>
    /// Easing equation function for a quartic (t^4) easing out: 
    /// decelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float QuartEaseOut( float t, float b, float c, float d ) {
            return -c * ( ( t = t - 1 ) * t * t * t - 1 ) + b;
    }

    /// <summary>
    /// Easing equation function for a quartic (t^4) easing in: 
    /// accelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float QuartEaseIn( float t, float b, float c, float d ) {
            return c * t * t * t * t + b;
    }

    /// <summary>
    /// Easing equation function for a quartic (t^4) easing in/out: 
    /// acceleration until halfway, then deceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float QuartEaseInOut( float t, float b, float c, float d ) {
            if ( (t *= 2f) < 1 )
                    return c * 0.5f * t * t * t * t + b;

            return -c * 0.5f * ( ( t -= 2 ) * t * t * t - 2 ) + b;
    }

    /// <summary>
    /// Easing equation function for a quartic (t^4) easing out/in: 
    /// deceleration until halfway, then acceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float QuartEaseOutIn( float t, float b, float c, float d ) {
            if ( t < 0.5f )
                    return QuartEaseOut( t * 2, b, c * 0.5f, d );

            return QuartEaseIn( ( t * 2 ) - 1f, b + c * 0.5f, c * 0.5f, d );
    }

    /// <summary>
    /// Easing equation function for a quintic (t^5) easing out: 
    /// decelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float QuintEaseOut( float t, float b, float c, float d ) {
            return c * ( ( t = t - 1 ) * t * t * t * t + 1 ) + b;
    }

    /// <summary>
    /// Easing equation function for a quintic (t^5) easing in: 
    /// accelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float QuintEaseIn( float t, float b, float c, float d ) {
            return c * t * t * t * t * t + b;
    }

    /// <summary>
    /// Easing equation function for a quintic (t^5) easing in/out: 
    /// acceleration until halfway, then deceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float QuintEaseInOut( float t, float b, float c, float d ) {
            if ( (t *= 2f) < 1 )
                    return c * 0.5f * t * t * t * t * t + b;
            return c * 0.5f * ( ( t -= 2 ) * t * t * t * t + 2 ) + b;
    }

    /// <summary>
    /// Easing equation function for a quintic (t^5) easing in/out: 
    /// acceleration until halfway, then deceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float QuintEaseOutIn( float t, float b, float c, float d ) {
            if ( t < 0.5f )
                    return QuintEaseOut( t * 2, b, c * 0.5f, d );
            return QuintEaseIn( ( t * 2 ) - 1f, b + c * 0.5f, c * 0.5f, d );
    }

    /// <summary>
    /// Easing equation function for an elastic (exponentially decaying sine wave) easing out: 
    /// decelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float ElasticEaseOut( float t, float b, float c, float d ) {
            if ( t == 1 )
                    return b + c;

            float p = d * 0.3f;
            float s = p * 0.25f;

            return (float)( c * Math.pow( 2, -10 * t ) * Math.sin( ( t * d - s ) * ( 2 * Math.PI ) / p ) + c + b );
    }

    /// <summary>
    /// Easing equation function for an elastic (exponentially decaying sine wave) easing in: 
    /// accelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float ElasticEaseIn( float t, float b, float c, float d ) {
            if ( t == 1 )
                    return b + c;

            float p = d * 0.3f;
            float s = p * 0.25f;

            return -(float)( c * Math.pow( 2, 10 * ( t -= 1 ) ) * Math.sin( ( t * d - s ) * ( 2 * Math.PI ) / p ) ) + b;
    }

    /// <summary>
    /// Easing equation function for an elastic (exponentially decaying sine wave) easing in/out: 
    /// acceleration until halfway, then deceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float ElasticEaseInOut( float t, float b, float c, float d ) {
            if ( (t *= 2f) == 2 )
                    return b + c;

            float p = d * 0.45f;//( 0.3f * 1.5f );
            float s = p * 0.25f;

            if ( t < 1 )
                    return -0.5f * (float)( c * Math.pow( 2, 10 * ( t -= 1 ) ) * Math.sin( ( t * d - s ) * ( 2 * Math.PI ) / p ) ) + b;
            return (float)(c * Math.pow( 2, -10 * ( t -= 1 ) ) * Math.sin( ( t * d - s ) * ( 2 * Math.PI ) / p ) * 0.5f + c + b);
    }

    /// <summary>
    /// Easing equation function for an elastic (exponentially decaying sine wave) easing out/in: 
    /// deceleration until halfway, then acceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float ElasticEaseOutIn( float t, float b, float c, float d ) {
            if ( t < 0.5f )
                    return ElasticEaseOut( t * 2, b, c * 0.5f, d );
            return ElasticEaseIn( ( t * 2 ) - 1f, b + c * 0.5f, c * 0.5f, d );
    }

    /// <summary>
    /// Easing equation function for a bounce (exponentially decaying parabolic bounce) easing out: 
    /// decelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float BounceEaseOut( float t, float b, float c, float d ) {
            if ( t < 0.36363f )//1 * 0.36363f )
                    return c * ( 7.5625f * t * t ) + b;
            else if ( t < 0.72726f )//2f * 0.36363f )
                    return c * ( 7.5625f * ( t -= 0.545445f ) * t + 0.75f ) + b;
            else if ( t < 0.909075f )//2.5f * 0.36363f )
                    return c * ( 7.5625f * ( t -= 0.8181675f ) * t + 0.9375f ) + b;
            else
                    return c * ( 7.5625f * ( t -= 0.95452875f ) * t + 0.984375f ) + b;
    }

    /// <summary>
    /// Easing equation function for a bounce (exponentially decaying parabolic bounce) easing in: 
    /// accelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float BounceEaseIn( float t, float b, float c, float d ) {
            return c - BounceEaseOut( 1f - t, 0, c, d ) + b;
    }

    /// <summary>
    /// Easing equation function for a bounce (exponentially decaying parabolic bounce) easing in/out: 
    /// acceleration until halfway, then deceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float BounceEaseInOut( float t, float b, float c, float d ) {
            if ( t < 0.5f )
                    return BounceEaseIn( t * 2, 0, c, d ) * 0.5f + b;
            else
                    return BounceEaseOut( t * 2 - 1f, 0, c, d ) * 0.5f + c * 0.5f + b;
    }

    /// <summary>
    /// Easing equation function for a bounce (exponentially decaying parabolic bounce) easing out/in: 
    /// deceleration until halfway, then acceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float BounceEaseOutIn( float t, float b, float c, float d ) {
            if ( t < 0.5f )
                    return BounceEaseOut( t * 2, b, c * 0.5f, d );
            return BounceEaseIn( ( t * 2 ) - 1f, b + c * 0.5f, c * 0.5f, d );
    }

    /// <summary>
    /// Easing equation function for a back (overshooting cubic easing: (s+1)*t^3 - s*t^2) easing out: 
    /// decelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float BackEaseOut( float t, float b, float c, float d ) {
            return c * ( ( t = t - 1 ) * t * ( 2.70158f * t + 1.70158f ) + 1 ) + b;
    }

    /// <summary>
    /// Easing equation function for a back (overshooting cubic easing: (s+1)*t^3 - s*t^2) easing in: 
    /// accelerating from zero velocity.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float BackEaseIn( float t, float b, float c, float d ) {
            return c * t * t * ( 2.70158f * t - 1.70158f ) + b;
    }

    /// <summary>
    /// Easing equation function for a back (overshooting cubic easing: (s+1)*t^3 - s*t^2) easing in/out: 
    /// acceleration until halfway, then deceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float BackEaseInOut( float t, float b, float c, float d ) {
            float s = 1.70158f;
            if ( (t *= 2f) < 1 )
                    return c * 0.5f * ( t * t * ( ( ( s *= 1.525f ) + 1 ) * t - s ) ) + b;
            return c * 0.5f * ( ( t -= 2 ) * t * ( ( ( s *= 1.525f ) + 1 ) * t + s ) + 2 ) + b;
    }

    /// <summary>
    /// Easing equation function for a back (overshooting cubic easing: (s+1)*t^3 - s*t^2) easing out/in: 
    /// deceleration until halfway, then acceleration.
    /// </summary>
    /// <param name="t">Lerp time (0~1).</param>
    /// <param name="b">Starting value.</param>
    /// <param name="c">Change in value.</param>
    /// <param name="d">Duration of animation.</param>
    /// <returns>The correct value.</returns>
    public static float BackEaseOutIn( float t, float b, float c, float d ) {
            if ( t < 0.5f )
                    return BackEaseOut( t * 2, b, c * 0.5f, d );
            return BackEaseIn( ( t * 2 ) - 1f, b + c * 0.5f, c * 0.5f, d );
    }

    
    /**
     * Delegate for handling ease events.
     */
    public interface EaseHandler {
        float Invoke(float t, float b, float c, float d);
    }
}
