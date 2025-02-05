import {
	Table,
	TableBody,
	TableCaption,
	TableCell,
	TableFooter,
	TableHead,
	TableHeader,
	TableRow,
} from "@/components/ui/table";
import Transaction from "./(objects)/transaction";

export default function TransactionTable() {

    const transactions = getTransactions({username:"username", end:6});

	return (
		<div className="items-align-center mx-3 w-4/5">
			<Table>
				<TableHeader>
					<TableRow>
						<TableHead className="w-[150px]">Date</TableHead>
						<TableHead>Name</TableHead>
						<TableHead>Amount</TableHead>
						<TableHead className="text-right">
							Description
						</TableHead>
					</TableRow>
				</TableHeader>

				<TableBody>
					{transactions.map((transaction: Transaction) => (
						<TableRow key={transaction.id + transaction.name}>
							<TableCell className="font-medium">
								{transaction.getDateString()}
							</TableCell>
							<TableCell className="font-bold">
								{transaction.name}
							</TableCell>
							<TableCell>{transaction.amount}</TableCell>
							<TableCell className="text-right">
								{transaction.description}
							</TableCell>
						</TableRow>
					))}
				</TableBody>
			</Table>
		</div>
	);
}


function getTransactions({username, start = 0, end=10}:{username:string, start?:number, end?:number}):Transaction[] {
    let transactions = []
    for (let i = start; i < end; i++){
        transactions.push(
			new Transaction(
                i.toString(),
				new Date(Date.now()).toISOString(),
				"Transaction " + i,
                0,
                "This is a test transaction"
			)
		);
    }
    return transactions;
}
