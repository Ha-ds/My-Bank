import { CurrencyPipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { AccountService } from '../../services/account/account.service';
import { TransactionService } from '../../services/transaction/transaction.service';
import { ClientDto } from '../../models/account/client.model';
import { TransactionDto } from '../../models/transaction/transaction.model';

type Mode = 'transfer' | 'deposit' | 'withdraw';

@Component({
  selector: 'app-transaction-page',
  imports: [ReactiveFormsModule, CurrencyPipe],
  templateUrl: './transaction.page.html',
  styleUrl: './transaction.page.css',
})
export class TransactionPage implements OnInit {
  private readonly accountService = inject(AccountService);
  private readonly transactionService = inject(TransactionService);
  private readonly fb = inject(FormBuilder);

  readonly modes: { value: Mode; label: string }[] = [
    { value: 'transfer', label: 'Transfer' },
    { value: 'deposit', label: 'Deposit' },
    { value: 'withdraw', label: 'Withdraw' },
  ];

  readonly mode = signal<Mode>('transfer');
  readonly clients = signal<ClientDto[]>([]);
  readonly loadingClients = signal(false);
  readonly submitting = signal(false);
  readonly error = signal<string | null>(null);
  readonly result = signal<TransactionDto | null>(null);
  readonly submitLabel = computed(() => this.modes.find((m) => m.value === this.mode())!.label);

  readonly form = this.fb.nonNullable.group({
    sourceIban: ['', Validators.required],
    targetIban: ['', Validators.required],
    amount: [0, [Validators.required, Validators.min(0.01)]],
  });

  ngOnInit(): void {
    this.loadClients();
    this.applyMode('transfer');
  }

  loadClients(): void {
    this.loadingClients.set(true);
    this.accountService.getAllClients().subscribe({
      next: (clients) => {
        this.clients.set(clients);
        this.loadingClients.set(false);
      },
      error: () => {
        this.error.set('Could not load clients.');
        this.loadingClients.set(false);
      },
    });
  }

  /** Transfer uses source + target, deposit only target, withdraw only source. */
  applyMode(mode: Mode): void {
    this.mode.set(mode);
    this.error.set(null);
    this.result.set(null);
    this.form.reset();

    const { sourceIban, targetIban } = this.form.controls;
    mode === 'deposit' ? sourceIban.disable() : sourceIban.enable();
    mode === 'withdraw' ? targetIban.disable() : targetIban.enable();
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    const { sourceIban, targetIban, amount } = this.form.getRawValue();

    if (this.mode() === 'transfer' && sourceIban === targetIban) {
      this.error.set('Source and target must be different clients.');
      return;
    }

    let request$: Observable<TransactionDto>;
    switch (this.mode()) {
      case 'transfer':
        request$ = this.transactionService.transfer({
          sourceClientIban: sourceIban,
          targetClientIban: targetIban,
          amount,
        });
        break;
      case 'deposit':
        request$ = this.transactionService.deposit({ iban: targetIban, amount });
        break;
      case 'withdraw':
        request$ = this.transactionService.withdraw({ iban: sourceIban, amount });
        break;
    }

    this.submitting.set(true);
    this.error.set(null);
    this.result.set(null);

    request$.subscribe({
      next: (result) => {
        this.submitting.set(false);
        this.result.set(result);
        this.form.controls.amount.reset();
        this.loadClients(); // refresh balances in the dropdowns
      },
      error: (err: HttpErrorResponse) => {
        this.submitting.set(false);
        this.error.set(err.error?.message ?? 'Transaction failed.');
      },
    });
  }
}
