import { Progress } from "@/components/ui/progress";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
  CardDescription,
} from "@/components/ui/card";
import Goal from "./objects/goal";

interface GoalsListProps {
  goals: Goal[];
}

export default function GoalsList({ goals }: GoalsListProps) {
  return (
    <div className="space-y-6">
      {goals.map((goal: Goal) => (
        <Card key={goal.id} className="w-full">
          <CardHeader>
            <CardTitle>Goal: {goal.name}</CardTitle>
            <CardDescription>Save By: {goal.getDateString()}</CardDescription>
          </CardHeader>

          <CardContent>
            <Progress
              value={mapToProgress(goal.currAmount, goal.targetPrice)}
              className="h-2"
            />
          </CardContent>

          <CardFooter className="flex justify-between">
            <p className="text-lg font-medium">${goal.currAmount}</p>
            <p className="text-lg font-medium">${goal.targetPrice}</p>
          </CardFooter>
        </Card>
      ))}
    </div>
  );
}

function mapToProgress(amount: number, target: number) {
  let newVal: number;
  newVal = amount * (100 / target);
  newVal = Math.min(100, newVal);
  return newVal;
}
