package com.massivedisaster.afm.sample

import com.massivedisaster.afm.activity.BaseActivity

class RegularActivity : BaseActivity() {

    override fun layoutToInflate(): Int {
        return R.layout.activity_main
    }

    override fun getContainerViewId(): Int {
        return R.id.frmContainer
    }

}