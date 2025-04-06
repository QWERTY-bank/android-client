package ru.qwerty.impl.data.repository

import ru.qwerty.feature.credits.api.domain.Loan
import ru.qwerty.feature.credits.api.domain.LoanDetails
import ru.qwerty.feature.credits.api.domain.Tariff
import ru.qwerty.feature.credits.api.repository.CreditsRepository
import ru.qwerty.impl.data.api.CreateLoanRequest
import ru.qwerty.impl.data.mapper.toDomain
import ru.qwerty.impl.services.CreditService
import ru.qwerty.impl.services.ReduceLoanPayment
import javax.inject.Inject

/**
 * @author y.yangel
 */
internal class CreditsRepositoryImpl @Inject constructor(
    private val creditService: CreditService,
) : CreditsRepository {

    override suspend fun getCreditTariffs(): List<Tariff> {
        return creditService.getTariffs().results.map { it.toDomain() }
    }

    override suspend fun getLoans(): List<Loan> {
        return creditService.getLoans().results.map { it.toDomain() }
    }

    override suspend fun applyLoan(
        key: String,
        accountId: Int,
        tariffId: String,
        periodDays: Int,
        loanAmount: Double
    ) {
        return creditService.applyLoan(
            createLoan = CreateLoanRequest(
                key = key,
                accountId = accountId,
                tariffId = tariffId,
                periodDays = periodDays,
                loanAmount = loanAmount,
            )
        )
    }

    override suspend fun getLoanDetails(id: String): LoanDetails {
        return creditService.creditDetails(id = id).toDomain()
    }

    override suspend fun reduceLoanPayments(
        creditId: String,
        key: String,
        accountId: Int,
        value: Double,
    ) {
        return creditService.reduceLoanPayment(
            id = creditId,
            reduceLoanPayment = ReduceLoanPayment(
                key = key,
                accountId = accountId,
                value = value,
            )
        )
    }

    override suspend fun loadCreditRating(): Pair<Int, Int> {
        try {
            val rating = creditService.getLoanRating()
            return Pair(rating.rating, rating.maxRating)
        } catch (e: Exception) {
            return Pair(1000, 1000)
        }
    }
}
