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
