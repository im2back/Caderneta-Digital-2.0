import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserResponse } from '../interfaces/UserResponse';
import { UserServiceService } from '../service/UserService.service';
import { HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UndoPurchase } from '../interfaces/UndoPurchaseDto';

@Component({
  selector: 'app-userdetail-component',
  standalone: true,
  imports: [HttpClientModule,CommonModule],
  providers : [UserServiceService],
  templateUrl: './userdetail-component.component.html',
  styleUrl: './userdetail-component.component.css'
})
export class UserdetailComponentComponent {

  constructor(private route: ActivatedRoute,private service : UserServiceService,private router: Router) {}

  userResponse: UserResponse | undefined;

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

  excluir(id: string, code: string, quantity: number): void {
    const undoPurchase: UndoPurchase = {
      purchaseId: Number(id),
      productCode: code,
      quantity: quantity
    };

    this.service.excluirCompra(undoPurchase).subscribe({
        next: () => {
          console.log('Compra excluída com sucesso!');
          this.ngOnInit();
        },
        error: (err) => {
          console.error('Erro ao excluir a compra', err);
        }
      });
  }

}
