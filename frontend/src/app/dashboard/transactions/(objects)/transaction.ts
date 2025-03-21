import { UUID } from "crypto";

export default interface Transaction {
	id: UUID;
	date: Date;
	name: string;
	amount: number;
	description: string;
	category: string;
}
