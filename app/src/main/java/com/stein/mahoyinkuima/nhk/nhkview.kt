package com.stein.mahoyinkuima.nhk

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun NhkNews.newsView(dp: PaddingValues? = null, onClicked: () -> Unit = {}) {
    val glModifier =
            Modifier.padding(all = 8.dp).fillMaxSize().let done@{
                if (dp == null) return@done it
                it.padding(dp)
            }
    Card(modifier = glModifier.clickable { onClicked() }) {
        Row {
            AsyncImage(
                    model = imageUrl,
                    contentDescription = "image",
                    modifier = Modifier.width(120.dp).height(100.dp)
            )
            Text(title)
        }
    }
}
