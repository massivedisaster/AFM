package com.massivedisaster.afm

import android.support.v4.app.Fragment
import com.massivedisaster.afm.activity.BaseActivity

class MainActivity : BaseActivity() {

    override fun layoutToInflate(): Int {
        return R.layout.activity_main
    }

    override fun getContainerViewId(): Int {
        return R.id.frmContainer
    }

    override fun getDefaultFragment(): Class<out Fragment>? {
        return MainFragment::class.java
    }

}