"use client";

import { useUserStore } from "@/stores/user-store";
import { useRouter } from "next/navigation";
import { useEffect, useRef } from "react";

export default function ProtectedRoute({
  children,
}: {
  children: React.ReactNode;
}) {
  const router = useRouter();
  const bearerToken = useUserStore((state) => state.bearerToken);
  const isRedirecting = useRef(false);

  useEffect(() => {
    console.log("ProtectedRoute", bearerToken);
    if (!bearerToken && !isRedirecting.current) {
      isRedirecting.current = true;
      router.replace("/login");
    }
  }, [bearerToken, router]);

  return bearerToken ? (
    <>{children}</>
  ) : (
    <div className="flex items-center justify-center h-screen">
      <p className="text-gray-500">Redirecting to login...</p>
    </div>
  );
}
