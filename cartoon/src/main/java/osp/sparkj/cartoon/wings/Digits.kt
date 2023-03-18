package osp.sparkj.cartoon.wings

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

inline val Number.todp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()

inline val Number.todpf
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )