package com.example.fakeshopping.ui.presentation.homscreen

import android.util.Log
import androidx.activity.compose.BackHandler
import com.example.fakeshopping.ui.presentation.components.IconButton
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fakeshopping.ui.model.homescreenViewmodels.SearchResultFragmentViewModel
import com.example.fakeshopping.ui.presentation.components.NormalHorizontalProductCard

@Composable
fun HomeScreenSearchResultsFragment(currentUserId:String, onProductClick:(Int)->Unit, searchTxt: String, onSearchClear:()->Unit, onBackBtnClick:()->Unit, onSearchTextClick:(String)->Unit, onBackPress:()->Unit){

    val searchResultViewModel: SearchResultFragmentViewModel = hiltViewModel()

    LaunchedEffect(key1 = true) {
        Log.d("FRAGMENT", "I'm: Results")
        searchResultViewModel.setCurrentUserAndFavs(currentUserId)
        searchResultViewModel.changeSearchText( TextFieldValue(searchTxt) )
    }

    BackHandler() {
        onBackPress()
    }

    Box(modifier = Modifier.fillMaxSize().statusBarsPadding(), contentAlignment= Alignment.TopCenter) {


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
                        },
                        isFavourite = searchResultViewModel.userFavs.contains(product.id),
                        onFavouriteButtonClick = {
                            searchResultViewModel.toggleUserFav(it)
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
                    .clickable{
                        onBackBtnClick()
                    }
                    .padding(6.dp),
                tint = Color.DarkGray
            )

            Spacer(Modifier.width(8.dp))

            Box(Modifier
                .clickable (
                    interactionSource= MutableInteractionSource(),
                    indication = null
                ){
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