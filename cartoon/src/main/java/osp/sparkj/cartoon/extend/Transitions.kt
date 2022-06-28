package osp.sparkj.cartoon.extend

import android.animation.*
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import androidx.transition.*
import kotlin.math.absoluteValue

/**
 * @author yun.
 * @date 2022/6/26
 * @des [一句话描述]
 * @since [https://github.com/mychoices]
 * <p><a href="https://github.com/mychoices">github</a>
 */

class ScaleTransition : Visibility() {
    val SCALEX = "android.scaleX"
    val SCALEY = "android.scaleY"
    val ALPHA = "android.alpha"

    override fun captureStartValues(transitionValues: TransitionValues) {
        super.captureStartValues(transitionValues)
        transitionValues.values[SCALEX] = transitionValues.view.scaleX
        transitionValues.values[SCALEY] = transitionValues.view.scaleX
        transitionValues.values[ALPHA] = transitionValues.view.alpha
    }

    override fun onAppear(
        sceneRoot: ViewGroup?,
        view: View,
        startValues: TransitionValues,
        endValues: TransitionValues?
    ): Animator? {
        return to1(startValues, view)
    }

    private fun to1(
        startValues: TransitionValues,
        view: View
    ): AnimatorSet {
        val scaleX = startValues.values[SCALEX]
        val scaleY = startValues.values[SCALEY]
        val alpha = startValues.values[ALPHA]
        val animatorSet = AnimatorSet()
        val scaleXani = ObjectAnimator.ofFloat(view, "scaleX", scaleX.toString().toFloat(), 1F).apply {
            interpolator = Cubic(.2,.02,.1,1.5)
        }
        val scaleYani = ObjectAnimator.ofFloat(view, "scaleY", scaleY.toString().toFloat(), 1F).apply {
            interpolator = Cubic(.2,.02,.1,1.5)
        }
        val alphaAni = ObjectAnimator.ofFloat(view, "alpha", alpha.toString().toFloat(), 1F).apply {
            interpolator = Cubic(.99,.39,.98,.83)
        }
        animatorSet.playTogether(scaleXani, scaleYani, alphaAni)
        return animatorSet
    }

    override fun onDisappear(
        sceneRoot: ViewGroup?,
        view: View,
        startValues: TransitionValues,
        endValues: TransitionValues?
    ): Animator {
        return to0(startValues, view)
    }

    private fun to0(
        startValues: TransitionValues,
        view: View
    ): AnimatorSet {
        val scaleX = startValues.values[SCALEX]
        val scaleY = startValues.values[SCALEY]
        val alpha = startValues.values[ALPHA]
        val animatorSet = AnimatorSet()
        val scaleXani = ObjectAnimator.ofFloat(view, "scaleX", scaleX.toString().toFloat(), 0F).apply {
//            interpolator = AccelerateDecelerateInterpolator()
            interpolator = Cubic(.0,-0.29,.97,.06)
        }
        val scaleYani = ObjectAnimator.ofFloat(view, "scaleY", scaleY.toString().toFloat(), 0F).apply {
//            interpolator = AccelerateDecelerateInterpolator()
            interpolator = Cubic(.0,-0.29,.97,.06)
        }
        val alphaAni = ObjectAnimator.ofFloat(view, "alpha", alpha.toString().toFloat(), 0F).apply {
            interpolator = Cubic(.99,.39,.98,.83)
        }
        animatorSet.playTogether(scaleXani, scaleYani, alphaAni)
        return animatorSet
    }
}

class Cubic(val a: Double, val b: Double, val c: Double, val d: Double) : Interpolator {
    val _cubicErrorBound = 0.001

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

    fun _evaluateCubic(a: Double, b: Double, m: Double): Double {
        return 3 * a * (1 - m) * (1 - m) * m + 3 * b * (1 - m) * m * m + m * m * m
    }
}

fun scaleTransition(): Transition = TransitionSet().apply {
    addTransition(ScaleTransition())
    addTransition(ChangeBounds())
}

fun ViewGroup.trans() {
    val transition = LayoutTransition()
    val alpha = PropertyValuesHolder.ofFloat("alpha", 0F, 1F)
    val scaleX = PropertyValuesHolder.ofFloat("scaleX", 0F, 1F)
    val scaleY = PropertyValuesHolder.ofFloat("scaleY", 0F, 1F)
    val show = ObjectAnimator.ofPropertyValuesHolder(Any(), alpha, scaleY, scaleX)
    show.target = null
    show.duration = 2000
    transition.setAnimator(LayoutTransition.APPEARING, show)
    layoutTransition = transition
}