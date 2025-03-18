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
import IncomeView from "./incomeView";
import { Button } from "@/components/ui/button";
import TablePagination from "@/components/ui/paginationButton";
import Income from "./(objects)/income";

interface Props {
    incomes: Income[];
}

export default function IncomeTableClient({incomes}: Props) {
    const [selectedTransactionId, setSelectedTransactionId] = useState<
        string | null
    >(null);

    const handleOpenDialog = (transactionId: string) => {
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
                        <TableHead className="w-1/8 text-white">Date</TableHead>
                        <TableHead className="w-1/5 text-white">Name</TableHead>
                        <TableHead className="w-1/5 text-white">
                            Amount
                        </TableHead>
                        <TableHead className="w-1/5 text-white">
                            Type
                        </TableHead>
                        <TableHead className="text-right text-white">
                            Description
                        </TableHead>
                    </TableRow>
                </TableHeader>

                <TableBody className="bg-gray-200">
                    {incomes.map((income) => (
                        <TableRow
                            key={"transaction-" + income.id}
                            onClick={() => handleOpenDialog(income.id)}
                        >
                            <TableCell
                                id={`transaction-${income.id}-date`}
                                className="font-medium"
                            >
                                {income.date.toLocaleDateString()}
                            </TableCell>
                            <TableCell
                                id={`transaction-${income.id}-name`}
                                className="font-bold"
                            >
                                {income.name}
                            </TableCell>
                            <TableCell
                                id={`transaction-${income.id}-amount`}
                            >
                                {`$${income.amount.toFixed(2)}`}
                            </TableCell>
                            <TableCell
                                id={`transaction-${income.id}-amount`}
                            >
                                {income.type}
                            </TableCell>
                            <TableCell
                                id={`transaction-${income.id}-description`}
                                className="text-right"
                            >
                                {income.description}
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
                        <IncomeView
                            transactionId={selectedTransactionId}
                        />
                    )}
                </DialogContent>
                <DialogFooter>
                    <Button onClick={handleCloseDialog}>Close</Button>
                </DialogFooter>
            </Dialog>
            <TablePagination />
        </div>
    );
}
