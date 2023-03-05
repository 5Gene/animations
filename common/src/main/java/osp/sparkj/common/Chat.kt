package osp.sparkj.common

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.security.SecureRandom

/**
 * @author yun.
 * @date 2022/5/30
 * @des [一句话描述]
 * @since [https://github.com/ZuYun]
 * <p><a href="https://github.com/ZuYun">github</a>
 */
class Chart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var speeds: List<ChatData> = mutableListOf()
    val path = android.graphics.Path()
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        setColor(Color.RED)
        strokeWidth = 6F
        strokeJoin = Paint.Join.ROUND
        style = Paint.Style.STROKE
    }

    val colors = intArrayOf(Color.YELLOW, Color.RED)
    var progressGradient: LinearGradient? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), 300)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        convert()
    }

    override fun onDraw(canvas: Canvas) {
        progressGradient?.run {
            paint.shader = progressGradient
        }
        canvas.drawPath(path, paint)
        super.onDraw(canvas)
    }

    fun setSpeedsData(speeds: List<ChatData>) {
        this.speeds = speeds
        if (width > 0) {
            convert()
        }
    }

    private fun convert() {
        if (speeds.isEmpty()) {
            return
        }
        val max = speeds.maxByOrNull {
            it.value
        }
        val starTime = speeds.first().time
        val totalTime = speeds.last().time - starTime.toFloat()
        val et = width / totalTime
        val eh = height / max!!.value
        path.reset()
        speeds.forEach {
            if (path.isEmpty) {
                path.moveTo((it.time - starTime) * et, it.value * eh)
            } else {
                path.lineTo((it.time - starTime) * et, it.value * eh)
            }
        }
    }

    fun setProgress(progress: Float) {
        progressGradient = if (progress > 0.995F) {
            LinearGradient(
                0F,
                0F,
                width.toFloat(),
                0F,
                colors,
                floatArrayOf(1F, 1F),
                android.graphics.Shader.TileMode.CLAMP
            )
        } else {
            LinearGradient(
                0F,
                0F,
                width.toFloat(),
                0F,
                colors,
                floatArrayOf(progress, progress + 0.0001F),
                android.graphics.Shader.TileMode.CLAMP
            )
        }
        postInvalidate()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE) {
            if (speeds.isEmpty()) {
                return
            }
            val max = speeds.maxByOrNull {
                it.value
            }
            val starTime = speeds.first().time
            val totalTime = speeds.last().time - starTime.toFloat()
            val et = width / totalTime
            val eh = height / max!!.value
            val hl = height / 2F
            ValueAnimator.ofFloat(0F, 1F).apply {
                duration = 1400
                addUpdateListener { ani ->
                    val prog = ani.animatedValue as Float
                    path.reset()
                    speeds.forEach {
                        val fl = it.value * eh
                        if (path.isEmpty) {
                            path.moveTo((it.time - starTime) * et, hl + (fl - hl) * prog)
                        } else {
                            path.lineTo((it.time - starTime) * et, hl + (fl - hl) * prog)
                        }
                    }
                    invalidate()
                }
                start()
            }
        }
    }
}

data class ChatData(val time: Long, val value: Float)

fun ranChatData(): List<ChatData> {
    return List(50) { index ->
        ChatData(index * 10L, SecureRandom().nextFloat() + 100)
    }
}