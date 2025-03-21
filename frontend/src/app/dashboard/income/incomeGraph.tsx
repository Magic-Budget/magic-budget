import {BarGraph} from "@/components/ui/barGraph";
import axios from "axios";
import { UUID } from "crypto";
import { useUserStore } from "@/stores/user-store";
import { useEffect, useState } from "react";

const config = {
  actual: {
    label: "Savings",
    color: "#2a9d90",
  }
};

export default function IncomeGraph() {
  const { id: userId, bearerToken } = useUserStore();
  const [monthlyIncomes, setmonthlyIncomes] = useState<{ month: string; actual: number }[]>([]);
  
  useEffect(() => {
    async function fetchIncomes() {
      const data = await getMonthlyIncomes(userId, bearerToken, 0, 0); // Modify start and end as needed
      setmonthlyIncomes(data);
    }
    fetchIncomes();
  }, [userId, bearerToken]);
  
  
  return (
    <div className="p-4 ">
      <h1 className="text-xl font-bold">Overview</h1>
      <BarGraph chartData={monthlyIncomes} chartConfig={config} />
    </div>
  );
}

function getMonth( date: Date): string{
  const months: string[] = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ];

  const monthIndex = date.getMonth();
  return months[monthIndex]; 
}

async function getMonthlyIncomes(
  userId: UUID,
  bearerToken: string,
  start: number,
  end: number
): Promise<{ month: string; actual: number }[]> {
  try
  {
      //get all incomes
  const response = await axios.get(
    `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}/income/view-all`,
    {
      headers: {
        Authorization: `Bearer ${bearerToken}`,
      },
    }
  );
  
  // Aggregate incomes by month
  const monthTotals: { [key: string]: number } = {};

  response.data.forEach((income: any) => {
    const month = getMonth(new Date(income.income_posted_date));

    // Aggregate the amounts for the same month
    if (monthTotals[month]) {
      monthTotals[month] += income.income_amount;
    } else {
      monthTotals[month] = income.income_amount;
    }
  });

  // Map the aggregated data into the format expected by BarGraph (Month and Actual)
  return Object.keys(monthTotals).map((month) => ({
    month: month,
    actual: monthTotals[month],
  }));
  } catch (err) {
  console.error(err);
  return [];
}
}
  
  

