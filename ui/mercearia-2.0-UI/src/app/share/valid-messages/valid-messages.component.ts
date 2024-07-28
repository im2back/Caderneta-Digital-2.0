import { Component, Input } from '@angular/core';
import { NgForm, NgModel } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-valid-messages',
  standalone: true,
  imports: [CommonModule],
  template: `
  <small *ngIf="temErro()" class="p-error">{{text}}</small>
  `,
  styles: ``
})
export class ValidMessagesComponent {

  @Input() error : string= '';
  @Input() controlForm?: NgModel;
  @Input() text: string ='';


  temErro(): boolean {
    return (this.controlForm?.hasError(this.error) && this.controlForm?.dirty) ?? false;
  }

}
