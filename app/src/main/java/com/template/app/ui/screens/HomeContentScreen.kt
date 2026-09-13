package com.template.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeContentScreen(
    onNavigateToQrScanner: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Home,
            contentDescription = "Home",
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Home Screen",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNavigateToQrScanner,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Abrir Scanner de QR Code")
        }
    }
}

@Composable
fun HomeContentScreenPreview() {
    com.template.app.ui.theme.TemplateAppTheme {
        HomeContentScreen(onNavigateToQrScanner = {})
    }
}