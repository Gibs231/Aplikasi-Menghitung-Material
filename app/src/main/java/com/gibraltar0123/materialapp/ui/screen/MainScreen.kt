package com.gibraltar0123.materialapp.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.gibraltar0123.materialapp.R
import com.gibraltar0123.materialapp.model.MaterialOption
import com.gibraltar0123.materialapp.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF795548),
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {

                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.about_application),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        CheckboxParentExample(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@Composable
fun CheckboxParentExample(modifier: Modifier = Modifier) {
    val materialOptions = listOf(
        MaterialOption(name = "Semen", imageResId = R.drawable.semen, pricePerPackage = 50000.0),
        MaterialOption(name = "Kayu", imageResId = R.drawable.kayu, pricePerPackage = 100000.0),
        MaterialOption(name = "BatuBata", imageResId = R.drawable.batamerah, pricePerPackage = 20000.0)
    )

    val childCheckedStates = remember { mutableStateListOf(false, false, false) }
    val packageCounts = remember { mutableStateListOf(0, 0, 0) }

    var showTotal by remember { mutableStateOf(false) }

    val parentState = when {
        childCheckedStates.all { it } -> androidx.compose.ui.state.ToggleableState.On
        childCheckedStates.none { it } -> androidx.compose.ui.state.ToggleableState.Off
        else -> androidx.compose.ui.state.ToggleableState.Indeterminate
    }

    val scrollState = rememberScrollState()

    Column(modifier = modifier.verticalScroll(scrollState).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(id = R.string.select_all))
            TriStateCheckbox(
                state = parentState,
                onClick = {
                    val newState = parentState != androidx.compose.ui.state.ToggleableState.On
                    childCheckedStates.forEachIndexed { index, _ ->
                        childCheckedStates[index] = newState
                        if (!newState) packageCounts[index] = 0
                    }
                }
            )
        }

        materialOptions.forEachIndexed { index, material ->
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = material.imageResId),
                        contentDescription = material.name,
                        modifier = Modifier.size(180.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(material.name)
                    Checkbox(
                        checked = childCheckedStates[index],
                        onCheckedChange = { isChecked ->
                            childCheckedStates[index] = isChecked
                            if (!isChecked) packageCounts[index] = 0
                        }
                    )
                }

                if (childCheckedStates[index]) {
                    var text by remember { mutableStateOf(packageCounts[index].toString()) }
                    var isError by remember { mutableStateOf(false) }

                    TextField(
                        value = text,
                        onValueChange = { newText ->
                            if (newText.isEmpty() || newText.toIntOrNull() != null) {
                                text = newText
                                val number = newText.toIntOrNull()
                                when {
                                    number == null -> {
                                        packageCounts[index] = 0
                                        isError = false
                                    }
                                    number in 1..10000 -> {
                                        packageCounts[index] = number
                                        isError = false
                                    }
                                    else -> {
                                        isError = true
                                    }
                                }
                            }
                        },
                        label = { Text(stringResource(id = R.string.package_count_label)) },
                        modifier = Modifier.width(180.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = isError
                    )

                    if (isError) {
                        Text(
                            text = stringResource(id = R.string.input_exceeded_total_package),
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))


        androidx.compose.material3.Button(
            onClick = { showTotal = true }
        ) {
            Text("Hitung Total")
        }

        Spacer(modifier = Modifier.padding(8.dp))


        if (showTotal) {
            val totalPrice = materialOptions.indices.sumOf { index ->
                if (childCheckedStates[index]) materialOptions[index].pricePerPackage * packageCounts[index] else 0.0
            }

            Text("🧾 Rincian Pembelian:", fontSize = 18.sp, color = Color.DarkGray)

            materialOptions.forEachIndexed { index, material ->
                if (childCheckedStates[index] && packageCounts[index] > 0) {
                    val subtotal = material.pricePerPackage * packageCounts[index]
                    Text(
                        "- ${material.name} x ${packageCounts[index]} = Rp ${"%,.0f".format(subtotal)}",
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Total Harga: Rp ${"%,.0f".format(totalPrice)}",
                fontSize = 20.sp,
                color = Color(0xFF4CAF50)
            )
        }

        if (childCheckedStates.all { it }) {
            Text(stringResource(id = R.string.all_options_selected))
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainScreen(rememberNavController())
    }
}