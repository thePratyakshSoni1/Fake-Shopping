package com.example.fakeshopping.ui.presentation.components

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable 
fun PaymentSuccesDialog( paymentSucceed:State<Boolean>, onDialogueRemove:()->Unit ) {

    Dialog(onDismissRequest = onDialogueRemove ) {

        Box(modifier = Modifier
            .fillMaxHeight(0.75f)
            .fillMaxWidth(0.95f)) {

            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(12.dp),
                backgroundColor = if(paymentSucceed.value) Color(0xFF56FF72) else Color(0xFFFF5656)
            ) {
                Column(
                    modifier= Modifier
                        .fillMaxSize()
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val dialogIcon = if(paymentSucceed.value) Icons.Default.CheckCircle else Icons.Default.Close
                    val iconBgColor = if(paymentSucceed.value) Color(0xFF008316) else Color(
                        0xFFA50000
                    )
                    Box(modifier= Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(iconBgColor)){
                        Icon(imageVector =dialogIcon, contentDescription = null, tint = Color.White, modifier=Modifier.size(120.dp))
                    }
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text = if(paymentSucceed.value) "Payemt Successful" else "Payment Failed",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier= Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp),
                        textAlign = TextAlign.Center
                    )


                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = if(paymentSucceed.value) "Your order was placed successfully" else "Payment wasn't successful !",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White ,
                        modifier= Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(16.dp))

                }
            }

            Box(modifier=Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd){
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "close payment status message",
                    modifier=Modifier.clip(CircleShape).clickable { onDialogueRemove() }.padding(16.dp)
                )
            }

        }

    }


}