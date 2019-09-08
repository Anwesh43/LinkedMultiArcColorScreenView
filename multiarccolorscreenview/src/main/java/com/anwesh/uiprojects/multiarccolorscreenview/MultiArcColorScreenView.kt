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

class MultiArcColorScreenView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class MACSNode(var i : Int, val state : State = State()) {

        private var next : MACSNode? = null
        private var prev : MACSNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = MACSNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, sc : Float, paint : Paint) {
            canvas.drawMACSNode(i, state.scale, sc, paint)
            if (state.scale > 0f) {
                next?.draw(canvas, state.scale, paint)
            }
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : MACSNode {
            var curr : MACSNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this 
        }

    }
}
