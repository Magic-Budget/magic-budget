import Transaction from "@/app/dashboard/transactions/(objects)/transaction";
import { NextApiRequest, NextApiResponse } from "next";
import { NextResponse } from "next/server";

export function GET(req: NextApiRequest) {
  try {
    const mockData: Transaction = {
		id: "transaction1",
		date: new Date(Date.now()),
		name: "Transaction 1",
		amount: 0,
		description:
			"According to all known laws of aviation, there is no way a bee should be able to fly. Its wings are too small",
		category: "Category 1",
	};

    return NextResponse.json(mockData, {status:200})
  } catch (error) {
    console.error("API Error:", error);
    return NextResponse.json({error:"Internal server error"}, {status:500})
  }
}