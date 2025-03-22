"use client";
import axios from "axios";
import Income from "./(objects)/income";
import IncomeTableClient from "./incomeTableClient";
import { useUserStore } from "@/stores/user-store";
import { UUID } from "crypto";
import { useEffect, useState } from "react";

export default function TransactionTable(props: {
	start: number;
	end: number;
}) {
	const { id: userId, bearerToken } = useUserStore();
	const [incomes, setIncomes] = useState<Income[]>([]);

	useEffect(() => {
		async function fetchIncomes() {
			const fetchedIncomes = await getIncomes(
				userId,
				bearerToken,
				props.start,
				props.end
			);
			setIncomes(fetchedIncomes);
		}

		const intervalId = setInterval(() => {
			fetchIncomes();
		  }, 2000);
	  
		  return () => clearInterval(intervalId);
	}, [userId, bearerToken, props.start, props.end]);

	return <IncomeTableClient incomes={incomes} />;
}




async function getIncomes(
    userId: UUID,
    bearerToken: string,
    start: number,
    end: number
): Promise<Income[]> {
    try {
        //get all incomes
		const response = await axios.get(
			`${process.env.NEXT_PUBLIC_API_URL}/api/${userId}/income/view-all`,
			{
				headers: {
					Authorization: `Bearer ${bearerToken}`,
				},
			}
		);

		
		return response.data.map((income: any) => ({
			
			id: income.transaction_id,
            amount: income.income_amount,
            name: income.income_name,
            description: income.income_description,
			date: new Date(income.income_posted_date),
			type: income.income_type,
			
		}));
	} catch (err) {
		console.error(err);
		return [];
	}
}
