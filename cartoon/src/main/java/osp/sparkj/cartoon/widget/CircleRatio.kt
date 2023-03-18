package osp.sparkj.cartoon.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.times
import androidx.core.graphics.toColorInt
import osp.sparkj.cartoon.wings.todpf


class CircleRatio @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    fun Int.alpha(alpha: Float): Int {
        val a = 255.coerceAtMost(0.coerceAtLeast((alpha * 255).toInt())) shl 24
        val rgb = 0x00Ffffff and this
        return a + rgb
    }

    val dp2 = 2.todpf
    val dp4 = 4.todpf
    val dp20 = 20.todpf

    private val textOffsetCicle = 9.todpf
    private var circleRectf = RectF()
    private val textSize = dp20
    private var circleBigRadius = 0F
    private val circleBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#0a000000")
        style = Paint.Style.FILL
        strokeWidth = dp4
    }
    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        strokeWidth = dp4
        style = Paint.Style.STROKE
    }
    private val circleDataPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GREEN
        strokeWidth = dp4
        style = Paint.Style.STROKE
    }
    private val circleDashPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        strokeWidth = dp2
        pathEffect = DashPathEffect(floatArrayOf(dp2 * 2, dp2 * 2), 0F)
        style = Paint.Style.STROKE
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        textSize = this@CircleRatio.textSize
    }
    private val dashCirclePath = Path()
    private var maxValue = 0
    private val dataShaders = mutableMapOf<Int, RadialGradient?>()
    private val textPath = Path()

    private val datas = listOf<Pair<String, Int>>(
        "测试" to 3,
        "测试1" to 1,
        "测试2" to 2,
        "测试3" to 4,
        "测试4" to 5
    )

    private val grayColors =
        intArrayOf(
            "#EDEEEE".toColorInt(),
            "#E7E8E8".toColorInt(),
            "#E1E3E3".toColorInt(),
            "#DCDEDF".toColorInt(),
            "#D8DADB".toColorInt()
        )
    private val greenColors =
        intArrayOf(
            "#E7F3D7".toColorInt(),
            "#DDF0C5".toColorInt(),
            "#D4EEB4".toColorInt(),
            "#CCECA5".toColorInt(),
            "#C5EA97".toColorInt()
        )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val side = width.coerceAtMost(height)
        val measureSpec = MeasureSpec.makeMeasureSpec(side, MeasureSpec.EXACTLY)
        super.onMeasure(measureSpec, measureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        circleBigRadius = (width - textSize * 4) / 2
        circleRectf = RectF(-circleBigRadius, -circleBigRadius, circleBigRadius, circleBigRadius)
        updateFromDatas()
    }

    private fun updateFromDatas() {
        maxValue = datas.asSequence().mapIndexed { i, pair ->
            //4条虚线
            if (i > 0) {
                dashCirclePath.addCircle(
                    0F,
                    0F,
                    circleBigRadius * i / datas.size.toFloat(), Path.Direction.CW
                )
            }

            //shader
            val value = pair.second
            if (value > 1) {
                stageGradient(value, i)
            } else {
                dataShaders[i] = null
            }
            value
        }.max()
    }

    private fun stageGradient(value: Int, i: Int) {
        val percent = value.toFloat() / datas.size
        val floatShader = 1f / value
        val size = value * 2 - 2
        val colors = IntArray(size)
        val floats = FloatArray(size) { index ->
            colors[index] = grayColors[((index + 1) / 2)]
            if (index % 2 == 0) {
                floatShader * (index / 2 + 1)
            } else {
                floatShader * (index / 2 + 1) + 0.0001f
            }
        }
        dataShaders[i] = RadialGradient(
            0F,
            0F,
            percent * circleBigRadius,
            colors,
            floats,
            Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(width / 2f, height / 2f)
        canvas.drawCircle(0F, 0F, circleBigRadius + dp2, circleBgPaint)

        val num = 5
        val eachAngle = 360F / num
        var startAngle = -eachAngle / 2 - 90
        val eachLength = 2 * Math.PI.toFloat() * circleBigRadius / num

        //画虚线
        canvas.drawPath(dashCirclePath, circleDashPaint)

        for (i in 0 until num) {
            val data = datas[i]
            val percent = data.second.toFloat() / num
            val dataRectF = circleRectf * percent

            //填充扇形
            circleDataPaint.style = Paint.Style.FILL
            circleDataPaint.color = grayColors[0]
            circleDataPaint.shader = dataShaders[i]
            canvas.drawArc(dataRectF, startAngle, eachAngle, true, circleDataPaint)

            //扇形外环
            if (data.second == maxValue) {
                circleDataPaint.color = greenColors[0]
            } else {
                circleDataPaint.color = grayColors[0]
            }
            circleDataPaint.style = Paint.Style.STROKE
            circleDataPaint.shader = null
            canvas.drawArc(dataRectF, startAngle, eachAngle, false, circleDataPaint)

            //最上层白色扇形边缘
            val scale = (circleBigRadius + circlePaint.strokeWidth) / circleBigRadius
            canvas.drawArc(circleRectf * scale, startAngle, eachAngle, true, circlePaint)

            //沿着 园画文字
            textPath.reset()
            textPath.addArc(circleRectf, startAngle, eachAngle)
            val str = datas[i].first
            val textWidth = textPaint.measureText(str)
            val offset = eachLength - textWidth
            canvas.drawTextOnPath(str, textPath, offset / 2, -textOffsetCicle, textPaint)
            startAngle += eachAngle
        }
    }
}
