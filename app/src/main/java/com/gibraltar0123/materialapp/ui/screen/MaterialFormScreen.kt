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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    val snackbarHostState = remember { SnackbarHostState() }


    val addMaterialTitle = stringResource(R.string.add_material)
    val editMaterialTitle = stringResource(R.string.edit_material)
    val backText = stringResource(R.string.back)
    val materialNameLabel = stringResource(R.string.material_name)
    val pricePerPackageLabel = stringResource(R.string.price_per_package)
    val stockPackageLabel = stringResource(R.string.stock_package)
    val selectMaterialImageText = stringResource(R.string.select_material_image)
    val saveMaterialText = stringResource(R.string.save_material)
    val updateMaterialText = stringResource(R.string.update_material)
    val deleteMaterialText = stringResource(R.string.delete_material)
    val deleteConfirmationTitle = stringResource(R.string.delete_confirmation)
    val cancelButtonText = stringResource(R.string.cancel)
    val deleteButtonText = stringResource(R.string.delete)
    val errorSavingData = stringResource(R.string.error_saving_data)

    val errorNameRequired = stringResource(R.string.error_name_required)
    val errorPriceRequired = stringResource(R.string.error_price_required)
    val errorPriceInvalidFormat = stringResource(R.string.error_price_invalid_format)
    val errorPriceNegative = stringResource(R.string.error_price_negative)
    val errorStockRequired = stringResource(R.string.error_stock_required)
    val errorStockInvalidFormat = stringResource(R.string.error_stock_invalid_format)
    val errorStockNegative = stringResource(R.string.error_stock_negative)

    var name by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var stock by rememberSaveable { mutableStateOf("") }


    var nameError by rememberSaveable { mutableStateOf<String?>(null) }
    var priceError by rememberSaveable { mutableStateOf<String?>(null) }
    var stockError by rememberSaveable { mutableStateOf<String?>(null) }

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


    fun validateName(): Boolean {
        return if (name.isBlank()) {
            nameError = errorNameRequired
            false
        } else {
            nameError = null
            true
        }
    }

    fun validatePrice(): Boolean {
        return when {
            price.isBlank() -> {
                priceError = errorPriceRequired
                false
            }

            price.toDoubleOrNull() == null -> {
                priceError = errorPriceInvalidFormat
                false
            }

            price.toDoubleOrNull()!! < 0 -> {
                priceError = errorPriceNegative
                false
            }

            else -> {
                priceError = null
                true
            }
        }
    }

    fun validateStock(): Boolean {
        return when {
            stock.isBlank() -> {
                stockError = errorStockRequired
                false
            }

            stock.toIntOrNull() == null -> {
                stockError = errorStockInvalidFormat
                false
            }

            stock.toIntOrNull()!! < 0 -> {
                stockError = errorStockNegative
                false
            }

            else -> {
                stockError = null
                true
            }
        }
    }

    fun validateAllFields(): Boolean {
        val isNameValid = validateName()
        val isPriceValid = validatePrice()
        val isStockValid = validateStock()
        return isNameValid && isPriceValid && isStockValid
    }

    if (showDeleteConfirmation) {

        val deleteConfirmationMessage = stringResource(R.string.delete_confirmation_message, name)

        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text(deleteConfirmationTitle) },
            text = { Text(deleteConfirmationMessage) },
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
                    Text(deleteButtonText, color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text(cancelButtonText)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (materialId == null) addMaterialTitle else editMaterialTitle,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = backText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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
                onValueChange = {
                    name = it
                    nameError = null
                },
                label = { Text(materialNameLabel) },
                isError = nameError != null,
                supportingText = { nameError?.let { Text(it) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = price,
                onValueChange = {
                    price = it
                    priceError = null
                },
                label = { Text(pricePerPackageLabel) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = priceError != null,
                supportingText = { priceError?.let { Text(it) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = stock,
                onValueChange = {
                    stock = it
                    stockError = null
                },
                label = { Text(stockPackageLabel) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = stockError != null,
                supportingText = { stockError?.let { Text(it) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Text(
                selectMaterialImageText,
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
                    if (validateAllFields()) {
                        val priceValue = price.toDouble()
                        val stockValue = stock.toInt()

                        scope.launch {
                            try {
                                if (materialId == null) {
                                    viewModel.insert(
                                        name,
                                        priceValue,
                                        stockValue,
                                        selectedImageResId
                                    )
                                } else {
                                    viewModel.update(
                                        materialId,
                                        name,
                                        priceValue,
                                        stockValue,
                                        selectedImageResId
                                    )
                                }
                                navController.popBackStack()
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar(errorSavingData)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(if (materialId == null) saveMaterialText else updateMaterialText)
            }

            if (materialId != null) {
                Button(
                    onClick = {
                        showDeleteConfirmation = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(deleteMaterialText)
                }
            }
        }
    }
}