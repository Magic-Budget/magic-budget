// Used the prebuild login form from Shadcn https://ui.shadcn.com/blocks/login
// to create this all credit goes to the shadcn project.
import { LoginForm } from "@/app/components/login-form";

export const metadata = {
  title: "Login",
  description: "Log into your account",
};

export default function Page() {
  return (
    <div className="flex min-h-svh w-full items-center justify-center p-6 md:p-10">
      <div className="w-full max-w-sm">
        <LoginForm />
      </div>
    </div>
  );
}
