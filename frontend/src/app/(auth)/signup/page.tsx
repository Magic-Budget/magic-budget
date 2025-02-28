import { SignupForm } from "@/app/components/signup-form";

export const metadata = {
  title: "Create Account",
  description: "Sign up for a new account",
};

export default function Page() {
  return (
    <div className="flex min-h-svh w-full items-center justify-center p-6 md:p-10">
      <div className="w-full max-w-sm">
        <SignupForm />
      </div>
    </div>
  );
}