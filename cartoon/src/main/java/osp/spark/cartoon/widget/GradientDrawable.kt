package osp.spark.cartoon.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.drawable.Drawable

/**
 * @author yun.
 * @date 2022/6/28
 * @des [一句话描述]
 * @since [https://github.com/5hmlA]
 * <p><a href="https://github.com/5hmlA">github</a>
 */

typealias ShaderSupplyer = (bounds:Rect)-> Shader

class GradientDrawable(val shperSupplyer: ShaderSupplyer = defShaperSupplyer) : Drawable() {
    val gradientPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun draw(canvas: Canvas) {
        canvas.drawRect(0F, 0F, bounds.width().toFloat(), bounds.height().toFloat(), gradientPaint)
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        gradientPaint.shader = shperSupplyer.invoke(bounds)
    }

    override fun setAlpha(alpha: Int) {
        gradientPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        throw RuntimeException("not support")
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}

val black50 = Color.parseColor("#80000000")
val black20 = Color.parseColor("#36000000")
val black00 = Color.parseColor("#00000000")

val defShaperSupplyer:ShaderSupplyer = { bounds->
    LinearGradient(
        0F, 0F, 0F, bounds.height().toFloat(),
        intArrayOf(
            black50, black20, black00, black00, black20, black50
        ),
        floatArrayOf(0F, .2F, .37F, .63F, .8F, 1F), Shader.TileMode.REPEAT
    )
}