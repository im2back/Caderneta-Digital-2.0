import { FinanceData } from '../../core/interfaces/FinanceData';

import { Component, OnDestroy } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { CustomerServiceService } from '../../services/customer-service/customer-service.service';
import { MonthComparisonChartComponent } from '../month-comparison-chart/month-comparison-chart.component';
import { PerformanceSevenDaysChartComponent } from '../performance-seven-days-chart/performance-seven-days-chart.component';
import { ValuesStatisticsComponent } from '../values-statistics/values-statistics.component';


@Component({
  selector: 'app-metrics-main-page',
  standalone: true,
  imports: [HttpClientModule,MonthComparisonChartComponent,PerformanceSevenDaysChartComponent,ValuesStatisticsComponent],
  providers:[CustomerServiceService],
  templateUrl: './metrics-main-page.component.html',
  styleUrl: './metrics-main-page.component.css'
})
export class MetricsMainPageComponent implements OnDestroy{

  financeData: FinanceData | undefined;
  private intervalId: any;

  constructor(private service: CustomerServiceService) {}

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
    this.service.getMetrics().subscribe((response: FinanceData) => {
      this.financeData = response;
    },
    (error) => {
      const errorMessage = `Status do erro: ${error.status}\nTexto do status: ${error.statusText}\nMensagem do erro: ${error.message}`;
      alert(errorMessage);

    });
  }
}
