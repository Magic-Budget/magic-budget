import React from "react";
import TransactionTable from "./transactionTable";
import { Button } from "@/components/ui/button";
import { Dialog } from "@radix-ui/react-dialog";
import TransactionBarGraph from "./transactionBarGraph";

export default function Transactions() {
	return (
		<div className="m-3 w-[90%] lg:w-[calc(100vw-320px)] max-w-full">
			<div className="flex justify-between">
				<div>
					<h2 className="text-2xl font-bold tracking-tight">
						Transactions
					</h2>
					<p className="text-muted-foreground">
						View your transactions and track your spending
					</p>
				</div>
				<div>
					<Button>Add Expense</Button>
				</div>
			</div>
				<div>
					<TransactionBarGraph />
				</div>
			<div>
				<div className="">
					<TransactionTable start={0} end={10} />
				</div>
			</div>
		</div>
	);
}
