package ru.qwerty.feature.credits.api.repository

import ru.qwerty.feature.credits.api.domain.Loan
import ru.qwerty.feature.credits.api.domain.LoanDetails
import ru.qwerty.feature.credits.api.domain.Tariff

/**
 * @author y.yangel
 */
interface CreditsRepository {

    suspend fun getCreditTariffs(): List<Tariff>

    suspend fun getLoans(): List<Loan>

    suspend fun applyLoan(
        key: String,
        accountId: Int,
        tariffId: String,
        periodDays: Int,
        loanAmount: Double,
    )

    suspend fun getLoanDetails(id: String): LoanDetails

    suspend fun reduceLoanPayments(creditId: String, key: String, accountId: Int, value: Double)

    suspend fun loadCreditRating() : Pair<Int, Int>
}