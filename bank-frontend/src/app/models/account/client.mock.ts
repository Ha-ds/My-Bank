import { ClientDto } from './client.model';

export const MOCK_CLIENTS: ClientDto[] = [
  {
    name: 'Ivan Petrov',
    iban: 'BG80BNBG96611020345678',
    balance: 1520.5,
    accounts: [
      { accountType: 'CURRENT', balance: 1520.5 },
      { accountType: 'SAVINGS', balance: 8000 },
    ],
  },
  {
    name: 'Maria Georgieva',
    iban: 'BG18RZBB91550123456789',
    balance: 340.75,
    accounts: [
      { accountType: 'CURRENT', balance: 340.75 },
      { accountType: 'SAVINGS', balance: 12500 },
      { accountType: 'INVESTMENT', balance: 25000 },
    ],
  },
  {
    name: 'Georgi Dimitrov',
    iban: 'BG27UNCR70001522334455',
    balance: 0,
    accounts: [{ accountType: 'CURRENT', balance: 0 }],
  },
  {
    name: 'Elena Ivanova',
    iban: 'BG45FINV91501017654321',
    balance: 9875.2,
    accounts: [
      { accountType: 'CURRENT', balance: 9875.2 },
      { accountType: 'CREDIT', balance: 3000 },
    ],
  },
  {
    name: 'Nikolay Stoyanov',
    iban: 'BG63STSA93000123987654',
    balance: 215.4,
    // no accounts → page falls back to a single CURRENT row
  },
];
