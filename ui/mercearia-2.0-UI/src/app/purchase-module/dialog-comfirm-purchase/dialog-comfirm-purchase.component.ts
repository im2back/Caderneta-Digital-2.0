import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { StockServiceService } from '../../services/stock-service/stock-service.service';
import { PurchaseDto } from '../../core/interfaces/PurchaseDto';
import { CustomerServiceService } from '../../services/customer-service/customer-service.service';
import { DataPurchase } from '../../core/interfaces/DataPurchase';

@Component({
  selector: 'app-dialog-comfirm-purchase',
  standalone: true,
  imports: [InputNumberModule,InputTextModule,ButtonModule,FormsModule,DialogModule],
  providers:[StockServiceService,CustomerServiceService],
  templateUrl: './dialog-comfirm-purchase.component.html',
  styleUrl: './dialog-comfirm-purchase.component.css'
})
export class DialogComfirmPurchaseComponent {

  constructor (private service : StockServiceService,private customerService : CustomerServiceService) {}
  @Output() sucessMessageEvent = new EventEmitter<string>();
  @Output() errorMessageEvent = new EventEmitter<string>();
  @Output() clearShoppingCart = new EventEmitter<void>();

  displayDialogConfirmPurchase : boolean = false;
  userName : string = '';
  totalValuePurchase : number = 0;
  purchase: PurchaseDto | null = null;
  clientDocument : string ='';

   //fazer requisição para salvar a compra
   onRegisterPurchase(){
    this.service.finalizePurchase(this.purchase!).subscribe((purchaseResponseDto) =>{
    this.sucessMessageEvent.emit('Compra realizada com sucesso');
    this.clearShoppingCart.emit();
   },
  (error) => {
    this.errorMessageEvent.emit(`'Erro na compra: ${error.error.message}'`);
  });

  this.displayDialogConfirmPurchase = false;
  this.clientDocument = '';
  this.userName = '';
}

onCancelPurchase(){
  this.displayDialogConfirmPurchase = false;
  this.clientDocument = '';
  this.userName = '';
}



iniciarCompra(dataPurchaseArray : DataPurchase[],document : string){
  this.clientDocument = document

    //Calculando o valor total da compra
    let totalValue = 0;

    dataPurchaseArray.forEach(e => {
    totalValue += e.price*e.quantity;
    });
    this.totalValuePurchase = totalValue;

    this.getUserName();

    this.purchase =
    {
      document : document,
      purchasedItems: dataPurchaseArray,
    }

}


getUserName(){
 this.customerService.buscar(this.clientDocument).subscribe((userResponse) =>{
   this.userName = userResponse.name
   this.displayDialogConfirmPurchase = true;
 },(error) => {
 this.errorMessageEvent.emit(`'Erro ao buscar nome do cliente: ${error.error.message}'`);

 });
}
}
