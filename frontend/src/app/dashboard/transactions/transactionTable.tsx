import axios from "axios";
import Transaction from "./(objects)/transaction";
import TransactionTableClient from "./transactionTableClient";

interface Props {
	start: number;
	end: number;
}

export default async function TransactionTable(props: Props) {
	let username = getUsername();
	const transactions = await getTransactions(
		username,
		props.start,
		props.end
	);

	return <TransactionTableClient transactions={transactions} />;
}

async function getTransactions(
	username: string,
	start: number,
	end: number
): Promise<Transaction[]> {
	let transactions: Transaction[] = [];
	axios
		.get(
			`${process.env.NEXT_PUBLIC_API_URL}/api/${username}/expense/view-all`
		)
		.then((response) => {
			transactions = response.data.slice(start, end);
		})
		.catch((err) => {
			console.log(err);
		});
	return transactions;
}

function getUsername() {
	return "username";
}
