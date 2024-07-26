import { Address } from "./Adress";
import { PurchaseRecord } from "./PurchaseRecord";

export interface UserResponse {
  id: number;
  name: string;
  document: string;
  email: string;
  phone: string;
  address: Address;
  purchaseRecord: PurchaseRecord[];
}
