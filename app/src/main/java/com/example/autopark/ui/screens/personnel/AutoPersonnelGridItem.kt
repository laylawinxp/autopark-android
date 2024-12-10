package com.example.autopark.ui.screens.personnel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.example.autopark.dto.AutoPersonnelDto
import com.example.autopark.ui.screens.EditDeleteButtons
import com.example.autopark.ui.screens.FieldBaseText
import com.example.autopark.ui.screens.isAdmin

@Composable
fun AutoPersonnelGridItem(
    personnel: AutoPersonnelDto,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
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
                FieldBaseText(value = personnel.firstName, modifier = Modifier.width(120.dp).align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(4.dp))
                FieldBaseText(value = personnel.fatherName, modifier = Modifier.width(150.dp).align(Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(4.dp))
                FieldBaseText(value = personnel.lastName, modifier = Modifier.width(120.dp).align(Alignment.CenterVertically))

                if (isAdmin) {
                    EditDeleteButtons(true, onEditClick, onDeleteClick)
                }
            }
        }
    }
}
