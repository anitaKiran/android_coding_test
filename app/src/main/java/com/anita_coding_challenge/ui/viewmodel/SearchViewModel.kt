package com.anita_coding_challenge

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Anita Kiran on 6/1/2022.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: MainRepository,
    private val networkHelper: NetworkHelper
    ) : ViewModel() {

    private var _items = MutableLiveData<Resource<SearchItemModel>?>()
    val items: LiveData<Resource<SearchItemModel>?> get() = _items

    fun getSearchItems(searchStr: String,perPage:Int, pageNo:Int) {
        if (networkHelper.isNetworkConnected()) {
            viewModelScope.launch(Dispatchers.IO) {
                repo.getItems(searchStr,perPage,pageNo).let { response ->
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            _items.postValue(Resource.success(response.body()))
                        }
                        else
                            _items.postValue(Resource.error(response.errorBody().toString(), null))
                    }
                }
            }
        } else
            _items.postValue(Resource.error("No internet connection", null))
    }
}