import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ChartModule } from 'primeng/chart';
import { FinanceData } from '../interfaces/FinanceData';

@Component({
  selector: 'app-valuescomponent',
  standalone: true,
  imports: [ChartModule],
  templateUrl: './valuescomponent.component.html',
  styleUrl: './valuescomponent.component.css'
})
export class ValuescomponentComponent {
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

    this.data = {
        labels: ['Total em Aberto', 'Venda do dia'],
        datasets: [
            {
                data: [this.financeData?.totalOutstandingAmount, this.financeData?.partialValueForCurrentDay],
                backgroundColor: [documentStyle.getPropertyValue('--yellow-500'), documentStyle.getPropertyValue('--blue-500')],
                hoverBackgroundColor: [documentStyle.getPropertyValue('--yellow-400'), documentStyle.getPropertyValue('--blue-400')]
            }
        ]
    };

    this.options = {
      maintainAspectRatio: false,
      aspectRatio: 1,
        plugins: {
            legend: {
                labels: {
                    usePointStyle: true,
                    color: textColor
                }
            }
        }
    };
}
  }

