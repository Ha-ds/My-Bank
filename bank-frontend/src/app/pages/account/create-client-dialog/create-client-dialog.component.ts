import { Component, inject, output, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AccountService } from '../../../services/account/account.service';

@Component({
  selector: 'app-create-client-dialog',
  imports: [ReactiveFormsModule],
  templateUrl: './create-client-dialog.component.html',
  styleUrl: './create-client-dialog.component.css',
})
export class CreateClientDialogComponent {
  private readonly accountService = inject(AccountService);
  private readonly fb = inject(FormBuilder);

  readonly closed = output<void>();
  readonly created = output<void>();

  readonly saving = signal(false);
  readonly error = signal<string | null>(null);

  readonly form = this.fb.nonNullable.group({
    clientId: ['', Validators.required],
    name: ['', Validators.required],
    iban: ['', Validators.required],
    password: ['', Validators.required],
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving.set(true);
    this.error.set(null);
    this.accountService.createClient(this.form.getRawValue()).subscribe({
      next: () => {
        this.saving.set(false);
        this.created.emit();
      },
      error: () => {
        this.saving.set(false);
        this.error.set('Could not create client.');
      },
    });
  }

  close(): void {
    this.closed.emit();
  }
}
