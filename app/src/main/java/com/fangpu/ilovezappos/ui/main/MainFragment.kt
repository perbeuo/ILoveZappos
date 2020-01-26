package com.fangpu.ilovezappos.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.fangpu.ilovezappos.R
import com.fangpu.ilovezappos.databinding.MainFragmentBinding
import com.github.mikephil.charting.charts.LineChart

class MainFragment : Fragment() {
    private lateinit var chart: LineChart

    companion object {
        fun newInstance() = PriceHistoryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<MainFragmentBinding>(
            inflater, R.layout.main_fragment, container, false)
        binding.setLifecycleOwner(this)
        binding.toPriceButton.setOnClickListener (
            Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_priceHistoryFragment)
        )
        binding.toOrderButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_mainFragment_to_orderBookFragment)
        )
        return binding.root
    }

}
