package com.example.fakeshopping.ui.presentation.homscreen

import android.util.Log
import android.view.Window
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fakeshopping.utils.HomeScreenFragmentRoutes
import com.example.fakeshopping.utils.Routes

@Composable
fun HomeScreen(rootNavController: NavController, window: Window, category: String = "All", getCurrentUser:()->String) {

    val fragmentNavController = rememberNavController()

    Column(modifier = Modifier.fillMaxSize()) {

        Box(modifier = Modifier.fillMaxSize()) {
            HomeScreenNavigation(
                rootNavController = rootNavController, window = window,
                fragmentNavController = fragmentNavController,
                category = category,
                userId = getCurrentUser()
            )
        }

    }


}

@Composable
fun HomeScreenNavigation(
    rootNavController: NavController,
    window: Window,
    category: String = "All",
    fragmentNavController: NavHostController,
    userId: String
) {

    NavHost(
        fragmentNavController,
        startDestination = HomeScreenFragmentRoutes.mainFragment,
    ) {



        composable(HomeScreenFragmentRoutes.mainFragment) {
            HomeScreenMainFragment(
                rootNavController = rootNavController,
                window = window,
                category = category,
                onSearchbarClick = {
                    fragmentNavController.navigate(HomeScreenFragmentRoutes.searchSuggestionFragment)
                },
                userId = userId
            )
        }

        composable(
            HomeScreenFragmentRoutes.searchSuggestionFragment+"?queryString={queryString}",
            arguments= listOf(
                navArgument(name="queryString"){
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {

            HomeScreenSearchSuggestionFragment(
                previousQueryString = it.arguments?.getString("queryString"),
                searchProduct = { searchText ->
                    fragmentNavController.popBackStack()
                    fragmentNavController.navigate("${HomeScreenFragmentRoutes.searchResultsFragment}/$searchText")
                },
                goToPreviousFragment = { fragmentNavController.popBackStack() }
            )

        }

        composable(
            HomeScreenFragmentRoutes.searchResultsFragment + "/{searchString}",
            arguments = listOf(
                navArgument(name="searchString"){
                    type = NavType.StringType
                }
            )
        ) {

            HomeScreenSearchResultsFragment(
                onProductClick = { productId ->
                    rootNavController.navigate("${Routes.productDetailScreen}/$productId")
                },
                searchTxt = it.arguments?.getString("searchString")!!,
                onBackBtnClick = {
                    fragmentNavController.popBackStack(HomeScreenFragmentRoutes.mainFragment,false)
                },
                onSearchClear = {
                    fragmentNavController.navigate(HomeScreenFragmentRoutes.searchSuggestionFragment) {
                        popUpTo(HomeScreenFragmentRoutes.searchSuggestionFragment) {
                            inclusive = true
                        }
                    }
                                },
                onSearchTextClick = { searchTxt ->
                    fragmentNavController.navigate("${HomeScreenFragmentRoutes.searchSuggestionFragment}?queryString=$searchTxt")
                },
                onBackPress = {
                    fragmentNavController.popBackStack()
                },
                currentUserId = userId
            )

        }


    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreenSearchBar(
    searchbarTxt:TextFieldValue,
    onSearchTextValueChange:(TextFieldValue)->Unit,
    focusRequester: FocusRequester,
    searchProducts:(String)->Unit,
    onBackPress: () -> Unit,
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(true) {
        Log.d("TOOLBAR", "Hi i am Toolbar Composable here")
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
                .padding(start = 12.dp, top = 8.dp, bottom = 8.dp)
                .clip(CircleShape)
                .clickable {
                    keyboardController?.hide()
                    onBackPress()
                }
                .padding(6.dp),
            tint = Color.DarkGray
        )

        TextField(
            value = searchbarTxt,
            onValueChange = { onSearchTextValueChange(it) },
            modifier = Modifier
                .height(TextFieldDefaults.MinHeight)
                .weight(1f)
                .focusRequester(focusRequester),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.DarkGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                cursorColor = Color.DarkGray,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search,
                autoCorrect = false
            ),
            keyboardActions = KeyboardActions {
                keyboardController?.hide()
                searchProducts(searchbarTxt.text)
            },
            maxLines = 1
        )

        if (searchbarTxt.text.isNotEmpty()) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear",
                modifier = Modifier
                    .padding(end = 12.dp, top = 8.dp, bottom = 8.dp)
                    .clip(CircleShape)
                    .clickable {
                        onSearchTextValueChange(TextFieldValue(""))
                        keyboardController?.show()
                    }
                    .padding(6.dp),
                tint = Color.DarkGray
            )
        }
    }
}


