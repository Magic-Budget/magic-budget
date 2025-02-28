import { useEffect, useState } from "react";
import Transaction from "./(objects)/transaction";
import axios from "axios";
import { Button } from "@/components/ui/button";

export default function TransactionView(props: { transactionId: string }) {
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
			<div className="flex my-2 px-4 justify-center">
				<Button>Edit</Button>
			</div>
		</div>
	);
}
