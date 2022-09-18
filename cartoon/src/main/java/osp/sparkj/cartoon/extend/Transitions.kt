package osp.sparkj.cartoon.extend

import android.animation.*
import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.transition.*
import osp.sparkj.cartoon.curves.Curve

/**
 * @author yun.
 * @date 2022/6/26
 * @des [一句话描述]
 * @since [https://github.com/ZuYun]
 * <p><a href="https://github.com/ZuYun">github</a>
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
            interpolator = Curve(.2F,.02F,.1F,1.5F)
        }
        val scaleYani = ObjectAnimator.ofFloat(view, "scaleY", scaleY.toString().toFloat(), 1F).apply {
            interpolator = Curve(.2F,.02F,.1F,1.5F)
        }
        val alphaAni = ObjectAnimator.ofFloat(view, "alpha", alpha.toString().toFloat(), 1F).apply {
            interpolator = Curve(.99F,.39F,.98F,.83F)
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
            interpolator = Curve(.0F,-0.29F,.97F,.06F)
        }
        val scaleYani = ObjectAnimator.ofFloat(view, "scaleY", scaleY.toString().toFloat(), 0F).apply {
//            interpolator = AccelerateDecelerateInterpolator()
            interpolator = Curve(.0F,-0.29F,.97F,.06F)
        }
        val alphaAni = ObjectAnimator.ofFloat(view, "alpha", alpha.toString().toFloat(), 0F).apply {
            interpolator = Curve(.99F,.39F,.98F,.83F)
        }
        animatorSet.playTogether(scaleXani, scaleYani, alphaAni)
        return animatorSet
    }
}

fun scaleTransition(): Transition = TransitionSet().apply {
    addTransition(ScaleTransition())
    addTransition(ChangeBounds())
}

@SuppressLint("ObjectAnimatorBinding")
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