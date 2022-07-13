package com.example.fakeshopping.ui.presentation

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.example.fakeshopping.utils.ToolbarSizes
import com.example.fakeshopping.utils.ToolbarSizes.inDp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController, category:String = "All"){

    val viewmodel:HomeScreenViewmodel = hiltViewModel()
    val toolBaroffsetY:MutableState<Float> = remember{ mutableStateOf(0f) }
    val homefeedScrollOffset = rememberLazyGridState()


    Log.d("CLICKED","HOME SCREEN RECOMPOSED")
    LaunchedEffect(key1 = true ) {
        viewmodel.changeCategory(category)
        viewmodel.refreshCategories()
        Log.d("API", "Api requests were made does that succeed ?")
    }
    Box(
        modifier= Modifier.fillMaxSize()
    ){

        Column(
            modifier= Modifier.nestedScroll(

                object :NestedScrollConnection {

                    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                        Log.d("TOPBAR","${available.y}")
                        val delta = available.y
                        val newScrollOffset = toolBaroffsetY.value + delta

                        if(homefeedScrollOffset.firstVisibleItemIndex == 0 && homefeedScrollOffset.firstVisibleItemScrollOffset <= ToolbarSizes.STATE_COLLAPSING_CONTENT_HEIGHT){
                            toolBaroffsetY.value = newScrollOffset.coerceIn(-ToolbarSizes.TOOLBAR_COLLAPSED_HEIGHT , 0f)
                            Log.d("TOPBAR","TOOLBAR OFFSET: ${toolBaroffsetY.value}")
                        }else{
                            toolBaroffsetY.value = newScrollOffset.coerceIn(-ToolbarSizes.TOOLBAR_COLLAPSED_HEIGHT , -ToolbarSizes.STATE_COLLAPSING_CONTENT_HEIGHT)
                            Log.d("TOPBAR","TOOLBAR OFFSET: ${toolBaroffsetY.value}")
                        }

                        return Offset.Zero
                    }

                }
            )
        ){
            ContentSection(
                navController,
                viewmodel.bannerResources,
                viewmodel.userInteractedWithBanners,
                homefeedScrollOffset,
                viewmodel.products
            )
        }

        HeaderSection(

            modifier = Modifier
                .fillMaxWidth()
                .offset {
                    IntOffset(x = 0, y = toolBaroffsetY.value.toInt())
                },
            viewmodel.searchBoxText, //73 dp
            categories = viewmodel.categories,
            selectedCategory = viewmodel.selectedCategory,
            onCategoryChange = {
                Log.d("CLICKED","Category Header Section")
                viewmodel.changeCategory(it)
            },
            offsetReq = toolBaroffsetY

        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentSection(
    navController:NavController,
    bannerResource: SnapshotStateMap<String, Int>,
    userInteracted: MutableState<Boolean>,
    listState: LazyGridState,
    products: SnapshotStateList<ShopApiProductsResponse>
){

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        state = listState,
    ){

        item(span = { GridItemSpan(3) }){
            Spacer(Modifier.height(ToolbarSizes.TOOLBAR_EXPANDED_HEIGHT.inDp() - ToolbarSizes.TOOLBAR_COLLAPSED_HEIGHT.inDp()))
        }

        item(span = { GridItemSpan(3) }){
            Spacer(Modifier.height(ToolbarSizes.TOOLBAR_COLLAPSED_HEIGHT.inDp()))
        }

        item( span= { GridItemSpan(3) } ){

            BannerSection(
                Modifier.padding(vertical=22.dp),
                bannerResource,
                userInteracted

            )
        }

        allProductsSection(
            products
        ) { product ->
            navController.navigate("${Routes.productDetailScreen}/${product.id}")
        }

    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeaderSection(
    modifier: Modifier = Modifier.fillMaxWidth(),
    searchText: MutableState<String>,
    categories: List<String>,
    selectedCategory: MutableState<String>,
    onCategoryChange: (String) -> Unit,
    offsetReq: MutableState<Float>
) {


    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Blue)
    ) {

        Column() {

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
                    .offset(x = 0.dp, y = -offsetReq.value.inDp()),
                shape = RoundedCornerShape(8.dp),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                ),
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
