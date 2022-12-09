package com.example.fakeshopping.ui.presentation.homscreen

import android.util.Log
import android.view.Window
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakeshopping.R
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.ui.model.homescreenViewmodels.HomeScreenFragmentViewmodel
import com.example.fakeshopping.ui.presentation.components.AccountDialog
import com.example.fakeshopping.ui.presentation.components.MenuItemData
import com.example.fakeshopping.ui.presentation.components.ProductsCard
import com.example.fakeshopping.ui.theme.ColorWhiteVariant
import com.example.fakeshopping.utils.Routes
import com.example.fakeshopping.utils.ToolbarProperties
import com.example.fakeshopping.utils.ToolbarProperties.inDp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenMainFragment(
    rootNavController: NavController,
    onSearchbarClick:()->Unit,
    category:String,
    window: Window,
    userId:String
){

    val homeScreenviewmodelFragment: HomeScreenFragmentViewmodel = hiltViewModel()
    val showAccountDialog =  remember { mutableStateOf(false) }

    val homefeedScrollOffset = rememberLazyGridState( homeScreenviewmodelFragment.feedFirstVisibleItemIndx, homeScreenviewmodelFragment.feedFirstVisibleItemoffset )

    val context = LocalContext.current

    val toolbarMotionScene = remember{
        context.resources.openRawResource(R.raw.topappbar_motion_scene).readBytes().decodeToString()
    }
    window.statusBarColor = homeScreenviewmodelFragment.statusBarColor.value.toArgb()

    LaunchedEffect(key1 = true ){
        Log.d("FRAGMENT","IM here: Suggestion")
        homeScreenviewmodelFragment.setUserId(userId)
        homeScreenviewmodelFragment.changeCategory(category)

    }

    DisposableEffect(key1 = true, effect = {
        onDispose{
            homeScreenviewmodelFragment.feedFirstVisibleItemIndx = homefeedScrollOffset.firstVisibleItemIndex
            homeScreenviewmodelFragment.feedFirstVisibleItemoffset = homefeedScrollOffset.firstVisibleItemScrollOffset
        }
    })

    fun setHeaderColor(isCollapsed:Boolean){
        if(isCollapsed){
            homeScreenviewmodelFragment.toolbarColor.value = ToolbarProperties.CollapsedToolbarColorBrush
            homeScreenviewmodelFragment.searchBarColor.value = Color.White
            //window.statusBarColor = Color(0xFF350099).toArgb()
            homeScreenviewmodelFragment.statusBarColor.value = Color(0xFF350099)
        }else{
            homeScreenviewmodelFragment.toolbarColor.value = ToolbarProperties.ExpandedToolbarColorBrush
            homeScreenviewmodelFragment.searchBarColor.value = ColorWhiteVariant
            //window.statusBarColor = Color(0xFFE9E9E9).toArgb()
            homeScreenviewmodelFragment.statusBarColor.value = Color.White
        }
    }

    val animateSearchBarColor = animateColorAsState(targetValue = homeScreenviewmodelFragment.searchBarColor.value)

    Log.d("CLICKED","HOME SCREEN RECOMPOSED")

    Box(
        modifier= Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
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
                            homeScreenviewmodelFragment.toolBaroffsetY.value =
                                (homefeedScrollOffset.firstVisibleItemScrollOffset / 100f).coerceIn(
                                    0f,
                                    1f
                                )
                            if (homeScreenviewmodelFragment.toolBaroffsetY.value == 1f) setHeaderColor(
                                true
                            ) else setHeaderColor(
                                false
                            )
                            Log.d("FLING SCROLL", "VISIBLE: VISIBLE ")
                        } else {
                            setHeaderColor(true)
                            homeScreenviewmodelFragment.toolBaroffsetY.value = 1f
                        }

                        homeScreenviewmodelFragment.feedFirstVisibleItemIndx =
                            homefeedScrollOffset.firstVisibleItemIndex
                        homeScreenviewmodelFragment.feedFirstVisibleItemoffset =
                            homefeedScrollOffset.firstVisibleItemScrollOffset

                        return Offset.Zero
                    }
                })
        ){
            ContentSection(
                    rootNavController,
                    homeScreenviewmodelFragment.bannerResources,
                    homeScreenviewmodelFragment.userInteractedWithBanners,
                    listState= homefeedScrollOffset,
                    homeScreenviewmodelFragment.products,
                    onFavouriteButtonClick= {
                        if (homeScreenviewmodelFragment.userFavs.contains(it)) {
                            homeScreenviewmodelFragment.removeFromFavourites(it)
                        }else{
                            homeScreenviewmodelFragment.addProductToFavourites(it)
                        }
                    },
                    isFavouriteProduct = {
                        homeScreenviewmodelFragment.userFavs.contains(it)
                    }
                )
        }
    }

        CollapsingTopAppBar(
            motionScene = toolbarMotionScene,
            progress = homeScreenviewmodelFragment.toolBaroffsetY,
            toolbarBackground = homeScreenviewmodelFragment.toolbarColor,
            searchbarColor = animateSearchBarColor,
            categories = homeScreenviewmodelFragment.categories,
            selectedCategory = homeScreenviewmodelFragment.selectedCategory,
            onCategoryChange = {
                homeScreenviewmodelFragment.toolBaroffsetY.value = 0f
                homeScreenviewmodelFragment.changeCategory( it )
                setHeaderColor(isCollapsed = false)
            },
            showDialog= showAccountDialog,
            onCartIconClick = {
                rootNavController.navigate(Routes.shoppingCartScreen)
            },
            onSearchBarClick = onSearchbarClick,
            username = homeScreenviewmodelFragment.currentUserName.value.split(" ").first()
        )

        if(showAccountDialog.value){
            AccountDialog(userName = homeScreenviewmodelFragment.currentUserName.value, menuItems = generateMenuItems(rootNavController), showAccountDialog, painterResource(id = R.drawable.test_product_placeholder))
        }

}

