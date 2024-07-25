
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar2Component } from './core/navbar2/navbar2.component';
import { UserComponentComponent } from './user-component/user-component.component';




@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,Navbar2Component,UserComponentComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'mercearia-2.0-UI';
}
