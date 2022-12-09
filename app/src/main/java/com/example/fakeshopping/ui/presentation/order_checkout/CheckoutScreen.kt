package com.example.fakeshopping.ui.presentation.order_checkout

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.fakeshopping.ui.model.CheckoutScreenViewModel
import com.example.fakeshopping.ui.theme.ColorYellow
import com.example.fakeshopping.utils.PaymentOptionId
import com.example.fakeshopping.utils.PaymentScreenRoutes
import com.example.fakeshopping.ui.presentation.components.IconButton
import com.example.fakeshopping.data.userdatabase.repository.UserAddress
import com.example.fakeshopping.ui.presentation.components.UserAddressTextFiled
import com.example.fakeshopping.utils.SettingStateDataStore

@Composable
fun ProductCheckoutScreen(
    navController:NavHostController, selectedProductQuantity:String, selectedProductIds:String, currentUser:String,
    onContinueToPayment:(paymentOptionRoute:String, amountToBePaid:Float, itemsToBuy:Map<Int,Int>)->Unit,
) {

    val viewModel: CheckoutScreenViewModel = hiltViewModel()
    val context = LocalContext.current

    LaunchedEffect(key1 = true, block = {

        viewModel.setCurrentUser(
            currentUser,
            selectedProductsId = selectedProductIds,
            selectedProductsQuantity = selectedProductQuantity
        )

    })

    Scaffold(
        topBar = { CheckoutTopBar(onBackArrowClick = { navController.popBackStack() }) },
        bottomBar = {
            CheckoutScreenBottomBar(
                onProceedClick = {
                    val paymentRoute = when (viewModel.paymentMethod.value) {
                        PaymentOptionId.OPTION_CARD -> PaymentScreenRoutes.cardFragment
                        PaymentOptionId.OPTION_POD -> PaymentScreenRoutes.cardFragment
                        PaymentOptionId.OPTION_UPI -> PaymentScreenRoutes.upiFragment
                        PaymentOptionId.OPTOIN_NETBANKING -> PaymentScreenRoutes.netBankingFragment
                        PaymentOptionId.OPTOIN_WALLET -> PaymentScreenRoutes.walletFragment
                    }

                    if(!viewModel.isStoredAddressSelectedFordelivery.value){
                        if(viewModel.verifyAddress()){
                            if(viewModel.checkAddressSchemeSettingState()) {
                                Toast.makeText(context, "Address Updated ✔", Toast.LENGTH_SHORT).show()
                                viewModel.updateUserAddress()
                            }
                        }else{
                            Toast.makeText(context,"Please enter a valid Address !", Toast.LENGTH_SHORT).show()
                        }
                    }
                    onContinueToPayment(paymentRoute, viewModel.totalCost.value , viewModel.itemsToBuy)

                },
                onCancel = { navController.popBackStack() }
            )
        },
        modifier = Modifier
            .statusBarsPadding()
    ) {

        val scrollState = rememberScrollState()
        Column(Modifier.verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp), contentAlignment = Alignment.TopCenter
            ) {

                PriceDetailsCard(
                    numberOfItems = viewModel.itemsToBuy.size,
                    itemsCost = viewModel.itemsCost.value,
                    totalTax = viewModel.tax.value,
                    totalDeliveryCharge = viewModel.deliveryCharge.value,
                    discount = viewModel.discount.value,
                    totalAmount = viewModel.totalCost.value
                )

            }

            Spacer(Modifier.height(36.dp))
            Text(
                "Choose Delivery Address",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 32.dp)
                    .fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            DeliveryAddressSelectionSection(viewModel = viewModel)

            Spacer(Modifier.height(36.dp))
            Text(
                "Choose Payment Method",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 32.dp)
                    .fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            PaymentOptionsSelection(
                currentSelectedOption = viewModel.paymentMethod,
                onOptionChange = {
                    viewModel.changeCurrentPaymentMethod(it)
                })
            Spacer(Modifier.height(68.dp))

        }


    }

}

@Composable
private fun CheckoutTopBar( onBackArrowClick:()->Unit ){

    Box(Modifier.shadow(elevation = 4.dp)) {
        TopAppBar(
            title = {
                Text(
                    "Overview",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier=Modifier.padding(top=12.dp, bottom=12.dp)
                )
            },
            navigationIcon = {

                IconButton(
                    icon =Icons.Default.ArrowBack ,
                    onClick = { onBackArrowClick()  },
                    contentDescription = "Go back"
                )

            },
            backgroundColor = Color.White,
            elevation = 0.dp
        )
    }


}


