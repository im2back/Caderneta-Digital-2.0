

import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { HttpClientModule } from '@angular/common/http';
import { ProductRegister } from '../../core/interfaces/ProductRegister';
import { StockServiceService } from '../../services/stock-service/stock-service.service';


@Component({
  selector: 'app-product-form-register',
  standalone: true,
  imports: [FormsModule,CommonModule,ButtonModule,InputTextModule,DialogModule,InputNumberModule,HttpClientModule],
  providers:[StockServiceService],
  templateUrl: './product-form-register.component.html',
  styleUrl: './product-form-register.component.css'
})
export class ProductFormRegisterComponent {

constructor (private service : StockServiceService){}

@Input() productCode: string  = '';
@Output() sucessRegisterEvent = new EventEmitter<string>();
@Output() errorRegisterEvent = new EventEmitter<string>();

isVisible = false;
displayDialogForm :boolean = false;



  openForm() {
    this.isVisible = true;
    this.displayDialogForm = true;
  }

  closeForm() {
    this.isVisible = false;
    this.displayDialogForm = false;
  }

  cadastrar(form:NgForm) {

    const product: ProductRegister = {
      name: form.value.name,
      price: form.value.price,
      code: form.value.code,
      quantity: form.value.quantity,
      productUrl: form.value.productUrl
    };

    this.service.saveNewProduct(product).subscribe((response) =>{
      this.sucessRegisterEvent.emit('Cadastro Realizado com sucesso');
    },
    (error) =>{
      this.errorRegisterEvent.emit('Erro ao Cadastrar: '+error.error.message );
    }
  )

    this.closeForm();
  }
}


