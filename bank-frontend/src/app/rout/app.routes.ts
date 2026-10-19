import { Routes } from '@angular/router';
import { AccountPage } from '../pages/account/account.page';
import { TransactionPage } from '../pages/transaction/transaction.page';

export const routes: Routes = [
  { path: '', redirectTo: 'account', pathMatch: 'full' },
  { path: 'account', component: AccountPage },
  { path: 'transaction', component: TransactionPage },
  { path: '**', redirectTo: 'account' },
];
