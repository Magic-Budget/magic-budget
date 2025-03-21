import React from "react";
import TransactionTable from "./transactionTable";

export default function Transactions() {
	return (
		<div className="m-3">
			<div>
				<h2 className="text-2xl font-bold tracking-tight">
					Transactions
				</h2>
				<p className="text-muted-foreground">
					View your transactions and track your spending
				</p>
			</div>
			<div>
				<div className="w-full lg:w-[calc(100vw-250px)] max-w-full transition-all duration-300">
					<TransactionTable start={0} end={10} />
				</div>
			</div>
		</div>
	);
}
