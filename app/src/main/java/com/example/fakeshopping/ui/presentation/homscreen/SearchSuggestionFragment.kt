package com.example.fakeshopping.ui.presentation.homscreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fakeshopping.ui.model.homescreenViewmodels.SearchSuggestionFragmentViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreenSearchSuggestionFragment(
    previousQueryString:String?,
    searchProduct:(String)->Unit,
    goToPreviousFragment:()->Unit
){

    val searchSuggestionViewModel: SearchSuggestionFragmentViewModel = hiltViewModel()
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchBarFocusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = true ){
        Log.d("FRAGMENT","IM here: Suggestion, prev $previousQueryString")
        if( previousQueryString != null ){
            Log.d("FRAGMENT","IM here: Suggestion, prev not null")

            searchSuggestionViewModel.changeSearchText(
                TextFieldValue(
                text= previousQueryString,
                selection= TextRange(previousQueryString.length),
                )
            )

        }
    }

    LaunchedEffect(key1 = searchSuggestionViewModel.searchString.value, block = {
        Log.d("SUGGESTION","i'm Suggestion Searchtext chnage")
        searchSuggestionViewModel.querySuggestions()
    })

    Column(modifier=Modifier.statusBarsPadding()){
        HomeScreenSearchBar(
            searchbarTxt= searchSuggestionViewModel.searchString.value,
            focusRequester = searchBarFocusRequester,
            onBackPress = goToPreviousFragment,
            searchProducts = {
                searchProduct(it)
            },
            onSearchTextValueChange = {
                searchSuggestionViewModel.changeSearchText(it)
            }
        )

        LazyColumn(
            Modifier
                .fillMaxWidth()
                .weight(1f)) {
            if (searchSuggestionViewModel.searchString.value.text.isNotEmpty()){
                items(
                    searchSuggestionViewModel.suggestions
                ) { item ->

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 16.dp)
                            .clickable{
                                searchSuggestionViewModel.changeSearchText(TextFieldValue(item))
                                keyboardController?.hide()
                                searchProduct(searchSuggestionViewModel.searchString.value.text)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search Suggestion",
                            modifier = Modifier
                                .padding(end = 12.dp, top = 8.dp)
                                .padding(6.dp),
                            tint = Color.LightGray
                        )

                        Text(
                            text = item,
                            Modifier
                                .padding(start = 10.dp)
                                .weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color= Color.Black
                        )

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Search Suggestion",
                            modifier = Modifier
                                .padding(end = 12.dp, top = 8.dp)
                                .clip(CircleShape)
                                .padding(6.dp)
                                .clickable {
                                    searchSuggestionViewModel.changeSearchText(
                                        TextFieldValue(
                                            item,
                                            selection = TextRange(item.length)
                                        )
                                    )
                                },
                            tint = Color.LightGray
                        )


                    }

                }

            }
        }
    }

    LaunchedEffect(key1 = true ){
        searchBarFocusRequester.requestFocus()
    }


}