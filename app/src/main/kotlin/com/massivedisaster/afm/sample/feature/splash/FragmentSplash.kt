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

package com.massivedisaster.afm.sample.feature.splash

import android.app.Activity
import com.massivedisaster.adal.fragment.AbstractSplashFragment
import com.massivedisaster.afm.ActivityCall
import com.massivedisaster.afm.sample.R
import com.massivedisaster.afm.sample.activity.ActivityHome
import com.massivedisaster.afm.sample.feature.home.FragmentHome

class FragmentSplash : AbstractSplashFragment() {

    override fun layoutToInflate(): Int {
        return R.layout.fragment_splash
    }

    override fun onSplashStarted() {
        onSplashFinish { openHomeScreen(activity) }
    }

    /**
     * Open the home screen
     *
     * @param activity The actual activity.
     */
    private fun openHomeScreen(activity: Activity) {
        ActivityCall.init(activity, ActivityHome::class, FragmentHome::class).build()
        activity.finish()
    }
}
