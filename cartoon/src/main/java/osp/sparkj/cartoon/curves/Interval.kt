package osp.sparkj.cartoon.curves

import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing

/**
 * @author yun.
 * @date 2022/6/26
 * @des [https://cubic-bezier.com/]
 * @since [https://github.com/ZuYun]
 * <p><a href="https://github.com/ZuYun">github</a>
 */
class Interval(
    private val begin: Float,
    private val end: Float,
    private val curve: (Float)->Float = LinearInterpolator()::getInterpolation
) : Interpolator {

    override fun getInterpolation(t: Float): Float {
        val t2 = ((t - begin) / (end - begin)).clamp(0.0F, 1.0F)
        if (t2 == 0.0F || t2 == 1.0F)
            return t2
        return curve.invoke(t2)
    }
}

fun Float.clamp(lowerLimit: Float, upperLimit: Float): Float {
    if (this > upperLimit) {
        return upperLimit
    } else if (this < lowerLimit) {
        return lowerLimit
    }
    return this
}

//波浪插值

class WaveInterpolator(
    private val index: Int,
    private val size: Int,
    private val eachDuration: Float = .5F,
    private val curve: (Float)->Float = LinearEasing::transform
) : Interpolator {

    override fun getInterpolation(progress: Float): Float {
        val interval = ((1 - eachDuration) / size).toDouble()
        val offset = interval * (index + 1)
        if (progress <= offset) {
            return 0F
        }
        val newProgress = progress - offset
        return if (newProgress >= eachDuration) {
            1F
        } else curve.invoke((newProgress / eachDuration).toFloat())
    }
}

class Wave(
    private val eachDuration: Float = .5F,
    private val curve: Interpolator = LinearInterpolator()
) {
    fun wave(index: Int, size: Int, progress: Float): Float {
        val interval = ((1 - eachDuration) / size).toDouble()
        val offset = interval * (index + 1)
        if (progress <= offset) {
            return 0F
        }
        val newProgress = progress - offset
        return if (newProgress >= eachDuration) {
            1F
        } else curve.getInterpolation((newProgress / eachDuration).toFloat())
    }
}

fun wave(index: Int, size: Int, progress: Float, eachDuration: Float = .5F): Float {
    val interval = ((1 - eachDuration) / size).toDouble()
    val offset = interval * (index + 1)
    if (progress <= offset) {
        return 0F
    }
    val newProgress = progress - offset
    return if (newProgress >= eachDuration) {
        1F
    } else (newProgress / eachDuration).toFloat()
}

//5.wave(10)(.3F)(.5F)
fun Int.wave(size: Int) = fun(progress: Float) = fun(eachDuration: Float): Float {
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