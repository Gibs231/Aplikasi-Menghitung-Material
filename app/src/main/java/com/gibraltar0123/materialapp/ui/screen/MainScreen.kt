package com.gibraltar0123.materialapp.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.gibraltar0123.materialapp.R
import com.gibraltar0123.materialapp.navigation.Screen
import com.gibraltar0123.materialapp.viewmodel.MaterialViewModel

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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddMaterial.route)
                },
                containerColor = Color(0xFF4CAF50)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Material",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        CheckboxParentExample(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            viewModel = viewModel,
            navController = navController
        )
    }
}

@Composable
fun CheckboxParentExample(
    modifier: Modifier = Modifier,
    viewModel: MaterialViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val materialOptions by viewModel.allMaterials.collectAsState(initial = emptyList())

    // Manage checked states
    val childCheckedStates = remember(materialOptions.size) {
        mutableStateListOf<Boolean>().apply {
            repeat(materialOptions.size) { add(false) }
        }
    }

    // Manage package counts
    val packageCounts = remember(materialOptions.size) {
        mutableStateListOf<Int>().apply {
            repeat(materialOptions.size) { add(0) }
        }
    }

    var showTotal by rememberSaveable { mutableStateOf(false) }
    var totalPrice by rememberSaveable { mutableStateOf(0.0) }

    val parentState = when {
        childCheckedStates.all { it } -> androidx.compose.ui.state.ToggleableState.On
        childCheckedStates.none { it } -> androidx.compose.ui.state.ToggleableState.Off
        else -> androidx.compose.ui.state.ToggleableState.Indeterminate
    }

    val scrollState = rememberScrollState()

    Column(modifier = modifier.verticalScroll(scrollState).padding(16.dp)) {
        if (materialOptions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Belum ada material tersimpan.\nTekan tombol + untuk menambahkan material.",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        } else {
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
                            modifier = Modifier
                                .size(180.dp)
                                .clickable {
                                    navController.navigate(Screen.EditMaterial.createRoute(material.id))
                                }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(material.name)
                            Text(
                                "Rp ${"%,.0f".format(material.pricePerPackage)} / paket",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                            Text(
                                "Stok: ${material.stockPackage} paket",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
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
                                        in 1..material.stockPackage -> {
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
                                text = "Jumlah melebihi stok yang tersedia (${material.stockPackage})",
                                color = Color.Red,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    totalPrice = materialOptions.indices.sumOf { index ->
                        if (childCheckedStates[index]) {
                            materialOptions[index].pricePerPackage * packageCounts[index]
                        } else 0.0
                    }
                    showTotal = true
                },
                enabled = childCheckedStates.any { it } && childCheckedStates.mapIndexed { index, checked ->
                    checked && packageCounts.getOrNull(index)?.let { it > 0 } ?: false
                }.any { it }
            ) {
                Text("Hitung Total")
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (showTotal) {
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

            if (childCheckedStates.isNotEmpty() && childCheckedStates.all { it }) {
                Text(stringResource(id = R.string.all_options_selected))
            }
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