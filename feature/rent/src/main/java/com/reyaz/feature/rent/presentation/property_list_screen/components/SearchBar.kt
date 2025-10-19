package com.reyaz.feature.rent.presentation.property_list_screen.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchText: String,
    onSearchChange: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    maxLength: Int = 30,
) {
    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        value = searchText,
        onValueChange = {
            if (it.length <= maxLength) {
                onSearchChange(it)
            }
        },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(fontSize = 12.sp),
        keyboardOptions = KeyboardOptions.Companion.Default.copy(imeAction = ImeAction.Companion.Search),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchTriggered() }
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .heightIn(min = 40.dp)
    ) { innerTextField ->

        TextFieldDefaults.DecorationBox(
            value = searchText,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = true,
            placeholder = {
                Text(
                    text = searchText.ifEmpty { "Search property" },
                    fontSize = 13.sp,
                    color = Color.Companion.Gray
                )
            },
            visualTransformation = VisualTransformation.Companion.None,
            interactionSource = interactionSource,

            contentPadding = PaddingValues(vertical = 2.dp, horizontal = 16.dp),

            container = {
                OutlinedTextFieldDefaults.ContainerBox(
                    enabled = true,
                    isError = false,
                    interactionSource = interactionSource,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedBorderColor = Color.Companion.LightGray,
                        focusedBorderColor = Color.Companion.LightGray
                    ),
                    shape = RoundedCornerShape(16.dp),
                    focusedBorderThickness = 1.dp,
                    unfocusedBorderThickness = 1.dp,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        )
    }
}