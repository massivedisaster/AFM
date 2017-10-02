package com.massivedisaster.afm.sample

import com.massivedisaster.afm.FragmentCall
import com.massivedisaster.afm.sample.databinding.FragmentFirstBinding

class FirstFragment : BaseFragment<FragmentFirstBinding>() {

    override fun layoutToInflate(): Int {
        return R.layout.fragment_first
    }

    override fun defineViews() {
        dataBinding.btnActionAddAnotherFragment.setOnClickListener {
            FragmentCall.init(baseActivity, SecondFragment::class)
                    .addToBackStack(false)
                    .setTransitionType(FragmentCall.TransitionType.ADD)
                    .build()
        }

        dataBinding.btnActionReplaceFragment.setOnClickListener {
            FragmentCall.init(baseActivity, SecondFragment::class)
                    .addToBackStack(false)
                    .setTransitionType(FragmentCall.TransitionType.REPLACE)
                    .build()
        }
    }

}