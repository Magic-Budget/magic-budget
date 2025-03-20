export default class Income{
    id:string;
    amount: number;
    name: string;
    description:string;
    date: Date;
    type:string;
    constructor(
        id:string,
        amount: number,
        name: string,
        description:string,
        date: string,
        type:string
    ){
        this.id = id
        this.date= new Date(date);
        this.name= name;
        this.amount= amount;
        this.description= description;
        this.type = type;
    }

    getDateString():string {
        return this.date.toISOString().split('T')[0];
    }
}