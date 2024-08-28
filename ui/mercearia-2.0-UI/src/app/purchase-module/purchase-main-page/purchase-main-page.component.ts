import { Component, Output, EventEmitter } from '@angular/core';
import { ScannerComponent } from '../scanner/scanner.component';
import { Message } from 'primeng/api';
import { MessagesModule } from 'primeng/messages';
import { CommonModule } from '@angular/common';
import { PurchaseRandomComponent } from '../purchase-random/purchase-random.component';
import { DialogAddToCarComponent } from '../dialog-add-to-car/dialog-add-to-car.component';
import { DialogComfirmPurchaseComponent } from '../dialog-comfirm-purchase/dialog-comfirm-purchase.component';
import { ShoppingCartComponent } from '../shopping-cart/shopping-cart.component';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ProductDto } from '../../core/interfaces/ProductDto';
import { DataPurchase } from '../../core/interfaces/DataPurchase';
import { PurchaseDto } from '../../core/interfaces/PurchaseDto';
import { CustomerServiceService } from '../../services/customer-service/customer-service.service';

@Component({
  selector: 'app-purchase-main-page',
  standalone: true,
  imports: [ScannerComponent,MessagesModule,CommonModule,PurchaseRandomComponent,
    DialogAddToCarComponent,DialogComfirmPurchaseComponent,ShoppingCartComponent,FormsModule,ButtonModule,InputTextModule],
    providers:[CustomerServiceService],
  templateUrl: './purchase-main-page.component.html',
  styleUrl: './purchase-main-page.component.css'
})
export class PurchaseMainPageComponent {
  constructor(private customerService:CustomerServiceService){}

  @Output() diagConfirmarCompra = new EventEmitter<string>();

   //menssagem de feedback de compra
   messages: Message[] | undefined;
   messageInterruptor = false;

 sucessMessage(event: string) {
  this.messages = [{ severity: 'success', detail: event }];
  this.messageInterruptor = true;
}

errorMessage(event: string) {
  this.messages = [{ severity: 'error', detail: event }];
  this.messageInterruptor = true;
}


   purchase: PurchaseDto | null = null;
   totalValuePurchase : number = 0;
   userName : string ='';

   clientDocument : string ='';

   iniciarCompra(dataPurchaseArray : DataPurchase[]){

       //Calculando o valor total da compra
       let totalValue = 0;

       dataPurchaseArray.forEach(e => {
       totalValue += e.price*e.quantity;
       });
       this.totalValuePurchase = totalValue;

       this.getUserName();

       this.purchase =
       {
         document : this.clientDocument,
         purchasedItems: dataPurchaseArray,
       }

   }

   getUserName(){
    this.customerService.buscar(this.clientDocument).subscribe((userResponse) =>{
      this.userName = userResponse.name
    },(error) => {
      this.messages = [{ severity: 'error', detail: `'Erro ao buscar nome do cliente: ${error.error.message}'` }];
      this.messageInterruptor = true;

    });
  }

  compraAvulso(document:string){
    this.clientDocument = document
  }

}
