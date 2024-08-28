import { DataPurchase } from './../../core/interfaces/DataPurchase';
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { ProductDto } from '../../core/interfaces/ProductDto';

@Component({
  selector: 'app-shopping-cart',
  standalone: true,
  imports: [CommonModule,ButtonModule,CardModule],
  templateUrl: './shopping-cart.component.html',
  styleUrl: './shopping-cart.component.css'
})
export class ShoppingCartComponent {

  dataPurchaseArray : DataPurchase[] = [];

  removeProduct(code: string) {
    const index = this.dataPurchaseArray.findIndex(e => e.code === code);
    if (index !== -1) {
      this.dataPurchaseArray.splice(index, 1);
    }
  }

  addCar(product :ProductDto){
    this.dataPurchaseArray.push(
      {
        "code": product.code,
        "quantity": product.quantity,
        "price": product.price,
        "url": product.productUrl,
        "name": product.name
      })}

  clearCart(){
    this.dataPurchaseArray = [];
  }


  }