@Composable
private fun CheckoutScreenBottomBar( onProceedClick:()->Unit, onCancel:()->Unit ){

    Row(modifier= Modifier
        .fillMaxWidth(1f)
        .background(Color.White)
        .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.Center
    ){

        Button(
            onClick = { onCancel() },
            modifier=Modifier.weight(0.16f),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent
            ),
            elevation = ButtonDefaults.elevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
            ),
            border = BorderStroke(2.dp, ColorYellow)
        ) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Box(Modifier.fillMaxWidth(),contentAlignment = Alignment.CenterEnd){

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "cancel",
                    )
                }
            }
        }

        Spacer(Modifier.width(8.dp))

        Button(
            onClick = { onProceedClick() },
            modifier=Modifier.weight(0.8f),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ColorYellow
            )
        ) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text("Proceed", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Box(Modifier.fillMaxWidth(),contentAlignment = Alignment.CenterEnd){

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Proceed to Payment ?",
                    )
                }
            }
        }



    }

}

@Composable
private fun PriceDetailsCard( numberOfItems:Int, itemsCost:Float, totalTax:Float, totalDeliveryCharge:Float, discount:Float, totalAmount:Float ){

    Card(modifier= Modifier
        .fillMaxWidth(0.9f),
        shape=RoundedCornerShape(12.dp),
        elevation= 6.dp
    )
    {
        Column {
            Text(
                text = "Overview", modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 12.dp, start = 21.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Box(
                Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .background(Color.White), contentAlignment = Alignment.Center) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {

                    Text(
                        text = "$numberOfItems items",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .padding(start = 10.dp)
                    )

                    OverviewCardItem(title = "Item's Cost", cost = itemsCost, true)
                    OverviewCardItem(title = "Discount", cost = discount, false)
                    OverviewCardItem(title = "Total Tax", cost = totalTax, true)
                    OverviewCardItem(title = "Delivery charge", cost = totalDeliveryCharge, true)

                }
            }

            Card(Modifier.fillMaxWidth(), elevation = 6.dp, shape= RoundedCornerShape( topStart = 0.dp, topEnd = 0.dp, bottomStart = 12.dp, bottomEnd =  12.dp)) {
                Column(
                    Modifier
                        .background(ColorYellow)
                ) {

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {

                        Text(
                            "Total",
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .padding(start = 16.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            "$$totalAmount",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 18.dp),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        )

                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

        }
    }
}

@Composable
fun OverviewCardItem(title:String, cost:Float, isAddingToAmount:Boolean){

    Row(
        modifier = Modifier
            .fillMaxWidth()

    ) {

        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .padding(start = 10.dp)
        )
        Text(
            text = if(isAddingToAmount) "$$cost" else "- $$cost",
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            textAlign = TextAlign.End,
            color = if(isAddingToAmount) Color.Black else Color.Green
        )

    }

}

@Composable
fun PaymentOptionsSelection(currentSelectedOption: State<PaymentOptionId>, onOptionChange:(option:PaymentOptionId)->Unit){

    Column(modifier= Modifier
        .fillMaxWidth(0.95f),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        PaymentMethodItem(
            onOptionChange = { onOptionChange(PaymentOptionId.OPTION_CARD) },
            currentSelectedOption = currentSelectedOption.value,
            methodTitle = "Card",
            paymentMethodId = PaymentOptionId.OPTION_CARD
        )

        Spacer(modifier=Modifier.height(8.dp))
        PaymentMethodItem(
            onOptionChange = { onOptionChange(PaymentOptionId.OPTOIN_WALLET) },
            currentSelectedOption = currentSelectedOption.value,
            methodTitle = "Wallet",
            paymentMethodId = PaymentOptionId.OPTOIN_WALLET
        )


        Spacer(modifier=Modifier.height(8.dp))
        PaymentMethodItem(
            onOptionChange = { onOptionChange(PaymentOptionId.OPTION_UPI) },
            currentSelectedOption = currentSelectedOption.value,
            methodTitle = "UPI",
            paymentMethodId = PaymentOptionId.OPTION_UPI
        )


        Spacer(modifier=Modifier.height(8.dp))
        PaymentMethodItem(
            onOptionChange = { onOptionChange(PaymentOptionId.OPTOIN_NETBANKING) },
            currentSelectedOption = currentSelectedOption.value,
            methodTitle = "Net Banking",
            paymentMethodId = PaymentOptionId.OPTOIN_NETBANKING
        )

        Spacer(modifier=Modifier.height(8.dp))
        PaymentMethodItem(
            onOptionChange = { onOptionChange(PaymentOptionId.OPTION_POD) },
            currentSelectedOption = currentSelectedOption.value,
            methodTitle = "Pay On Delivery",
            paymentMethodId = PaymentOptionId.OPTION_POD
        )

        Spacer(modifier=Modifier.height(8.dp))


    }

}

@Composable
private fun DeliveryAddressSelectionSection(viewModel: CheckoutScreenViewModel) {

    val userAddressText = "${viewModel.currentUserStoredAddress.value?.landmark}, ${viewModel.currentUserStoredAddress.value?.pincode}, ${viewModel.currentUserStoredAddress.value?.city}, ${viewModel.currentUserStoredAddress.value?.state}, ${viewModel.currentUserStoredAddress.value?.country}"

    Column(
        modifier = Modifier.fillMaxWidth(0.9f)
    ) {

        if(viewModel.currentUserStoredAddress.value != null && viewModel.currentUserStoredAddress.value!!.country.isNotEmpty()){
            Card(
                shape= RoundedCornerShape(12.dp), modifier=Modifier.clickable { viewModel.toggleDeleveryAddressMode(true) },
                border = if(viewModel.isStoredAddressSelectedFordelivery.value) BorderStroke(1.7.dp, ColorYellow) else null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(21.dp)
                            .clip(CircleShape)
                            .background(if (viewModel.isStoredAddressSelectedFordelivery.value) ColorYellow else Color.LightGray)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        text = userAddressText
                    )

                }

            }
            Spacer(modifier=Modifier.height(24.dp))
        }

        Card(
            shape= RoundedCornerShape(12.dp), modifier= Modifier
                .fillMaxWidth()
                .clickable { viewModel.toggleDeleveryAddressMode(false) },
            border = if(!viewModel.isStoredAddressSelectedFordelivery.value) BorderStroke(1.7.dp, ColorYellow) else null
        ){
            Column(modifier= Modifier
                .fillMaxWidth(0.9f)
                .padding(horizontal = 14.dp, vertical = 16.dp)){
                Row(
                    Modifier
                        .fillMaxWidth()
                        , verticalAlignment = Alignment.CenterVertically
                ){
                    Box(
                        Modifier
                            .size(21.dp)
                            .clip(CircleShape)
                            .background(if (viewModel.isStoredAddressSelectedFordelivery.value) Color.LightGray else ColorYellow))
                    Spacer(Modifier.width(14.dp))
                    Text(
                        text= "Add new address",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold
                    )
                }

                if(!viewModel.isStoredAddressSelectedFordelivery.value){
                    Spacer(Modifier.height(12.dp))
                    UserAddressTextFiled(
                        country = viewModel.currentUserCountry,
                        state = viewModel.currentUserState,
                        city = viewModel.currentUserCity,
                        landmark = viewModel.currentUserLandmark,
                        pincode = viewModel.currentUserPincode,
                        onCountryTextValueChange = { viewModel.onCountryTextValueChange(it) },
                        onStateTextValueChange = { viewModel.onStateTextValueChange(it) },
                        onCityTextValueChange = { viewModel.onCityTextValueChange(it) },
                        onPincodeTextValueChange = { viewModel.onPincodeTextValueChange(it) },
                        onLandmarkTextValueChange = { viewModel.onLandMarkTextValueChange(it) }
                    )
                }

            }
        }

    }

}

@Composable
private fun PaymentMethodItem(onOptionChange:()->Unit, currentSelectedOption:PaymentOptionId, methodTitle:String, paymentMethodId:PaymentOptionId){
    Card(
        modifier= Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        shape = RoundedCornerShape(12.dp),
        border = if(currentSelectedOption == paymentMethodId) BorderStroke(1.7.dp, ColorYellow) else null
    ) {
        Text(methodTitle, modifier= Modifier
            .fillMaxWidth()
            .clickable { onOptionChange() }
            .padding(start = 18.dp, top = 16.dp, bottom = 16.dp), fontWeight = FontWeight.Medium )
    }
}
