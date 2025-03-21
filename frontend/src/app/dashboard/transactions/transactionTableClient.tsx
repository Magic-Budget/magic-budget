"use client";

import { useState } from "react";
import {
	Table,
	TableBody,
	TableCell,
	TableHead,
	TableHeader,
	TableRow,
} from "@/components/ui/table";
import {
	Dialog,
	DialogContent,
	DialogFooter,
	DialogHeader,
	DialogTitle,
} from "@/components/ui/dialog";
import TransactionView from "./transactionView";
import { Button } from "@/components/ui/button";
import TablePagination from "./paginationButton";
import Transaction from "./(objects)/transaction";
import { UUID } from "crypto";
import { Skeleton } from "@/components/ui/skeleton";

interface Props {
	transactions: Transaction[];
}

export default function TransactionTableClient({ transactions }: Props) {
	const [selectedTransactionId, setSelectedTransactionId] =
		useState<UUID | null>(null);

	const handleOpenDialog = (transactionId: UUID) => {
		setSelectedTransactionId(transactionId);
	};

	const handleCloseDialog = () => {
		setSelectedTransactionId(null);
	};

	return (
		<div>
			<Table className="m-3 w-11/12">
				<TableHeader>
					<TableRow className="bg-gray-600">
						<TableHead className="text-white">Date</TableHead>
						<TableHead className="text-white">Name</TableHead>
						<TableHead className="text-white">Amount</TableHead>
						<TableHead className="text-white">Category</TableHead>
						<TableHead className="text-right text-white">
							Description
						</TableHead>
					</TableRow>
				</TableHeader>

				<TableBody className="bg-gray-200">
					{transactions.length > 0 ? (
						transactions.map((transaction) => (
							<TableRow
								key={"transaction-" + transaction.id}
								onClick={() => handleOpenDialog(transaction.id)}
							>
								<TableCell
									id={`transaction-${transaction.id}-date`}
									className="font-medium"
								>
									{(() => {
										try {
											return transaction.date.toLocaleDateString();
										} catch (error) {
											console.log(error);
											return "No date";
										}
									})()}
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
									id={`transaction-${transaction.id}-category`}
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
						))
					) : (
						<>
							{[...Array(5)].map((_, index) => (
								<TableRow key={`skeleton-${index}`}>
									<TableCell>
										<Skeleton className="h-4 w-full" />
									</TableCell>
									<TableCell>
										<Skeleton className="h-4 w-full" />
									</TableCell>
									<TableCell>
										<Skeleton className="h-4 w-full" />
									</TableCell>
									<TableCell>
										<Skeleton className="h-4 w-full" />
									</TableCell>
									<TableCell>
										<Skeleton className="h-4 w-full" />
									</TableCell>
								</TableRow>
							))}
						</>
					)}
				</TableBody>
			</Table>
			<Dialog
				open={selectedTransactionId !== null}
				onOpenChange={handleCloseDialog}
			>
				<DialogContent>
					<DialogHeader>
						<DialogTitle>Transaction</DialogTitle>
					</DialogHeader>
					{selectedTransactionId && (
						<TransactionView
							transactionId={selectedTransactionId}
						/>
					)}
				</DialogContent>
			</Dialog>
			<TablePagination />
		</div>
	);
}
