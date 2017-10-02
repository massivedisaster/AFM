/*
 * AFM - A library to help android developer working easily with activities and fragments.
 *
 * Copyright (c) 2017 AFM
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.massivedisaster.afm

import android.os.Build
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.transition.TransitionInflater
import android.view.View
import com.massivedisaster.afm.activity.BaseActivity
import com.massivedisaster.afm.animation.TransactionAnimation
import kotlin.reflect.KClass

/**
 * FragmentCall class allow add and replace fragments to your activity
 */
class FragmentCall private constructor() : Builder() {

    enum class TransitionType {
        ADD,
        REPLACE
    }

    private val INVALID_CONTAINER_ID = -1

    private val ACTIVITY_NULL_EXCEPTION = "You must call init() before to define an activity to open a fragment"
    private val CLASS_NULL_EXCEPTION = "You must call setFragment to define a fragment to be called"

    private var activity: BaseActivity? = null

    private var addToBackStack: Boolean = false
    private var bundle: Bundle? = null
    private var clazz: Class<out Fragment>? = null
    private var transactionAnimation: TransactionAnimation? = null
    private var shareElementsMap: MutableMap<String, View> = mutableMapOf()

    private var transitionType: TransitionType = TransitionType.ADD

    @IdRes
    private var containerId = INVALID_CONTAINER_ID

    companion object {

        /**
         * Initializes builder setting up the activity that will hold the fragment
         *
         * @param activity
         * *
         * @return Builder instance
         */
        fun init(activity: BaseActivity, clazz: KClass<out Fragment>): FragmentCall {
            val fragmentCall = FragmentCall()
            fragmentCall.init(activity, clazz)
            return fragmentCall
        }
    }

    /**
     * Initializes an instance of FragmentCall retrieving all necessary fields to add or replace
     * a fragment into an activity
     */
    private fun init(activity: BaseActivity, clazz: KClass<out Fragment>) {
        this.activity = activity
        this.clazz = clazz.java
    }

    override fun build() {
        validate()

        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        containerId = if (containerId != INVALID_CONTAINER_ID) containerId else activity!!.getContainerViewId()

        for ((transactionName, view) in shareElementsMap) {
            fragmentTransaction.addSharedElement(view, transactionName)
        }

        try {
            val fragment = clazz!!.newInstance()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fragment.sharedElementEnterTransition = TransitionInflater.from(activity).inflateTransition(android.R.transition.move)
                fragment.sharedElementReturnTransition = TransitionInflater.from(activity).inflateTransition(android.R.transition.move)
            }

            fragment.arguments = bundle

            val fragmentName = fragment.javaClass.name

            fragmentTransaction.addToBackStack(if (addToBackStack) fragmentName else null)

            if (transactionAnimation != null) {
                fragmentTransaction.setCustomAnimations(transactionAnimation!!.animationEnter, transactionAnimation?.animationExit!!,
                        transactionAnimation!!.animationPopEnter,
                        transactionAnimation!!.animationPopExit)
            }

            if (transitionType == TransitionType.ADD) {
                if (activity!!.supportFragmentManager.findFragmentById(containerId) != null) {
                    fragmentTransaction.hide(activity!!.supportFragmentManager.findFragmentById(containerId))
                }

                fragmentTransaction.add(containerId, fragment)
            } else {
                fragmentTransaction.replace(containerId, fragment)
            }

            fragmentTransaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        reset()
    }

    override fun validate() {
        if (activity == null) {
            throw NullPointerException(ACTIVITY_NULL_EXCEPTION)
        }

        if (clazz == null) {
            throw NullPointerException(CLASS_NULL_EXCEPTION)
        }
    }

    override fun reset() {
        activity = null
        addToBackStack = false
        bundle = null
        clazz = null
    }

    /**
     * Defines a container id to inflate the fragment
     *
     * @param containerId
     *
     * @return Builder instance
     */
    fun setContainerId(@IdRes containerId: Int): FragmentCall {
        this.containerId = containerId
        return this
    }

    /**
     * Defines bundle to be used by the opened fragment
     *
     * @param bundle arguments
     *
     * @return Builder instance
     */
    fun setBundle(bundle: Bundle): FragmentCall {
        this.bundle = bundle
        return this
    }

    /**
     * Define a flag to validate if adds the fragment to back stack or not
     *
     * @param addToBackStack
     *
     * @return Builder instance
     */
    fun addToBackStack(addToBackStack: Boolean): FragmentCall {
        this.addToBackStack = addToBackStack
        return this
    }

    /**
     * Transition type to be applied when instantiate the fragment
     * (It could be [.ADD] or [.REPLACE])
     *
     * @param transitionType [TransitionType]
     *
     * @return Builder instance
     */
    fun setTransitionType(transitionType: TransitionType): FragmentCall {
        this.transitionType = transitionType
        return this
    }

    /**
     * Set the transaction animation to be passed to the new Fragment.
     *
     * @param transactionAnimation The animation to used in fragment transaction.
     *
     * @return Builder instance
     */
    fun setTransactionAnimation(transactionAnimation: TransactionAnimation?): FragmentCall {
        this.transactionAnimation = transactionAnimation ?: activity

        return this
    }

    fun addSharedElement(view: View, transactionName: String): FragmentCall {
        shareElementsMap.put(transactionName, view)
        return this
    }
}