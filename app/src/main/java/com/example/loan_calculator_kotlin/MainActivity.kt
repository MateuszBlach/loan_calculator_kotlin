package com.example.loan_calculator_kotlin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.loan_calculator_kotlin.ui.theme.LoancalculatorkotlinTheme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoancalculatorkotlinTheme {
                LoanCalculatorApp()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoanCalculatorApp() {
    Scaffold {
        LoanInputScreen()
    }
}

@Composable
fun LoanInputScreen() {
    var loanAmount by remember { mutableStateOf("") }
    var interestRate by remember { mutableStateOf("") }
    var months by remember { mutableStateOf("") }
    var isEqualInstallments by remember { mutableStateOf(true) }
    var showResults by remember { mutableStateOf(false) }
    var installments by remember { mutableStateOf(listOf<Double>()) }

    if (showResults) {
        LoanResultScreen(
            installments = installments,
            onBack = { showResults = false }
        )
    } else {
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
                        installments = calculateInstallments(loan, rate, duration, isEqualInstallments)
                        showResults = true
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
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoanResultScreen(installments: List<Double>, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wyniki Kalkulacji", fontSize = 20.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Text("Wróć") } }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val totalPayment = installments.sum()

            Spacer(modifier = Modifier.height(20.dp))

            Text("Raty miesięczne:", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Miesiąc", fontWeight = FontWeight.Bold)
                Text("Rata (PLN)", fontWeight = FontWeight.Bold)
            }
            Divider(color = Color.Gray, thickness = 1.dp)

            installments.forEachIndexed { index, installment ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Miesiąc ${index + 1}")
                    Text("${"%.2f".format(installment)} PLN")
                }
                Divider(color = Color.LightGray, thickness = 0.5.dp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                "Łączna kwota do spłaty: ${"%.2f".format(totalPayment)} PLN",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0)
            )
        }
    }
}


fun calculateInstallments(loanAmount: Double, interestRate: Double, months: Int, isEqualInstallments: Boolean): List<Double> {
    val monthlyRate = interestRate / 100 / 12
    val installments = mutableListOf<Double>()

    if (isEqualInstallments) {
        val equalInstallment = loanAmount * monthlyRate / (1 - (1 + monthlyRate).pow(-months))
        repeat(months) {
            installments.add(equalInstallment)
        }
    } else {
        var remainingAmount = loanAmount
        val principalPayment = loanAmount / months

        for (i in 0 until months) {
            val interestForMonth = remainingAmount * monthlyRate
            val installment = principalPayment + interestForMonth
            installments.add(installment)
            remainingAmount -= principalPayment
        }
    }
    return installments
}
