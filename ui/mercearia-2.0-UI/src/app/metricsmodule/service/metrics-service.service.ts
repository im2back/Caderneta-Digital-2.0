
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FinanceData } from '../interfaces/FinanceData';

@Injectable({
  providedIn: 'root'
})

export class MetricsServiceService {

  private readonly API =  "https://192.168.1.111:8080/customer"


  constructor( private http : HttpClient) { }

  metrics() : Observable<FinanceData>{
    return this.http.get<FinanceData> (`${this.API}/metrics`);
  }





}
