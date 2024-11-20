package osp.sparkj.cartoon.wings

import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import android.util.TypedValue

/**
 * @author yun.
 * @date 2022/6/26
 * @des [一句话描述]
 * @since [https://github.com/5hmlA]
 * <p><a href="https://github.com/5hmlA">github</a>
 */
fun Int.alpha(alpha: Number) =
    Color.argb((alpha.toFloat() * 255).toInt(), Color.red(this), Color.green(this), Color.blue(this))

fun Number.dp(context: Activity? = null): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), (context?.resources ?: Resources.getSystem()).displayMetrics
).toInt()

fun Number.dpf(context: Activity? = null): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), (context?.resources ?: Resources.getSystem()).displayMetrics
)