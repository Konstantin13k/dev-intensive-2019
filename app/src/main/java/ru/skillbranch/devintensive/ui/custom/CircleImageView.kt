package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.graphics.toColorInt
import ru.skillbranch.devintensive.R
import java.lang.Math.min

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2f
    }

    private var border_color = DEFAULT_BORDER_COLOR
    private var border_width = DEFAULT_BORDER_WIDTH

    private val paint = Paint()
    private val path = Path()

    private var radius = 0.0f

    private lateinit var rect: RectF

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
        setBorderColor(a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR))
        setBorderWidth(a.getDimension(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH).toInt())
        a.recycle()
    }

    private fun cropCircle(canvas: Canvas) {
        radius = min(measuredWidth.toFloat(), measuredHeight.toFloat()) / 2
        rect = RectF(0.0f, 0.0f, measuredWidth.toFloat(), measuredHeight.toFloat())
        path.addRoundRect(rect, radius, radius, Path.Direction.CW)
        canvas.clipPath(path)
    }

    private fun drawBorder(canvas: Canvas) {
        paint.color = border_color
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = border_width * 2

        canvas.drawCircle(radius, radius, radius - border_width / 2f, paint)
    }

    fun getBorderWidth(): Int = border_width.toInt()
    fun setBorderWidth(dp: Int) {
        border_width = dp.toFloat()
    }

    fun getBorderColor(): Int = border_color
    fun setBorderColor(hex: String) {
        border_color = hex.toColorInt()
    }

    fun setBorderColor(colorId: Int) {
        border_color = colorId
    }


    override fun onDraw(canvas: Canvas) {
        cropCircle(canvas)
        super.onDraw(canvas)
        drawBorder(canvas)
    }
}