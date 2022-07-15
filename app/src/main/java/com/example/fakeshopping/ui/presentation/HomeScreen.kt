package com.example.fakeshopping.ui.presentation

import android.util.Log
import android.view.Window
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.ui.HomeScreenViewmodel
import com.example.fakeshopping.ui.theme.ColorWhiteVariant
import com.example.fakeshopping.utils.Routes
import com.example.fakeshopping.utils.ToolbarProperties
import com.example.fakeshopping.utils.ToolbarProperties.CollapsedToolbarColor
import com.example.fakeshopping.utils.ToolbarProperties.inDp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen( navController: NavController, category:String = "All",window: Window){

    val viewmodel:HomeScreenViewmodel = hiltViewModel()
    val toolBaroffsetY:MutableState<Float> = remember{ mutableStateOf(0f) }
    val homefeedScrollOffset = rememberLazyGridState()
    val toolbarColor = remember { mutableStateOf(ToolbarProperties.ExpandedToolbarColor) }

    val searchBarColor = remember {
        mutableStateOf(ColorWhiteVariant)
    }

    fun setHeaderColor(isCollapsed:Boolean){
        if(isCollapsed){
            toolbarColor.value = ToolbarProperties.CollapsedToolbarColor
            searchBarColor.value = Color.White
            window.statusBarColor = Color.Blue.toArgb()
        }else{
            toolbarColor.value = ToolbarProperties.ExpandedToolbarColor
            searchBarColor.value = ColorWhiteVariant
            window.statusBarColor = Color.White.toArgb()
        }
    }

    val animateSearchBarColor = animateColorAsState(targetValue = searchBarColor.value)

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

                        if(homefeedScrollOffset.firstVisibleItemIndex == 0 && homefeedScrollOffset.firstVisibleItemScrollOffset <= ToolbarProperties.STATE_COLLAPSING_CONTENT_HEIGHT){
                            toolBaroffsetY.value = newScrollOffset.coerceIn(-ToolbarProperties.TOOLBAR_COLLAPSED_HEIGHT , 0f)
                            setHeaderColor(false)
                            Log.d("TOPBAR","TOOLBAR OFFSET: ${toolBaroffsetY.value}")
                        }else{
                            toolBaroffsetY.value = newScrollOffset.coerceIn(-ToolbarProperties.STATE_COLLAPSING_CONTENT_HEIGHT , -ToolbarProperties.STATE_COLLAPSING_CONTENT_HEIGHT)
                            setHeaderColor(true)
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
            offsetReq = toolBaroffsetY,
            toolbarColor = toolbarColor,
            searchbarColor = animateSearchBarColor

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
        columns = GridCells.Fixed(2),
        state = listState,
    ){

        item(span = { GridItemSpan(2) }){
            Spacer(Modifier.height(ToolbarProperties.TOOLBAR_EXPANDED_HEIGHT.inDp() - ToolbarProperties.TOOLBAR_COLLAPSED_HEIGHT.inDp()))
        }

        item(span = { GridItemSpan(2) }){
            Spacer(Modifier.height(ToolbarProperties.TOOLBAR_COLLAPSED_HEIGHT.inDp()))
        }

        item( span= { GridItemSpan(2) } ){

            BannerSection(
                Modifier.padding(bottom=22.dp,top=36.dp),
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
    offsetReq: MutableState<Float>,
    toolbarColor: MutableState<Brush>,
    searchbarColor: State<Color>
) {


    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(toolbarColor.value)
    ) {

        Column() {

            //HeaderTitle
            Text(
                text = "Hello Pratyaksh",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, top = 16.dp),
                fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black,
                textAlign = TextAlign.Center
            )
            //HeaderSearchBar
            TextField(
                value = searchText.value,
                onValueChange = {
                    searchText.value = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = searchbarColor.value,
                    textColor = Color.Black,
                    cursorColor = Color.Blue,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(x = 0.dp, y = -offsetReq.value.inDp()),
                shape = RoundedCornerShape(10.dp),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                ),
                placeholder = { Text("Search Products ...") }

            )


        }

        Spacer(Modifier.height(12.dp))

        //category selection section
        Box(modifier = Modifier.fillMaxWidth()) {

            LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
                items(categories) { item ->

                    HeaderSectionCategoryListItem(
                        isSelected = (item == selectedCategory.value),
                        categoryName = item,
                        onCategoryClick = onCategoryChange,
                        chipColor = searchbarColor
                    )

                }
            }
        }

        Spacer(Modifier.height(8.dp))
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
                .fillMaxWidth(0.45f)
                .padding(horizontal = 10.dp, vertical = 10.dp),
            product = product,
            onNavigate = onNavigate,
            withEleveation = true
        )

    }
}
