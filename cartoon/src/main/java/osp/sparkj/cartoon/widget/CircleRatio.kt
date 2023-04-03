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
        strokeWidth = dp2 / 2
        pathEffect = DashPathEffect(floatArrayOf(dp2 * 2, dp2 * 2), 0F)
        style = Paint.Style.STROKE
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        textSize = this@CircleRatio.textSize
    }
    private val textHeight = run {
        textPaint.descent() - textPaint.ascent()
    }
    private val alignOffset by lazy {
        val pieceAngle = 360F / datas.size
        val alignAngle = 90 - pieceAngle / 2
        (2 * Math.PI * circleBigRadius * alignAngle / 360).toFloat()
    }

    private val dashCirclePath = Path()
    private var maxValue = 0
    private val dataShaders = mutableMapOf<Int, RadialGradient?>()
    private val textPath = Path()

    private val datas = listOf<Pair<String, Int>>(
        "a测试0" to 3,
        "b测试11" to 1,
        "c测试222" to 6,
        "d测试3333" to 4,
        "e测试44444" to 5,
        "e测试55" to 2,
    )

    private val ordinaryColors =
        intArrayOf(
            "#9C9B9A".toColorInt(),
            "#8E8D8C".toColorInt(),
            "#807F7E".toColorInt(),
            "#757474".toColorInt(),
            "#666666".toColorInt(),
            "#565656".toColorInt()
        )
    private val highlightColors =
        intArrayOf(
            "#FE8F77".toColorInt(),
            "#FD7C60".toColorInt(),
            "#FE6C4C".toColorInt(),
            "#FD5E3C".toColorInt(),
            "#FA4E29".toColorInt(),
            "#FD3E15".toColorInt(),
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
                dataShaders[i] = stageGradient(value)
            } else {
                dataShaders[i] = null
            }
            value
        }.max()
    }

    private fun stageGradient(value: Int, gradientColors: IntArray = ordinaryColors): RadialGradient {
        val percent = value.toFloat() / datas.size
        val floatShader = 1f / value
        val size = value * 2 - 2
        val colors = IntArray(size)
        val floats = FloatArray(size) { index ->
            colors[index] = gradientColors[((index + 1) / 2)]
            if (index % 2 == 0) {
                floatShader * (index / 2 + 1)
            } else {
                floatShader * (index / 2 + 1) + 0.0001f
            }
        }
        return RadialGradient(
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

        val num = datas.size
        val eachAngle = 360F / num
        var startAngle = -eachAngle / 2 - 90

        //最外面 文字绘制依赖的圆形的 半径
        val outerTextCirclePathRadius = circleBigRadius + circlePaint.strokeWidth
        val eachLength = 2 * Math.PI.toFloat() * outerTextCirclePathRadius / num

        //画虚线
        canvas.drawPath(dashCirclePath, circleDashPaint)

        for (i in 0 until num) {
            val data = datas[i]
            val percent = data.second.toFloat() / num
            val dataRectF = circleRectf * percent

            //填充扇形
            circleDataPaint.style = Paint.Style.FILL
            circleDataPaint.color = ordinaryColors[0]
            if (data.second == maxValue) {
                circleDataPaint.shader = stageGradient(maxValue, highlightColors)
            } else {
                circleDataPaint.shader = dataShaders[i]
            }
            canvas.drawArc(dataRectF, startAngle, eachAngle, true, circleDataPaint)

            //扇形外环 盖住虚线用的
            if (data.second == maxValue) {
                circleDataPaint.color = highlightColors[highlightColors.size - 1]
            } else {
                circleDataPaint.color = ordinaryColors[data.second - 1]
            }
            circleDataPaint.style = Paint.Style.STROKE
            circleDataPaint.shader = null
            canvas.drawArc(dataRectF, startAngle, eachAngle, false, circleDataPaint)

            //最上层白色扇形边缘
            val scale = (circleBigRadius + circlePaint.strokeWidth) / circleBigRadius
            val outerRectF = circleRectf * scale
            canvas.drawArc(outerRectF, startAngle, eachAngle, true, circlePaint)

            //沿着 圆 画文字
            if (data.second == maxValue) {
                textPaint.color = Color.RED
            } else {
                textPaint.color = Color.BLACK
            }
            val str = datas[i].first
//            val str = "$i:${startAngle}"
            textPath.reset()
            val textWidth = textPaint.measureText(str)
//            canvas.drawLine(0F, 0F, width.toFloat(), 0f, textPaint)
            val pieceCenterAngel = startAngle + eachAngle / 2
            if (pieceCenterAngel > 180 || pieceCenterAngel < 0) {
                textPath.addArc(outerRectF, startAngle, eachAngle)
                val offset = (eachLength - textWidth) / 2
                //贴紧 外层白色边框要移动的距离
                val clingTo = -circlePaint.strokeWidth
                canvas.drawTextOnPath(
                    str, textPath, offset, clingTo - textOffsetCicle, textPaint
                )
            } else {
                textPath.addCircle(0F, 0F, outerTextCirclePathRadius, Path.Direction.CCW)
                val offset = alignOffset + (eachLength - textWidth) / 2 + eachLength * (datas.size - i)
//                val offset = alignOffset + eachLength * 3
                //贴紧 外层白色边框要移动的距离
                val clingTo = -textPaint.ascent()
                canvas.drawTextOnPath(
                    str, textPath, offset, clingTo + textOffsetCicle, textPaint
                )
            }
            startAngle += eachAngle
        }
    }
}
