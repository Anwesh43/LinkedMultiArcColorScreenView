package com.anwesh.uiprojects.multiarccolorscreenview

/**
 * Created by anweshmishra on 09/09/19.
 */

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Color

val colors : Array<String> = arrayOf("#9C27B0", "#2196F3", "#DD2C00", "#00C853", "#f44336")
val circles : Int = 5
val scGap : Float = 0.01f
val delay : Long = 30
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawArcColorScreen(i : Int, sc1 : Float, sc2 : Float, size : Float, paint : Paint) {
    val sc1i : Float = sc1.divideScale(i, circles)
    val sc2i : Float = sc2.divideScale(i, circles)
    var sweepDeg : Float = 360f * (1 - sc1.divideScale(i, circles))
    if (sc2 > 0f) {
        sweepDeg = 360f * sc2
    }
    save()
    translate(i * 2 * size + size, 0f)
    paint.color = Color.parseColor(colors[i])
    drawArc(RectF(-size, -size, size, size), 360f * sc1i, sweepDeg, true, paint)
    restore()
}

fun Canvas.drawMultiArcColorScreen(sc1 : Float, sc2 : Float, size : Float, paint : Paint) {
    for (j in 0..(circles - 1)) {
        drawArcColorScreen(j, sc1, sc2, size, paint)
    }
}

fun Canvas.drawMACSNode(i : Int, scale : Float, sc : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w / (colors.size)
    val size : Float = gap / 2
    save()
    translate(0f, h / 2)
    drawMultiArcColorScreen(scale, sc, size, paint)
    restore()
}

