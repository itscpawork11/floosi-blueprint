package com.floosi.feature.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.floosi.feature.onboarding.screens.FirstWalletScreen
import com.floosi.feature.onboarding.screens.ProfileSetupScreen
import com.floosi.feature.onboarding.screens.WelcomeScreen
import com.floosi.feature.onboarding.ui.OnboardingIntent
import com.floosi.feature.onboarding.ui.OnboardingViewModel

@Composable
fun OnboardingNavHost(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    if (state.isComplete) {
        onComplete()
        return
    }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                val totalSteps = 4
                repeat(totalSteps) { index ->
                    val color = if (index <= state.step)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                    Surface(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .then(
                                if (index == state.step) Modifier.size(32.dp, 8.dp)
                                else Modifier.size(8.dp, 8.dp)
                            ),
                        shape = RoundedCornerShape(4.dp),
                        color = color
                    ) {}
                }
            }
        }
    ) { padding ->
        AnimatedContent(
            targetState = state.step,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) { step ->
            when (step) {
                0 -> WelcomeScreen(
                    onStart = { viewModel.onIntent(OnboardingIntent.NextStep) },
                    onSkip = {
                        viewModel.onIntent(OnboardingIntent.SetName(""))
                        viewModel.onIntent(OnboardingIntent.SetWalletName(stringResource(R.string.onboarding_default_wallet)))
                        viewModel.saveProfile()
                        viewModel.saveWallet()
                    }
                )
                1 -> ProfileSetupScreen(
                    userName = state.userName,
                    baseCurrency = state.baseCurrency,
                    onNameChange = { viewModel.onIntent(OnboardingIntent.SetName(it)) },
                    onCurrencyChange = { viewModel.onIntent(OnboardingIntent.SetCurrency(it)) },
                    onContinue = { viewModel.saveProfile() }
                )
                2 -> FirstWalletScreen(
                    walletName = state.walletName,
                    walletType = state.walletType,
                    walletBalance = state.walletBalance,
                    onNameChange = { viewModel.onIntent(OnboardingIntent.SetWalletName(it)) },
                    onTypeChange = { viewModel.onIntent(OnboardingIntent.SetWalletType(it)) },
                    onBalanceChange = { viewModel.onIntent(OnboardingIntent.SetWalletBalance(it)) },
                    onSave = { viewModel.saveWallet() }
                )
                3 -> CompleteScreen(
                    onFinish = { viewModel.onIntent(OnboardingIntent.Complete) }
                )
            }
        }
    }
}

@Composable
private fun CompleteScreen(onFinish: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "🎉",
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.onboarding_complete_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.onboarding_complete_desc),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onFinish,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(56.dp),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Text(
                    text = stringResource(R.string.onboarding_complete_cta),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
