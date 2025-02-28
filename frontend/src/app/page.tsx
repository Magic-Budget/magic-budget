import Link from "next/link";

export default function Home() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-center">
      <header className=" bg-white sticky top-0 z-40 border-b border-transparent transition duration-200 ease-in-out animate-header-slide">
        <div className="mx-auto w-full max-w-7xl px-52">
          <div className="flex items-center justify-between transition duration-500 ease-in-out md:flex">
            <nav className="flex gap-4">
              <Link href="/login">Sign in</Link>
              <Link href="/signup">Get Started</Link>
            </nav>
          </div>
        </div>
      </header>

      <section className="flex flex-col items-center justify-center px-4 py-32 text-center">
        <div className="text-center">
          <h1 className="mb-6 text-6xl font-bold tracking-tight">
            Take Control of Your
            <span className="bg-gradient-to-r from-purple-400 to-pink-500 bg-clip-text text-transparent">
              {" "}
              Finances
            </span>
          </h1>
          <h2 className="mb-8 mx-auto max-w-2xl text-xl text-gray-600 dark:text-gray-300">
            Manage your expenses, track transactions, and achieve your financial
            goals effortlessly with AI-powered budgeting and insights.
          </h2>
        </div>
        <Link
          href="/signup"
          className="px-4 py-2 bg-purple-500 text-white rounded-md hover:bg-purple-600"
        >
          Get Started
        </Link>
      </section>
    </main>
  );
}
