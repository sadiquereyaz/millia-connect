package com.reyaz.feature.rent.presentation.property_post_screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.reyaz.feature.rent.domain.model.Property
import com.reyaz.feature.rent.presentation.property_post_screen.components.DescriptionField
import com.reyaz.feature.rent.presentation.property_post_screen.components.DropDownField
import com.reyaz.feature.rent.presentation.property_post_screen.components.MultiSelectField
import com.reyaz.feature.rent.presentation.property_post_screen.components.TextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PropertyPostScreen(
    onPostClick: (property: Property) -> Unit,
    viewModel: PropertyPostViewModel,
) {
    val user by viewModel.user.collectAsStateWithLifecycle()//user tracking whether login or not
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp)
            .border(shape = RoundedCornerShape(10.dp), width = 1.dp, color = Color.LightGray)
    ) {
        Text(
            text = "Property Details",
            modifier = Modifier
                .padding(start = 12.dp, top = 16.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            "Property Title",
            "eg.spacious 2BHK flat in Jamia",
            uiState.propertyTitle
        ) { it ->
            viewModel.onPropertyTitleChange(it)
        }

        DropDownField(
            title = "Property Type",
            options = listOf("Flat", "PG", "Room"),
            onSelect = { it ->
                viewModel.onPropertyTypeChange(it)
            },
            selectedOption = uiState.propertyType
        )

        DropDownField(
            title = "Property BHK",
            options = listOf("1BHK", "2BHK", "3BHK", "4BHK"),
            onSelect = { it ->
                viewModel.onBhkChange(it)
            },
            selectedOption = uiState.propertyBHK
        )

        TextField("Total Floor", "eg. 2", uiState.totalFloor) { it ->
            viewModel.onTotalFloorChange(it)
        }

        TextField("Floor Number", "eg. 1", uiState.propertyFloorNumber) {
            viewModel.onFloorNumberChange(it)
        }

        TextField("Monthly Rent", "2500", uiState.propertyRent) {
            viewModel.onRentChange(it)
        }

        TextField("Security Deposit", "5000", uiState.securityDeposit) {
            viewModel.onSecurityDepositChange(it)
        }
        TextField(
            title = "Location",
            example = "Batla house gali no 6",
            propertyValue = uiState.propertyLocation
        ) { it ->
            viewModel.onLocationChange(it)
        }

        MultiSelectField(
            title = "Amenities",
            options = listOf(
                "balcony",
                "AC",
                "Parking",
                "lift",
                "Wi-Fi",
                "Kitchen",
                "Power backup"
            ),
            selectedOptions = uiState.amenities,
            onSelectionChanged = {
                viewModel.onAmenityChange(it)
            },
            modifier = Modifier
        )


        DescriptionField(
            title = "Description",
            example = "Describe your property,rules,nearby facilities etc",
            description = uiState.propertyDescription,
            onValueChange = {
                viewModel.onDescriptionChange(it)
            }
        )

        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, start = 12.dp, end = 12.dp)
        ) {
            Text(
                text = "Post",
            )
        }
    }
}