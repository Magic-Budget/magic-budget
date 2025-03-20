/**
 * This file is based in its entirety on the demo code on ShadCN documentation
 * Retrieved from: https://ui.shadcn.com/charts
 */

"use client";

import * as React from "react";
import { TrendingUp } from "lucide-react";
import { Label, Pie, PieChart } from "recharts";
import axios from 'axios'

import {
	Card,
	CardContent,
	CardDescription,
	CardFooter,
	CardHeader,
	CardTitle,
} from "@/components/ui/card";

import {
	ChartConfig,
	ChartContainer,
	ChartTooltip,
	ChartTooltipContent,
} from "@/components/ui/chart";
import { error } from "console";
import { Amarante } from "next/font/google";
import { useEffect, useMemo, useState } from "react";

interface ChartDataItem {
	category: string;
	amount: number;
	fill: string;
}

const chartConfig = getChartConfig();

function getChartConfig(): ChartConfig {
	return {
		totalAmount: {
			label: "Total Amount",
		},
		groceries: {
			label: "Groceries",
			color: "hsl(var(--chart-1))",
		},
		transportation: {
			label: "Transp.",
			color: "hsl(var(--chart-2))",
		},
		restaurants: {
			label: "Eat Outside",
			color: "hsl(var(--chart-3))",
		},
		housing: {
			label: "Housing",
			color: "hsl(var(--chart-4))",
		},
		utilities: {
			label: "Utilities",
			color: "hsl(var(--chart-5))",
		},
		other: {
			label: "Other",
			color: "hsl(var(--chart-6))",
		},
	};
}

const apiURL = "/api/transactions/category_totals";

export default function TransactionGraph() {
	const [chartData, setChartData] = useState<ChartDataItem[]>([]);

	useEffect(()=>{
		axios
			.get(apiURL)
			.then((response) => {
				setChartData( response.data.map(
					(total: { category: string; total: string }) => ({
						category: total.category,
						amount: total.total,
						fill: `var(--color-${total.category})`,
					})
				))
			})
			.catch(() => {
				console.log("Could not get category totals");
			});
	},
	[]
	)
	
	const totalSpent = useMemo(() => {
		return chartData.reduce((acc, { amount }) => acc + amount, 0);
	}, [chartData]); 

	return (
		<Card className="flex flex-col">
			<CardHeader className="items-center pb-0">
				<CardTitle>Expenses</CardTitle>
				<CardDescription>Past Month</CardDescription>
			</CardHeader>
			<CardContent className="flex-1 pb-0">
				<ChartContainer
					config={chartConfig}
					className="mx-auto aspect-square max-h-[250px]"
				>
					<PieChart>
						<ChartTooltip
							cursor={false}
							content={<ChartTooltipContent hideLabel />}
						/>
						<Pie
							data={chartData}
							dataKey="amount"
							nameKey="category"
							innerRadius={60}
							strokeWidth={5}
						>
							<Label
								content={({ viewBox }) => {
									if (
										viewBox &&
										"cx" in viewBox &&
										"cy" in viewBox
									) {
										return (
											<text
												x={viewBox.cx}
												y={viewBox.cy}
												textAnchor="middle"
												dominantBaseline="middle"
											>
												<tspan
													x={viewBox.cx}
													y={viewBox.cy}
													className="fill-foreground text-3xl font-bold"
												>
													${totalSpent.toLocaleString()}
												</tspan>
												<tspan
													x={viewBox.cx}
													y={(viewBox.cy || 0) + 24}
													className="fill-muted-foreground"
												>
													spent this month
												</tspan>
											</text>
										);
									}
								}}
							/>
						</Pie>
					</PieChart>
				</ChartContainer>
			</CardContent>
			<CardFooter className="flex-col gap-2 text-sm">
				<div className="flex items-center gap-2 font-medium leading-none">
					Trending up by 5.2% this month{" "}
					<TrendingUp className="h-4 w-4" />
				</div>
				<div className="leading-none text-muted-foreground">
					Showing total visitors for the last 6 months
				</div>
			</CardFooter>
		</Card>
	);
}
