import { UserResponse } from './../../usermodule/interfaces/UserResponse';
import { PurchaseResponseDto } from './../interfaces/PurchaseResponseDto';

import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Component } from '@angular/core';

// the scanner!
import { ZXingScannerModule } from '@zxing/ngx-scanner';
import { BarcodeFormat } from '@zxing/library';

import { PurchaseserviceService } from '../service/purchaseservice.service';
import { HttpClientModule } from '@angular/common/http';
import { SpinnerModule } from 'primeng/spinner';
import { InputNumberModule } from 'primeng/inputnumber';
import { PurchaseDto } from '../interfaces/PurchaseDto';
import { DataPurchase } from '../interfaces/DataPurchase';
import { UserServiceService } from '../../usermodule/service/UserService.service';
import { CardModule } from 'primeng/card';
import { Message } from 'primeng/api';
import { MessagesModule } from 'primeng/messages';
@Component({
  selector: 'app-purchasecomponent',
  standalone: true,
  imports: [DialogModule,ButtonModule,InputTextModule,InputNumberModule,CardModule,
    CommonModule,FormsModule,ZXingScannerModule,HttpClientModule,SpinnerModule,MessagesModule],
  providers:[PurchaseserviceService,UserServiceService],
  templateUrl: './purchasecomponent.component.html',
  styleUrl: './purchasecomponent.component.css'
})
export class PurchasecomponentComponent {

  constructor (private service : PurchaseserviceService, private userService :UserServiceService) {}
  //menssagem de feedback de compra
  messages: Message[] | undefined;
  messageInterruptor = false;

  //Valores lógicos para caixa de dialogo
  displayDialog: boolean = false;
  displayDialogConfirmPurchase : boolean = false;

  //Dados do produto adicionado no carrinho
  productName: string = '';
  quantity: number = 1;
  productUrl : string = '';
  productPrice : number = 0;

  //Valor total da compra
  totalValuePurchase : number = 0;

  //Objetos para montar o carrinho de compra e requisição para a mesma
  purchase: PurchaseDto | null = null;
  dataPurchaseArray : DataPurchase[] = [];

  //Código capturado
  scannedCode: string = '';

  //Nome do comprador. Utilizado para exibir na caixa de diálogo
  clientDocument: string = '';
  userName : string = '';



  allowedFormats = [
    BarcodeFormat.QR_CODE,
    BarcodeFormat.EAN_13, BarcodeFormat.CODE_128,
    BarcodeFormat.DATA_MATRIX ];

    onCodeResult(resultString: string) {
      this.service.getProductByCode(resultString).subscribe(
        (productDto) => {
          this.productName = productDto.name;
          this.productUrl = productDto.productUrl;
          this.productPrice = productDto.price
          this.scannedCode = resultString;
          this.displayDialog = true;
        },
        (error) => {
          console.error('Erro ao buscar produto: ', error); // Log de erro
        }
      );
    }

    onRegister() {
      this.dataPurchaseArray.push(
        {
        code: this.scannedCode,
        quantity: this.quantity,
        price : this.productPrice,//o preço não será usado pelo backend, porém será usando pelo front para calcular o total
        url : this.productUrl, //A url não será usada pelo backend, porém será usando pelo front
        name: this.productName
        }
      );

      this.displayDialog = false;
      this.productName = '';
      this.quantity = 1;
      this.productUrl = '';
      this.productPrice = 0;
    }

    onCancel() {
      this.displayDialog = false;
      this.productName = '';
      this.quantity = 0;
      this.productUrl = '';
      this.productPrice = 0;
    }

    iniciarCompra() {
      //Calculando o valor total da compra
      let totalValue = 0;

      this.dataPurchaseArray.forEach(e => {
      totalValue += e.price*e.quantity;
      });
      this.totalValuePurchase = totalValue;

      this.getUserName();



      this.purchase =
      {
        document : this.clientDocument,
        purchasedItems: this.dataPurchaseArray,
      }

    }

    //fazer requisição para salvar a compra
    onRegisterPurchase(){
        this.service.finalizePurchase(this.purchase!).subscribe((purchaseResponseDto) =>{
        this.messages = [{ severity: 'success', detail: 'Compra realizada com sucesso' }];
        this.messageInterruptor = true;
      },
      (error) => {
        this.messages = [{ severity: 'error', detail: `'Erro na compra: ${error.error.messages}'` }];
        this.messageInterruptor = true;
      });

      this.displayDialogConfirmPurchase = false;
      this.clientDocument = '';
      this.dataPurchaseArray = [];
      this.userName = '';
    }

    onCancelPurchase(){
      this.displayDialogConfirmPurchase = false;
      this.clientDocument = '';
      this.dataPurchaseArray = [];
      this.userName = '';
    }

    getUserName(){
      this.userService.buscar(this.clientDocument).subscribe((userResponse) =>{
        this.userName = userResponse.name
        this.displayDialogConfirmPurchase = true;
      },(error) => {
        this.messages = [{ severity: 'error', detail: `'Erro ao buscar nome do cliente: ${error.error.message}'` }];
        this.messageInterruptor = true;

      });
    }

    removeProduct(code: string) {
      const index = this.dataPurchaseArray.findIndex(e => e.code === code);
      if (index !== -1) {
        this.dataPurchaseArray.splice(index, 1);
      }
    }


}
