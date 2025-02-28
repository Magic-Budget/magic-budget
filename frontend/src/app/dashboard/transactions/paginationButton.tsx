import { Pagination, PaginationContent, PaginationItem, PaginationLink, PaginationNext, PaginationPrevious } from "@/components/ui/pagination";
import TransactionTable from "./transactionTable";


export default function TablePagination(){


    return (
		<div>
			<Pagination className="m-2">
				<PaginationContent className="rounded-sm bg-gray-100">
					<PaginationItem>
						<PaginationPrevious href="#" />
					</PaginationItem>
					{[1, 2, 3].map((item) => (
						<PaginationItem key={item}>
							<PaginationLink href="#">{item}</PaginationLink>
						</PaginationItem>
					))}
					<PaginationItem>
						<PaginationNext href="#" />
					</PaginationItem>
				</PaginationContent>
			</Pagination>
		</div>
	);
}