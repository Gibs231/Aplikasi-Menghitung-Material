package com.gibraltar0123.materialapp.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.gibraltar0123.materialapp.R
import com.gibraltar0123.materialapp.model.MaterialOption
import com.gibraltar0123.materialapp.navigation.Screen
import com.gibraltar0123.materialapp.viewmodel.MaterialViewModel
import com.gibraltar0123.materialapp.util.MaterialViewModelFactory
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, viewModel: MaterialViewModel) {
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
                .padding(innerPadding),
            viewModel = viewModel
        )
    }
}

@Composable
fun CheckboxParentExample(
    modifier: Modifier = Modifier,
    viewModel: MaterialViewModel
) {
    val context = LocalContext.current
    val materialOptions by viewModel.allMaterials.collectAsState(initial = emptyList())

    // Sinkronisasi state list
    val childCheckedStates = rememberSaveableMutableStateList(List(materialOptions.size) { false })
    val packageCounts = rememberSaveableMutableStateList(List(materialOptions.size) { 0 })
    var showTotal by rememberSaveable { mutableStateOf(false) }

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
                    childCheckedStates.indices.forEach { index ->
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
                    var text by rememberSaveable { mutableStateOf(packageCounts[index].toString()) }
                    var isError by rememberSaveable { mutableStateOf(false) }

                    TextField(
                        value = text,
                        onValueChange = { newText ->
                            if (newText.isEmpty() || newText.toIntOrNull() != null) {
                                text = newText
                                when (val number = newText.toIntOrNull()) {
                                    null -> {
                                        packageCounts[index] = 0
                                        isError = false
                                    }
                                    in 1..10000 -> {
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

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { showTotal = true }) {
            Text("Hitung Total")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (showTotal) {
            val totalPrice = materialOptions.indices.sumOf { index ->
                if (childCheckedStates[index]) materialOptions[index].pricePerPackage * packageCounts[index] else 0.0
            }

            Text("Rincian Pembelian:", fontSize = 18.sp, color = Color.DarkGray)

            val resultBuilder = StringBuilder("Rincian Pembelian:\n")
            materialOptions.forEachIndexed { index, material ->
                if (childCheckedStates[index] && packageCounts[index] > 0) {
                    val subtotal = material.pricePerPackage * packageCounts[index]
                    val detail = "- ${material.name} x ${packageCounts[index]} = Rp ${"%,.0f".format(subtotal)}"
                    Text(detail, fontSize = 14.sp)
                    resultBuilder.appendLine(detail)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            val totalLine = "Total Harga: Rp ${"%,.0f".format(totalPrice)}"
            Text(totalLine, fontSize = 20.sp, color = Color(0xFF4CAF50))
            resultBuilder.appendLine(totalLine)

            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                shareData(context, resultBuilder.toString())
            }) {
                Text("Bagikan")
            }
        }

        if (childCheckedStates.all { it }) {
            Text(stringResource(id = R.string.all_options_selected))
        }
    }
}

@Composable
fun <T> rememberSaveableMutableStateList(initialList: List<T>): SnapshotStateList<T> {
    return rememberSaveable(
        saver = listSaver(
            save = { it.toList() },
            restore = { it.toMutableStateList() }
        )
    ) {
        initialList.toMutableStateList()
    }
}

@SuppressLint("QueryPermissionsNeeded")
private fun shareData(context: Context, message: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    val chooser = Intent.createChooser(intent, "Bagikan melalui")
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(chooser)
    }
}
