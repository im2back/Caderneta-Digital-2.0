import { UndoPurchase } from './../../core/interfaces/UndoPurchaseDto';
import { UserResponse } from './../../core/interfaces/UserResponse';
import { UserData } from './../../core/interfaces/UserData';

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FinanceData } from '../../core/interfaces/FinanceData';


@Injectable({
  providedIn: 'root'
})
export class CustomerServiceService {
  private readonly API =  "https://192.168.1.111:8080/customer"

  constructor( private http : HttpClient) { }

  getMetrics() : Observable<FinanceData>{
    return this.http.get<FinanceData> (`${this.API}/metrics`);
  }

  cadastrar(clienteCadastro: UserData) : Observable <UserResponse> {
    return this.http.post<UserResponse> (this.API,clienteCadastro);
  }

  buscar(document: string) : Observable<UserResponse>{
    return this.http.get<UserResponse> (`${this.API}/findDocument?document=${document}`);
  }


  quitarCompra(undo: UndoPurchase) : Observable<void>{
    return this.http.put<void> (`${this.API}/payment`,undo);
  }


  zerarConta(document: string): Observable<void> {
    return this.http.delete<void> (`${this.API}/cleardebt?document=${document}`);
  }

  gerarNota(document: string): Observable<void>{
    return this.http.put<void> (`${this.API}/note?document=${document}`,null);
  }

  excluirCliente(document: string): Observable<void> {
    return this.http.delete<void> (`${this.API}/deletecustomer?document=${document}`);
  }
}

