import { PurchaseDto } from './../interfaces/PurchaseDto';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProductDto } from '../interfaces/ProductDto';
import { PurchaseResponseDto } from '../interfaces/PurchaseResponseDto';

@Injectable({
  providedIn: 'root'
})
export class PurchaseserviceService {

 private readonly API =  "http://127.0.1.1:8081/product"
  constructor( private http : HttpClient) { }

  getProductByCode(code: string): Observable<ProductDto> {
    return this.http.get<ProductDto>(`${this.API}/code?code=${code}`);
  }

  finalizePurchase(purchase :PurchaseDto): Observable<PurchaseResponseDto>{
    return this.http.post<PurchaseResponseDto>(`${this.API}/purchase`,purchase);
  }



}
