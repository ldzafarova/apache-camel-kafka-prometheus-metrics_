package com.example.demo

import java.time.Instant

data class TransactionDto(
    var companyCode: Company,
    var naturalAccount: String,
    var subAccount: String,
    var account: String,
    var projectNumber: String? = null,
    var qty: Double,
    var voucherDate: Instant,
)