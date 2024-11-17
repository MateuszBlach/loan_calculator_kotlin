package com.example.loan_calculator_kotlin.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanResultScreen(
    installments: List<Double>,
    onBack: () -> Unit
) {
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

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                "Łączna kwota do spłaty: ${"%.2f".format(totalPayment)} PLN",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Miesiąc", fontWeight = FontWeight.Bold)
                Text("Rata (PLN)", fontWeight = FontWeight.Bold)
            }
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)

            installments.forEachIndexed { index, installment ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${index + 1}")
                    Text("${"%.2f".format(installment)} PLN")
                }
                Divider(color = Color.LightGray, thickness = 0.5.dp)
            }
        }
    }
}
