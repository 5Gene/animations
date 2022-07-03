package osp.sparkj.cartoon.curves

import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

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
    private val curve: Interpolator = LinearInterpolator()
) : Interpolator {

    override fun getInterpolation(t: Float): Float {
        val t2 = ((t - begin) / (end - begin)).clamp(0.0F, 1.0F)
        if (t2 == 0.0F || t2 == 1.0F)
            return t2
        return curve.getInterpolation(t2)
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