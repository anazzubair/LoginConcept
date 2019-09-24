package com.anazzubair.loginconcept

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anazzubair.loginconcept.controllers.PagerController
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeControllerRouter(savedInstanceState)
    }

    private fun initializeControllerRouter(savedInstanceState: Bundle?) {
        router = Conductor.attachRouter(this, controller_container, savedInstanceState)
        if(!router.hasRootController()) router.setRoot(RouterTransaction.with(PagerController()))
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}
