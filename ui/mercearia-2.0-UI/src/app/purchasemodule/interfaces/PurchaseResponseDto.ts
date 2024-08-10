import { ListPurchaseProductResponse } from "./ListPurchasedProductResponse";

export interface PurchaseResponseDto
{
  customerName: string ,
  purchasedProducts : ListPurchaseProductResponse [],
  total: number

}
