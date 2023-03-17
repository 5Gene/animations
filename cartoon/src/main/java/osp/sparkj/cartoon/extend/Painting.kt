package osp.sparkj.cartoon.extend

import android.graphics.*

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
        debugPaint.strokeWidth = 1.todpf
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

inline fun Canvas.transForm(
    rotateX: Float = 0F, rotateY: Float = 0F,
    //左移负数               上移负数
    translateX: Float = 0F, translateY: Float = 0F,
    scaleX: Float = 1F, scaleY: Float = 1F,
    locationX: Float = 0F, locationY: Float = 0F, locationZ: Float = 0F,
    //正数左移宽的比例          正数上移高的比例
    widthFactor: Float = .5F, heightFactor: Float = .5F, camera: Camera? = null,
    //在裁剪之前旋转 围绕Z轴
    rotate: Float? = null, crossinline clip: Canvas.(Float, Float) -> Unit = { _, _ -> },
    crossinline draw: Canvas.() -> Unit
) {
    save()
    //默认 左移动-  上移动-
    val offsetX = width * widthFactor * -1
    val offsetY = height * heightFactor * -1
    //画布移动回原来的位置 (已经完成了旋转缩放后的动作)
    translate(-offsetX + translateX, -offsetY + translateY)
    //画布移动到中心点后 缩放
    scale(scaleX, scaleY)
    //裁剪和camera变换之后 旋转回去
    rotate?.run {
        rotate(this * -1F)
    }
    //画布移动到中心点后 旋转 camera旋转中心为0,0
    camera?.let {
        it.save()
        if (locationX != 0F || locationY != 0F || locationZ != 0F) {
            it.setLocation(locationX, locationY, locationZ)
        }
        if (rotateX != 0F) {
            it.rotateX(rotateX)
        }
        if (rotateY != 0F) {
            it.rotateY(rotateY)
        }
        it.applyToCanvas(this)
        it.restore()
    }
    //裁剪
    clip(offsetX, offsetY)
    //移动到中心点后 旋转操作
    rotate?.run {
        rotate(this)
    }
    //围绕中点旋转的时候 先移动到中心点 -，-
    translate(offsetX, offsetY)
    //先绘制后变换 动作从下往上走
    draw()
    restore()

//                    canvas.save()
//                    canvas.translate(width / 2, height / 2 - topOffsetValue)
//                    canvas.scale(widthScaleValue, 1F)
//                    camera.save()
//                    camera.setLocation(0F, 0F, (-30).todpf)
//                    camera.rotateX(-180F * ani)
//                    camera.applyToCanvas(canvas)
//                    camera.restore()
//                    canvas.clipRect(-width, -height / 2, width, 0F)
//                    canvas.translate(-width / 2, -height / 2)
//                    //先绘制后变换 动作从下往上走
//                    canvas.drawBitmap(bitmap, 0F, 0F, null)
//                    canvas.restore()
}