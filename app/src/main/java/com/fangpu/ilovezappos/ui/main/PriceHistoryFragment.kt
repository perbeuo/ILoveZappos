package com.fangpu.ilovezappos.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fangpu.ilovezappos.databinding.PriceHistoryFragmentBinding

class PriceHistoryFragment : Fragment() {

    private val viewModel: PriceHistoryViewModel by lazy {
        ViewModelProviders.of(this).get(PriceHistoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = PriceHistoryFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

}
