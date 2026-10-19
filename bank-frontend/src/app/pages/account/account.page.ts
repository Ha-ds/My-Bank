import { CurrencyPipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { AccountService } from '../../services/account/account.service';
import { AccountDto, ClientDto } from '../../models/account/client.model';
import { CreateClientDialogComponent } from './create-client-dialog/create-client-dialog.component';

@Component({
  selector: 'app-account-page',
  imports: [CurrencyPipe, CreateClientDialogComponent],
  templateUrl: './account.page.html',
  styleUrl: './account.page.css',
})
export class AccountPage implements OnInit {
  private readonly accountService = inject(AccountService);

  readonly clients = signal<ClientDto[]>([]);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);
  readonly showDialog = signal(false);
  readonly expandedIban = signal<string | null>(null);

  ngOnInit(): void {
    this.loadClients();
  }

  loadClients(): void {
    this.loading.set(true);
    this.error.set(null);
    this.accountService.getAllClients().subscribe({
      next: (clients) => {
        this.clients.set(clients);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Could not load clients.');
        this.loading.set(false);
      },
    });
  }

  toggleClient(iban: string): void {
    this.expandedIban.update((current) => (current === iban ? null : iban));
  }

  accountsOf(client: ClientDto): AccountDto[] {
    return client.accounts?.length
      ? client.accounts
      : [{ accountType: 'CURRENT', balance: client.balance }];
  }

  onClientCreated(): void {
    this.showDialog.set(false);
    this.loadClients();
  }
}
