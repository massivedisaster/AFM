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

package com.massivedisaster.afm.sample.feature.home

import android.view.View
import android.widget.Button
import com.massivedisaster.adal.fragment.BaseFragment
import com.massivedisaster.afm.ActivityCall
import com.massivedisaster.afm.sample.R
import com.massivedisaster.afm.sample.activity.ActivityFullscreen
import com.massivedisaster.afm.sample.activity.ActivityToolbar
import com.massivedisaster.afm.sample.feature.backpressed.FragmentOnBackPressed
import com.massivedisaster.afm.sample.feature.navigation.FragmentAddReplace
import com.massivedisaster.afm.sample.feature.sharedelements.FragmentSharedElementsOptions

class FragmentHome : BaseFragment(), View.OnClickListener {

    override fun layoutToInflate(): Int {
        return R.layout.fragment_home
    }

    override fun doOnCreated() {
        activity.title = getString(R.string.home)

        findViewById<Button>(R.id.btnSharedElements).setOnClickListener(this)
        findViewById<Button>(R.id.btnFragmentOnBackPressed).setOnClickListener(this)
        findViewById<Button>(R.id.btnOpenFragmentOtherActivity).setOnClickListener(this)
        findViewById<Button>(R.id.btnOpenFragmentOtherActivityFullscreen).setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnOpenFragmentOtherActivity -> ActivityCall.init(activity, ActivityToolbar::class, FragmentAddReplace::class)
                    .build()
            R.id.btnOpenFragmentOtherActivityFullscreen -> ActivityCall.init(activity, ActivityFullscreen::class, FragmentAddReplace::class)
                    .build()
            R.id.btnSharedElements -> ActivityCall.init(activity, ActivityToolbar::class, FragmentSharedElementsOptions::class)
                    .build()
            R.id.btnFragmentOnBackPressed -> ActivityCall.init(activity, ActivityToolbar::class, FragmentOnBackPressed::class)
                    .build()
            else -> {
            }
        }
    }
}
