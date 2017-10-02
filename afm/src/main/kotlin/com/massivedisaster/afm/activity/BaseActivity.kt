/*
 * AFM - A library to help android developer working easily with activities and fragments.
 *       (Kotlin Version)
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
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.massivedisaster.afm.activity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import com.massivedisaster.afm.animation.TransactionAnimation
import com.massivedisaster.afm.fragment.OnBackPressedListener
import java.lang.Exception


abstract class BaseActivity : AppCompatActivity(), TransactionAnimation {

    companion object {
        @JvmField val ACTIVITY_MANAGER_FRAGMENT = "ACTIVITY_MANAGER_FRAGMENT"
        @JvmField val ACTIVITY_MANAGER_FRAGMENT_TAG = "ACTIVITY_MANAGER_FRAGMENT_TAG"
        @JvmField val ACTIVITY_MANAGER_FRAGMENT_SHARED_ELEMENTS = "ACTIVITY_MANAGER_FRAGMENT_SHARED_ELEMENTS"
    }

    override val animationEnter: Int
        get() = android.R.anim.fade_in
    override val animationExit: Int
        get() = android.R.anim.fade_out
    override val animationPopEnter: Int
        get() = android.R.anim.fade_in
    override val animationPopExit: Int
        get() = android.R.anim.fade_out

    private val APP_CAST_EXCEPTION = "You must define in your manifest your custom App as name of the application"
    private val INVALID_CONTAINER_ID = -1

    @LayoutRes
    protected abstract fun layoutToInflate(): Int


    open protected fun getDefaultFragment(): Class<out Fragment>? {
        Log.w("BaseActivity", "No default fragment implemented!")
        return null
    }

    open protected fun inject() {

    }

    open protected fun initializeDataBinding(view: View) {

    }

    open protected fun defineViews() {

    }

    open protected fun doOnCreated() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        }

        super.onCreate(savedInstanceState)

        initialize()
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
            requestWindowFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        }

        super.onCreate(savedInstanceState, persistentState)

        initialize()
    }

    private fun initialize() {
        val rootView = LayoutInflater.from(this).inflate(layoutToInflate(), null)
        setContentView(rootView)
        initializeDataBinding(rootView)

        if (supportFragmentManager.fragments.isEmpty() && supportFragmentManager.backStackEntryCount == 0) {
            if (intent.hasExtra(ACTIVITY_MANAGER_FRAGMENT)) {
                performInitialTransaction(getFragment(intent.getStringExtra(ACTIVITY_MANAGER_FRAGMENT)), getFragmentTag())
            } else if (getDefaultFragment() != null) {
                performInitialTransaction(getFragment(getDefaultFragment()!!.canonicalName), null)
            }
        }

        inject()
        defineViews()
        doOnCreated()
    }

    override fun onStop() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        super.onStop()
    }

    /**
     * Retrieves the container view to inflate the fragments of the activity

     * @return view container id
     */
    @IdRes
    abstract fun getContainerViewId(): Int

    /**
     * Perform a transaction of a fragment.

     * @param fragment the fragment to be applied.
     * *
     * @param tag the tag to be applied.
     */
    private fun performInitialTransaction(fragment: Fragment?, tag: String?) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(getContainerViewId(), fragment, tag)
        fragmentTransaction.commitNow()
    }

    private fun getFragmentTag(): String? {
        return intent.getStringExtra(ACTIVITY_MANAGER_FRAGMENT_TAG)
    }

    /**
     * Get a new instance of the Fragment by name.

     * @param clazz the canonical Fragment name.
     * *
     * @return the instance of the Fragment.
     */
    private fun getFragment(clazz: String): Fragment? {
        try {
            val fragment = Class.forName(clazz).newInstance() as Fragment

            if (intent.extras != null) {
                fragment.arguments = intent.extras
            }

            return fragment
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    override fun onBackPressed() {
        if (!canBackPress()) {
            super.onBackPressed()
        }
    }

    /**
     * Checks if the active fragment wants to consume the back press.

     * @return false if the fragment wants the activity to call super.onBackPressed, otherwise nothing will happen.
     */
    private fun canBackPress(): Boolean {
        val activeFragment = getActiveFragment()
        return activeFragment != null
                && activeFragment is OnBackPressedListener
                && (activeFragment as OnBackPressedListener).onBackPressed()
    }

    /**
     * Gets the active fragment.

     * @return the active fragment.
     */
    private fun getActiveFragment(): Fragment? {
        return supportFragmentManager.findFragmentById(getContainerViewId())
    }

}