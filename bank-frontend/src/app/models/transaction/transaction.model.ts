import { ClientDto } from '../account/client.model';

export type TransactionType = 'TRANSFER' | 'DEPOSIT' | 'WITHDRAWAL';
export type TransactionStatus = 'COMPLETED' | 'FAILED';

/**
 * Mirrors com.mybank.transaction.model.Transaction / Transfer.
 * Field names assume Jackson's default getters — adjust if your model uses different names.
 */
export interface Transaction {
  transactionType: TransactionType;
  transactionStatus: TransactionStatus;
  amount: number;
  sourceClientIban?: string;
  targetClientIban?: string;
}

/** Mirrors com.mybank.transaction.dto.TransactionDto (response). */
export interface TransactionDto {
  transaction: Transaction;
  sourceClient?: ClientDto;
  targetClient?: ClientDto;
}

/** Mirrors com.mybank.transaction.dto.TransferDto (request). */
export interface TransferRequest {
  sourceClientIban: string;
  targetClientIban: string;
  amount: number;
}

/** Request for the upcoming deposit endpoint. */
export interface DepositRequest {
  iban: string;
  amount: number;
}

/** Request for the upcoming withdrawal endpoint. */
export interface WithdrawalRequest {
  iban: string;
  amount: number;
}
