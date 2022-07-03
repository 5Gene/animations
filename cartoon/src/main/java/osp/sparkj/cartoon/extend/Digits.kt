package osp.sparkj.cartoon

import android.content.res.Resources
import android.util.TypedValue

/**
 * @author yun.
 * @date 2022/6/26
 * @des [一句话描述]
 * @since [https://github.com/ZuYun]
 * <p><a href="https://github.com/ZuYun">github</a>
 */

val Number.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )