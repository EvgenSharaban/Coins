package com.example.customviewwithoutcompose.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt

class RhombusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFF0000.toInt() // тут для примера, не обязательно
        style = Paint.Style.FILL
    }

    private val path = Path()

    @ColorInt
    var backColor: Int = 0
        set(value) {
            field = value
            paint.color = backColor
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val centerX = width / 2
        val centerY = height / 2

        path.reset()
        path.moveTo(centerX, 0f) // Верхняя точка
        path.lineTo(width, centerY) // Правая точка
        path.lineTo(centerX, height) // Нижняя точка
        path.lineTo(0f, centerY) // Левая точка
        path.close()

        canvas.drawPath(path, paint)
    }
}