@Composable
private fun homefeedScrollState(viewModel:HomeScreenFragmentViewmodel):LazyGridState{
    
    val scrollState= rememberLazyGridState(viewModel.feedFirstVisibleItemIndx, viewModel.feedFirstVisibleItemoffset)
    
    DisposableEffect(key1 = true, effect = {
        onDispose{
            viewModel.feedFirstVisibleItemIndx = scrollState.firstVisibleItemIndex
            viewModel.feedFirstVisibleItemoffset = scrollState.firstVisibleItemScrollOffset
        }
    })
    
    return scrollState
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContentSection(
    rootNavController: NavController,
    bannerResource: SnapshotStateMap<String, Int>,
    userInteracted: MutableState<Boolean>,
    listState: LazyGridState,
    products: SnapshotStateList<ShopApiProductsResponse>,
    onFavouriteButtonClick: (Int) -> Unit,
    isFavouriteProduct:(Int)->Boolean
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
            products,
            onFavouriteButtonClick = onFavouriteButtonClick,
            isFavouriteProduct = isFavouriteProduct
        ) { product ->
            rootNavController.navigate("${Routes.productDetailScreen}/${product.id}")
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
    onFavouriteButtonClick:(Int)->Unit,
    isFavouriteProduct:(Int)->Boolean,
    onNavigate: (product: ShopApiProductsResponse) -> Unit
) {
    items(products.size) { index ->

        Card(
            shape = RoundedCornerShape(12.dp),
            elevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 6.dp,
                    bottom = 6.dp,
                    start = if (index % 2 == 0) 18.dp else 6.dp,
                    end = if (index % 2 == 0) 6.dp else 18.dp,
                )
        ) {
            ProductsCard(
                modifier = Modifier
                    .fillMaxWidth(),
                product = products[index],
                onNavigate = onNavigate,
                onFavouriteButtonClick= onFavouriteButtonClick,
                isFavourite = isFavouriteProduct(products[index].id)
            )
        }

    }
}

fun generateMenuItems(rootNavController: NavController):List<MenuItemData>{
    return listOf<MenuItemData>(
        MenuItemData(
            "My Profile",
            Icons.Outlined.AccountCircle,
        ) {
            rootNavController.navigate(Routes.myProfileScreen)
        } ,

        MenuItemData(
            "Your Cart",
            Icons.Outlined.ShoppingCart,
        ) {
            rootNavController.navigate(Routes.shoppingCartScreen)
        } ,

        MenuItemData(
            "Favourites",
            Icons.Rounded.FavoriteBorder
        ) {
            rootNavController.navigate(Routes.favouritesScreen)
        },

        MenuItemData(
            "Your Orders",
            Icons.Outlined.Send
        ) {
          rootNavController.navigate(Routes.userOrders)
        },

        MenuItemData(
            "Settings",
            Icons.Outlined.Settings
        ) {
           rootNavController.navigate(Routes.settingScreen)
        },

        MenuItemData(
            "Support",
            Icons.Rounded.Info
        ) {
            rootNavController.navigate(Routes.supportScreen)
        }

    )
}

