package com.example.fakeshopping.ui.presentation.myprofile

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fakeshopping.data.userdatabase.repository.UserAddress
import com.example.fakeshopping.ui.model.myprofileViewmodels.MyProfileScreenViewmodel
import com.example.fakeshopping.ui.presentation.components.LoadingView
import com.example.fakeshopping.utils.MyProfileScreenRoutes


@Composable
fun MyProfileScreenMainFragment(myprofileNavController:NavHostController, currentUser: String, onLogout:()->Unit, onProfileBackPress:()->Unit){

    val viewModel: MyProfileScreenViewmodel = hiltViewModel()
    LaunchedEffect(key1 = true, block = {
        viewModel.setUserIdAndDetails(currentUser)
    })

    Scaffold(
        modifier=Modifier.fillMaxSize(),
        topBar = {
            MyProfileScreenTopAppBar(
                onBackArrowPress = {
                    onProfileBackPress()
                },
                onEditBtnPress = {
                    myprofileNavController.navigate(MyProfileScreenRoutes.editingFragment)
                },
                myProfileNavController = myprofileNavController
            )
        }
    ) {

        if(viewModel.currentUserDetails.value == null){
            LoadingView(modifier = Modifier.fillMaxSize(), circleSize = 64.dp)
        }else{

            Column(modifier=Modifier.fillMaxSize()) {

                Spacer(modifier = Modifier.height(22.dp))
                UserProfileAndName("${viewModel.currentUserDetails.value!!.userFirstName} ${viewModel.currentUserDetails.value!!.userLastName}")

                Spacer(modifier = Modifier.height(22.dp))
                Column(modifier=Modifier.padding(start= 12.dp)){

                    UserDetails(userPhone = viewModel.currentUserId, userAddress = viewModel.currentUserAddress)

                    Spacer(modifier = Modifier.height(12.dp))
                    MyProfileScreenActionButtons(
                        onLogoutClick = { onLogout() } ,
                        onDeleteAccountClick = {
                            viewModel.deleteAccount()
                            onLogout()
                        }
                    )
                }
            }

        }
    }

}


@Composable
private fun UserDetails(userPhone:State<String>, userAddress: State<UserAddress?>){

    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 12.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Phone,
            contentDescription = "Your Phone Number",
            modifier = Modifier
                .padding(end = 8.dp)
                .padding(6.dp)
                .clip(CircleShape),
            tint = Color.LightGray
        )
        UserDetailItem(heading = "Phone Number", value = userPhone.value)
    }
    Spacer(modifier = Modifier.height(12.dp))
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
        UserAddressDetailItem(userAdress = userAddress.value)

    }

}

@Composable
private fun UserAddressDetailItem(userAdress: UserAddress?){

    Column(Modifier.fillMaxWidth()) {

        UserDetailItem(heading = "Country", userAdress?.country ?: "")
        Spacer(modifier = Modifier.height(12.dp))

        UserDetailItem(heading = "State", userAdress?.state ?: "")
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier=Modifier.fillMaxWidth()) {

            Box(modifier=Modifier.weight(1f)){
                UserDetailItem(heading = "Pincode", userAdress?.pincode.toString() )
            }
            Spacer(Modifier.width(8.dp))
            Box(modifier=Modifier.weight(1f)){
                UserDetailItem(heading = "City", userAdress?.city ?: "")
            }

        }
        Spacer(modifier = Modifier.height(12.dp))

        UserDetailItem(heading = "Landmark", userAdress?.landmark ?: "")
        Spacer(modifier = Modifier.height(12.dp))

    }

}

@Composable
private fun UserDetailHeading(text:String){
    Text(
        text = text,
        color = Color.LightGray,
        fontSize = 14.sp,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun UserDetailValue(value:String){
    Text(
        text = value,
        color = Color.Black,
        fontSize = 16.sp,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun UserDetailItem(heading:String, value:String){
    Column(Modifier.fillMaxWidth()) {

        UserDetailHeading(text = heading)
        Spacer(modifier = Modifier.height(6.dp))
        UserDetailValue(value = value)
    }
}

@Composable
private fun MyProfileScreenActionButtons ( onLogoutClick:()->Unit , onDeleteAccountClick:()->Unit){

    Column( Modifier.fillMaxWidth() ){

        Button(
            onClick = { onLogoutClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(3.dp, Color(0xFF350099)),
            elevation = ButtonDefaults.elevation(pressedElevation = 0.4.dp, defaultElevation = 0.dp, focusedElevation = 0.dp)
        ) {
            Text(
                "Log out",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(vertical = 4.dp),
                color= Color(0xFF350099)
            )
        }

        Spacer(modifier=Modifier.height(8.dp))

        Button(
            onClick = { onDeleteAccountClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(3.dp, Color.LightGray),
            elevation = ButtonDefaults.elevation(pressedElevation = 0.4.dp, defaultElevation = 0.dp, focusedElevation = 0.dp)
        ) {
            Text(
                "Delete Account",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(vertical = 4.dp),
                color=Color.DarkGray
            )
        }

    }

}

@Composable
private fun UserProfileAndName(userName:String){

    Column(modifier=Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        Box(modifier = Modifier
            .size(120.dp)
            .clip(CircleShape) ){
            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Your Profile", modifier = Modifier.fillMaxSize(), tint = Color.LightGray)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = userName, fontWeight = FontWeight.Medium, fontSize = 18.sp)

    }

}


@Composable
private fun MyProfileScreenTopAppBar( onBackArrowPress:()->Unit ,onEditBtnPress:()->Unit, myProfileNavController:NavHostController) {


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
                    text = "Edit",
                    color = Color(0xFF350099),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            onEditBtnPress()
                        },
                )
            }
        )
    }

}
