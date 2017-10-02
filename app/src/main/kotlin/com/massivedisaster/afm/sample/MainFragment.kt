package com.massivedisaster.afm.sample

import com.massivedisaster.afm.ActivityCall
import com.massivedisaster.afm.sample.databinding.FragmentMainBinding

class MainFragment : BaseFragment<FragmentMainBinding>() {

    override fun layoutToInflate(): Int {
        return R.layout.fragment_main
    }

    override fun defineViews() {
        dataBinding.btnActionOpenRegular.setOnClickListener {
            ActivityCall.init(BaseFragment@ this, RegularActivity::class, FirstFragment::class)
                    .build()
        }

        dataBinding.btnActionOpenBinding.setOnClickListener {
            ActivityCall.init(BaseFragment@ this, BindingActivity::class, FirstFragment::class)
                    .build()
        }
    }

}