import { DataPurchase } from "./DataPurchase";

export interface PurchaseDto
{
  document: string,
  purchasedItems: DataPurchase []
}
