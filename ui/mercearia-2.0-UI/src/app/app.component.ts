
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HomeComponentComponent } from './home-component/home-component.component';
import { Navbar2Component } from './core/navbar2/navbar2.component';




@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,HomeComponentComponent,Navbar2Component],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'mercearia-2.0-UI';
}
