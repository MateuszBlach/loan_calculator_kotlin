package com.example.loan_calculator_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.loan_calculator_kotlin.screens.LoanInputScreen
import com.example.loan_calculator_kotlin.screens.LoanResultScreen
import com.example.loan_calculator_kotlin.ui.theme.LoancalculatorkotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoancalculatorkotlinTheme {
                var showResults by remember { mutableStateOf(false) }
                var installments by remember { mutableStateOf(listOf<Double>()) }

                if (showResults) {
                    LoanResultScreen(
                        installments = installments,
                        onBack = { showResults = false }
                    )
                } else {
                    LoanInputScreen(
                        onCalculate = {
                            installments = it
                            showResults = true
                        }
                    )
                }
            }
        }
    }
}
