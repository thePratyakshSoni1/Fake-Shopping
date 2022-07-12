package com.example.fakeshopping.ui.presentation

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.ui.HomeScreenViewmodel
import com.example.fakeshopping.utils.Routes
import com.example.fakeshopping.utils.ToolbarSizes.inDp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

//    val toolbarHeightPx = ToolbarSizes.STATE_EXPANDED_CONTENT_HEIGHT
    //  our offset to collapse toolbar
    val toolbarCollapsingContentHeight =

        remember { mutableStateOf(0f) }
    // now, let's create connection to the nested scroll system and listen to the scroll
    // happening inside child LazyColumn
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // try to consume before LazyColumn to collapse toolbar if needed, hence pre-scroll
                val delta = available.y
                val newOffset = toolbarCollapsingContentHeight.value + delta
                Log.d("TOPBAR","newOffset: ${toolbarCollapsingContentHeight.value} + $delta")
//                toolbarCollapsingContentHeight.value = newOffset.coerceIn(0f, ToolbarSizes.STATE_COLLAPSED_CONSTENT_HEIGHT)
                // here's the catch: let's pretend we consumed 0 in any case, since we want
                // LazyColumn to scroll anyway for good UX
                // We're basically watching scroll without taking it
//                Log.d("TOPBAR","toolbarCollapsingContentHeight: ${toolbarCollapsingContentHeight.value}, toolbarHeightPx: $toolbarHeightPx")
                return Offset.Zero
            }
        }
    }

    val viewmodel: HomeScreenViewmodel = hiltViewModel()


    Scaffold( modifier=Modifier.nestedScroll(
        nestedScrollConnection
    ),
    ) {

        LazyVerticalGrid(columns = GridCells.Fixed(3)){

            item (span= { GridItemSpan(3)}){
                HeaderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(x = 0f.inDp(), y = toolbarCollapsingContentHeight.value.inDp()),
                    offsetReq = toolbarCollapsingContentHeight,
                    searchText=viewmodel.searchBoxText, //73 dp
//                    height = toolbarCollapsingContentHeight.value.inDp(),
                    categories = viewmodel.categories,
                    selectedCategory = viewmodel.selectedProductCategory,
                    onCategoryChange = {
                        viewmodel.onCategoryChange(it)
                    }
                )
            }

            item( span= { GridItemSpan(3) } ){

                BannerSection(
                    modifier = Modifier.padding(vertical = 22.dp),
                    viewmodel.bannerResources,
                    viewmodel.userInteractedWithBanners

                )
            }

            allProductsSection(
                viewmodel.products
            ) { product ->
                navController.navigate("${Routes.productDetailScreen}/${product.id}")
            }

        }

    }


}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeaderSection(
    modifier: Modifier = Modifier.fillMaxWidth(),
    searchText: MutableState<String>,
//    height: Dp,
    categories: List<String>,
    selectedCategory: MutableState<String>,
    onCategoryChange: (String) -> Unit,
    offsetReq: MutableState<Float>
) {


    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Blue)
//            .height(ToolbarSizes.STATE_EXPANDED_CONTENT_HEIGHT.inDp())
    ) {

        Column(
//            Modifier.height(height)
        ) {


            //HeaderTitle
            Text(
                text = "FakeShop",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, top = 16.dp, start = 16.dp),
                fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Color.White
            )
            //HeaderSearchBar
            TextField(
                value = searchText.value,
                onValueChange = {
                    searchText.value = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    textColor = Color.Black,
                    cursorColor = Color.Blue,
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(x=0.dp,y = -offsetReq.value.inDp()),
                shape = RoundedCornerShape(8.dp),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                ),
//                placeholder = { Text(((-height.value).inDp()).toString()) }
                placeholder = { Text("Serach Products ...") }

            )


        }
            Spacer(Modifier.height(12.dp))


        //category selection section
        Box(modifier = Modifier.fillMaxWidth()) {

            LazyRow(modifier = Modifier.padding(horizontal = 8.dp)) {
                items(categories) { item ->

                    HeaderSectionCategoryListItem(
                        isSelected = (item == selectedCategory.value),
                        categoryName = item,
                        onCategoryClick = onCategoryChange
                    )

                }
            }
        }
    }

}

@Composable
fun BannerSection(
    modifier: Modifier = Modifier.fillMaxWidth(),
    bannerResource: SnapshotStateMap<String, Int>,
    userInteracted: MutableState<Boolean>
) {

    Box(modifier = modifier) {
        BannerSlides(
            modifier = Modifier.fillMaxWidth(),
            bannersResource = bannerResource,
            userInteracted
        )
    }


}

fun LazyGridScope.allProductsSection(
    products: List<ShopApiProductsResponse>,
    onNavigate: (product: ShopApiProductsResponse) -> Unit
) {
    items(products) { product ->

        ProductsCard(
            modifier = Modifier
                .width(64.dp)
                .padding(4.dp),
            product = product,
            onNavigate = onNavigate
        )

    }
}

