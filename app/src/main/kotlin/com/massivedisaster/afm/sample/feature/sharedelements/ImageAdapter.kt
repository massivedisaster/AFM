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

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.massivedisaster.adal.adapter.AbstractLoadMoreBaseAdapter
import com.massivedisaster.adal.adapter.BaseViewHolder
import com.massivedisaster.afm.sample.R

internal class ImageAdapter
/**
 * Image Adapter constructor
 *
 * @param lstItems List of items to populate adapter
 */
(lstItems: List<Int>) : AbstractLoadMoreBaseAdapter<Int>(R.layout.adapter_image, lstItems) {

    override fun bindItem(holder: BaseViewHolder, item: Int?) {
        Glide.with(holder.itemView.context)
                .load("http://lorempixel.com/400/200/nature/" + item!!)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.getView(R.id.imgExample))
    }

    override fun bindError(holder: BaseViewHolder?, loadingError: Boolean) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
