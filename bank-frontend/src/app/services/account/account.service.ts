import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { delay, Observable, of } from 'rxjs';
import { ClientDto, CreateClientRequest } from '../../models/account/client.model';
import { MOCK_CLIENTS } from '../../models/account/client.mock';

@Injectable({ providedIn: 'root' })
export class AccountService {
  private readonly http = inject(HttpClient);

  private readonly baseUrl = 'http://localhost:8080/account';

  getAllClients(): Observable<ClientDto[]> {
    return this.http.get<ClientDto[]>(`${this.baseUrl}/get-all-clients`);
    //return of(MOCK_CLIENTS).pipe(delay(300));
  }

  getClientByIban(iban: string): Observable<ClientDto> {
    return this.http.get<ClientDto>(`${this.baseUrl}/get-client/${encodeURIComponent(iban)}`);
  }

  createClient(client: CreateClientRequest): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/create-client`, client);
  }

  updateClient(client: ClientDto): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/update-client`, client);
  }
}
