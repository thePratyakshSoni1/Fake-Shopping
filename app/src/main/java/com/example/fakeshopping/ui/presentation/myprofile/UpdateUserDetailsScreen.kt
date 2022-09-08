package com.example.fakeshopping.ui.presentation.myprofile

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fakeshopping.data.userdatabase.repository.UserAddress
import com.example.fakeshopping.ui.model.myprofileViewmodels.UpdateMyProfileViewmodel
import com.example.fakeshopping.ui.presentation.components.LoadingView
import com.example.fakeshopping.ui.theme.ColorWhiteVariant
import com.example.fakeshopping.utils.MyProfileScreenRoutes


@Composable
fun UpdateUserDetailsScreen(myprofileNavController: NavHostController, currentUserId:String, onBackPress:()->Unit){

    val viewModel :UpdateMyProfileViewmodel = hiltViewModel()
    val context = LocalContext.current

    BackHandler {
        onBackPress()
        myprofileNavController.popBackStack()
    }

    LaunchedEffect(key1 = true, block = {
        viewModel.setUserIdAndDetails(currentUserId)
    })

    Scaffold(
        modifier=Modifier.fillMaxSize().imePadding().navigationBarsPadding(),
        topBar = {
            UpdateMyProfileScreenTopAppBar(
                onBackArrowPress = { myprofileNavController.popBackStack() },
                onUpdateBtnPress = {
                    if(viewModel.verifyName()){
                        if(viewModel.verifyAddress()){
                            Toast.makeText(context,"Profile Updated ✔",Toast.LENGTH_SHORT).show()
                            viewModel.updateUser()
                            myprofileNavController.popBackStack()
                        }else{
                            Toast.makeText(context,"Please enter a valid Address !",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(context,"Please enter a valid name !",Toast.LENGTH_SHORT).show()
                    }

                },
            )
        }
    ){
        if(viewModel.currentUserDetails.value == null){
            LoadingView(modifier = Modifier.fillMaxSize(), circleSize = 24.dp)
        }else{
            val screenScrollState = rememberScrollState()
            Column(
                modifier= Modifier
                    .fillMaxSize()
                    .verticalScroll(enabled = true, state = screenScrollState)
            ) {

                Spacer(modifier = Modifier.height(22.dp))
                UserProfileAndNameUpdate(
                    viewModel.currentUserFirstName,
                    viewModel.currentUserLastName,
                    onFirstNameChange = {
                        viewModel.onFirstNameValueChange(it)
                    },
                    onLastNameChange = {
                        viewModel.onLastNameValueChange(it)
                    }
                )

                Column(modifier=Modifier.padding(end= 12.dp)){
                    Spacer(modifier = Modifier.height(12.dp))
                    UserDetails(
                        country = viewModel.currentUserCountry,
                        state = viewModel.currentUserState,
                        city = viewModel.currentUserCity,
                        landmark = viewModel.currentUserLandmark,
                        pincode = viewModel.currentUserPincode,
                        viewModel = viewModel
                    )
                }

            }

        }
    }

}


@Composable
private fun UserDetails(country: State<String>,state: State<String>,city: State<String>,landmark: State<String>,pincode: State<String>, viewModel: UpdateMyProfileViewmodel){

    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Your Address",
            modifier = Modifier
                .padding(end = 8.dp)
                .padding(6.dp)
                .clip(CircleShape),
            tint = Color.LightGray
        )
        UserAddressDetailUpdate(
            country = country,
            state = state,
            city = city,
            landmark = landmark,
            pincode = pincode,
            viewModel = viewModel
        )

    }

}

@Composable
private fun UserAddressDetailUpdate(
    country: State<String>,
    state: State<String>,
    city: State<String>,
    landmark: State<String>,
    pincode: State<String>,
    viewModel: UpdateMyProfileViewmodel
){

    Column(Modifier.fillMaxWidth()) {

        UserDetailItemUpdate(
            heading = "Country", country.value ,
            onValueChange = {
                viewModel.onCountryTextValueChange(it)
            },
            keyBardType = KeyboardType.Text
        )
        Spacer(modifier = Modifier.height(8.dp))

        UserDetailItemUpdate(
            heading = "State", state.value,
            onValueChange = {
                viewModel.onStateTextValueChange(it)
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier= Modifier.fillMaxWidth()) {

            Box(modifier=Modifier.weight(1f)){

                UserDetailItemUpdate(
                    heading = "Pincode", pincode.value,
                    onValueChange = {
                        viewModel.onPincodeTextValueChange(it.toInt())
                    },
                    keyBardType = KeyboardType.Phone
                )
            }
            Spacer(Modifier.width(8.dp))
            Box(modifier=Modifier.weight(1f)) {
                UserDetailItemUpdate(
                    heading = "City", city.value,
                    onValueChange = {
                        viewModel.onCityTextValueChange(it)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        UserDetailItemUpdate(
            heading = "Landmark", landmark.value,
            onValueChange = {
                viewModel.onLandMarkTextValueChange(it)
            },
            keyBardType = KeyboardType.Text
        )
        Spacer(modifier = Modifier.height(8.dp))

    }

}

@Composable
private fun UserDetailHeading(text:String){
    Text(
        text = text,
        color = ColorWhiteVariant,
        fontSize = 14.sp,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun UserDetailValueUpdate(value:String, onValueChange:(String)->Unit, keyBardType:KeyboardType){
    TextField(
        value = value,
        onValueChange= {
            onValueChange(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.5.dp, color = Color(0xFF350099), shape = RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = Color.Transparent
        ),
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyBardType
        ),
        maxLines = 1
    )
}

@Composable
private fun UserDetailItemUpdate( heading:String, value:String, onValueChange:(String)->Unit, keyBardType:KeyboardType = KeyboardType.Text ){
    Column(Modifier.fillMaxWidth()) {
        UserDetailHeading(text = heading)
        Spacer(modifier=Modifier.height(6.dp))
        UserDetailValueUpdate(value = value, onValueChange= onValueChange, keyBardType)
    }
}


@Composable
private fun UserProfileAndNameUpdate(firstName: State<String>,lastName: State<String>, onFirstNameChange:(String)->Unit,onLastNameChange:(String)->Unit){

    Column(modifier= Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        Box(modifier = Modifier
            .size(120.dp)
            .clip(CircleShape) ){
            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Your Profile", modifier = Modifier.fillMaxSize(), tint = Color.LightGray)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier= Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)){

            TextField(
                value = firstName.value,
                onValueChange= {
                    onFirstNameChange(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(width = 1.5.dp, color = Color(0xFF350099), RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
            )

            Spacer(Modifier.width(8.dp))

            TextField(
                value = lastName.value,
                onValueChange= {
                    onLastNameChange(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .border(width = 1.5.dp, color = Color(0xFF350099), RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
            )



        }

    }

}

@Composable
private fun UpdateMyProfileScreenTopAppBar( onBackArrowPress:()->Unit ,onUpdateBtnPress:()->Unit ) {


    Box(Modifier.shadow(elevation = 4.dp)) {
        TopAppBar(
            backgroundColor = Color.White,
            title = {
                Text(
                    "My Profile",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },

            navigationIcon = {

                com.example.fakeshopping.ui.presentation.components.IconButton(
                    icon = Icons.Default.ArrowBack,
                    onClick = { onBackArrowPress() },
                    contentDescription = "Go back"
                )

            },
            actions = {

                Text(
                    text = "Save",
                    color = Color(0xFF350099),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            onUpdateBtnPress()
                        },
                )
            }
        )
    }

}
