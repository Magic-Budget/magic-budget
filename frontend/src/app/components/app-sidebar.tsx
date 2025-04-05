import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupContent,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/components/ui/sidebar";
import {
  Home,
  ScrollText,
  Target,
  Receipt,
  Split,
  PiggyBank,
  BotMessageSquare
} from "lucide-react";
import Link from "next/link";

const items = [
  {
    title: "Home",
    url: "/dashboard",
    icon: Home,
  },
  {
    title: "Transactions",
    url: "/dashboard/transactions",
    icon: ScrollText,
  },
  {
    title: "Income",
    url: "/dashboard/income",
    icon: PiggyBank,
  },
  {
    title: "Goals",
    url: "/dashboard/goals",
    icon: Target,
  },
  {
    title: "Receipt",
    url: "/dashboard/receipt",
    icon: Receipt,
  },
  {
    title: "Split",
    url: "/dashboard/split",
    icon: Split,
  },
  {
    title: "Support Chat",
    url: "/dashboard/chat",
    icon: BotMessageSquare,
  }
];

export function AppSidebar() {
  return (
    <Sidebar className="w-64 min-h-screen bg-white border-r shadow-md">
      <SidebarHeader className="p-4 text-lg font-bold">
        Magic Budget🔮
      </SidebarHeader>
      <SidebarContent>
        <SidebarGroup />
        <SidebarGroupContent>
          <SidebarMenu className="space-y-2">
            {items.map((item) => (
              <SidebarMenuItem
                key={item.title}
                className="hover:bg-gray-100 rounded-lg"
              >
                <SidebarMenuButton asChild>
                  <Link
                    href={item.url}
                    className="flex items-center gap-3 px-4 py-2 text-gray-700"
                  >
                    <item.icon className="w-5 h-5 text-gray-600" />
                    <span className="text-sm font-medium">{item.title}</span>
                  </Link>
                </SidebarMenuButton>
              </SidebarMenuItem>
            ))}
          </SidebarMenu>
        </SidebarGroupContent>
        <SidebarGroup />
      </SidebarContent>
      <SidebarFooter className="p-4 text-sm text-gray-500">
        © 2025 MagicBudget
      </SidebarFooter>
    </Sidebar>
  );
}
