package osp.sparkj.cartoon.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.CallSuper
import osp.sparkj.cartoon.extend.todpf
import kotlin.concurrent.thread


/**
 * @author yun.
 * @date 2022/5/20
 * @des [一句话描述]
 * @since [https://github.com/5hmlA]
 * <p><a href="https://github.com/5hmlA">github</a>
 */
abstract class Graph @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    open val xOffset: Float by lazy {
        debugPaint.strokeWidth
    }
    open val yOffset: Float by lazy {
        debugPaint.strokeWidth
    }

    open val showCoordinateSystem = false

    val debugPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
        style = Paint.Style.STROKE
        strokeWidth = 1.todpf
    }

    fun coordinateSystemHeight(paint: Paint?): Float {
        return height - yOffset - (paint?.strokeWidth ?: 0F)
    }

    fun coordinateSystemWidth(paint: Paint?): Float {
        return width - xOffset - (paint?.strokeWidth ?: 0F)
    }

    @CallSuper
    override fun onDraw(canvas: Canvas) {
        //<editor-fold desc="转为正常坐标系">
        canvas.translate(xOffset, height.toFloat() - yOffset)
        canvas.scale(1F, -1F)
        //</editor-fold>
        if (showCoordinateSystem) {//纵轴
            canvas.drawLine(xOffset, -yOffset, xOffset, height.toFloat(), debugPaint)
            //横轴
            canvas.drawLine(-xOffset, yOffset, width.toFloat(), yOffset, debugPaint)
        }

    }
}

class ProgressGraph @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Graph(context, attrs, defStyleAttr) {

    val dp5 = 5.todpf

    override val yOffset: Float by lazy {
        dp5
    }

    var points: List<PointF> = mutableListOf()
    val linePath = Path()
    val bgLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#33FFFFFF")
        strokeWidth = 2.todpf
        strokeJoin = Paint.Join.ROUND
        style = Paint.Style.STROKE
        pathEffect = CornerPathEffect(10F)
    }

    val progPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        strokeWidth = 1.todpf
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
            intArrayOf(Color.TRANSPARENT, Color.parseColor("#66FFFFFF")),
            floatArrayOf(0F, h.toFloat()),
            Shader.TileMode.CLAMP
        )
        progShaderPaint.shader = progressGradient
        _scaleX = if (visibility == GONE) 0F else 1F
        convert()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
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
        val ratiow = coordinateSystemWidth(bgLinePaint) / totalw
        val height = coordinateSystemHeight(bgLinePaint)
        val ratioh = (height - 2 * dp5) / (max!!.y - min!!.y)
        val halfHeight = height / 2
        linePath.reset()
        //默认x自增
        val coordinates = points.mapIndexed { index, element ->
            val y = (element.y - min.y) * ratioh
            val pointf = PointF(
                (element.x - firstw) * ratiow, halfHeight + (y - halfHeight) * _scaleX
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