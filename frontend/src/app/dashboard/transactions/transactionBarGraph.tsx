import {BarGraph} from "@/components/ui/barGraph";
const mockData = [
  { month: "October", actual: 180},
  { month: "November", actual: 220},
  { month: "December", actual: 190},
  { month: "January", actual: 210},
  { month: "February", actual: 250},
  { month: "Marchh", actual: 300},
  ];
  
  const config = {
    actual: {
      label: "Spendings",
      color: "#ff746c",
    },
  };

export default function TransactionBarGraph() {
  return (
    <div className="p-4">
      <h1 className="text-xl font-bold">Overview</h1>
      <BarGraph chartData={mockData} chartConfig={config} />
    </div>
  );
}