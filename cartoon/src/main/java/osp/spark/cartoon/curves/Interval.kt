package osp.spark.cartoon.curves

import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.annotation.FloatRange
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing

/**
 * @author yun.
 * @date 2022/6/26
 * @des [https://cubic-bezier.com/]
 * @since [https://github.com/5hmlA]
 * <p><a href="https://github.com/5hmlA">github</a>
 */

//progress.interval(0F,0.3F)
fun Float.interval(
    @FloatRange(from = 0.0, to = 1.0) begin: Float,
    @FloatRange(from = 0.0, to = 1.0) end: Float
): Float = ((this - begin) / (end - begin)).coerceIn(0.0F, 1.0F)

class Interval(
    @FloatRange(from = 0.0, to = 1.0) private val begin: Float,
    @FloatRange(from = 0.0, to = 1.0) private val end: Float,
    private val curve: (Float) -> Float = LinearInterpolator()::getInterpolation
) : Interpolator, Easing {

    override fun getInterpolation(t: Float): Float {
        val t2 = ((t - begin) / (end - begin)).coerceIn(0.0F, 1.0F)
        if (t2 == 0.0F || t2 == 1.0F)
            return t2
        return curve.invoke(t2)
    }

    override fun transform(fraction: Float) = getInterpolation(fraction)
}

//波浪插值
class WaveInterpolator(
    private val index: Int,
    private val size: Int,
    @FloatRange(from = 0.0, to = 1.0) private val eachDuration: Float = .5F,
    private val curve: (Float) -> Float = LinearEasing::transform
) : Interpolator, Easing {

    override fun getInterpolation(progress: Float): Float {
//        val interval = ((1 - eachDuration) / size).toDouble()
//        val offset = interval * (index + 1)
//        if (progress <= offset) {
//            return 0F
//        }
//        val newProgress = progress - offset
//        return if (newProgress >= eachDuration) {
//            1F
//        } else curve.invoke((newProgress / eachDuration).toFloat())
        val wave = wave(index, size, progress, eachDuration)
        if (wave == 0F || wave == 1F) {
            return wave
        }
        return curve.invoke(wave)
    }

    override fun transform(fraction: Float) = getInterpolation(fraction)
}

class Wave(
    @FloatRange(from = 0.0, to = 1.0) private val eachDuration: Float = .5F,
    private val curve: (Float) -> Float = LinearInterpolator()::getInterpolation
) {
    fun wave(index: Int, size: Int, progress: Float): Float {
//        val interval = ((1 - eachDuration) / size).toDouble()
//        val offset = interval * (index + 1)
//        if (progress <= offset) {
//            return 0F
//        }
//        val newProgress = progress - offset
//        return if (newProgress >= eachDuration) {
//            1F
//        } else curve((newProgress / eachDuration).toFloat())
        val wave = wave(index, size, progress, eachDuration)
        if (wave == 0F || wave == 1F) {
            return wave
        }
        return curve.invoke(wave)
    }
}

fun wave(
    index: Int,
    size: Int,
    @FloatRange(from = 0.0, to = 1.0) progress: Float,
    @FloatRange(from = 0.0, to = 1.0) eachDuration: Float = .5F
): Float {
//    val interval = ((1 - eachDuration) / size).toDouble()
//    val offset = interval * (index + 1)
//    if (progress <= offset) {
//        return 0F
//    }
//    val newProgress = progress - offset
//    return if (newProgress >= eachDuration) {
//        1F
//    } else (newProgress / eachDuration).toFloat()
    return index.wave(size)(progress)(eachDuration)
}

//5.wave(10)(.3F)(.5F)
fun Int.wave(size: Int) = fun(@FloatRange(from = 0.0, to = 1.0) progress: Float) = fun(@FloatRange(from = 0.0, to = 1.0) eachDuration: Float): Float {
    val interval = ((1 - eachDuration) / size).toDouble()
    val offset = interval * (this + 1)
    if (progress <= offset) {
        return 0F
    }
    val newProgress = progress - offset
    return if (newProgress >= eachDuration) {
        1F
    } else (newProgress / eachDuration).toFloat()
}