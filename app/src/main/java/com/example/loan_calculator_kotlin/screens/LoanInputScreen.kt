package com.example.loan_calculator_kotlin.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loan_calculator_kotlin.services.CalculatorService

@Composable
fun LoanInputScreen(
    onCalculate: (List<Double>) -> Unit
) {
    var loanAmount by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("") }
    var months by remember { mutableStateOf("") }
    var isEqualInstallments by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text("Kalkulator Pożyczki", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = loanAmount,
            onValueChange = { loanAmount = it },
            label = { Text("Kwota pożyczki (PLN)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = interestRate,
            onValueChange = { interestRate = it },
            label = { Text("Oprocentowanie (%)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = months,
            onValueChange = { months = it },
            label = { Text("Liczba miesięcy") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Rodzaj rat:", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = isEqualInstallments,
                onClick = { isEqualInstallments = true }
            )
            Text("Równe", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = !isEqualInstallments,
                onClick = { isEqualInstallments = false }
            )
            Text("Malejące", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                val loan = loanAmount.toDoubleOrNull()
                val rate = interestRate.toDoubleOrNull()
                val duration = months.toIntOrNull()

                if (loan != null && rate != null && duration != null) {
                    errorMessage = ""
                    val installments = CalculatorService.calculateInstallments(
                        loan, rate, duration, isEqualInstallments
                    )
                    onCalculate(installments)
                } else {
                    errorMessage = "Wprowadź poprawne dane."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Oblicz raty", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
