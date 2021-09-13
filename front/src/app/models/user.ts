import { Address } from './address';

export class User {
  id: number;
  username: string;
  role: string;
  addresses: Address[];
  token: string;
}
