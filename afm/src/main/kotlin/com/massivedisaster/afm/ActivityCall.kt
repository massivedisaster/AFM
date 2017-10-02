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

package com.massivedisaster.afm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.massivedisaster.afm.activity.BaseActivity
import kotlin.reflect.KClass

/**
 *
 */

class ActivityCall private constructor() : Builder() {

    private val DEFAULT_FLAGS = 0x0

    private val CONTEXT_NULL_EXCEPTION = "You must call init() before to define a baseActivity to make you're call"
    private val CLASS_NULL_EXCEPTION = "You must call setActivity to define an activity to be called"

    private var activity: BaseActivity? = null
    private var fragment: Fragment? = null
    private var context: Context? = null

    private var bundle: Bundle = Bundle()
    private var activityClass: Class<out AppCompatActivity>? = null
    private var fragmentClass: Class<out Fragment>? = null
    private var flags: Int = DEFAULT_FLAGS
    private var tag: String? = null
    private var requestCode: Int? = null
    private var bundleOptions: Bundle? = null

    private var shareElementsMap: Array<android.support.v4.util.Pair<View, String>> = emptyArray()

    companion object {
        /**
         * Initializes builder setting up values to bundle (it will be used at the end, even empty)
         * and baseActivity to start the activity

         * @param baseActivity
         * *
         * @return Builder instance
         */
        fun init(baseActivity: BaseActivity, activity: KClass<out BaseActivity>, fragmentClass: KClass<out Fragment>): ActivityCall {
            val activityCall = ActivityCall()
            activityCall.init(baseActivity, activity.java, fragmentClass.java)
            return activityCall
        }

        /**
         * Initializes builder setting up the activity that will hold the fragment

         * @param fragment
         * *
         * @return Builder instance
         */
        fun init(fragment: Fragment, activity: KClass<out BaseActivity>, fragmentClass: KClass<out Fragment>): ActivityCall {
            val activityCall = ActivityCall()
            activityCall.init(fragment.context, activity.java, fragment, fragmentClass.java)
            return activityCall
        }

        /**
         * Initializes builder setting up the activity that will hold the fragment

         * @param context
         * *
         * @return Builder instance
         */
        fun init(context: Context, activity: KClass<out BaseActivity>, fragmentClass: KClass<out Fragment>): ActivityCall {
            val activityCall = ActivityCall()
            activityCall.init(context, activity.java, fragmentClass.java)
            return activityCall
        }
    }

    /**
     * Initializes an instance of ActivityCall retrieving all necessary fields to be able to at least
     * instantiate an activity with a container fragment and a given fragment (This fragment may be used
     * to retrieve context to use with shared elements and start activity for result)
     */
    private fun init(context: Context, activity: Class<out BaseActivity>, fragment: Fragment?, fragmentClass: Class<out Fragment>) {
        this.activityClass = activity
        this.fragmentClass = fragmentClass
        this.context = context
        fragment ?: return
        this.fragment = fragment
    }

    /**
     * Initializes an instance of ActivityCall retrieving all necessary fields to be able to at least
     * instantiate an activity with a container fragment
     */
    private fun init(context: Context, activity: Class<out BaseActivity>, fragmentClass: Class<out Fragment>) {
        init(context, activity, null, fragmentClass)
    }

    override fun build() {
        validate()

        val intent = Intent(context, activityClass)

        if (flags != DEFAULT_FLAGS) {
            intent.flags = flags
        }

        if (tag != null) {
            intent.putExtra(BaseActivity.ACTIVITY_MANAGER_FRAGMENT_TAG, tag)
        }

        if (!shareElementsMap.isEmpty()) {
            intent.putExtra(BaseActivity.ACTIVITY_MANAGER_FRAGMENT_SHARED_ELEMENTS, true)
            bundleOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity ?: fragment?.activity, *shareElementsMap).toBundle()
        }

        bundle.putString(BaseActivity.ACTIVITY_MANAGER_FRAGMENT, fragmentClass?.canonicalName)
        intent.putExtras(bundle)

        if (requestCode == null) {
            when {
                activity != null -> ContextCompat.startActivity(activity, intent, bundleOptions)
                fragment != null -> fragment?.startActivity(intent, bundleOptions)
                else -> context?.startActivity(intent)
            }
        } else {
            when {
                activity != null -> ActivityCompat.startActivityForResult(activity, intent, requestCode!!, bundleOptions)
                fragment != null -> fragment?.startActivityForResult(intent, requestCode!!, bundleOptions)
                else -> context?.startActivity(intent)
            }
        }

        reset()
    }

    override fun validate() {
        if (activity == null && fragment == null && context == null) {
            throw NullPointerException(CONTEXT_NULL_EXCEPTION)
        }

        if (activityClass == null) {
            throw NullPointerException(CLASS_NULL_EXCEPTION)
        }
    }

    override fun reset() {
        bundle = Bundle()
        bundleOptions = null
        requestCode = null
        activity = null
        activityClass = null
        fragmentClass = null
        fragment = null
        context = null
        flags = DEFAULT_FLAGS
        tag = null
        shareElementsMap = emptyArray()
    }

    /**
     * Defines activity's flags to be used.
     * e.g.: Intent.FLAG_ACTIVITY_NEW_TASK

     * @param flags used to open the activity
     * *
     * @return Builder instance
     */
    fun setFlags(flags: Int): ActivityCall {
        this.flags = flags
        return this
    }

    /**
     * Defines bundle to be used by the opened activity or fragment

     * @param bundle arguments
     * *
     * @return Builder instance
     */
    fun setBundle(bundle: Bundle?): ActivityCall {
        if (bundle != null) {
            this.bundle = bundle
        }
        return this
    }

    fun setTag(tag: String): ActivityCall {
        this.tag = tag
        return this
    }

    fun addSharedElement(view: View, transactionName: String): ActivityCall {
        shareElementsMap = shareElementsMap.plus(android.support.v4.util.Pair(view, transactionName))
        return this
    }

    /**
     * Set the Bundle to be passed to the new Fragment.
     *
     * @param requestCode The code to be used in the {@link Activity#startActivityForResult}.
     * @return Return the Transaction instance.
     */
    fun setRequestCode(requestCode: Int): ActivityCall {
        this.requestCode = requestCode
        return this
    }

}