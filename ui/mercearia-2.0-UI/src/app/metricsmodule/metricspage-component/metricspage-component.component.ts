// MetricspageComponentComponent.ts
import { FinanceData } from './../interfaces/FinanceData';
import { Component, OnDestroy } from '@angular/core';
import { MonthcomparisonchartComponentComponent } from '../monthcomparisonchart-component/monthcomparisonchart-component.component';
import { HttpClientModule } from '@angular/common/http';
import { MetricsServiceService } from '../service/metrics-service.service';
import { PerformancesevendaysComponentComponent } from "../performancesevendays-component/performancesevendays-component.component";
import { ValuescomponentComponent } from '../valuescomponent/valuescomponent.component';

@Component({
  selector: 'app-metricspage-component',
  standalone: true,
  imports: [MonthcomparisonchartComponentComponent, HttpClientModule, PerformancesevendaysComponentComponent,
    ValuescomponentComponent
  ],
  providers: [MetricsServiceService],
  templateUrl: './metricspage-component.component.html',
  styleUrls: ['./metricspage-component.component.css']
})
export class MetricspageComponentComponent implements OnDestroy {
  financeData: FinanceData | undefined;
  private intervalId: any;

  constructor(private service: MetricsServiceService) {}

  ngOnInit() {
    this.fetchData();
    this.intervalId = setInterval(() => {
      this.fetchData();
    }, 30000);
  }

  ngOnDestroy() {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }

  private fetchData() {
    this.service.metrics().subscribe((response: FinanceData) => {
      this.financeData = response;
    },
    (error) => {
      const errorMessage = `Status do erro: ${error.status}\nTexto do status: ${error.statusText}\nMensagem do erro: ${error.message}`;
      alert(errorMessage);

    });
  }
}
