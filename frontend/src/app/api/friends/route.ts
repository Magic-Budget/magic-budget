import { NextApiRequest, NextApiResponse } from "next";
import { NextResponse } from "next/server";
import {Option as MultiSelectOption} from '@/components/ui/multiselect';

export function GET(req: NextApiRequest) {
  try {
    const mockData: {id:string, name:string}[] = [
        { id: "1", name: "Alice" },
        { id: "2", name: "Bob" },
        { id: "3", name: "Charlie" },
        { id: "4", name: "David" },
    ];

    return NextResponse.json(mockData, {status:200})
  } catch (error) {
    console.error("API Error:", error);
    return NextResponse.json({error:"Internal server error"}, {status:500})
  }
}