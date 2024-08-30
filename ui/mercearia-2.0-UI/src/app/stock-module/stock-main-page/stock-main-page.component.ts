import { Component } from '@angular/core';
import { ProductFormEditingComponent } from '../product-form-editing/product-form-editing.component';
import { ProductFormRegisterComponent } from '../product-form-register/product-form-register.component';

import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RadioButtonModule } from 'primeng/radiobutton';

import { SpinnerModule } from 'primeng/spinner';

import { CardModule } from 'primeng/card';
import { Message } from 'primeng/api';
import { MessagesModule } from 'primeng/messages';
import { ScannerCamComponent } from '../scanner-cam/scanner-cam.component';


@Component({
  selector: 'app-stock-main-page',
  standalone: true,
  imports: [DialogModule,ButtonModule,InputTextModule,CardModule,RadioButtonModule,CommonModule,FormsModule,ScannerCamComponent,
    SpinnerModule,MessagesModule,ProductFormEditingComponent,ProductFormRegisterComponent],
  templateUrl: './stock-main-page.component.html',
  styleUrl: './stock-main-page.component.css'
})
export class StockMainPageComponent {

// menssagem de feedback de compra
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

// Valores lógicos para caixa de dialogo e product code
genericDisplayDialog: boolean = false;
productCode = '';


// recebe valor do produto atraves do evento
receiveProductCode(code:string){
  this.productCode = code;
}

activeGenericDialog() {
  this.genericDisplayDialog = true;
}

closeGenericDialog(){
  this.genericDisplayDialog = false
}

}
