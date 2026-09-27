package com.template.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.template.app.domain.model.RecebivelFilterOption

@Composable
fun FilterChipRow(
    selectedOption: RecebivelFilterOption,
    onOptionSelected: (RecebivelFilterOption) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = RecebivelFilterOption.entries

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(options) { option ->
            val isSelected = option == selectedOption
            val (icon, label) = when (option) {
                RecebivelFilterOption.Todos -> Icons.Default.FilterList to "Todos"
                RecebivelFilterOption.Pendentes -> Icons.Default.Schedule to "Pendentes"
                RecebivelFilterOption.Pagos -> Icons.Default.CheckCircle to "Pagos"
                RecebivelFilterOption.Vencidos -> Icons.Default.Error to "Vencidos"
            }

            FilterChip(
                selected = isSelected,
                onClick = { onOptionSelected(option) },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Preview
@Composable
private fun FilterChipRowPreview() {
    com.template.app.ui.theme.TemplateAppTheme {
        FilterChipRow(
            selectedOption = RecebivelFilterOption.Todos,
            onOptionSelected = {}
        )
    }
}
