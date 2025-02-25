import React from "react";
import TransactionTable from "./transactionTable";
import TransactionGraph from "./transactionGraph";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";


export default function Transactions() {
	const queryClient = new QueryClient();
	return (
		<QueryClientProvider client={queryClient}>
				<div className="m-3">
					<h1 className="text-2xl text-bold text-align-left">Welcome!</h1>
					<div className="flex">
						<div className="w-1/3 my-3">
							<TransactionGraph />
						</div>

						<div className="m-3 w-2/3">
							<h2 className="text-lg text-bold">Transactions</h2>
							<TransactionTable start={0} end={10} />
						</div>
					</div>
				</div>
		</QueryClientProvider>
	);
}
