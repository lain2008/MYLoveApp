package com.loveapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class LoveEffectView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val hearts = mutableListOf<Heart>()
    private val stars = mutableListOf<Star>()
    private var textLines = mutableListOf<String>()
    private var textList = mutableListOf<String>()
    private var currentTextIndex = 0
    private var textSize = 100f

    private val textPaint = Paint().apply {
        color = Color.parseColor("#FF69B4")
        textSize = 120f
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
        setShadowLayer(10f, 0f, 0f, Color.WHITE)
    }

    private val heartPaint = Paint().apply {
        color = Color.parseColor("#FF69B4")
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val starPaint = Paint().apply {
        color = Color.YELLOW
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    init {
        textList.add("我爱你")
        startAnimation()
    }

    fun setTextList(list: List<String>) {
        textList.clear()
        textList.addAll(list)
    }

    fun nextText() {
        if (textList.isNotEmpty()) {
            currentTextIndex = (currentTextIndex + 1) % textList.size
            val rawText = textList[currentTextIndex]
            textLines = rawText.split("|").toMutableList()
            invalidate()
        }
    }

    fun setRandomText() {
        if (textList.isNotEmpty()) {
            currentTextIndex = Random.nextInt(textList.size)
            val rawText = textList[currentTextIndex]
            textLines = rawText.split("|").toMutableList()
            
            val totalLength = textLines.joinToString("").length
            textSize = when {
                totalLength > 20 -> 45f
                totalLength > 15 -> 55f
                totalLength > 10 -> 70f
                else -> 90f
            }
            textPaint.textSize = textSize
            invalidate()
        }
    }

    private fun startAnimation() {
        postDelayed(object : Runnable {
            override fun run() {
                if (hearts.size < 30) {
                    hearts.add(Heart(
                        x = Random.nextFloat() * width,
                        y = height.toFloat() + Random.nextFloat() * 200,
                        size = Random.nextFloat() * 40 + 20,
                        speed = Random.nextFloat() * 5 + 3,
                        alpha = Random.nextInt(100, 255)
                    ))
                }
                if (stars.size < 100) {
                    stars.add(Star(
                        x = Random.nextFloat() * width,
                        y = Random.nextFloat() * height,
                        size = Random.nextFloat() * 8 + 2,
                        alpha = Random.nextInt(100, 255),
                        blinkSpeed = Random.nextFloat() * 0.1f + 0.02f
                    ))
                }

                for (i in hearts.indices) {
                    hearts[i].y -= hearts[i].speed
                    hearts[i].x += kotlin.math.sin(hearts[i].y / 50f) * 2f
                }
                hearts.removeAll { it.y < -100 }

                for (i in stars.indices) {
                    stars[i].alpha = (stars[i].alpha + stars[i].blinkSpeed * 50).toInt()
                    if (stars[i].alpha > 255) stars[i].blinkSpeed = -kotlin.math.abs(stars[i].blinkSpeed)
                    if (stars[i].alpha < 50) stars[i].blinkSpeed = kotlin.math.abs(stars[i].blinkSpeed)
                }

                invalidate()
                postDelayed(this, 30)
            }
        }, 30)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(Color.BLACK)

        for (star in stars) {
            starPaint.alpha = star.alpha.toInt()
            drawStar(canvas, star.x, star.y, star.size)
        }

        for (heart in hearts) {
            heartPaint.alpha = heart.alpha
            drawHeart(canvas, heart.x, heart.y, heart.size)
        }

        val x = width / 2f
        val centerY = height / 2f
        textPaint.color = Color.parseColor("#FF69B4")
        
        val lineHeight = textSize * 1.4f
        val totalHeight = textLines.size * lineHeight
        var startY = centerY - totalHeight / 2 + textSize / 2
        
        for (line in textLines) {
            canvas.drawText(line, x, startY, textPaint)
            startY += lineHeight
        }
    }

    private fun drawHeart(canvas: Canvas, x: Float, y: Float, size: Float) {
        val path = android.graphics.Path()
        val topCurveHeight = size * 0.3f
        path.moveTo(x, y + topCurveHeight)
        path.cubicTo(x - size / 2, y - size / 2, x - size, y + topCurveHeight / 3, x, y + size)
        path.moveTo(x, y + topCurveHeight)
        path.cubicTo(x + size / 2, y - size / 2, x + size, y + topCurveHeight / 3, x, y + size)
        canvas.drawPath(path, heartPaint)
    }

    private fun drawStar(canvas: Canvas, cx: Float, cy: Float, radius: Float) {
        val path = android.graphics.Path()
        val angle = Math.PI / 2.5
        path.moveTo(cx, cy - radius)
        for (i in 0 until 5) {
            val x = cx + (radius * kotlin.math.cos(angle + i * 2 * Math.PI / 5)).toFloat()
            val y = cy + (radius * kotlin.math.sin(angle + i * 2 * Math.PI / 5)).toFloat()
            path.lineTo(x, y)
            val innerX = cx + (radius * 0.4f * kotlin.math.cos(angle + Math.PI / 5 + i * 2 * Math.PI / 5)).toFloat()
            val innerY = cy + (radius * 0.4f * kotlin.math.sin(angle + Math.PI / 5 + i * 2 * Math.PI / 5)).toFloat()
            path.lineTo(innerX, innerY)
        }
        path.close()
        canvas.drawPath(path, starPaint)
    }

    data class Heart(var x: Float, var y: Float, var size: Float, var speed: Float, var alpha: Int)
    data class Star(var x: Float, var y: Float, var size: Float, var alpha: Int, var blinkSpeed: Float)
}
