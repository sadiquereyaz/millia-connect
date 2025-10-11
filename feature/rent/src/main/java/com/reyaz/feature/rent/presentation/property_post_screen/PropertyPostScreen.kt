package com.reyaz.feature.rent.presentation.property_post_screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyaz.feature.rent.domain.model.Property
import com.reyaz.feature.rent.presentation.property_post_screen.components.DropDownMenu
import com.reyaz.feature.rent.presentation.property_post_screen.components.FloorTextField
import com.reyaz.feature.rent.presentation.property_post_screen.components.LocationTextField
import com.reyaz.feature.rent.presentation.property_post_screen.components.MultiSelectMenu
import com.reyaz.feature.rent.presentation.property_post_screen.components.RentTextField
import com.reyaz.feature.rent.presentation.property_post_screen.components.TitleTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyPostScreen(
   onPostClick:(property: Property)->Unit,
   viewModel: PropertyPostViewModel,
) {
    val user by viewModel.user.collectAsStateWithLifecycle()//user tracking whether login or not
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {}


    val selectedBHK = remember { mutableStateOf("1BHK") }//to show which bhk is selected
    val selectedType = remember { mutableStateOf("FLAT") }//selected type of flat
    val title = remember { mutableStateOf("") }
    val totalFloor = remember { mutableStateOf("") }
    val floorNumber = remember { mutableStateOf("") }
    val totalRent = remember { mutableStateOf("") }
    val securityDeposit = remember { mutableStateOf("") }
    val location = remember { mutableStateOf("") }

    val options = listOf("wifi","fridge","AC",
        "balcony","security","fan",
        "Utensil","kitchen","power backup",
        "lift")
    var selectedOption by remember { mutableStateOf(listOf<String>()) }




    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Post Your Property",
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    // 👈 Left-side icon
                    //navigate back
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // back arrow
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                }
            )
        },

        bottomBar = { // 👈 Static Post button
            Button(
                //here it will check whether user is logged in or not
                //if not logged in then it will invoke login function
                onClick = {
                    if(user==null){
                        viewModel.doGoogleSignIn(
                context = context,
                scope = scope,
                launcher = launcher,
                login = {
                    onPostClick(Property())
                }
            )
                    }else{
                        onPostClick(Property())
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start=8.dp,end=8.dp)
                    .navigationBarsPadding() // 👈 pushes button above nav bar
                    .imePadding(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50) // green shade
                )
            ) {
                Text(
                    text = "Post",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }



    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 4.dp)
                .verticalScroll(rememberScrollState())
        ) {

            //for title
            Spacer(modifier = Modifier.height(8.dp))
            TitleTextField(
                onValueChange = {
                    title.value = it
                },
                title = title.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp, end = 6.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            //for selecting bhk type
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp, end = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                val typeList = listOf("FLAT", "PG")
                DropDownMenu(
                    option = typeList,
                    label = "Property Type",
                    onSelect = {
                        selectedType.value = it
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .weight(1F)
                )

                val bhkList = listOf("1BHK", "2BHk", "3BHK")
                DropDownMenu(
                    option = bhkList,
                    label = "BHK Type",
                    onSelect = {
                        selectedBHK.value = it
                    },
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(8.dp))
                        .weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                FloorTextField(
                    floor = totalFloor.value,
                    title = "Total Floor",
                    onValueChange = {
                        totalFloor.value = it
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 6.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                )

                FloorTextField(
                    floor = floorNumber.value,
                    title = "Floor Number?",
                    onValueChange = {
                        floorNumber.value = it
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end=6.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement
                    .spacedBy(4.dp)
            ) {

                RentTextField(
                    rent = totalRent.value,
                    title = "Total rent",
                    onValueChange = {
                        totalRent.value = it
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start=6.dp,)
                        .clip(shape = RoundedCornerShape(8.dp))
                )
                RentTextField(
                    rent = securityDeposit.value,
                    title = "Deposit",
                    onValueChange = {
                        securityDeposit.value = it
                    },
                    modifier = Modifier
                        .padding(end=6.dp)
                        .weight(1f)
                        .clip(shape = RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LocationTextField(
                location = location.value,
                title = "Your location",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start=6.dp,end=6.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                onValueChange = { location.value = it }
            )
            Spacer(modifier = Modifier.height(8.dp))
            MultiSelectMenu(
                options = options,
                selectedOptions = selectedOption,
                onSelectionChanged = {updatedList->
                    selectedOption=updatedList
                },
                modifier= Modifier
            )
        }
    }
}