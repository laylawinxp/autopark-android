package  com.example.autopark.ui.screens.auto

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.autopark.dto.AutoDto
import com.example.autopark.dto.AutoPersonnelDto

@Composable
fun EditAutoForm(
    auto: AutoDto,
    autoPersonnelList: List<AutoPersonnelDto>,
    onSave: (AutoDto) -> Unit,
    onCancel: () -> Unit
) {
    var num by remember { mutableStateOf(auto.num) }
    var color by remember { mutableStateOf(auto.color) }
    var mark by remember { mutableStateOf(auto.mark) }
    var personalId by remember { mutableIntStateOf(auto.personalId) }

    var expanded by remember { mutableStateOf(false) }
    var selectedPersonnelName by remember { mutableStateOf("") }

    fun onPersonnelSelected(personnel: AutoPersonnelDto) {
        selectedPersonnelName = "${personnel.firstName} ${personnel.lastName}"
        personalId = personnel.id
        expanded = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Enter Auto Details",
                    fontSize = 19.sp,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                TextField(
                    value = num,
                    onValueChange = { num = it },
                    label = { Text("Number") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                TextField(
                    value = color,
                    onValueChange = { color = it },
                    label = { Text("Color") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                TextField(
                    value = mark,
                    onValueChange = { mark = it },
                    label = { Text("Mark") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textStyle = MaterialTheme.typography.bodyMedium
                )

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        TextField(
                            value = selectedPersonnelName,
                            onValueChange = { },
                            label = { Text("Personnel") },
                            readOnly = true,
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.Transparent)
                                .clickable { expanded = !expanded }
                                .padding(16.dp),
                            textStyle = MaterialTheme.typography.bodyMedium
                        )

                        IconButton(
                            onClick = { expanded = !expanded },
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = if (expanded) android.R.drawable.arrow_down_float else android.R.drawable.arrow_up_float),
                                contentDescription = "Toggle Personnel Dropdown"
                            )
                        }
                    }

                    if (autoPersonnelList.isNotEmpty() && expanded) {
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopStart)
                                .padding(top = 8.dp)
                        ) {
                            autoPersonnelList.forEach { personnel ->
                                DropdownMenuItem(
                                    text = { Text("${personnel.firstName} ${personnel.lastName}") },
                                    onClick = { onPersonnelSelected(personnel) }
                                )
                            }
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Button(onClick = { onSave(AutoDto(auto.id, num, color, mark, personalId)) }) {
                        Text("Save")
                    }
                    Button(onClick = onCancel) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}
