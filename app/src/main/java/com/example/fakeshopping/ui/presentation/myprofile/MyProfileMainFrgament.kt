package com.example.fakeshopping.ui.presentation.myprofile

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import com.example.fakeshopping.ui.presentation.components.IconButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fakeshopping.data.userdatabase.repository.UserAddress
import com.example.fakeshopping.ui.model.myprofileViewmodels.MyProfileScreenViewmodel
import com.example.fakeshopping.ui.presentation.components.LoadingView
import com.example.fakeshopping.ui.presentation.components.PasswordTextField
import com.example.fakeshopping.ui.theme.ColorExtraDarkGray
import com.example.fakeshopping.utils.MyProfileScreenRoutes
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun MyProfileScreenMainFragment(myprofileNavController:NavHostController, currentUser: String, onLogout:()->Unit, onProfileBackPress:()->Unit){

    val viewModel: MyProfileScreenViewmodel = hiltViewModel()
    LaunchedEffect(key1 = true, block = {
        viewModel.setUserIdAndDetails(currentUser)
    })


    val sheetExpandingCoroutineScope = rememberCoroutineScope()

    val localKeyboardControler= LocalSoftwareKeyboardController.current
    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    LaunchedEffect( key1 = bottomSheetState.isVisible) {
        if (!bottomSheetState.isVisible) {
            localKeyboardControler?.hide()
        }
    }
    BackHandler(bottomSheetState.isVisible) {
        sheetExpandingCoroutineScope.launch {
            bottomSheetState.hide()
        }
    }

    ModalBottomSheetLayout(
        sheetContent = {
            ChangePasswordBottomSheetContent(
                onTogglePasswordVisibility = { viewModel.togglePasswordVisibility() },
                isPasswordVisible = viewModel.isPasswordVisible,
                onPasswordChange = { newPassword ->
                    viewModel.updatePassword(newPassword)
                    sheetExpandingCoroutineScope.launch { bottomSheetState.show() }
                },
                onVerifyOldPassword = { oldPassword ->
                    viewModel.verifyOldPassword(oldPassword)
                } ,
                onVerifyNewPasswordPattern = { newPassword ->
                    viewModel.verifyNewPasswordPattern(newPassword)
                },
                onCancel = { sheetExpandingCoroutineScope.launch { bottomSheetState.hide() } },
                sheetState = bottomSheetState
            )
        } ,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd= 16.dp, bottomStart = 0.dp, bottomEnd= 0.dp),
        sheetElevation = 4.dp,
        sheetBackgroundColor = Color.White,
        sheetState = bottomSheetState
    ){

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
            },
        ) {

            if(viewModel.currentUserDetails.value == null){
                LoadingView(modifier = Modifier.fillMaxSize(), circleSize = 64.dp)
            }else{

                Column(modifier=Modifier.fillMaxSize()) {

                    Spacer(modifier = Modifier.height(22.dp))
                    UserProfileAndName("${viewModel.currentUserDetails.value!!.userFirstName} ${viewModel.currentUserDetails.value!!.userLastName}")

                    Spacer(modifier = Modifier.height(22.dp))
                    Column(modifier=Modifier.padding(start= 12.dp)){

                        UserDetails(
                            userPhone = viewModel.currentUserId,
                            userAddress = viewModel.currentUserAddress,
                            userPassword = viewModel.currentUserDetails.value!!.password,
                            onPasswordChange = { sheetExpandingCoroutineScope.launch { bottomSheetState.show() } },
                            onPhoneChange = { myprofileNavController.navigate(MyProfileScreenRoutes.changeNumberFragment) }
                        )

                        Spacer(modifier = Modifier.height(36.dp))
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

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ChangePasswordBottomSheetContent(
    onTogglePasswordVisibility:()->Unit,
    isPasswordVisible:State<Boolean>,
    onPasswordChange:(String)->Unit,
    onVerifyOldPassword:(oldPassword:String)->Boolean,
    onVerifyNewPasswordPattern:(newPassword:String)->Boolean,
    onCancel:()->Unit,
    sheetState:ModalBottomSheetState
){

    val oldPasswordValue = remember{ mutableStateOf("") }
    val newPasswordValue = remember{ mutableStateOf("") }
    val confirmNewPasswordValue = remember{ mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(key1 = sheetState.isVisible, block = {
        if(sheetState.isVisible){
            oldPasswordValue.value = ""
            newPasswordValue.value = ""
            confirmNewPasswordValue.value = ""
        }
    })

    Column(modifier= Modifier
        .fillMaxWidth()
        .padding(top = 18.dp, bottom = 18.dp, start = 12.dp, end = 12.dp)
        .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PasswordTextField(
            value = oldPasswordValue,
            onValuechange = {
                oldPasswordValue.value = it
            },
            backgroundColor = Color.Transparent,
            textColor = Color.Black,
            hintColor = Color.LightGray,
            hintTxt = "Old Password",
            textType = KeyboardType.Password,
            isPasswordVisible = isPasswordVisible,
            onTogglePassword = { onTogglePasswordVisibility() },
        )
        Spacer(Modifier.height(8.dp))

        PasswordTextField(
            value = newPasswordValue,
            onValuechange = {
                newPasswordValue.value = it
            },
            backgroundColor = Color.Transparent,
            textColor = Color.Black,
            hintColor = Color.LightGray,
            hintTxt = "New Password",
            textType = KeyboardType.Password,
            isPasswordVisible = isPasswordVisible,
            onTogglePassword = { onTogglePasswordVisibility() },
        )
        Spacer(Modifier.height(8.dp))

        PasswordTextField(
            value = confirmNewPasswordValue,
            onValuechange = {
                confirmNewPasswordValue.value = it
            },
            backgroundColor = Color.Transparent,
            textColor = Color.Black,
            hintColor = Color.LightGray,
            hintTxt = "Confirm New Password",
            textType = KeyboardType.Password,
            isPasswordVisible = isPasswordVisible,
            onTogglePassword = { onTogglePasswordVisibility() },
        )

        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                "Cancel",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null
                    ) {
                        onCancel()
                    },
                textAlign = TextAlign.Center,
                color = Color.Red
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF350099)
                ),
                onClick = {
                    if (onVerifyOldPassword(oldPasswordValue.value)) {
                        if (onVerifyNewPasswordPattern(newPasswordValue.value)) {
                            if (newPasswordValue.value == confirmNewPasswordValue.value) {
                                onPasswordChange(newPasswordValue.value)
                                Toast.makeText(context, "Password Updated ✔", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Password didn't confirm",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Password must be 8 digit containing a symbol, number & alphabet",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(context, "Wrong old password !", Toast.LENGTH_SHORT).show()
                    }
                }

            ) {
                Text(
                    "Change",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.padding(vertical = 4.dp),
                    color = Color.White
                )
            }

        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun UserDetails(userPhone:State<String>, userAddress: State<UserAddress?>, userPassword:String, onPasswordChange:() -> Unit,  onPhoneChange:() -> Unit){

    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 8.dp), verticalAlignment = Alignment.CenterVertically
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

        Box(Modifier.weight(1f)) {
            UserDetailItem(heading = "Phone Number", value = userPhone.value)
        }
        IconButton(
            icon = Icons.Default.Edit,
            onClick = { onPhoneChange() },
            contentDescription = "Change Phone number ?"
        )
    }

    Spacer(modifier = Modifier.height(16.dp))
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Your Phone Number",
            modifier = Modifier
                .padding(end = 8.dp)
                .padding(6.dp)
                .clip(CircleShape),
            tint = Color.LightGray
        )

        var passwordTxt = ""
        repeat(userPassword.length){
            passwordTxt+= "*"
        }

        Box(Modifier.weight(1f)){
            UserDetailItem(heading = "Password", passwordTxt)
        }

        IconButton(
            icon = Icons.Default.Edit,
            onClick = { onPasswordChange() },
            contentDescription = "Change Password ?"
        )
    }

    Spacer(modifier = Modifier.height(16.dp))
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
        val address = "${userAddress.value?.landmark}, ${userAddress.value?.city}, ${userAddress.value?.pincode}, ${userAddress.value?.state}, ${userAddress.value?.country}"
        UserDetailItem(heading = "Address", value = address)

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
                color= Color(0xFF350099),
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
            border = BorderStroke(3.dp, Color.Transparent),
            elevation = ButtonDefaults.elevation(pressedElevation = 0.dp, defaultElevation = 0.dp, focusedElevation = 0.dp)
        ) {
            Text(
                "Delete Account",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(vertical = 4.dp),
                color=Color.Red
            )
        }

    }

}

@Composable
private fun UserProfileAndName(userName:String){

    Column(modifier=Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        Box(modifier = Modifier
            .size(112.dp)
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
