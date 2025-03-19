import {BarGraph} from "@/components/ui/barGraph";
const mockData = [
    { month: "Oct", actual: 180},
    { month: "Nov", actual: 220},
    { month: "Dec", actual: 190},
    { month: "Jan", actual: 210},
    { month: "Feb", actual: 250},
    { month: "Mar", actual: 300},
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