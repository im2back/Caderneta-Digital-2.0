import { DataGraphic } from "./DataGraphic";

export interface FinanceData {
  totalValueForLastMonth: number;
  partialValueForCurrentMonth: number;
  partialValueForCurrentDay: number;
  totalOutstandingAmount: number;
  dataGraphicSevenDays: DataGraphic[];
}
