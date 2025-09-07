package com.therxmv.base.ui.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.therxmv.base.ui.theme.PromovaTheme

@Composable
fun ErrorContainer(
    message: String = "Something went wrong!",
    onRetry: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(PromovaTheme.paddings.main),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
        )

        if (onRetry != null) {
            Spacer(modifier = Modifier.height(PromovaTheme.paddings.main))

            RetryButton(onClick = onRetry)
        }
    }
}

@Composable
fun RetryButton(
    size: Dp = 75.dp,
    onClick: () -> Unit,
) {
    FilledIconButton(
        modifier = Modifier.size(size),
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorContainerNoRetryPreview() {
    PromovaTheme {
        ErrorContainer(message = "Something went wrong!")
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorContainerRetryPreview() {
    PromovaTheme {
        ErrorContainer(message = "Something went wrong. Please try again!", onRetry = {})
    }
}