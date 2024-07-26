import { HttpClientModule } from '@angular/common/http';
import { Component } from '@angular/core';
import { ValidMessagesComponent } from '../../share/valid-messages/valid-messages.component';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserServiceService } from '../service/UserService.service';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { MessagesModule } from 'primeng/messages';
import { UserResponse } from '../interfaces/UserResponse';
import { UserData } from '../interfaces/UserData';

@Component({
  selector: 'app-userformregister-component',
  standalone: true,
  imports: [CommonModule,FormsModule,InputTextModule,InputNumberModule,ButtonModule,
    MessagesModule,MessagesModule,ValidMessagesComponent,HttpClientModule,],
  providers : [UserServiceService],
  templateUrl: './userformregister-component.component.html',
  styleUrl: './userformregister-component.component.css'
})
export class UserformregisterComponentComponent {

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
          this.message = `${response.name} cadastrado com sucesso`;
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
}
