package com.example.fakeshopping.ui.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.fakeshopping.data.ShopApiProductsResponse
import com.example.fakeshopping.ui.HomeScreenViewmodel
import com.example.fakeshopping.utils.Routes


@Composable
fun HomeScreen(navController: NavController) {

    val viewmodel: HomeScreenViewmodel = hiltViewModel()

    Column(modifier = Modifier.fillMaxSize()) {

        HeaderSection(
            modifier = Modifier
                .fillMaxWidth(),
            viewmodel.searchBoxText,
            categories = viewmodel.categories,
            selectedCategory = viewmodel.selectedProductCategory,
            onCategoryChange = {
                viewmodel.onCategoryChange(it)
            }
        )


        Spacer(Modifier.height(22.dp))

        BannerSection(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f),
            viewmodel.bannerResources,
            viewmodel.userInteractedWithBanners

        )

        AllProductsSection(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            viewmodel.products
        ) { navController.navigate(Routes.productDetailScreen) }

    }


}

@Composable
fun HeaderSection(
    modifier: Modifier = Modifier.fillMaxWidth(),
    searchText: MutableState<String>,
    categories: List<String>,
    selectedCategory: MutableState<String>,
    onCategoryChange: (String) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Blue)
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
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(8.dp),
            textStyle = TextStyle(
                fontSize = 16.sp,
            ),
            placeholder = { Text("Search Products...") }

        )

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllProductsSection(
    modifier: Modifier = Modifier.fillMaxWidth(),
    products: List<ShopApiProductsResponse>,
    onNavigate: () -> Unit
) {

    Column(modifier = modifier) {


        LazyVerticalGrid(cells = GridCells.Fixed(3)) {

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
    }

}
