
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserpageComponentComponent } from './userpage-component/userpage-component.component';




@NgModule({
  declarations: [],
  imports: [ CommonModule,UserpageComponentComponent],
  exports:[UserpageComponentComponent]
})
export class UsermoduleModule { }
