import { NextApiRequest, NextApiResponse } from "next";
import { NextResponse } from "next/server";

export function GET(req: NextApiRequest) {
  try {
    const mockData: CategoryTotals[] = [
      { category: "groceries", total: 275 },
      { category: "transportation", total: 432 },
      { category: "restaurants", total: 287 },
      { category: "housing", total: 173 },
      { category: "utilities", total: 173 },
      { category: "other", total: 190 }
    ];

    return NextResponse.json(mockData, {status:200})
  } catch (error) {
    console.error("API Error:", error);
    return NextResponse.json({error:"Internal server error"}, {status:500})
  }
}