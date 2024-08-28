import { Address } from "./Adress";
import { PurchaseRecordDto } from "./PurchaseRecordDto";

export interface UserResponse {
  id: number;
  name: string;
  document: string;
  email: string;
  phone: string;
  address: Address;
  purchaseRecord: PurchaseRecordDto[];
  totalDaConta : string
}
