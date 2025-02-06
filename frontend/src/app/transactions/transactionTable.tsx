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
import TablePagination from "./paginationButton";

interface Props{
	start:number;
	end:number;
}

export default async function TransactionTable(props:Props) {
	let username = getUsername();
	return (
		<div className="mx-3">
			<Table>
				<TableHeader>
					<TableRow className="bg-gray-600">
						<TableHead className="w-25 text-white">Date</TableHead>
						<TableHead className="text-white">Name</TableHead>
						<TableHead className="text-white w-">Amount</TableHead>
						<TableHead className="text-white w-">Category</TableHead>
						<TableHead className="text-right text-white">
							Description
						</TableHead>
					</TableRow>
				</TableHeader>

				<TableBody className="bg-gray-200">
					{(
						await getTransactions(username, props.start, props.end)
					).map((transaction: Transaction) => (
						<TableRow key={"transaction-" + transaction.id}>
							<TableCell
								id={`transaction-${transaction.id}-date`}
								className="font-medium"
							>
								{transaction.getDateString()}
							</TableCell>
							<TableCell
								id={`transaction-${transaction.id}-name`}
								className="font-bold"
							>
								{transaction.name}
							</TableCell>
							<TableCell
								id={`transaction-${transaction.id}-amount`}
							>
								{`$${transaction.amount.toFixed(2)}`}
							</TableCell>
							<TableCell
								id={`transaction-${transaction.id}-amount`}
							>
								{transaction.category}
							</TableCell>
							<TableCell
								id={`transaction-${transaction.id}-description`}
								className="text-right"
							>
								{transaction.description}
							</TableCell>
						</TableRow>
					))}
				</TableBody>
			</Table>
			<TablePagination></TablePagination>
		</div>
	);
}


async function getTransactions(username:string, start:number, end:number):Promise<Transaction[]> {
	let transacts:Transaction[] = [];
	for (let i = start; i<end; i++){
		transacts.push(new Transaction(
			`transaction${i}`,
			new Date(Date.now()).toISOString(),
			`Transaction ${i}`,
			0,
			"Category 1",
			"This is a test"
		))
	}
	return transacts
}

function getUsername(){
	return "username";
}