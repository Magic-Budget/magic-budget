import React from "react";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import TransactionTable from "./transactionTable";
import TablePagination from "./paginationButton";
import TransactionGraph from "./transactionGraph";

export default function Transactions() {
	return (
		<div className="flex justify-center m-3">
			<Tabs
				defaultValue="transaction-table"
				className="w-5/6 items-align-center"
			>
				<TabsList>
					<TabsTrigger value="transaction-table">Table</TabsTrigger>
					<TabsTrigger value="transaction-graph">Graph</TabsTrigger>
				</TabsList>

				<TabsContent value="transaction-table">
					<TransactionTable start={0} end={5} />
				</TabsContent>

				<TabsContent value="transaction-graph">
					<TransactionGraph />
				</TabsContent>
			</Tabs>
		</div>
	);
}
