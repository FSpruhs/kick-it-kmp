package com.spruhs.screens.match

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.spruhs.dateTimeNow
import com.spruhs.match.presentation.PlanMatchEffect
import com.spruhs.match.presentation.PlanMatchIntent
import com.spruhs.match.presentation.PlanMatchUIState
import com.spruhs.match.presentation.PlanMatchViewModel
import com.spruhs.screens.common.SubmitButton
import com.spruhs.screens.common.formatDateTime
import java.util.Calendar
import kotlinx.datetime.LocalDateTime
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlanMatchScreen(
    onPlanMatchSuccess: () -> Unit,
    planMatchViewModel: PlanMatchViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val planMatchUiState by planMatchViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        planMatchViewModel.effects.collect { effect ->
            when (effect) {
                PlanMatchEffect.MatchPlanned -> {
                    Toast
                        .makeText(
                            context,
                            "Match planned!",
                            Toast.LENGTH_SHORT
                        ).show()
                    onPlanMatchSuccess()
                }
            }

            if (planMatchUiState.error != null) {
                Toast
                    .makeText(
                        context,
                        planMatchUiState.error,
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { paddingValues ->
            PlanMatchContent(
                modifier = Modifier.padding(paddingValues),
                context = context,
                planMatchUiState = planMatchUiState,
                onIntent = planMatchViewModel::processIntent
            )
        }
    )
}

@Composable
fun PlanMatchContent(
    modifier: Modifier = Modifier,
    context: Context,
    onIntent: (PlanMatchIntent) -> Unit,
    planMatchUiState: PlanMatchUIState
) {
    Column(
        modifier =
        modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PlanMatchForm(
            context = context,
            onIntent = onIntent,
            planMatchUiState = planMatchUiState
        )
    }
}

@Composable
fun PlanMatchForm(
    context: Context,
    onIntent: (PlanMatchIntent) -> Unit,
    planMatchUiState: PlanMatchUIState
) {
    Box(
        modifier =
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Plan Match",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier =
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier =
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    showDateTimePicker(context) { selectedDateTime ->
                        onIntent(PlanMatchIntent.SelectDateTime(selectedDateTime))
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = planMatchUiState.start?.formatDateTime() ?: "Select Date & Time")
            }

            OutlinedTextField(
                value = planMatchUiState.location,
                onValueChange = { onIntent(PlanMatchIntent.SelectLocation(it)) },
                label = { Text("(optional) Location (max. 50 Chars)") },
                modifier = Modifier.fillMaxWidth()
            )

            PlayerCountInputs(
                planMatchUiState = planMatchUiState,
                onMinPlayersChange = { onIntent(PlanMatchIntent.SelectMinPlayers(it)) },
                onMaxPlayersChange = { onIntent(PlanMatchIntent.SelectMaxPlayers(it)) }
            )
        }
        SubmitButton(
            modifier =
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            enabled = isSubmitButtonEnabled(planMatchUiState),
            onSubmitClick = { onIntent(PlanMatchIntent.PlanMatch) }
        )
    }
}

@Composable
fun PlayerCountInputs(
    planMatchUiState: PlanMatchUIState,
    onMinPlayersChange: (String) -> Unit,
    onMaxPlayersChange: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = planMatchUiState.minPlayers,
            onValueChange = { onMinPlayersChange(it) },
            label = { Text("min. Player") },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = planMatchUiState.maxPlayers,
            onValueChange = { onMaxPlayersChange(it) },
            label = { Text("max. Player") },
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

private fun isSubmitButtonEnabled(planMatchUiState: PlanMatchUIState) =
    validateStart(planMatchUiState) && validatePlayerCount(planMatchUiState)

private fun validatePlayerCount(uiState: PlanMatchUIState): Boolean {
    val min = uiState.minPlayers.toIntOrNull() ?: return false
    val max = uiState.maxPlayers.toIntOrNull() ?: return false
    return min >= 0 && max >= 0 && min <= max
}

private fun validateStart(uiState: PlanMatchUIState) = uiState.start
    ?.let { it > dateTimeNow() }
    ?: false

fun showDateTimePicker(context: Context, onDateTimeSelected: (LocalDateTime) -> Unit) {
    val now = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    val selectedDateTime =
                        LocalDateTime(
                            year,
                            month + 1,
                            dayOfMonth,
                            hourOfDay,
                            minute
                        )
                    onDateTimeSelected(selectedDateTime)
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
            ).show()
        },
        now.get(Calendar.YEAR),
        now.get(Calendar.MONTH),
        now.get(Calendar.DAY_OF_MONTH)
    ).show()
}