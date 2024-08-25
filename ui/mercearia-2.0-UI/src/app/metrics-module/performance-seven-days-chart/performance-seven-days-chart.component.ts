import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ChartModule } from 'primeng/chart';
import { FinanceData } from '../../core/interfaces/FinanceData';

@Component({
  selector: 'app-performance-seven-days-chart',
  standalone: true,
  imports: [ChartModule],
  templateUrl: './performance-seven-days-chart.component.html',
  styleUrl: './performance-seven-days-chart.component.css'
})
export class PerformanceSevenDaysChartComponent implements OnChanges{

  data: any;
  options: any;
  @Input() financeData: FinanceData | undefined;

  ngOnChanges(changes: SimpleChanges) {
    if (changes['financeData'] && this.financeData) {
      this.updateChartData();
    }
  }

  updateChartData() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--surface-border');

    if (this.financeData?.dataGraphicSevenDays) {
      // Processar dataGraphicSevenDays para obter labels e valores
      const labels = this.financeData.dataGraphicSevenDays.map(item => item.purchaseDate);
      const values = this.financeData.dataGraphicSevenDays.map(item => item.totalValue);

      // Configurar os dados do gráfico
      this.data = {
        labels: labels, // Labels do eixo X
        datasets: [
          {
            type: 'line',
            label: 'Vendas dos Últimos 7 Dias',
            backgroundColor: documentStyle.getPropertyValue('--black-500'),
            data: values, // Dados para o gráfico
            borderColor: 'black',
            borderWidth: 1
          }
        ]
      };
    } else {
      // Configuração padrão ou tratamento de erro
      this.data = {
        labels: [],
        datasets: []
      };
    }

    // Configuração das opções do gráfico
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

}
