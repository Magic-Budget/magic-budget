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
import { CalendarClock, Flag, Target } from "lucide-react";

interface GoalsListProps {
  goals: Goal[];
}

export default function GoalsList({ goals }: GoalsListProps) {
  return (
    <div className="flex flex-wrap justify-start gap-4 w-full">
      {goals.map((goal: Goal) => (
        <Card key={goal.id} className="w-64">
          <CardHeader>
            <CardTitle className="flex items-center gap-1">
              {goal.name}
            </CardTitle>
            <CardDescription className="flex items-center gap-1">
              <CalendarClock className="h-4 w-4" /> {goal.getDateString()}
            </CardDescription>
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
