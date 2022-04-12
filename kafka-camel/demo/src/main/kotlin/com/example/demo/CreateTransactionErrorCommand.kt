package com.example.demo

class CreateTransactionErrorCommand(
    val errorType: TransactionErrorType,
    val transaction: TransactionDto
)