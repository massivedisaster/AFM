package com.massivedisaster.afm.sample

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.massivedisaster.afm.activity.BaseActivity

abstract class BaseFragment<U : ViewDataBinding> : Fragment() {

    val baseActivity:BaseActivity by lazy {
        activity as BaseActivity
    }

    lateinit var dataBinding: U

    @LayoutRes
    protected abstract fun layoutToInflate(): Int

    open fun doOnCreated() {
        // Intended.
    }

    protected fun <T> retrieveBundleParam(bundleName: String, default: T): T {
        return if (arguments != null && arguments.containsKey(bundleName)) arguments[bundleName] as T else default
    }

    protected open fun defineViews() {
        // Intended.
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(layoutInflater, layoutToInflate(), container, false)

        return dataBinding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defineViews()
        doOnCreated()
    }

    override fun onPause() {
        val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        super.onPause()
    }

}