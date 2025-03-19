import { useEffect, useState } from "react";
import Transaction from "./(objects)/transaction";
import axios from "axios";
import { Button } from "@/components/ui/button";
import { ChevronDown, ChevronUp } from "lucide-react";
import SplitExpense from "./splitExpense";
import { Command, CommandInput } from "@/components/ui/command";

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
			<div className="my-2 justify-center">
				<Button>Edit</Button>
				<SplitExpense/>
			</div>
		</div>
	);
}
