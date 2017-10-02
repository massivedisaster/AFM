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

package com.massivedisaster.afm.sample.feature.navigation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.massivedisaster.adal.fragment.BaseFragment
import com.massivedisaster.afm.FragmentCall
import com.massivedisaster.afm.activity.BaseActivity
import com.massivedisaster.afm.animation.TransactionAnimation
import com.massivedisaster.afm.sample.R

class FragmentAddReplace : BaseFragment(), View.OnClickListener {

    companion object {
        private val VALUE = "value"
    }

    private var mValue: String? = null

    private lateinit var mTxtNumberOfFragments: TextView
    private lateinit var mEdtValue: EditText

    override fun layoutToInflate(): Int {
        return R.layout.fragment_add
    }

    override fun restoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null && savedInstanceState.containsKey(VALUE)) {
            mValue = savedInstanceState.getString(VALUE)
        }
    }

    override fun doOnCreated() {
        activity.setTitle(R.string.navigation)

        findViewById<Button>(R.id.btnAddFragment).setOnClickListener(this)
        findViewById<Button>(R.id.btnReplaceFragment).setOnClickListener(this)
        findViewById<Button>(R.id.btnAddFragmentWithAnimation).setOnClickListener(this)

        mTxtNumberOfFragments = findViewById(R.id.txtNumberOfFragments)
        mEdtValue = findViewById(R.id.edtValue)

        mEdtValue.setText(mValue)

        mTxtNumberOfFragments.text = getString(R.string.number_fragment_in_this_activity,
                activity.supportFragmentManager.backStackEntryCount)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnAddFragment -> FragmentCall.init(activity as BaseActivity, FragmentAddReplace::class)
                    .setTransitionType(FragmentCall.TransitionType.ADD)
                    .addToBackStack(true)
                    .build()
            R.id.btnAddFragmentWithAnimation -> FragmentCall.init(activity as BaseActivity, FragmentAddReplace::class)
                    .setTransitionType(FragmentCall.TransitionType.ADD)
                    .addToBackStack(true)
                    .setTransactionAnimation(object : TransactionAnimation {
                        override val animationEnter: Int
                            get() = R.anim.enter_from_right

                        override val animationExit: Int
                            get() = R.anim.exit_from_left

                        override val animationPopEnter: Int
                            get() = R.anim.pop_enter

                        override val animationPopExit: Int
                            get() = R.anim.pop_exit
                    }).build()
            R.id.btnReplaceFragment -> FragmentCall.init(activity as BaseActivity, FragmentAddReplace::class)
                    .setTransitionType(FragmentCall.TransitionType.REPLACE)
                    .addToBackStack(true)
                    .build()
            else -> {
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        // Refresh value when fragment changes to visible
        if (!hidden) {
            mTxtNumberOfFragments.text = getString(R.string.number_fragment_in_this_activity,
                    activity.supportFragmentManager.backStackEntryCount)
            Log.d("AFM", "Number:" + activity.supportFragmentManager.backStackEntryCount)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putString(VALUE, mEdtValue.text.toString())
    }

}
