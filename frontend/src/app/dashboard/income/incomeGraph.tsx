import {BarGraph} from "@/components/ui/barGraph";
const mockData = [
    { month: "Oct", actual: 180, expected:  250},
    { month: "Nov", actual: 220, expected:  300},
    { month: "Dec", actual: 190, expected:  280},
    { month: "Jan", actual: 210, expected:  350},
    { month: "Feb", actual: 250, expected:  400},
    { month: "Mar", actual: 300, expected:  450},
  ];
  
  const config = {
    actual: {
      label: "Actual Savings",
      color: "#2a9d90",
    },
    expected: {
      label: "Expected Savings",
      color: "#274754",
    },
  };

export default function IncomeGraph() {
  return (
    <div className="p-4">
      <h1 className="text-xl font-bold">Overview</h1>
      <BarGraph chartData={mockData} chartConfig={config} />
    </div>
  );
}