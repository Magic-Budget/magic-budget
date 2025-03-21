"use client";
import axios from "axios";
import Transaction from "./(objects)/transaction";
import TransactionTableClient from "./transactionTableClient";
import { useUserStore } from "@/stores/user-store";
import { UUID } from "crypto";
import { useEffect, useState } from "react";

export default function TransactionTable(props: {
	start: number;
	end: number;
}) {
	const { id: userid, bearerToken } = useUserStore();
	const [transactions, setTransactions] = useState<Transaction[]>([]);

	useEffect(() => {
		async function fetchTransactions() {
			const fetchedTransactions = await getTransactions(
				userid,
				bearerToken,
				props.start,
				props.end
			);
			setTransactions(fetchedTransactions);
		}

		fetchTransactions();
	}, [userid, bearerToken, props.start, props.end]);

	return <TransactionTableClient transactions={transactions} />;
}

async function getTransactions(
	userId: UUID,
	bearerToken: string,
	start: number,
	end: number
): Promise<Transaction[]> {
	try {
		const response = await axios.get(
			`${process.env.NEXT_PUBLIC_API_URL}/api/${userId}/expense/view-all`,
			{
				headers: {
					Authorization: `Bearer ${bearerToken}`,
				},
			}
		);

		return response.data.map((expense: any) => ({
			id: expense.transaction_id,
			date: new Date(expense.expense_posted_date),
			name: expense.expense_name,
			amount: expense.income_amount,
			description: expense.expense_description,
			category: expense.category,
			business_name: expense.business_name,
		}));
	} catch (err) {
		console.error(err);
		return [];
	}
}
