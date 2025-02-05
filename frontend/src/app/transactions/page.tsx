import React from "react";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import TransactionTable from "./transactionTable";

export default function Transactions() {
	return (
		<div>
			<Tabs defaultValue="transaction-table">
				<TabsList>
					<TabsTrigger value="transaction-table">Table</TabsTrigger>
					<TabsTrigger value="transaction-graph">Graph</TabsTrigger>
				</TabsList>
				
				<TabsContent value="transaction-table">
					<TransactionTable/>
				</TabsContent>


				<TabsContent value="transaction-graph">

				</TabsContent>
			</Tabs>
		</div>
	);
}
