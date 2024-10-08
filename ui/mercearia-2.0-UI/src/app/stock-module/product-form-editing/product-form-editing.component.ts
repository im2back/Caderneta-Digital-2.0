import { ProductDto } from './../../core/interfaces/ProductDto';
import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { HttpClientModule } from '@angular/common/http';


import {  OnChanges, SimpleChanges } from '@angular/core';
import { StockServiceService } from '../../services/stock-service/stock-service.service';

@Component({
  selector: 'app-product-form-editing',
  standalone: true,
  imports: [FormsModule,CommonModule,ButtonModule,InputTextModule,DialogModule,InputNumberModule,HttpClientModule],
  providers:[StockServiceService],
  templateUrl: './product-form-editing.component.html',
  styleUrl: './product-form-editing.component.css'
})
export class ProductFormEditingComponent  {

  @Output() sucessUpdateEvent = new EventEmitter<string>();
  @Output() errorUpdateEvent = new EventEmitter<string>();

  product: ProductDto = {
    id: 0,
    name: '',
    price: 0,
    code: '',
    quantity: 0,
    productUrl: ''
  };

constructor (private service : StockServiceService){}


isVisible = false;
displayDialogForm :boolean = false;

openForm(productCode:string) {

  this.service.getProductByCode(productCode).subscribe((response: ProductDto) => {
    this.product = response;
    this.isVisible = true;
    this.displayDialogForm = true;
  },
  (error)=>{
    this.errorUpdateEvent.emit('Erro:'+error.error.message);

  });
}



closeForm() {
  this.isVisible = false;
  this.displayDialogForm = false;
}

updateProduct(form:NgForm){
this.service.updateProduct(form.value).subscribe((response)=>{
  this.sucessUpdateEvent.emit('Atualizado com sucesso');
},
(error)=>{
  this.errorUpdateEvent.emit('Erro ao atualizar :'+error.error.message);
})
  this.closeForm();
}

}
