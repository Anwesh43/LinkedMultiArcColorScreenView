package com.anwesh.uiprojects.linkedmultiarccolorscreenview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.multiarccolorscreenview.MultiArcColorScreenView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MultiArcColorScreenView.create(this)
    }
}
