package com.reyaz.feature.rent.presentation.property_post_screen.components



import androidx.compose.foundation.border
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LocationTextField(
    location: String,
    title: String,
    onValueChange:(String)->Unit,
    modifier: Modifier
){
    TextField(
        modifier=modifier
            .border(
                1.dp,Color.Gray
            ),
        value = location,
        onValueChange={
            onValueChange(it)
        },
        label = {
            Text(text=title)
        }, placeholder = {
            Text(text=title)
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background, // 👈 background same as screen
            unfocusedContainerColor =MaterialTheme.colorScheme.background,
            disabledContainerColor =MaterialTheme.colorScheme.background,
            focusedIndicatorColor = Color.Transparent,  // 👈 remove default underline
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
}