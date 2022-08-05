package com.example.fakeshopping.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.data.repository.TestDataRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultFragmentViewModel @Inject constructor( val repository:TestDataRepo ): ViewModel() {

    private val _searchString:MutableState<TextFieldValue> = mutableStateOf( TextFieldValue("") )
    private val _resultProducts:SnapshotStateList<ShopApiProductsResponse> = mutableStateListOf()


    val resultProducts get() = _resultProducts
    val searchString get() = _searchString.value.text

    fun changeSearchText(searchTxt: TextFieldValue){
        _searchString.value = searchTxt

        viewModelScope.launch{
            _resultProducts.clear()
            _resultProducts.addAll(repository.getallProducts().filter {
                it.title.contains(_searchString.value.text, true)
            })
        }
    }

//  Feature extend later
//    fun onFilterProducts()
//    fun onSortProducts()

}
