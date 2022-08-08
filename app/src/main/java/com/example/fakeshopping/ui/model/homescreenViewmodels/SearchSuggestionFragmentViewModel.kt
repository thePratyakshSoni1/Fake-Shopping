package com.example.fakeshopping.ui.model.homescreenViewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.fakeshopping.data.repository.TestDataRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SearchSuggestionFragmentViewModel @Inject constructor(val repository:TestDataRepo ): ViewModel() {

    var _searchString:MutableState<TextFieldValue> = mutableStateOf( TextFieldValue("") )
    private var _suggestions:SnapshotStateList<String> = mutableStateListOf()


    val suggestions get() = _suggestions
    val searchString get() = _searchString

    fun changeSearchText(searchTxt: TextFieldValue) {
        _searchString.value = searchTxt
    }

    suspend fun querySuggestions() {

        withContext(Dispatchers.IO){
            if (searchString.value.text.isNotEmpty()) {
                _suggestions.clear()
                delay(1000L)
                Log.d("SUGGESTION_VIEWMODEL", "I was running ${searchString.value.text}")
                val responseProducts = async {
                    repository.getallProducts().filter {
                        it.title.contains(searchString.value.text, true)
                    }
                }

                val temp = mutableListOf<String>()
                responseProducts.await().map{
                    if(it.title.contains(searchString.value.text)){
                        temp.add(it.title)
                    }
                }

                _suggestions.addAll(temp)

            }
        }
    }

}

//  Feature extend later
//    fun onFilterProducts()
//    fun onSortProducts()

