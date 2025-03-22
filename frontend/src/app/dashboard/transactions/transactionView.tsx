import { useEffect, useState } from "react";
import Transaction from "./(objects)/transaction";
import axios from "axios";
import { Button } from "@/components/ui/button";
import SplitExpense from "./splitExpense";
import { UUID } from "crypto";
import { useUserStore } from "@/stores/user-store";

export default function TransactionView(props: { transactionId: UUID }) {
	const { id: userid, bearerToken } = useUserStore();
	const apiURL = `${process.env.NEXT_PUBLIC_API_URL}/api/${userid}/expense/view/${props.transactionId}`;
	const [transaction, setTransaction] = useState<Transaction>();

	useEffect(() => {
		axios
			.get(`${apiURL}`, {
				headers: {
					Authorization: `Bearer ${bearerToken}`,
				},
			})
			.then((response) => {
				const transactionData = response.data;
				const data: Transaction = {
					id: response.data.transaction_id,
					date: new Date(response.data.expense_posted_date),
					name: response.data.expense_name,
					amount: response.data.income_amount,
					description: response.data.expense_description,
					category: response.data.category,
				};
				setTransaction(data);
			})
			.catch((error) => {
				console.error(
					"There was an error fetching the transaction!",
					error
				);
			});
	}, [props.transactionId]);

	return (
		<div>
			<p className="my-1">
				<b>Name: </b>
				{transaction?.name}
			</p>
			<p className="my-1">
				<b>Amount: </b>${transaction?.amount}
			</p>
			<p className="my-1">
				<b>Date: </b>
				{transaction?.date.toLocaleDateString()}
			</p>
			<p className="my-1">
				<b>Description: </b>
				{transaction?.description}
			</p>
			<div className="my-2 justify-center">
				<Button>Edit</Button>
				<SplitExpense expense_id={props.transactionId} />
			</div>
		</div>
	);
}
