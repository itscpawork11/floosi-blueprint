package com.floosi.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.floosi.ui.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.floosi.security.crypto.EncryptionManager
import dagger.hilt.android.EntryPointAccessors

@Composable
fun PinSetupScreen(
    onComplete: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val appContext = context.applicationContext
    val encryptionManager = EntryPointAccessors.fromApplication(
        appContext,
        PinSetupEntryPoint::class.java
    ).encryptionManager()

    var step by remember { mutableStateOf(0) }
    var firstPin by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var biometricEnabled by remember { mutableStateOf(true) }
    val pinLength = 4

    fun handleDigit(digit: Char) {
        if (pin.length >= pinLength) return
        pin += digit
        isError = false
        errorMessage = ""
        if (pin.length == pinLength) {
            if (step == 0) {
                firstPin = pin
                pin = ""
                step = 1
            } else {
                if (pin == firstPin) {
                    encryptionManager.savePinHash(pin)
                    encryptionManager.setBiometricEnabled(biometricEnabled)
                    onComplete()
                } else {
                    isError = true
                    errorMessage = stringResource(R.string.pin_mismatch)
                    pin = ""
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (step == 0) "👤 إنشاء PIN" else "👤 تأكيد PIN",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (step == 0) stringResource(R.string.pin_choose)
            else stringResource(R.string.pin_confirm),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pinLength) { index ->
                val isFilled = index < pin.length
                val color = when {
                    isError -> MaterialTheme.colorScheme.error
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
        AnimatedVisibility(visible = isError, enter = fadeIn(), exit = fadeOut()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        if (step == 0) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = stringResource(R.string.pin_use_biometric), style = MaterialTheme.typography.bodyLarge)
                }
                Switch(
                    checked = biometricEnabled,
                    onCheckedChange = { biometricEnabled = it }
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
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
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                                .clickable { handleDigit(digit) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = digit.toString(), fontSize = 28.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(modifier = Modifier.size(80.dp))
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .clickable { handleDigit('0') },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "0", fontSize = 28.sp, fontWeight = FontWeight.Medium)
                }
                IconButton(
                    onClick = { if (pin.isNotEmpty()) pin = pin.dropLast(1) },
                    modifier = Modifier.size(80.dp)
                ) {
                    Text(text = "⌫", fontSize = 24.sp)
                }
            }
        }
    }
}

interface PinSetupEntryPoint {
    fun encryptionManager(): EncryptionManager
}
