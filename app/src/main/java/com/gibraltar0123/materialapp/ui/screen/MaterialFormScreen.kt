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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    viewModel: MaterialViewModel = viewModel(), // Use viewModel() to obtain the ViewModel
    materialId: Long? = null
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var name by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var stock by rememberSaveable { mutableStateOf("") }

    val imageOptions = listOf(
        R.drawable.batamerah to "Bata",
        R.drawable.semen to "Semen",
        R.drawable.kayu to "Kayu"
    )

    var selectedImageResId by rememberSaveable { mutableStateOf(imageOptions.first().first) }

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (materialId == null) "Tambah Material" else "Edit Material",
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
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
            // Material name field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama Material") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Price field
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Harga per Paket (Rp)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Stock field
            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stok Paket") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Image selection
            Text(
                "Pilih Gambar Material",
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp)
                    .align(Alignment.Start)
            )

            // Display image options as radio buttons
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

            // Save button
            Button(
                onClick = {
                    val priceValue = price.toDoubleOrNull() ?: 0.0
                    val stockValue = stock.toIntOrNull() ?: 0

                    scope.launch {
                        if (materialId == null) {
                            // Insert new material
                            viewModel.insert(name, priceValue, stockValue, selectedImageResId)
                        } else {
                            // Update existing material
                            viewModel.update(materialId, name, priceValue, stockValue, selectedImageResId)
                        }
                        // Navigate back
                        navController.popBackStack()
                    }
                },
                enabled = name.isNotBlank() && price.isNotBlank() && stock.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(if (materialId == null) "Simpan Material" else "Update Material")
            }

            // Delete button (only show when editing)
            if (materialId != null) {
                Button(
                    onClick = {
                        scope.launch {
                            val material = viewModel.getMaterialById(materialId)
                            material?.let {
                                viewModel.delete(it)
                                navController.popBackStack()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("Hapus Material")
                }
            }
        }
    }
}