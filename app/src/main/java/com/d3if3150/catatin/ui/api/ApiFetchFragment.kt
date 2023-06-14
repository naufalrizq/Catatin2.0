package com.d3if3150.catatin.ui.api


import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.d3if3150.catatin.R
import com.d3if3150.catatin.adapter.RealEstateAdapter
import com.d3if3150.catatin.databinding.FragmentApiFetchBinding
import com.d3if3150.catatin.utils.network.ConnectivityLiveData
import com.d3if3150.catatin.utils.network.NetworkReceiver
import com.d3if3150.catatin.ui.api.RealEstateViewModel
import com.d3if3150.catatin.utils.network.NetworkReceiver.Companion.mobileConnected
import com.d3if3150.catatin.utils.network.NetworkReceiver.Companion.refreshDisplay
import com.d3if3150.catatin.utils.network.NetworkReceiver.Companion.wifiConnected
import com.d3if3150.catatin.utils.network.Resource
import com.d3if3150.catatin.utils.network.getApiFetchList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ApiFetchFragment : Fragment(R.layout.fragment_api_fetch) {
    private lateinit var realEstateAdapter: RealEstateAdapter
    private val viewModel: RealEstateViewModel by activityViewModels()
    private var realEstateList = getApiFetchList()
    private lateinit var binding: FragmentApiFetchBinding
    private var receiverIsRegistered: Boolean = false

    //The broadcast receiver that tracks network connectivity changes
    private lateinit var receiver: NetworkReceiver
    private lateinit var connectivityLiveData: ConnectivityLiveData


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentApiFetchBinding.bind(view)
        receiver = NetworkReceiver()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        activity?.registerReceiver(receiver, filter)
        receiverIsRegistered = true

        realEstateAdapter = RealEstateAdapter()

        viewModel.loadPaginatedRealEstateList()
        viewModel.realEstate.observe(viewLifecycleOwner)  { response ->
            when (response) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    response.data?.let { data ->
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.apiFetchRecyclerView.adapter = realEstateAdapter
                        val realEstateList = data.toMutableList()
                        realEstateAdapter.differ.submitList(realEstateList)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(
                        requireContext(),
                        "An error occurred: $response",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }

        // setup search functionality
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchRealEstate(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.searchRealEstate(it) }
                return false
            }
        })

        //setup recyclerView swipe to refresh
        binding.swipeRefresh.setOnRefreshListener {
            updateConnectedFlags()
            if (refreshDisplay) {
                viewModel.loadPaginatedRealEstateList()
                loadPage()
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    //unregister network receiver to prevent memory leak
    override fun onDestroy() {
        super.onDestroy()
        //unregisters broadcast receiver when app is destroyed
        if (receiverIsRegistered) {
            activity?.unregisterReceiver(receiver)
            receiverIsRegistered = false
        }
    }

    private fun loadPage() {
        if (wifiConnected || mobileConnected) {
            binding.apiFetchRecyclerView.visibility = View.VISIBLE
            binding.statusButton.visibility = View.INVISIBLE
            viewModel.realEstate.observe(viewLifecycleOwner, Observer {

            }
            )
        }
    }

    private fun updateConnectedFlags() {
        val connMgr = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeInfo: NetworkInfo? = connMgr.activeNetworkInfo
        if (activeInfo?.isConnected == true) {
            binding.apiFetchRecyclerView.visibility = View.VISIBLE
            binding.statusButton.visibility = View.INVISIBLE
            wifiConnected = activeInfo.type == ConnectivityManager.TYPE_WIFI
            mobileConnected = activeInfo.type == ConnectivityManager.TYPE_MOBILE
        } else {
            wifiConnected = false
            mobileConnected = false
            binding.apiFetchRecyclerView.visibility = View.INVISIBLE
            binding.statusButton.visibility = View.VISIBLE
            hideProgressBar()
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

}