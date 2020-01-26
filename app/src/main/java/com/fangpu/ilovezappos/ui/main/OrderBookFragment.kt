package com.fangpu.ilovezappos.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.fangpu.ilovezappos.OrderBookAdapter
import com.fangpu.ilovezappos.databinding.OrderBookFragmentBinding
import com.fangpu.ilovezappos.databinding.PriceHistoryFragmentBinding
import com.github.mikephil.charting.charts.LineChart

class OrderBookFragment : Fragment() {
    private lateinit var chart: LineChart

    companion object {
        fun newInstance() = OrderBookFragment()
    }

    private val viewModel: OrderBookViewModel by lazy {
        ViewModelProviders.of(this).get(OrderBookViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = OrderBookFragmentBinding.inflate(inflater)
        val adapter = OrderBookAdapter()

        binding.bidDataRecycler.adapter = adapter
        binding.setLifecycleOwner(this)
        binding.orderData = viewModel

        binding.swipeContainer.setOnRefreshListener {
            viewModel.getInternetData()
            binding.swipeContainer.isRefreshing = false
        }
        return binding.root
    }


}