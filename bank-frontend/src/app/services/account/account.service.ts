import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ClientDto, CreateClientRequest } from '../../models/account/client.model';

@Injectable({ providedIn: 'root' })
export class AccountService {

  private readonly http = inject(HttpClient);

  private readonly baseUrl = 'http://localhost:8080/account'; // proxied to the account microservice (see proxy.conf.json)

  /** NOTE: endpoint not implemented in the backend yet — adjust the path once it exists. */
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
}
