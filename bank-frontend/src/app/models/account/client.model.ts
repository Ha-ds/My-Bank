export type AccountType = 'CURRENT' | 'SAVINGS' | 'INVESTMENT' | 'CREDIT';

/** Mirrors com.mybank.account.model.Account. */
export interface AccountDto {
  accountType: AccountType;
  balance: number;
}

/**
 * Mirrors com.mybank.account.dto.ClientDto.
 * `accounts` is expected from the upcoming get-all-clients endpoint;
 * if the backend doesn't send it, the UI falls back to a single CURRENT account.
 */
export interface ClientDto {
  name: string;
  iban: string;
  balance: number;
  accounts?: AccountDto[];
}

/** Payload for POST /account/create-client (mirrors the Client constructor args). */
export interface CreateClientRequest {
  clientId: string;
  name: string;
  iban: string;
  password: string;
}
