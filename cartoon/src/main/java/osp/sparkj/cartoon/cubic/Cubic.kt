package osp.sparkj.cartoon.cubic

import android.view.animation.Interpolator
import kotlin.math.absoluteValue

/**
 * @author yun.
 * @date 2022/6/26
 * @des [https://cubic-bezier.com/]
 * @since [https://github.com/mychoices]
 * <p><a href="https://github.com/mychoices">github</a>
 */
class Cubic(val a: Double, val b: Double, val c: Double, val d: Double) : Interpolator {
    private val _cubicErrorBound = 0.001

    override fun getInterpolation(t: Float): Float {
        var start = 0.0
        var end = 1.0
        while (true) {
            val midpoint = (start + end) / 2
            val estimate = _evaluateCubic(a, c, midpoint)
            if ((t - estimate).absoluteValue < _cubicErrorBound) return _evaluateCubic(b, d, midpoint).toFloat()
            if (estimate < t) start = midpoint else end = midpoint
        }
    }

    private fun _evaluateCubic(a: Double, b: Double, m: Double): Double {
        return 3 * a * (1 - m) * (1 - m) * m + 3 * b * (1 - m) * m * m + m * m * m
    }
}