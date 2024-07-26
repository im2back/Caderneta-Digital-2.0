
import { Component } from '@angular/core';
import { PrimeIcons, MenuItem } from 'primeng/api';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';
@Component({
  selector: 'app-navbar2',
  standalone: true,
  imports: [RouterModule,],
  templateUrl: './navbar2.component.html',
  styleUrl: './navbar2.component.css'
})
export class Navbar2Component {
  constructor(private router: Router) {}

exibindoMenu : boolean = false;

}
