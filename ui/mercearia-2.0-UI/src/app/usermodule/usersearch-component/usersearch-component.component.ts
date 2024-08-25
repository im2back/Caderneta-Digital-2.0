import { UserResponse } from './../../core/interfaces/UserResponse';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { MessagesModule } from 'primeng/messages';
import { FormsModule, NgForm } from '@angular/forms';
import { ValidMessagesComponent } from '../../share/valid-messages/valid-messages.component';
import { RouterModule, Routes } from '@angular/router';
import { CustomerServiceService } from '../../services/customer-service/customer-service.service';


@Component({
  selector: 'app-usersearch-component',
  standalone: true,
  imports: [CommonModule,MessagesModule,ButtonModule,InputTextModule,HttpClientModule,ValidMessagesComponent,FormsModule
    ,RouterModule
  ],
  providers : [CustomerServiceService],
  templateUrl: './usersearch-component.component.html',
  styleUrl: './usersearch-component.component.css'
})
export class UsersearchComponentComponent {
  constructor(private service : CustomerServiceService) {}

  message: string | null = null;
  userResponse: UserResponse | null = null;

  localizar(form: NgForm){
    this.service.buscar(form.value.document).subscribe((response: UserResponse) => {
        // Tratamento em caso de sucesso
        this.userResponse = response;
        this.message = ` Cliente Encontrado: ${response.name}!`;
        form.reset();
        setTimeout(() => {
          this.message = null;
        }, 5000);
      },
      (error) => {
        // Tratamento em caso de erro
        console.log(error);
        this.message = 'Erro: ' + (error.error?.message || 'Erro desconhecido');
        // Exibe a mensagem de erro por 8 segundos
        setTimeout(() => {
          this.message = null;
        }, 8000);
      }
    );
  }
}
