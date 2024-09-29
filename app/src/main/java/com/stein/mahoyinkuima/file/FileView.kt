package com.stein.mahoyinkuima.file

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

val LEFT_WIDTH_FULL = 125.dp
val RIGHT_WIDTH_FULL = 150.dp

@Composable
fun CpuDataInfo.View() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
                modifier = Modifier.padding(all = 8.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
                Text(
                        text = "HardWare:",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(LEFT_WIDTH_FULL)
                )
                Spacer(modifier = Modifier.width(4.dp))

                Text(text = hardware, modifier = Modifier.width(RIGHT_WIDTH_FULL))
            }
            Row {
                Text(
                        text = "Core:",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(LEFT_WIDTH_FULL)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = coreNumber.toString(), modifier = Modifier.width(RIGHT_WIDTH_FULL))
            }
        }
    }
}

@Composable
fun FelicaData?.View() {
    if (this == null) {
        return Card { Text("This phone do not support felica") }
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
                modifier = Modifier.padding(all = 8.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
                Text(
                        text = "Trouble:",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(LEFT_WIDTH_FULL)
                )
                Spacer(modifier = Modifier.width(4.dp))

                Text(text = trouble, modifier = Modifier.width(RIGHT_WIDTH_FULL))
            }
            Row {
                Text(
                        text = "Region:",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(LEFT_WIDTH_FULL)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = region, modifier = Modifier.width(RIGHT_WIDTH_FULL))
            }
            Row {
                Text(
                        text = "market:",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(LEFT_WIDTH_FULL)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = market, modifier = Modifier.width(RIGHT_WIDTH_FULL))
            }
        }
    }
}

@Composable
fun Memory.View() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
                modifier = Modifier.padding(all = 8.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
                Text(
                        text = "FreeMem:",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(LEFT_WIDTH_FULL)
                )
                Spacer(modifier = Modifier.width(4.dp))

                Text(text = freeMemory, modifier = Modifier.width(RIGHT_WIDTH_FULL))
            }
            Row {
                Text(
                        text = "TotalMem:",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.width(LEFT_WIDTH_FULL)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = totalMemory, modifier = Modifier.width(RIGHT_WIDTH_FULL))
            }
        }
    }
}

@Composable
fun PhoneInfo.View(dp: PaddingValues? = null) {
    val glModifier =
            Modifier.padding(all = 8.dp).fillMaxSize().let done@{
                if (dp == null) return@done it
                it.padding(dp)
            }
    Column(
            modifier = glModifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
    ) {
        Text(
                text = "BaseInfo",
                modifier = Modifier.padding(all = 8.dp),
                fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(15.dp))
        memory.View()
        Spacer(modifier = Modifier.height(10.dp))
        cpuInfo.View()
        Spacer(modifier = Modifier.height(25.dp))
        Text(text = "Felica", modifier = Modifier.padding(all = 8.dp), fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(15.dp))
        felicaData.View()
    }
}
