import { UndoPurchase } from './../../core/interfaces/UndoPurchaseDto';
import { ProductRegister } from '../../core/interfaces/ProductRegister';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PurchaseDto } from '../../core/interfaces/PurchaseDto';
import { PurchaseResponseDto } from '../../core/interfaces/PurchaseResponseDto';
import { ProductDto } from '../../core/interfaces/ProductDto';


@Injectable({
  providedIn: 'root'
})
export class StockServiceService {

  private readonly API =  "https://192.168.1.111:8443/product"

  constructor(private http : HttpClient) { }

  saveNewProduct(productRegister: ProductRegister): Observable<ProductDto> {
    return this.http.post<ProductDto>(this.API,productRegister);
  }

  updateProduct(productDto: ProductDto): Observable<void> {
    return this.http.put<void>(`${this.API}/update`,productDto);
  }

  getProductByCode(code: string): Observable<ProductDto> {
    return this.http.get<ProductDto>(`${this.API}/code?code=${code}`);
  }

  finalizePurchase(purchase :PurchaseDto): Observable<PurchaseResponseDto>{
    return this.http.post<PurchaseResponseDto>(`${this.API}/purchase`,purchase);
  }

  excluirCompra(undo: UndoPurchase) : Observable<void>{
    return this.http.put<void> (`${this.API}/undopurchase`,undo);
  }

}
