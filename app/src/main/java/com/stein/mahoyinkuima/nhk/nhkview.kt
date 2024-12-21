package com.stein.mahoyinkuima.nhk

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NhkNews.NewsView(dp: PaddingValues? = null) {
    val glModifier =
            Modifier.padding(all = 8.dp).fillMaxSize().let done@{
                if (dp == null) return@done it
                it.padding(dp)
            }
    Card(modifier = glModifier) { Text(title) }
}
