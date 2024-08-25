import { UndoPurchase } from './../../core/interfaces/UndoPurchaseDto';
import { UserResponse } from './../../core/interfaces/UserResponse';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProgressBarModule } from 'primeng/progressbar';
import { MessagesModule } from 'primeng/messages';
import { Message } from 'primeng/api';
import { CustomerServiceService } from '../../services/customer-service/customer-service.service';
import { StockServiceService } from '../../services/stock-service/stock-service.service';


@Component({
  selector: 'app-userdetail-component',
  standalone: true,
  imports: [HttpClientModule,CommonModule,ProgressBarModule,MessagesModule],
  providers : [CustomerServiceService,StockServiceService],
  templateUrl: './userdetail-component.component.html',
  styleUrl: './userdetail-component.component.css'
})
export class UserdetailComponentComponent {

  constructor(private route: ActivatedRoute,private service : CustomerServiceService,
    private stockService : StockServiceService,private router: Router) {}

  userResponse: UserResponse | undefined;
  isLoading = false;
  messages: Message[] | undefined;
  messageInterruptor = false;

  ngOnInit(): void {
    const userId = this.route.snapshot.paramMap.get('id');
    if (userId) {
      this.service.buscar(userId).subscribe(user => {
        this.userResponse = user;
        console.log(user)
      });
    } else {
      console.error('User ID is null');
    }
  }

  excluirProduto(id: string, code: string, quantity: number): void {
    const undoPurchase: UndoPurchase = {
      purchaseId: Number(id),
      productCode: code,
      quantity: quantity
    };

    this.stockService.excluirCompra(undoPurchase).subscribe({
        next: () => {
          console.log('Compra excluída com sucesso!');
          this.ngOnInit();
        },
        error: (err) => {
          console.error('Erro ao excluir a compra', err);
        }
      });
  }

  quitarProduto(id: string, code: string, quantity: number): void {
    const undoPurchase: UndoPurchase = {
      purchaseId: Number(id),
      productCode: code,
      quantity: quantity
    };

    this.service.quitarCompra(undoPurchase).subscribe({
        next: () => {
          console.log('Compra excluída com sucesso!');
          this.ngOnInit();
        },
        error: (err) => {
          console.error('Erro ao excluir a compra', err);
        }
      });
  }
  zerarConta(document:string){
    this.isLoading = true;
    this.service.zerarConta(document).subscribe({
      next: () => {
        this.isLoading = false;
        this.messages = [{ severity: 'success', detail: 'Conta Zerada' }];
        this.messageInterruptor = true;
        this.ngOnInit();
      },
      error: (err) => {
        this.isLoading = false;
        this.messages = [{ severity: 'error', detail: 'Erro ao zerar conta' }];
        this.messageInterruptor = true;
        console.error('Erro ao excluir!', err);
      }

    });
  }

  gerarNota(document:string){
    this.isLoading = true;
    this.service.gerarNota(document).subscribe({
      next: () => {
        this.isLoading = false;
        this.messages = [{ severity: 'success', detail: 'Nota Emitida' }];
        this.messageInterruptor = true;
        this.ngOnInit();
      },
      error: (err) => {
        this.isLoading = false;
        this.messages = [{ severity: 'error', detail: 'Erro ao emitir nota' }];
        this.messageInterruptor = true;
        console.error('Erro ao emitir nota!', err);
      }

    });
  }

  exclusaoLogicaCliente(document:string){
    this.isLoading = true;
    this.service.excluirCliente(document).subscribe({
      next: () => {
        this.isLoading = false;
        this.messages = [{ severity: 'success', detail: 'Cliente Desativado!' }];
        this.messageInterruptor = true;
        this.ngOnInit();
      },
      error: (err) => {
        this.isLoading = false;
        this.messages = [{ severity: 'error', detail: 'Erro ao desativar cliente!' }];
        this.messageInterruptor = true;
        console.error('Erro ao desativar cliente!', err);
      }

    });
  }

}
