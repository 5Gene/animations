package osp.spark.cartoon.curves

import android.view.animation.Interpolator
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing

/**
 * @author yun.
 * @date 2022/6/26
 * @des [https://cubic-bezier.com/]
 * @since [https://github.com/5hmlA]
 * <p><a href="https://github.com/5hmlA">github</a>
 * @see androidx.compose.animation.core.CubicBezierEasing
 */

//https://cubic-bezier.com/#.25,1,.5,1
val EaseOutQuart = CubicBezierEasing(0.25f, 1f, 0.5f, 1f)

/**
 * https://cubic-bezier.com/
 * 这个网站可以直接看曲线
 */
class Curve(
    private val a: Float,
    private val b: Float,
    private val c: Float,
    private val d: Float
) : Interpolator, Easing by CubicBezierEasing(a, b, c, d) {

    override fun getInterpolation(t: Float): Float {
        return transform(t)
    }
}
//class Curve(
//    private val a: Float,
//    private val b: Float,
//    private val c: Float,
//    private val d: Float
//) : Interpolator, Easing {
//
//    private val _cubicErrorBound = 0.001
//
//    override fun getInterpolation(t: Float): Float {
//        var start = 0.0F
//        var end = 1.0F
//        while (true) {
//            val midpoint = (start + end) / 2
//            val estimate = _evaluateCubic(a, c, midpoint)
//            if ((t - estimate).absoluteValue < _cubicErrorBound)
//                return _evaluateCubic(b, d, midpoint).toFloat()
//            if (estimate < t) start = midpoint else end = midpoint
//        }
//    }
//
//    private fun _evaluateCubic(a: Float, b: Float, m: Float): Float {
//        return 3 * a * (1 - m) * (1 - m) * m + 3 * b * (1 - m) * m * m + m * m * m
//    }
//
//    override fun transform(fraction: Float): Float {
//        return getInterpolation(fraction)
//    }
//}