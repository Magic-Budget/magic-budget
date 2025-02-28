import TransactionGraph from "./transactions/transactionGraph";

export default function DashboardPage() {
  return (
    <div className="m-3">
      <div>
        <h2 className="text-2xl font-bold tracking-tight">Home</h2>
        <p className="text-muted-foreground">Weclome to your dashboard</p>
      </div>

      <TransactionGraph />
    </div>
  );
}
