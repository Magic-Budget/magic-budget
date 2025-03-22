"use client";
import { useUserStore } from "@/stores/user-store";

import TransactionGraph from "./transactions/transactionGraph";

export default function DashboardPage() {
  const firstName = useUserStore((state) => state.firstName);
  const lastName = useUserStore((state) => state.lastName);

  return (
    <div className="m-3">
      <div>
        <h2 className="text-2xl font-bold tracking-tight">Home</h2>
        <p className="text-muted-foreground">
          Welcome {firstName} {lastName} to your dashboard
        </p>
      </div>

      <TransactionGraph />
    </div>
  );
}
