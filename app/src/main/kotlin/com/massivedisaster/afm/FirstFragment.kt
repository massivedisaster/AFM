package com.massivedisaster.afm

import com.massivedisaster.afm.databinding.FragmentFirstBinding

class FirstFragment : BaseFragment<FragmentFirstBinding>() {

    override fun layoutToInflate(): Int {
        return R.layout.fragment_first
    }

    override fun defineViews() {
        dataBinding.btnActionAddAnotherFragment.setOnClickListener {
            FragmentCall
                    .init(baseActivity, SecondFragment::class)
                    .addToBackStack(false)
                    .setTransitionType(FragmentCall.TransitionType.ADD)
                    .build()
        }

        dataBinding.btnActionReplaceFragment.setOnClickListener {
            FragmentCall
                    .init(baseActivity, SecondFragment::class)
                    .addToBackStack(false)
                    .setTransitionType(FragmentCall.TransitionType.REPLACE)
                    .build()
        }
    }

}