import { UUID } from "crypto";

export default interface Income {
  id: UUID;
  amount: number;
  name: string;
  description: string;
  date: Date;
  type: string;
}
