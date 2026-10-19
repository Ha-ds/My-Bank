export type AccountType = 'CURRENT' | 'SAVINGS' | 'INVESTMENT' | 'CREDIT';

export interface AccountDto {
  accountType: AccountType;
  balance: number;
}

export interface ClientDto {
  name: string;
  iban: string;
  balance: number;
  accounts?: AccountDto[];
}

export interface CreateClientRequest {
  clientId: string;
  name: string;
  iban: string;
  password: string;
}
