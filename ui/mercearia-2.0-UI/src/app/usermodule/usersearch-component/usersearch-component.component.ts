import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { MessagesModule } from 'primeng/messages';
import { UserServiceService } from '../service/UserService.service';
import { FormsModule, NgForm } from '@angular/forms';
import { UserResponse } from '../interfaces/UserResponse';
import { ValidMessagesComponent } from '../../share/valid-messages/valid-messages.component';

@Component({
  selector: 'app-usersearch-component',
  standalone: true,
  imports: [CommonModule,MessagesModule,ButtonModule,InputTextModule,HttpClientModule,ValidMessagesComponent,FormsModule],
  providers : [UserServiceService],
  templateUrl: './usersearch-component.component.html',
  styleUrl: './usersearch-component.component.css'
})
export class UsersearchComponentComponent {
  constructor(private service : UserServiceService) {}

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
