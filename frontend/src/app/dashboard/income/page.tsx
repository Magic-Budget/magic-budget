import IncomeTable from "./incomeTable";

export default function incomes() {
  return (
    <div className="m-3">
      
      <div>
        <h2 className="text-2xl font-bold tracking-tight">Income</h2>
        <p className="text-muted-foreground">
          View your credit and track your income
        </p>
      </div>
      <div className="flex">
              <div className="m-3 w-full">
                <IncomeTable start={0} end={10} />
              </div>
      </div>
    </div>
  );
}
