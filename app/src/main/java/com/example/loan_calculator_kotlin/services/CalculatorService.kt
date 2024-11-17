package com.example.loan_calculator_kotlin.services

import kotlin.math.pow

object CalculatorService {
    fun calculateInstallments(
        loanAmount: Double,
        interestRate: Double,
        months: Int,
        isEqualInstallments: Boolean
    ): List<Double> {
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
}
