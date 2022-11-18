package osp.sparkj.cartoon.extend

import android.graphics.*
import android.view.View
import osp.sparkj.cartoon.dp

/**
 * @author yun.
 * @date 2022/7/6
 * @des [一句话描述]
 * @since [https://github.com/mychoices]
 * <p><a href="https://github.com/mychoices">github</a>
 */

inline fun Bitmap.paint(ten: Boolean = false, drawing: (Canvas, Bitmap) -> Unit): Bitmap {
    val bitmap = this.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(bitmap)
    if (ten) {
        val debugPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.YELLOW }
        debugPaint.strokeWidth = 1.dp
        canvas.drawLine(width / 2F, 0F, width / 2F, height.toFloat(), debugPaint)
        canvas.drawLine(0F, height / 2F, width.toFloat(), height / 2F, debugPaint)
    }
    drawing.invoke(canvas, this)
    return bitmap
}

fun Canvas.coordinateSystemNormal(xOffset: Float = 0F, yOffset: Float = 0F) {
    translate(xOffset, height - yOffset)
    scale(1F, -1F)
}

fun Canvas.coordinateSystemCenter() {
    translate(width / 2F, height / 2F)
    scale(1F, -1F)
}

val Paint.textHeight: Int
    get() {
        val bounds = Rect()
        getTextBounds("0", 0, 1, bounds)
        return bounds.height()
    }

context(View)
inline fun Canvas.rotateDraw(
    x: Float = 0F,
    y: Float = 0F,
    camera: Camera,
    crossinline change: Canvas.() -> Unit = {},
    crossinline revert: Canvas.() -> Unit = {},
    crossinline onDraw: Canvas.() -> Unit
) {
    save()
    translate(width / 2F, height / 2F)
    revert()
    camera.save()
    if (x != 0F) {
        camera.rotateX(x)
    }
    if (y != 0F) {
        camera.rotateY(y)
    }
    camera.applyToCanvas(this)
    camera.restore()
    change()
    translate(-width / 2F, -height / 2F)
    //先变换后绘制 动作从下往上走
    onDraw()
    restore()
}