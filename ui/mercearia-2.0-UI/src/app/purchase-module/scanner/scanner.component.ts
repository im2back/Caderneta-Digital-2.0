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
import { MessagesModule } from 'primeng/messages';
import { StockServiceService } from '../../services/stock-service/stock-service.service';
import { PurchaseDto } from '../../core/interfaces/PurchaseDto';
import { DataPurchase } from '../../core/interfaces/DataPurchase';
import { ProductDto } from '../../core/interfaces/ProductDto';

@Component({
  selector: 'app-scanner',
  standalone: true,
  imports: [DialogModule,ButtonModule,InputTextModule,InputNumberModule,CardModule,RadioButtonModule,
    CommonModule,FormsModule,ZXingScannerModule,HttpClientModule,SpinnerModule,MessagesModule],
    providers:[StockServiceService],
  templateUrl: './scanner.component.html',
  styleUrl: './scanner.component.css'
})
export class ScannerComponent {

  constructor (private stockService : StockServiceService) {}

  //adicionar produto manual
  isSectionVisible: boolean = false;
  toggleSection() {
    this.isSectionVisible = !this.isSectionVisible;
  }

  @Output() addProductDialogEvent = new EventEmitter<ProductDto>();

  //Dados do produto adicionado no carrinho

  productDto:ProductDto | undefined;

  //Código capturado
  scannedCode: string = '';

  allowedFormats = [
    BarcodeFormat.QR_CODE,
    BarcodeFormat.EAN_13, BarcodeFormat.CODE_128,
    BarcodeFormat.DATA_MATRIX ];

    onCodeResult(resultString: string) {
      this.stockService.getProductByCode(resultString).subscribe(
        (productDto) => {
          this.productDto = productDto;
          this.addProductDialogEvent.emit(productDto);
          this.scannedCode = '';
        },
        (error) => {
          console.error('Erro ao buscar produto: ', error); // Log de erro
        }
      );
    }


}
