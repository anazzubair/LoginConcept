package com.anazzubair.loginconcept.glide

import android.view.View
import com.bluelinelabs.conductor.Controller
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager

object ControllerRequestManager {

    fun with(controller: Controller) : RequestManager {
        if(controller.activity == null) throw IllegalArgumentException("You cannot start a load until the Controller has been bound to a Context.")

        val requestManager = Glide.with(controller.activity!!)
        controller.addLifecycleListener(object : Controller.LifecycleListener() {
            override fun postCreateView(controller: Controller, view: View) {
                requestManager.onStart()
            }

            override fun postDestroyView(controller: Controller) {
                requestManager.onStop()
            }

            override fun preDestroy(controller: Controller) {
                requestManager.onDestroy()
                controller.removeLifecycleListener(this)
            }
        })
        return requestManager
    }
}