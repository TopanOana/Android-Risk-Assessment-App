package com.example.androidproject.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidproject.AndroidProjectTheme
import com.example.androidproject.ScanViewModel
import com.example.androidproject.lightGreenColor
import com.example.androidproject.lightRedColor
import com.example.androidproject.lightYellowColor
import com.example.androidproject.ui.theme.Typography

//@Preview
@Composable
fun MainScreen(viewModel: ScanViewModel, onScanClick: (Boolean) -> Unit) {
    val progress = viewModel.uiState.collectAsState().value.inProgress
    AndroidProjectTheme {
        Surface(
            color = MaterialTheme.colorScheme.inverseSurface,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Scan applications",
                    style = Typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Row(horizontalArrangement = Arrangement.End) {
                    Button(onClick = {
                        onScanClick(false)
                    }) {
                        Text(
                            text = "Scan apps!",
                            style = Typography.bodyMedium,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Button(onClick = {
                        onScanClick(true)
                    }) {
                        Text(
                            text = "Scan with system apps!",
                            style = Typography.bodyMedium,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
                CircularIndeterminateProgressBar(isDisplayed = progress)
                ListApplications(viewModel)
            }
        }
    }
}


@Composable
fun ListApplications(viewModel: ScanViewModel) {
    val riskApps =
        viewModel.uiState.collectAsState().value.listRiskApps

    LazyColumn {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "risk",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 20.dp),
//                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    "apps",
//                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(3f)
                        .padding(horizontal = 20.dp),
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
        items(items = riskApps) {
            ItemCard(it)
        }
    }
}


@Composable
fun getBackgroundColor(status: String): Color {
    return when (status) {
        "low" -> lightGreenColor
        "medium" -> lightYellowColor
        "high" -> lightRedColor
        else -> Color.Gray
    }
}

@Composable
fun ItemCard(currentPair: Pair<List<String>, String>) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = currentPair.second,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Monospace
            )

            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(horizontal = 20.dp)
            ) {
                currentPair.first.forEach {
                    Text(it, fontFamily = FontFamily.Monospace)
                }
            }
        }
    }

}

@Composable
//@Preview
fun ItemForList() {
    val currentPair = Pair(listOf("package1", "package2", "package7"), "low")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = currentPair.second,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Column(
            modifier = Modifier.weight(3f)
        ) {
            currentPair.first.forEach {
                Text(it)
            }
        }
    }

}

@Composable
fun CircularIndeterminateProgressBar(isDisplayed: Boolean) {
    if (isDisplayed) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
