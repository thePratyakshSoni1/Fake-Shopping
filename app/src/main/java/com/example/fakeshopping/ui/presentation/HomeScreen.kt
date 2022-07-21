package com.example.fakeshopping.ui.presentation

import android.util.Log
import android.view.Window
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.FavoriteBorder
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakeshopping.R
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.ui.HomeScreenViewmodel
import com.example.fakeshopping.ui.theme.ColorWhiteVariant
import com.example.fakeshopping.ui.presentation.components.AccountDialog
import com.example.fakeshopping.ui.presentation.components.MenuItemData
import com.example.fakeshopping.ui.presentation.components.ProductsCard
import com.example.fakeshopping.ui.presentation.components.menuItems
import com.example.fakeshopping.utils.Routes
import com.example.fakeshopping.utils.ToolbarProperties
import com.example.fakeshopping.utils.ToolbarProperties.CollapsedToolbarColor
import com.example.fakeshopping.utils.ToolbarProperties.inDp
import kotlin.math.roundToInt


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen( navController: NavController, category:String = "All",window: Window ){

    val viewmodel:HomeScreenViewmodel = hiltViewModel()
    val toolBaroffsetY:MutableState<Float> = remember{ mutableStateOf(0f) }
    val homefeedScrollOffset = rememberLazyGridState()
    val toolbarColor = remember { mutableStateOf(ToolbarProperties.ExpandedToolbarColor) }
    val showAccountDialog = remember{ mutableStateOf(false) }

    val accountMenuItems = menuItems + MenuItemData(
        "Your Cart",
        Icons.Outlined.ShoppingCart,
    ) {
        navController.navigate(Routes.shoppingCartScreen)
    } + MenuItemData(
        "Favourites",
        Icons.Rounded.FavoriteBorder
    ) {
        navController.navigate(Routes.favouritesScreen)
    }


    val context = LocalContext.current

    val toolbarMotionScene = remember{
        context.resources.openRawResource(R.raw.topappbar_motion_scene).readBytes().decodeToString()
    }

    val searchBarColor = remember {
        mutableStateOf(ColorWhiteVariant)
    }

    fun setHeaderColor(isCollapsed:Boolean){
        if(isCollapsed){
            toolbarColor.value = CollapsedToolbarColor
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
        viewmodel.refreshCategories()
        viewmodel.changeCategory(category)
        Log.d("API", "Api requests were made does that succeed ?")
    }
    Box(
        modifier= Modifier.fillMaxSize()
    ){

        Column(
            modifier= Modifier
                .fillMaxSize()
                .nestedScroll(object : NestedScrollConnection {
                    override fun onPostScroll(
                        consumed: Offset,
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        if (homefeedScrollOffset.firstVisibleItemIndex == 0) {
                            toolBaroffsetY.value =
                                (homefeedScrollOffset.firstVisibleItemScrollOffset / 100f).coerceIn(
                                    0f,
                                    1f
                                )
                            if (toolBaroffsetY.value == 1f) setHeaderColor(true) else setHeaderColor(
                                false
                            )
                            Log.d("FLING SCROLL", "VISIBLE: VISIBLE ")
                        } else {
                            setHeaderColor(true)
                            toolBaroffsetY.value = 1f
                        }
                        return Offset.Zero
                    }
                })
        ){
            ContentSection(
                navController,
                viewmodel.bannerResources,
                viewmodel.userInteractedWithBanners,
                homefeedScrollOffset,
                viewmodel.products
            )
        }

        CollapsingTopAppBar(
            motionScene = toolbarMotionScene,
            progress = toolBaroffsetY,
            toolbarBackground = toolbarColor,
            searchbarColor = animateSearchBarColor,
            searchText = viewmodel.searchBoxText,
            categories = viewmodel.categories,
            selectedCategory = viewmodel.selectedCategory,
            onCategoryChange = {
                toolBaroffsetY.value = 0f
                viewmodel.changeCategory( it )
            },
            showDialog= showAccountDialog,
            onCartIconClick = {
                navController.navigate(Routes.shoppingCartScreen)
            }
        )


        if(showAccountDialog.value){
            AccountDialog(menuItems = accountMenuItems, showAccountDialog)
        }

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
        state = listState
    ){

        item(span = { GridItemSpan(2) }){
            Spacer(Modifier.height(ToolbarProperties.TOOLBAR_EXPANDED_HEIGHT.inDp() + 24.dp))
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

@Composable
fun BannerSection(
    modifier: Modifier = Modifier.fillMaxWidth(),
    bannerResource: SnapshotStateMap<String, Int>,
    userInteracted: MutableState<Boolean>
) {

        BannerSlides(
            modifier = modifier,
            bannersResource = bannerResource,
            userInteracted
        )


}

fun LazyGridScope.allProductsSection(
    products: List<ShopApiProductsResponse>,
    onNavigate: (product: ShopApiProductsResponse) -> Unit
) {
    items(products.size) { index ->

        ProductsCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 6.dp,
                    bottom = 6.dp,
                    start = if (index % 2 == 0) 18.dp else 6.dp,
                    end = if (index % 2 == 0) 6.dp else 18.dp,
                ),
            product = products[index],
            onNavigate = onNavigate,
            withEleveation = true,
            borderColor = if(products[index].rating.rate.roundToInt() == 5) CollapsedToolbarColor else Brush.linearGradient(listOf(Color.DarkGray,Color.DarkGray))
        )

    }
}
