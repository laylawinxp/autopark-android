package com.example.autopark.ui.screens.auto

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.autopark.dto.AutoDto
import com.example.autopark.dto.AutoPersonnelDto
import com.example.autopark.ui.screens.EditDeleteButtons
import com.example.autopark.ui.screens.FieldBaseText
import com.example.autopark.ui.screens.isAdmin

@Composable
fun AutoGridItem(
    auto: AutoDto,
    autoPersonnelList: List<AutoPersonnelDto>,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val personnel = autoPersonnelList.find { it.id == auto.personalId }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FieldBaseText(value = auto.num, modifier = Modifier.width(90.dp).align(Alignment.CenterVertically))
                FieldBaseText(value = auto.mark, modifier = Modifier.width(100.dp).align(Alignment.CenterVertically))
                FieldBaseText(value = auto.color, modifier = Modifier.width(150.dp).align(Alignment.CenterVertically))
                FieldBaseText(
                    value = personnel?.let { "${it.firstName} ${it.lastName}" } ?: "Unknown",
                    modifier = Modifier.width(200.dp).align(Alignment.CenterVertically)
                )

                if (isAdmin) {
                    EditDeleteButtons(true, onEditClick, onDeleteClick)
                }
            }
        }
    }
}
