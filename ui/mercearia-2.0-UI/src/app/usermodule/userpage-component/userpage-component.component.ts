import { Component} from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserformregisterComponentComponent } from "../userformregister-component/userformregister-component.component";
import { UsersearchComponentComponent } from '../usersearch-component/usersearch-component.component';

@Component({
  selector: 'app-userpage-component',
  standalone: true,
  imports: [CommonModule, UserformregisterComponentComponent,UsersearchComponentComponent],
  templateUrl: './userpage-component.component.html',
  styleUrl: './userpage-component.component.css'
})
export class UserpageComponentComponent {

  }








