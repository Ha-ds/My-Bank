import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import {
  DepositRequest,
  TransactionDto,
  TransferRequest,
  WithdrawalRequest,
} from '../../models/transaction/transaction.model';

@Injectable({ providedIn: 'root' })
export class TransactionService {
  private readonly http = inject(HttpClient);

  private readonly baseUrl = 'http://localhost:8081/transaction'; // set to your transaction microservice port

  transfer(request: TransferRequest): Observable<TransactionDto> {
    return this.http.post<TransactionDto>(`${this.baseUrl}/transfer`, request);
  }

  /** NOTE: endpoint not implemented in the backend yet. */
  deposit(request: DepositRequest): Observable<TransactionDto> {
    return this.http.post<TransactionDto>(`${this.baseUrl}/deposit`, request);
  }

  /** NOTE: endpoint not implemented in the backend yet. */
  withdraw(request: WithdrawalRequest): Observable<TransactionDto> {
    return this.http.post<TransactionDto>(`${this.baseUrl}/withdraw`, request);
  }
}
