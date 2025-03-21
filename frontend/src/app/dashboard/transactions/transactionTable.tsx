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
	let transacts: Transaction[] = [];
	for (let i = start; i < end; i++) {
		transacts.push({
			id: `transaction${i}`,
			date: new Date(Date.now()),
			name: `Transaction ${i}`,
			amount: 0,
			category: "Category 1",
			description:
				"According to all known laws of aviation, there is no way a bee should be able to fly. Its wings are too small to get its fat little body off the ground. The bee, of course, flies anyway, because bees don't care what humans think is impossible.",
		});
	}
	return transacts;
}

function getUsername() {
	return "username";
}
