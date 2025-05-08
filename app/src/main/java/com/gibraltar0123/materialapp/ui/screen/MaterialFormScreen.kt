package com.gibraltar0123.materialapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.gibraltar0123.materialapp.R
import com.gibraltar0123.materialapp.viewmodel.MaterialViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialFormScreen(
    navController: NavHostController,
    viewModel: MaterialViewModel = viewModel(),
    materialId: Long? = null
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var name by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var stock by rememberSaveable { mutableStateOf("") }


    var showDeleteConfirmation by rememberSaveable { mutableStateOf(false) }

    val imageOptions = listOf(
        R.drawable.batamerah to "Bata",
        R.drawable.semen to "Semen",
        R.drawable.kayu to "Kayu"
    )

    var selectedImageResId by rememberSaveable { mutableIntStateOf(imageOptions.first().first) }

    LaunchedEffect(materialId) {
        if (materialId != null && materialId > 0) {
            val material = viewModel.getMaterialById(materialId)
            material?.let {
                name = it.name
                price = it.pricePerPackage.toString()
                stock = it.stockPackage.toString()
                selectedImageResId = it.imageResId
            }
        }
    }


    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text(stringResource(R.string.delete_confirmation)) },
            text = { Text(stringResource(R.string.delete_confirmation_message, name)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            val material = viewModel.getMaterialById(materialId ?: 0L)
                            material?.let {
                                viewModel.delete(it)
                                navController.popBackStack()
                            }
                        }
                        showDeleteConfirmation = false
                    }
                ) {
                    Text(stringResource(R.string.delete), color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            if (materialId == null) R.string.add_material else R.string.edit_material
                        ),
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF795548),
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.material_name)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )


            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text(stringResource(R.string.price_per_package)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )


            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text(stringResource(R.string.stock_package)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )


            Text(
                stringResource(R.string.select_material_image),
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp)
                    .align(Alignment.Start)
            )

            imageOptions.forEach { (imageResId, imageName) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = selectedImageResId == imageResId,
                        onClick = { selectedImageResId = imageResId }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(imageName)
                }
            }


            Button(
                onClick = {
                    val priceValue = price.toDoubleOrNull() ?: 0.0
                    val stockValue = stock.toIntOrNull() ?: 0

                    scope.launch {
                        if (materialId == null) {

                            viewModel.insert(name, priceValue, stockValue, selectedImageResId)
                        } else {

                            viewModel.update(materialId, name, priceValue, stockValue, selectedImageResId)
                        }

                        navController.popBackStack()
                    }
                },
                enabled = name.isNotBlank() && price.isNotBlank() && stock.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(stringResource(if (materialId == null) R.string.save_material else R.string.update_material))
            }

            if (materialId != null) {
                Button(
                    onClick = { showDeleteConfirmation = true },  // Show confirmation dialog instead of direct delete
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(stringResource(R.string.delete_material))
                }
            }
        }
    }
}