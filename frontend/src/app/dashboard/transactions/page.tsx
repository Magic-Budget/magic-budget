import React from "react";
import TransactionTable from "./transactionTable";
import TransactionGraph from "./transactionGraph";
import TransactionBarGraph from "./transactionBarGraph";

export default function Transactions() {
  return (
    <div className="m-3">
      <div>
        <h2 className="text-2xl font-bold tracking-tight">Transactions</h2>
        <p className="text-muted-foreground">
          View your transactions and track your spending
        </p>
        <div>
          <TransactionBarGraph/>
        </div>
      </div>
      <div className="flex">
        <div className="m-3 w-full">
          <TransactionTable start={0} end={10} />
        </div>
      </div>
    </div>
  );
}
