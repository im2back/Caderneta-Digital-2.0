import { Component, EventEmitter, Output } from '@angular/core';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RadioButtonModule } from 'primeng/radiobutton';

// the scanner!
import { ZXingScannerModule } from '@zxing/ngx-scanner';
import { BarcodeFormat } from '@zxing/library';

import { HttpClientModule } from '@angular/common/http';
import { SpinnerModule } from 'primeng/spinner';
import { InputNumberModule } from 'primeng/inputnumber';

import { CardModule } from 'primeng/card';
import { Message } from 'primeng/api';
import { MessagesModule } from 'primeng/messages';

@Component({
  selector: 'app-scanner-cam',
  standalone: true,
  imports: [DialogModule,ButtonModule,InputTextModule,InputNumberModule,CardModule,RadioButtonModule,
    CommonModule,FormsModule,ZXingScannerModule,HttpClientModule,SpinnerModule,MessagesModule,
    ],
  templateUrl: './scanner-cam.component.html',
  styleUrl: './scanner-cam.component.css'
})
export class ScannerCamComponent {

@Output() productCodeEvent = new EventEmitter<string>();
@Output() genericDialogEvent = new EventEmitter<void>();


 productCode = '';

 //adicionarproduto manual
 isSectionVisible: boolean = false;
 toggleSection() {
   this.isSectionVisible = !this.isSectionVisible;
 }

 //Valores lógicos para caixa de dialogo
 displayDialog: boolean = false;
 displayDialogConfirmPurchase : boolean = false;


 allowedFormats = [
  BarcodeFormat.QR_CODE,
  BarcodeFormat.EAN_13, BarcodeFormat.CODE_128,
  BarcodeFormat.DATA_MATRIX ];

  onCodeResult(resultString: string) {
    this.productCodeEvent.emit(resultString);
    this.genericDialogEvent.emit();
  }

  cadastrar(){
    this.displayDialog = false
  }

}
