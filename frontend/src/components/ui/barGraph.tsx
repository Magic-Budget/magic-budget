//retrived from https://ui.shadcn.com/docs/components/chart
"use client"
 
import { Bar, BarChart, CartesianGrid, XAxis } from "recharts"
 
import {
  ChartConfig,
  ChartContainer,
  ChartLegend,
  ChartLegendContent,
  ChartTooltip,
  ChartTooltipContent,
} from "@/components/ui/chart"

interface BarGraphProps {
    chartData: { month: string; actual: number; expected: number }[];
    chartConfig: ChartConfig;
  }
 
export function BarGraph({ chartData, chartConfig }: BarGraphProps) {
  return (
    <ChartContainer config={chartConfig} className="max-h-[50vh] w-full ">
      <BarChart accessibilityLayer data={chartData}>
        <CartesianGrid vertical={false} />
        <XAxis
          dataKey="month"
          tickLine={false}
          tickMargin={10}
          axisLine={false}
          tickFormatter={(value) => value.slice(0, 3)}
        />
        <ChartTooltip content={<ChartTooltipContent />} />
        <ChartLegend content={<ChartLegendContent />} />
        <Bar dataKey="actual" fill={chartConfig.actual.color} radius={4} />
        <Bar dataKey="expected" fill={chartConfig.expected.color} radius={4} />
      </BarChart>
    </ChartContainer>
  )
}