package com.d3if3150.catatin.ui.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d3if3150.catatin.model.RealEstate
import com.d3if3150.catatin.repo.RealEstateRepository
import com.d3if3150.catatin.utils.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RealEstateViewModel @Inject constructor(private val repository: RealEstateRepository): ViewModel(){

    private var _realEstate = MutableLiveData<Resource<List<RealEstate>>>()
    val realEstate: LiveData<Resource<List<RealEstate>>> get() = _realEstate

    private var cachedRealEstate = MutableLiveData<Resource<List<RealEstate>>>()
    private var isSearchStaring = true
    var isSearching = MutableStateFlow(false)

    init {
        loadPaginatedRealEstateList()
    }
    fun loadPaginatedRealEstateList() = viewModelScope.launch {
       try {
           _realEstate.postValue(Resource.Loading)
           val array = repository.getRealEstateItems()
           _realEstate.postValue(handleRealEstateData(array))

        } catch (e: Exception){

        }
    }

    //function to search real estate items using type
    fun searchRealEstate(query: String) {
        if (isSearchStaring) {
            cachedRealEstate.value = _realEstate.value
            isSearchStaring = false
        }

        val itemToSearch = if (isSearchStaring) {
            realEstate.value
        } else {
            cachedRealEstate.value
        }
        viewModelScope.launch {
            if (query.isEmpty()) {
                _realEstate.value = cachedRealEstate.value
                isSearching.value = false
                isSearchStaring = true
                return@launch
            } else {
                val results = itemToSearch?.data?.filter {
                    it.type.contains(query.trim(), ignoreCase = true)
                }
                results?.let {
                    _realEstate.value = Resource.Success(results)
                }
            }
            if (isSearchStaring){
                cachedRealEstate.value = _realEstate.value
                isSearchStaring = false
            }
            isSearching.value = true
        }
    }

    private fun handleRealEstateData(
        realEstateData: Response<List<RealEstate>>
    ): Resource<List<RealEstate>> {
        if (realEstateData.isSuccessful){
            realEstateData.body()?.let { data ->
                return Resource.Success(data)
            }
        }
        return Resource.Error(realEstateData.message(), null)
    }
}