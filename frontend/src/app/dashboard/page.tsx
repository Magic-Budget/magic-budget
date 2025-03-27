"use client";
import { useUserStore } from "@/stores/user-store";

import TransactionGraph from "./transactions/transactionGraph";
import { useEffect, useState } from "react";
import axios from "axios";
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
} from "@/components/ui/card";
import { Progress } from "@radix-ui/react-progress";

interface Stats {
  averageIncome: number;
  averageExpense: number;
  totalTarget: number;
  totalAchieved: number;
}

export default function DashboardPage() {
  const { firstName, lastName, id: userId, bearerToken } = useUserStore();
  const [stats, setStats] = useState<Stats | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const response = await axios.get(
          `${process.env.NEXT_PUBLIC_API_URL}/api/${userId}/stats`,
          {
            headers: {
              Authorization: `Bearer ${bearerToken}`,
            },
          }
        );
        setStats(response.data);
        console.log("Stats:", response.data);
      } catch (error) {
        console.error("Error fetching stats:", error);
      } finally {
        setLoading(false);
      }
    };

    if (userId) {
      fetchStats();
    }
  }, [userId, bearerToken]);

  const savingsProgress =
    stats && stats.totalTarget > 0
      ? Math.min((stats.totalAchieved / stats.totalTarget) * 100, 100)
      : 0;

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat("en-US", {
      style: "currency",
      currency: "USD",
    }).format(value);
  };

  return (
    <div className="m-3">
      <div>
        <h2 className="text-2xl font-bold tracking-tight">Home</h2>
        <p className="text-muted-foreground">
          Welcome {firstName} {lastName} to your dashboard
        </p>
      </div>

      {loading ? (
        <div className="flex items-center justify-center h-40">
          <p>Loading stats...</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium">
                Average Income
              </CardTitle>
              <CardDescription>Monthly average</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-green-600">
                {stats ? formatCurrency(stats.averageIncome) : "$0.00"}
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium">
                Average Expense
              </CardTitle>
              <CardDescription>Monthly average</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-red-600">
                {stats ? formatCurrency(stats.averageExpense) : "$0.00"}
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="pb-2">
              <CardTitle className="text-sm font-medium">
                Savings Goal
              </CardTitle>
              <CardDescription>Progress toward target</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-2">
                <div className="flex justify-between">
                  <span className="text-2xl font-bold text-blue-600">
                    {stats ? formatCurrency(stats.totalAchieved) : "$0.00"}
                  </span>
                  <span className="text-sm text-muted-foreground text-blue-600">
                    of {stats ? formatCurrency(stats.totalTarget) : "$0.00"}
                  </span>
                </div>
                <Progress
                  value={isNaN(savingsProgress) ? 0 : savingsProgress}
                  className="h-2"
                />
                <p className="text-xs text-muted-foreground text-right">
                  {savingsProgress.toFixed(1)}% of target
                </p>
              </div>
            </CardContent>
          </Card>
        </div>
      )}
    </div>
  );
}
