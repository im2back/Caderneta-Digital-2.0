import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ChartModule } from 'primeng/chart';
import { FinanceData } from '../../core/interfaces/FinanceData';

@Component({
  selector: 'app-values-statistics',
  standalone: true,
  imports: [ChartModule],
  templateUrl: './values-statistics.component.html',
  styleUrl: './values-statistics.component.css'
})
export class ValuesStatisticsComponent {

  data: any;
  options: any;
  @Input() financeData: FinanceData | undefined;

  ngOnChanges(changes: SimpleChanges) {
    if (changes['financeData']) {
      this.updateChartData();
    }
  }

  updateChartData() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');


  }

}
