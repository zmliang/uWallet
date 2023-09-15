package com.self.app.ui.view.chart


import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.self.app.R
import com.self.app.dp
import com.self.app.getPixels
import com.self.app.sp
import kotlinx.android.parcel.Parcelize
import kotlin.math.ceil

//zml
class ChartView  : View {
    enum class CHART_GRAVITY(val value:Int){
        LEFT(0),
        RIGHT(1)
    }
    private val defaultWidth:Int = getPixels(400,context)


    private val mPaint:Paint = Paint()
    private var mChartHeight:Float = 50f
    private var chartCount:Int = 10

    private var textSize:Float = 12.sp.toFloat()

    private var animUpdate:Boolean = false
    private var leftColor:Int = Color.RED
    private var rightColor:Int = Color.GREEN
    private var chartColor:Int = Color.BLACK
    private var chartGravity:CHART_GRAVITY = CHART_GRAVITY.RIGHT

    private val mTextRect:Rect = Rect()

    private var chartModels:MutableList<ChartModel> = arrayListOf()

    private var volume:Float = 10f


    private var animator:ValueAnimator? = null

    private var diff: DiffEval? = DiffEval(mutableListOf())


    init {
        mPaint.color = Color.RED
        mPaint.isAntiAlias = true
    }

    constructor(context: Context, attributeSet: AttributeSet? = null,defStyleAttr: Int = 0):super(context,attributeSet,defStyleAttr){
        this.initStyle(context, attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet? = null):this(context,attributeSet,0)

    constructor(context: Context):this(context,null,0)

    private fun initStyle(context: Context, attributeSet: AttributeSet? = null){
        val typedArray: TypedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ChartViewLayout)
        animUpdate = typedArray.getBoolean(R.styleable.ChartViewLayout_anim_update,false)
        leftColor = typedArray.getColor(R.styleable.ChartViewLayout_left_text_color,context.getColor(R.color.colorPrimary))
        rightColor = typedArray.getColor(R.styleable.ChartViewLayout_right_text_color,context.getColor(R.color.colorPrimaryDark))
        mChartHeight = typedArray.getDimension(R.styleable.ChartViewLayout_chart_height,30f.dp.toFloat())
        mChartHeight = ceil(mChartHeight)
        textSize = typedArray.getDimension(R.styleable.ChartViewLayout_text_size,15f.sp.toFloat())

        chartColor = typedArray.getColor(R.styleable.ChartViewLayout_chart_color,context.getColor(R.color.teal_700))
        val gravity =typedArray.getInteger(R.styleable.ChartViewLayout_char_gravity,CHART_GRAVITY.RIGHT.value)
        if (gravity == 0){
            chartGravity = CHART_GRAVITY.LEFT
        }
        typedArray.recycle()
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // 获取view宽的SpecSize和SpecMode
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)

        // 获取view高的SpecSize和SpecMode
        var heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val totalH:Int = (mChartHeight*chartCount).toInt()
        heightSpecSize = if (heightSpecSize<totalH){totalH}else{heightSpecSize}
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            Log.i("zml","1")
            // 当view的宽和高都设置为wrap_content时，调用setMeasuredDimension(measuredWidth,measureHeight)方法设置view的宽/高为400px
            setMeasuredDimension(defaultWidth, totalH)
        }else if (widthSpecMode == MeasureSpec.AT_MOST){
            Log.i("zml","2")
            // 当view的宽设置为wrap_content时，设置View的宽为你想要设置的大小（这里我设置400px）,高就采用系统获取的heightSpecSize
            setMeasuredDimension(defaultWidth, heightSpecSize)
        }else if (heightSpecMode == MeasureSpec.AT_MOST){
            Log.i("zml","3")
            // 当view的高设置为wrap_content时，设置View的高为你想要设置的大小（这里我设置400px）,宽就采用系统获取的widthSpecSize
            setMeasuredDimension(widthSpecSize,  totalH)
        }else{
            setMeasuredDimension(widthSpecSize,  heightSpecSize)
        }



        //mChartHeight = (measuredHeight/chartCount).toFloat()
        Log.i("zml","measuredHeight=$measuredHeight, chartHeight=$mChartHeight , totalH=$totalH  ,  heightSize=$heightSpecSize")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val tmp = diff?.diffs ?: arrayListOf()
        Log.i("zml","onDraw=${this.measuredHeight}")
        for (i in 0 until chartCount){
            mPaint.color = chartColor
            var percent = if (tmp.size<=i){0.5f}else{tmp[i]}
            val mRectF = RectF((measuredWidth* percent),
                (mChartHeight*i),measuredWidth.toFloat(),mChartHeight*(i+1))
           // Log.i("zml","top=${mChartHeight*i}, bottom=${mChartHeight*(i+1)}")
            canvas?.drawRect(mRectF,mPaint)

            val text = "150K"
            //val fontMetrics = mPaint.fontMetricsInt
            mPaint.getTextBounds(text, 0, text.length, mTextRect)

            mPaint.color = leftColor
            mPaint.textSize = textSize//left
            canvas?.drawText(text,0f,
                mRectF.top+(mChartHeight-mTextRect.height())/2+mTextRect.height(),mPaint)

            mPaint.color = rightColor
            mPaint.textSize = textSize
            canvas?.drawText(text,(measuredWidth-mTextRect.width()).toFloat(),
                mRectF.top+(mChartHeight-mTextRect.height())/2+mTextRect.height(),mPaint)
        }


    }

    fun setChartModel(volume: Float,models:MutableList<ChartModel>){
        this.volume = volume
        chartModels = models
        val count = models.size
        if (count!=chartCount){
            chartCount = count
            requestLayout()
        }

        val d = DiffEval(arrayListOf())
        for (i in 0 until models.size) {
            d.diffs.add((models[i].volume/volume))
        }

        if (animUpdate) {
            animateUpdate(d)
        }else {
            diff = d
            invalidate()
        }

    }

    private fun animateUpdate(targetDif:DiffEval){
        if (animator!=null && animator?.isRunning!!) {
            animator?.end()
        }

        post {

            animator = ValueAnimator.ofObject(ChartDifEvaluator(),diff,targetDif)
                .setDuration(200).apply {
                    interpolator = LinearInterpolator()
                    addUpdateListener { animation ->
                        diff = animation.animatedValue as DiffEval

                        invalidate()
                    }
                    start()
                }
        }
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (animator?.isRunning!!) {
            animator?.end()
        }
    }




    @Parcelize
    data class ChartModel(
        var volume:Float = 0.5f,
        var leftTxt:String = "5.98",
        var rightTxt:String = "150K"
    ) : Parcelable

@Parcelize
data class DiffEval(
    var diffs:MutableList<Float>
) : Parcelable

}


