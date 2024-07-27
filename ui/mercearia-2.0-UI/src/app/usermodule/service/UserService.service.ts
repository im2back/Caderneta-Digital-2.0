import { UndoPurchase } from '../interfaces/UndoPurchaseDto';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserData } from '../interfaces/UserData';
import { UserResponse } from '../interfaces/UserResponse';
@Injectable({
  providedIn: 'root'
})

export class UserServiceService {

  private readonly API =  "http://localhost:8080/customer"
  private readonly APISTOCK =  "http://localhost:8081/product"

  constructor( private http : HttpClient) { }

  cadastrar(clienteCadastro: UserData) : Observable <UserResponse> {
    return this.http.post<UserResponse> (this.API,clienteCadastro);
  }

  buscar(document: string) : Observable<UserResponse>{
    return this.http.get<UserResponse> (`${this.API}/findDocument?document=${document}`);
  }

  excluirCompra(undo: UndoPurchase) : Observable<void>{
    return this.http.put<void> (`${this.APISTOCK}/undopurchase`,undo);
  }
}
