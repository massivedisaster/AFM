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

package com.massivedisaster.afm.sample.feature.sharedelements

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.massivedisaster.adal.fragment.BaseFragment
import com.massivedisaster.afm.ActivityCall
import com.massivedisaster.afm.activity.BaseActivity
import com.massivedisaster.afm.sample.R
import com.massivedisaster.afm.sample.activity.ActivityToolbar

class FragmentSharedElementsOptions : BaseFragment() {

    override fun layoutToInflate(): Int {
        return R.layout.fragment_shared_elements_options
    }

    override fun doOnCreated() {
        activity.setTitle(R.string.shared_elements)

        val adapter = ImageAdapter(makeSequence())
        adapter.setOnChildClickListener { view, integer, _ -> openItem(view, integer) }


        val rclItems = findViewById<RecyclerView>(R.id.rclItems)
        rclItems.layoutManager = GridLayoutManager(context, 2)
        rclItems.adapter = adapter
    }

    /**
     * Open a new Activity and share the element
     *
     * @param view    item view
     * @param integer element id
     */
    private fun openItem(view: View, integer: Int?) {
        val bundle = Bundle()
        bundle.putString("URL", "http://lorempixel.com/400/200/nature/" + integer!!)

        ActivityCall.init(activity as BaseActivity, ActivityToolbar::class, FragmentSharedElement::class)
                .setBundle(bundle)
                .addSharedElement(view.findViewById<View>(R.id.imgExample), "sharedElement")
                .build()
    }

    /**
     * Generate a List of integers between 2 values
     *
     * @return the List of items.
     */
    private fun makeSequence(): List<Int> {
        return (1..10).toList()
    }

}