export default class Transaction{
    id:string;
    date: Date;
    name: string;
    amount: number;
    description:string;

    constructor(
        id:string,
        date:string,
        name:string,
        amount:number,
        description:string
    ){
        this.id = id
        this.date= new Date(date);
        this.name= name;
        this.amount= amount;
        this.description= description;
    }

    getDateString():string {
        return this.date.toISOString().split('T')[0];
    }
}