package com.floosi.feature.lock

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.floosi.feature.lock.ui.LockIntent
import com.floosi.feature.lock.ui.LockViewModel

@Composable
fun LockScreen(
    onUnlocked: () -> Unit,
    onSetupPin: () -> Unit,
    viewModel: LockViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.setOnUnlocked(onUnlocked)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.lock_app_locked),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (viewModel.isPinSet) stringResource(R.string.lock_enter_pin)
            else stringResource(R.string.lock_setup_pin_prompt),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        if (viewModel.isPinSet) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(4) { index ->
                    val isFilled = index < state.pin.length
                    val color = when {
                        state.isError -> MaterialTheme.colorScheme.error
                        isFilled -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                    Surface(
                        modifier = Modifier.size(16.dp),
                        shape = CircleShape,
                        color = color
                    ) {}
                }
            }

            if (state.isCooldown) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.lock_cooldown, state.cooldownRemaining),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (state.isError && state.errorMessageRes != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(state.errorMessageRes),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        if (state.isBiometricAvailable && viewModel.isPinSet) {
            Spacer(modifier = Modifier.height(24.dp))
            IconButton(
                onClick = { viewModel.onIntent(LockIntent.AuthenticateBiometric) }
            ) {
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = stringResource(R.string.lock_biometric_hint),
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (viewModel.isPinSet) {
            NumberPad(
                onDigitClick = { viewModel.onIntent(LockIntent.AddDigit(it)) },
                onDelete = { viewModel.onIntent(LockIntent.DeleteDigit) },
                enabled = !state.isCooldown
            )
        } else {
            TextButton(onClick = onSetupPin) {
                Text(
                    text = stringResource(R.string.lock_setup_pin),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun NumberPad(
    onDigitClick: (Char) -> Unit,
    onDelete: () -> Unit,
    enabled: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        for (row in listOf(listOf('1', '2', '3'), listOf('4', '5', '6'), listOf('7', '8', '9'))) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { digit ->
                    val color = if (enabled) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.surfaceVariant
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .clickable(enabled = enabled) { onDigitClick(digit) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = digit.toString(),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Medium,
                            color = color
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(modifier = Modifier.size(80.dp))
            val color = if (enabled) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.surfaceVariant
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .clickable(enabled = enabled) { onDigitClick('0') },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "0",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                    color = color
                )
            }
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .clickable(enabled = enabled) { onDelete() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⌫",
                    fontSize = 24.sp,
                    color = color
                )
            }
        }
    }
}
