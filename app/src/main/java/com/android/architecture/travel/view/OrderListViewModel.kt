package com.android.architecture.travel.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.architecture.core.BaseViewModel
import kotlinx.coroutines.launch

class OrderListViewModel : BaseViewModel() {

    private val _orderList: MutableLiveData<List<String>> = MutableLiveData()
    val orderList: LiveData<List<String>> = _orderList

    init {
        getOrderList()
    }

    private fun getOrderList() {
        viewModelScope.launch {
            _orderList.value = listOf(
                "#20220605001 西安-北京\n东方航空MU5715",
                "#20220605002 西安-深圳\n南方航空CZ3280",
                "#20220605003 西安-成都\n四川航空3U8356"
            )
        }
    }
}
