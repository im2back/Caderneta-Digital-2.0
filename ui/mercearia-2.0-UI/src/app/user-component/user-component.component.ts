import { UserData } from './interfaces/UserData';
import { UserResponse } from './interfaces/UserResponse';

import { Component} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgModel,NgForm } from '@angular/forms';

import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { ButtonModule } from 'primeng/button';
import { InputMaskModule } from 'primeng/inputmask';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { ValidMessagesComponent } from '../share/valid-messages/valid-messages.component';
import { UserServiceService } from './service/UserService.service';

import { HttpClientModule } from '@angular/common/http';

import { TableModule } from 'primeng/table';

@Component({
  selector: 'app-user-component',
  standalone: true,
  imports: [CommonModule,FormsModule,InputTextModule,InputNumberModule,ButtonModule,InputMaskModule,
    MessagesModule,MessageModule,ValidMessagesComponent,HttpClientModule,TableModule ],
    providers : [UserServiceService],
  templateUrl: './user-component.component.html',
  styleUrl: './user-component.component.css'
})
export class UserComponentComponent {

  constructor(private service : UserServiceService) {}

  message: string | null = null;

  userResponse: UserResponse | null = null;

  cadastrar(form: NgForm) {
    if (form.valid) {
      const clienteCadastro: UserData = {
        name: form.value.name,
        document: form.value.document,
        email: form.value.email,
        phone: form.value.phone,
        address: {
          streetName: form.value.streetName,
          houseNumber: form.value.houseNumber,
          complement: form.value.complement
        }
      };

      this.service.cadastrar(clienteCadastro).subscribe(
        (response: UserResponse) => {
          // Tratamento em caso de sucesso
          this.userResponse = response;
          this.message = `${response.name} cadastrado com sucesso`;
          console.log(response);
          console.log('teste');
          form.reset();
          setTimeout(() => {
            this.message = null;
          }, 5000);
        },
        (error) => {
          // Tratamento em caso de erro
          console.log(error);
          this.message = 'Erro ao cadastrar: ' + (error.error?.message || 'Erro desconhecido');
          // Exibe a mensagem de erro por 8 segundos
          setTimeout(() => {
            this.message = null;
          }, 8000);
        }
      );
    } else {
      this.message = 'Formulário inválido. Por favor, verifique os campos.';
    }
  }

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








