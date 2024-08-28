import { ProductDto } from './../../core/interfaces/ProductDto';
import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { DataPurchase } from '../../core/interfaces/DataPurchase';

@Component({
  selector: 'app-dialog-add-to-car',
  standalone: true,
  imports: [DialogModule,ButtonModule,InputNumberModule,InputTextModule,CommonModule,FormsModule],
  templateUrl: './dialog-add-to-car.component.html',
  styleUrl: './dialog-add-to-car.component.css'
})
export class DialogAddToCarComponent {

  @Output() addToCarEvent = new EventEmitter<ProductDto>();
  @Output() addCar = new EventEmitter<ProductDto>();
  displayDialog: boolean = false;
  dataPurchaseArray : DataPurchase[] = [];

  producDTO: ProductDto = {
    id: 0,
    name: '',
    price: 0,
    code: '',
    quantity: 0,
    productUrl: ''
  };

    onCancel() {
     this.displayDialog = false;
     this.producDTO ={
      id: 0,
      name: '',
      price: 0,
      code: '',
      quantity: 0,
      productUrl: ''
    };
    }

    openDialog(product : ProductDto){
      this.producDTO = product;
      this.producDTO.quantity = 1;
      this.displayDialog = true;
    }

    addShoppingCart(){
        this.addCar.emit(this.producDTO!)
        this.displayDialog = false;
    }

}
