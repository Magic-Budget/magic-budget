import { UUID } from "crypto";

export interface User {
	userId: UUID;
	username: "string";
	fullName: "string";
	email: "string";
}
