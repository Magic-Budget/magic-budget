export default class Goal {
  id: string;
  due: Date;
  name: string;
  currAmount: number;
  targetPrice: number;

  constructor(
    id: string,
    due: string,
    name: string,
    currAmount: number,
    targetPrice: number,
  ) {
    this.id = id;
    this.due = new Date(due);
    this.name = name;
    this.currAmount = currAmount;
    this.targetPrice = targetPrice;
  }

  getDateString(): string {
    return this.due.toISOString().split("T")[0];
  }
}
