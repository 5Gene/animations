package osp.sparkj.cartoon.wings

import java.lang.IllegalArgumentException


/**
 * @param (sx,sy)
 * @param (ex,ey)
 * 上面两点的直线上 计算x为t对应的y
 */
fun straightLine(sx: Float, sy: Float, ex: Float, ey: Float, t: Float): Float {
    val k = (ey - sy) / (ex - sx)
    return k * t + sy - k * sx
}

/**
 * ```kotlin
 * val colorStops = gradientColorStops(5, colors)
 * Brush.horizontalGradient(*colorStops.toTypedArray())
 * ```
 * @param segments 有几段颜色
 * @param colors 具体的几段颜色值
 */
inline fun <reified COLOR> gradientColorStops(segments: Int, colors: List<COLOR>): List<Pair<Float, COLOR>> {
    if (colors.size < segments) {
        throw IllegalArgumentException("segments 必须小于等于 colors.size")
    }
    val ratio = 1f / segments
    val size = segments * 2 - 2
    return mutableListOf<Pair<Float, COLOR>>().apply {
        for (index in 0..size) {
            val stop = if (index % 2 == 0) {
                ratio * (index / 2 + 1)
            } else {
                ratio * (index / 2 + 1) + 0.0001f
            }
            add(stop to colors[((index + 1) / 2)])
        }
    }
}