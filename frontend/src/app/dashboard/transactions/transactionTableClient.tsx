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
			<Table>
				<TableHeader>
					<TableRow className="bg-gray-600">
						<TableHead className="w-28 text-white">Date</TableHead>
						<TableHead className="w-56 text-white">Name</TableHead>
						<TableHead className="w-56 text-white">
							Amount
						</TableHead>
						<TableHead className="w-56 text-white">
							Category
						</TableHead>
						<TableHead className="w-96 text-right text-white">
							Description
						</TableHead>
					</TableRow>
				</TableHeader>

				<TableBody className="bg-gray-200">
					{transactions.map((transaction) => (
						<TableRow
							key={"transaction-" + transaction.id}
							onClick={() => handleOpenDialog(transaction.id)}
						>
							<TableCell
								id={`transaction-${transaction.id}-date`}
								className="font-medium"
							>
								{transaction.date.toLocaleDateString()}
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
