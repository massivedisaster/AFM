package com.massivedisaster.afm

import com.massivedisaster.afm.activity.DataBindingBaseActivity
import com.massivedisaster.afm.databinding.ActivitySecondaryBinding

class BindingActivity : DataBindingBaseActivity<ActivitySecondaryBinding>() {

    override fun layoutToInflate(): Int {
        return R.layout.activity_secondary
    }

    override fun getContainerViewId(): Int {
        return R.id.frmContainer
    }

}