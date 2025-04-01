package com.gibraltar0123.materialapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gibraltar0123.materialapp.ui.theme.MaterialAppTheme
import com.gibraltar0123.materialapp.model.MaterialOption


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
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
                    containerColor = Color(0xFF795548)
                )
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
        MaterialOption(
            name = "BatuBata",
            imageResId = R.drawable.batamerah,
            pricePerPackage = 20000.0
        )
    )


    val childCheckedStates = remember { mutableStateListOf(false, false, false) }
    val packageCounts = remember { mutableStateListOf(0, 0, 0) }

    val parentState = when {
        childCheckedStates.all { it } -> androidx.compose.ui.state.ToggleableState.On
        childCheckedStates.none { it } -> androidx.compose.ui.state.ToggleableState.Off
        else -> androidx.compose.ui.state.ToggleableState.Indeterminate
    }

    val totalPrice = materialOptions.indices.sumOf { index ->
        if (childCheckedStates[index]) materialOptions[index].pricePerPackage * packageCounts[index] else 0.0
    }

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Select all")
            TriStateCheckbox(
                state = parentState,
                onClick = {
                    val newState = parentState != androidx.compose.ui.state.ToggleableState.On
                    childCheckedStates.forEachIndexed { index, _ ->
                        childCheckedStates[index] = newState
                    }
                }
            )
        }


        materialOptions.forEachIndexed { index, material ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
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
                    }
                )
            }
        }
    }

    if (childCheckedStates.all { it }) {
        Text("All options selected")
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MaterialAppTheme {
        MainScreen()
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}
