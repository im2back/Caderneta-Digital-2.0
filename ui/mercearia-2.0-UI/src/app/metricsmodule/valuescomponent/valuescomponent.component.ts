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


  }
}
