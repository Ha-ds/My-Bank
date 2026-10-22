import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable} from 'rxjs';
import { AccountType, ClientDto, CreateClientRequest } from '../../models/account/client.model';

@Injectable({ providedIn: 'root' })
export class AccountService {
  private readonly http = inject(HttpClient);

  private readonly baseUrl = 'http://localhost:8080/account';

  getAllClients(): Observable<ClientDto[]> {
    return this.http.get<ClientDto[]>(`${this.baseUrl}/get-all-clients`);
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

  createAccount(iban: string, accountType: AccountType): Observable<void> {
    const params = new HttpParams().set('iban', iban).set('accountType', accountType);
    return this.http.post<void>(`${this.baseUrl}/create-account`, null, { params });
  }
}
