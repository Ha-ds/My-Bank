export type AccountType = 'CURRENT' | 'SAVINGS' | 'INVESTMENT' | 'CREDIT';

export const ACCOUNT_TYPES: AccountType[] = ['CURRENT', 'SAVINGS', 'INVESTMENT', 'CREDIT'];

export interface AccountDto {
  accountType: AccountType;
  balance: number;
}

export interface ClientDto {
  name: string;
  iban: string;
  balance: number;
  accounts?: AccountDto[];
  email: string;
}

export interface CreateClientRequest {
  name: string;
  password: string;
  email: string;
}
