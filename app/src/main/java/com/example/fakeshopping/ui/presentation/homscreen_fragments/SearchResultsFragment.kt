package com.example.fakeshopping.ui.presentation.homscreen_fragments

import android.util.Log
import android.widget.Toolbar
import androidx.activity.compose.BackHandler
import com.example.fakeshopping.ui.presentation.components.IconButton
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.ui.HomeScreenViewmodel
import com.example.fakeshopping.ui.SearchResultFragmentViewModel
import com.example.fakeshopping.ui.presentation.components.NormalHorizontalProductCard
import com.example.fakeshopping.utils.Routes

@Composable
fun HomeScreenSearchResultsFragment(onProductClick:(Int)->Unit, searchTxt: String, onSearchClear:()->Unit, onBackBtnClick:()->Unit, onSearchTextClick:(String)->Unit, onBackPress:()->Unit){

    val searchResultViewModel:SearchResultFragmentViewModel = hiltViewModel()
    searchResultViewModel.changeSearchText( TextFieldValue(searchTxt) )

    BackHandler() {
        onBackPress()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment= Alignment.TopCenter) {

        LaunchedEffect(key1 = true) {
            Log.d("FRAGMENT", "I'm: Results")
        }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(top= 80.dp, bottom= 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                items(searchResultViewModel.resultProducts) { product ->
                    NormalHorizontalProductCard(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .height(126.dp),
                        product = product,
                        onNavigate = {
                            onProductClick(product.id)
                        }
                    )
                }

            }

        TopAppBar(
            elevation = 2.dp,
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 4.dp),
            backgroundColor = Color.White
        ) {

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Go Back",
                modifier = Modifier
                    .padding(start = 12.dp, top = 8.dp, bottom = 2.dp)
                    .clip(CircleShape)
                    .clickable {
                        onBackBtnClick()
                    }
                    .padding(6.dp),
                tint = Color.DarkGray
            )

            Spacer(Modifier.width(8.dp))

            Box(Modifier
                .clickable {
                    onSearchTextClick(searchTxt)
                }
                .fillMaxHeight()
                .weight(1f)
                .padding(start = 8.dp),
                contentAlignment = Alignment.CenterStart
            ){

                Text(
                    text=searchResultViewModel.searchString,
                    color = Color.DarkGray,
                    maxLines = 1
                )
            }

            IconButton(
                icon = Icons.Default.Clear,
                onClick = {
                    onSearchClear()
                },
                contentDescription = "clear searchbar"
            )
        }

    }
}