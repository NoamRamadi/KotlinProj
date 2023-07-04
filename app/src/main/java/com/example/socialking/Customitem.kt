package com.example.socialking

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

import com.example.socialking.models.Person
import com.example.socialking.ui.theme.SocialKingTheme
import com.example.socialking.ui.theme.Typography

@Composable
fun CustomItem( person : Person) {

    Row(
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxWidth()
            .border(12.dp, Color.Red)
            .padding(24.dp),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "${person.firstName}",
            color = Color.Black,
            fontSize = Typography.h5.fontSize,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = "${person.lastName}",
            color = Color.Black,
            fontSize = Typography.h5.fontSize,
            fontWeight = FontWeight.Normal
        )
        Text(
        text = "${person.age}",
        color = Color.Black,
        fontSize = Typography.h4.fontSize,
        fontWeight = FontWeight.Bold
        )
    }

}
@Preview
@Composable
fun CustomItemPreview() {
        CustomItem(
            person = Person(
                id = 0,
                firstName = "noam",
                lastName = "ramadi",
                age = 25
            )
        )
}
