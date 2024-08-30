import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RadioButtonModule } from 'primeng/radiobutton';

@Component({
  selector: 'app-purchase-random',
  standalone: true,
  imports: [RadioButtonModule,CommonModule,FormsModule],
  templateUrl: './purchase-random.component.html',
  styleUrl: './purchase-random.component.css'
})
export class PurchaseRandomComponent {
  //Nome do comprador. Utilizado para exibir na caixa de diálogo

  @Output() compraAvulso = new EventEmitter<string>();
  clientDocument: string = '';

  onRadioButtonChange(){
    this.compraAvulso.emit(this.clientDocument);
  }
}
