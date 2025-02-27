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
import { useQuery, useQueryClient } from "@tanstack/react-query";

interface Props{
	start:number;
	end:number;
}
export default async function TransactionTable(props:Props) {
	let username = getUsername();
	const transactions  = getTransactions(
		username,
		props.start,
		props.end
	);
	return (
		<div>
			<Table>
				<TableHeader>
					<TableRow className="bg-gray-600">
						<TableHead className="w-1/8 text-white">Date</TableHead>
						<TableHead className="w-1/5 text-white">Name</TableHead>
						<TableHead className="w-1/5 text-white">Amount</TableHead>
						<TableHead className="w-1/5 text-white ">Category</TableHead>
						<TableHead className="text-right text-white">Description</TableHead>
					</TableRow>
				</TableHeader>

				<TableBody className="bg-gray-200">
					{(await transactions)?.map((transaction: Transaction) => (
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
		transacts.push(
			new Transaction(
				`transaction${i}`,
				new Date(Date.now()).toISOString(),
				`Transaction ${i}`,
				0,
				"Category 1",
				"According to all known laws of aviation, there is no way a bee should be able to fly. Its wings are too small to get its fat little body off the ground. The bee, of course, flies anyway, because bees don't care what humans think is impossible."
			)
		);
	}
	return transacts
}

function getUsername(){
	return "username";
}