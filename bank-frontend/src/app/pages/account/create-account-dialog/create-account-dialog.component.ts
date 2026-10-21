import { Component, computed, inject, input, output, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AccountService } from '../../../services/account/account.service';
import { ACCOUNT_TYPES, AccountType, ClientDto } from '../../../models/account/client.model';

@Component({
  selector: 'app-create-account-dialog',
  imports: [ReactiveFormsModule],
  templateUrl: './create-account-dialog.component.html',
  styleUrl: '../create-client-dialog/create-client-dialog.component.css',
})
export class CreateAccountDialogComponent {
  private readonly accountService = inject(AccountService);
  private readonly fb = inject(FormBuilder);

  readonly client = input.required<ClientDto>();

  readonly closed = output<void>();
  readonly created = output<void>();

  readonly saving = signal(false);
  readonly error = signal<string | null>(null);

  /** Account types the client doesn't own yet (every client starts with CURRENT). */
  readonly availableTypes = computed<AccountType[]>(() => {
    const owned = this.client().accounts?.map((a) => a.accountType) ?? ['CURRENT'];
    return ACCOUNT_TYPES.filter((type) => !owned.includes(type));
  });

  readonly form = this.fb.nonNullable.group({
    accountType: ['' as AccountType | '', Validators.required],
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    this.error.set(null);
    this.accountService
      .createAccount(this.client().iban, this.form.getRawValue().accountType as AccountType)
      .subscribe({
        next: () => {
          this.saving.set(false);
          this.created.emit();
        },
        error: () => {
          this.saving.set(false);
          this.error.set('Could not create account.');
        },
      });
  }

  close(): void {
    this.closed.emit();
  }
}

