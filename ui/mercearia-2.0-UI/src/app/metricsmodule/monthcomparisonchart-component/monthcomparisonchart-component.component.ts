// MonthcomparisonchartComponentComponent.ts
import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ChartModule } from 'primeng/chart';
import { FinanceData } from '../interfaces/FinanceData';

@Component({
  selector: 'app-monthcomparisonchart-component',
  standalone: true,
  imports: [ChartModule],
  templateUrl: './monthcomparisonchart-component.component.html',
  styleUrls: ['./monthcomparisonchart-component.component.css']
})
export class MonthcomparisonchartComponentComponent implements OnChanges {
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
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    this.data = {
      labels: ['Total das Vendas'],
      datasets: [
        {
          type: 'bar',
          label: this.getLastMonth(),
          backgroundColor: documentStyle.getPropertyValue('--yellow-500'),
          data: [this.financeData?.totalValueForLastMonth],
          borderColor: 'white',
          borderWidth: 2
        },
        {
          type: 'bar',
          label: this.getCurrentMonth(),
          backgroundColor: documentStyle.getPropertyValue('--blue-500'),
          data: [this.financeData?.partialValueForCurrentMonth]
        }
      ]
    };

    this.options = {
      maintainAspectRatio: false,
      aspectRatio: 0.6,
      plugins: {
        legend: {
          labels: {
            color: textColor
          }
        }
      },
      scales: {
        x: {
          ticks: {
            color: textColorSecondary
          },
          grid: {
            color: surfaceBorder
          }
        },
        y: {
          ticks: {
            color: textColorSecondary
          },
          grid: {
            color: surfaceBorder
          }
        }
      }
    };
  }


  formatMonthAndYear(date: Date): string {
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Mês começa em 0
    const year = date.getFullYear();
    return `${month}/${year}`;
}

   getCurrentMonth(): string {
    const currentDate = new Date();
    return this.formatMonthAndYear(currentDate);
}

 getLastMonth(): string {
    const currentDate = new Date();
    const lastMonthDate = new Date(currentDate.getFullYear(), currentDate.getMonth() - 1);
    return this.formatMonthAndYear(lastMonthDate);
}


}
