"use client"
import {BarGraph} from "@/components/ui/barGraph";
import axios from "axios";
import { UUID } from "crypto";
import { useUserStore } from "@/stores/user-store";
import { useEffect, useState } from "react";
  
  const config = {
    actual: {
      label: "Spendings",
      color: "#ff746c",
    },
  };
  
  export default function TransactionBarGraph() {
    const { id: userId, bearerToken } = useUserStore();
    const [monthlySpendings, setMonthlySpendings] = useState<{ month: string; actual: number }[]>([]);
    
    useEffect(() => {
      async function fetchSpendings() {
        const data = await getMonthlySpendings(userId, bearerToken, 0, 0); 
        setMonthlySpendings(data);
      }
      //update
      const intervalId = setInterval(() => {
        fetchSpendings();
        }, 1000);
      return () => clearInterval(intervalId); 
  
    }, [userId, bearerToken]);
    
    
    return (
      <div className="p-4 ">
        <h1 className="text-xl font-bold">Overview</h1>
        <BarGraph chartData={monthlySpendings} chartConfig={config} />
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
  
  async function getMonthlySpendings(
    userId: UUID,
    bearerToken: string,
    start: number,
    end: number
  ): Promise<{ month: string; actual: number }[]> {
    try {
      const response = await axios.get(
        `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}/expense/view-all`,
        {
          headers: {
            Authorization: `Bearer ${bearerToken}`,
          },
        }
      );
    
    // Aggregate spendings by the month
    const monthTotals: { [key: string]: number } = {};
  
    response.data.forEach((expense: any) => {
      const month = getMonth(new Date(expense.expense_posted_date));
  
      // Aggregate the amounts for the same month
      if (monthTotals[month]) {
        monthTotals[month] += expense.income_amount;
      } else {
        monthTotals[month] = expense.income_amount;
      }
    });
  
    return Object.keys(monthTotals).map((month) => ({
      month: month,
      actual: monthTotals[month],
    }));
    } catch (err) {
    console.error(err);
    return [];
  }
  }