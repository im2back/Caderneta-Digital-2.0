import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class VisibilityService {

  private isVisible = new BehaviorSubject<boolean>(true);
  isVisible$ = this.isVisible.asObservable();

  show() {
    this.isVisible.next(true);
  }

  hide() {
    this.isVisible.next(false);
  }

}
