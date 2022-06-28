package osp.sparkj.cartoon.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import osp.sparkj.cartoon.dp
import kotlin.concurrent.thread


/**
 * @author yun.
 * @date 2022/5/20
 * @des [一句话描述]
 * @since [https://github.com/ZuYun]
 * <p><a href="https://github.com/ZuYun">github</a>
 */
class Curve @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    val dp5 = 5.dp
    var points: List<PointF> = mutableListOf()
    val linePath = Path()
    val bgLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#33FFFFFF")
        strokeWidth = 2.dp
        strokeJoin = Paint.Join.ROUND
        style = Paint.Style.STROKE
        pathEffect = CornerPathEffect(10F)
    }

    val progPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        strokeWidth = 1.dp
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        pathEffect = CornerPathEffect(10F)
    }

    val progShaderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val progShaderPath: Path = Path()
    var progressGradient: LinearGradient? = null
    var prog = 0F
    var _scaleX = 0F

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        progressGradient = LinearGradient(
            0F,
            0F,
            0F,
            h.toFloat(),
            intArrayOf(Color.TRANSPARENT,Color.parseColor("#66FFFFFF")),
            floatArrayOf(0F, h.toFloat()),
            Shader.TileMode.CLAMP
        )
        progShaderPaint.shader = progressGradient
        _scaleX = if (visibility == GONE) 0F else 1F
        convert()
    }

    override fun onDraw(canvas: Canvas) {
        val save = canvas.save()
        //<editor-fold desc="转为正常坐标系">
        canvas.translate(0F, height.toFloat() - dp5)
        canvas.scale(1F, -1F)
        //</editor-fold>
        canvas.drawPath(linePath, bgLinePaint)
        if (prog > 0) {
//            canvas.drawPoint(currentPoint.x, currentPoint.y, dotPaint)
//            canvas.drawLine(currentPoint.x, currentPoint.y, currentPoint.x, height.toFloat(), linePaint)
            val saveProgress = canvas.save()
            canvas.clipRect(0F, 0F, prog * width.toFloat(), height.toFloat())
            canvas.drawPath(progShaderPath, progShaderPaint)
            canvas.drawPath(linePath, progPaint)
            canvas.restoreToCount(saveProgress)
        }
        canvas.restoreToCount(save)
    }

    fun feeding(points: List<PointF>) {
        this.points = points
        if (width > 0) {
            convert()
        }
    }

    private fun convert() {
        if (points.isEmpty()) {
            return
        }
        thread {
            covertInner()
        }
    }

    private fun covertInner() {
        if (points == null || points.isEmpty()) {
            return
        }
        val max = points.maxByOrNull {
            it.y
        }
        val min = points.minByOrNull {
            it.y
        }
        val firstw = points.first().x
        val totalw = points.last().x - firstw
        val ratiow = width / totalw
        val ratioh = (height - 2 * dp5) / (max!!.y - min!!.y)
        val halfHeight = height / 2
        linePath.reset()
        //默认x自增
        val coordinates = points.mapIndexed { index, element ->
            val y = (element.y - min.y) * ratioh
            val pointf = PointF(
                (element.x - firstw) * ratiow, halfHeight + (y-halfHeight )*_scaleX
            )
            if (linePath.isEmpty) {
                linePath.moveTo(pointf.x, pointf.y)
            } else {
                linePath.lineTo(pointf.x, pointf.y)
            }
            pointf
        }.toList()

        progShaderPath.reset()
        progShaderPath.addPath(linePath)
        progShaderPath.lineTo(coordinates.last().x, 0F)
        progShaderPath.lineTo(coordinates.first().x, 0F)
        progShaderPath.close()
        postInvalidate()
    }

    fun setProgress(progress: Float) {
        if (points.isNotEmpty()) {
            prog = progress
            postInvalidate()
        }
    }

    override fun setScaleX(scaleX: Float) {
        _scaleX = scaleX
        covertInner()
    }

    override fun getScaleX(): Float {
        return _scaleX
    }

    override fun setScaleY(scaleY: Float) {
        super.setScaleY(1F)
    }
}