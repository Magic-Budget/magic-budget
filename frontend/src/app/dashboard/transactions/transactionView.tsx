import { useEffect, useState } from "react";
import Transaction from "./(objects)/transaction";
import axios from "axios";
import { Button } from "@/components/ui/button";
import SplitExpense from "./splitExpense";
import { UUID } from "crypto";
import { useUserStore } from "@/stores/user-store";

export default function TransactionView(props: { transactionId: UUID }) {
	const { id: userid, bearerToken } = useUserStore();
	const apiURL = `${process.env.NEXT_PUBLIC_API_URL}/api/${userid}/expense/view-all`;
	const [transaction, setTransaction] = useState<Transaction>();

	useEffect(() => {
		axios
			.get(`/api/transactions/${props.transactionId}`)
			.then((response) => {
				const transactionData = response.data;
				transactionData.date = new Date(transactionData.date);
				setTransaction(transactionData);
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
			<p>
				<b>Name:</b>
				{transaction?.name}
			</p>
			<p>
				<b>Amount:</b>${transaction?.amount}
			</p>
			<p>
				<b>Date:</b>
				{transaction?.date.toLocaleDateString()}
			</p>
			<p>
				<b>Description:</b>
				{transaction?.description}
			</p>
			<div className="my-2 justify-center">
				<Button>Edit</Button>
				<SplitExpense expense_id={props.transactionId} />
			</div>
		</div>
	);
}
