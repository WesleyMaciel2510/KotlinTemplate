package com.template.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerSheet(
    onDismissRequest: () -> Unit,
    onApplyDateRange: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    var startDate by remember { mutableStateOf("01/09/2026") }
    var endDate by remember { mutableStateOf("30/09/2026") }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = "Filtrar por Período",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = startDate,
                onValueChange = { startDate = it },
                label = "Data Inicial",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            AppTextField(
                value = endDate,
                onValueChange = { endDate = it },
                label = "Data Final",
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                AppButton(
                    text = "Cancelar",
                    onClick = onDismissRequest,
                    outlined = true,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                AppButton(
                    text = "Aplicar Filtro",
                    onClick = {
                        onApplyDateRange(startDate, endDate)
                        onDismissRequest()
                    },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun DateRangePickerSheetPreview() {
    com.template.app.ui.theme.TemplateAppTheme {
        DateRangePickerSheet(
            onDismissRequest = {},
            onApplyDateRange = { _, _ -> }
        )
    }
}
