import { test, expect } from "@playwright/test";

test("has title", async ({ page }) => {
  await page.goto("http://localhost:3000/login");

  // Expect the page title to be exactly "Login".
  await expect(page).toHaveTitle("Login");
});

test("login form submission works correctly", async ({ page }) => {
  await page.goto("http://localhost:3000/login");

  // Fill in the email field using its id.
  await page.fill("input#email", "mail@example.com");

  // Fill in the password field using its id.
  await page.fill("input#password", "yourpassword");

  await Promise.all([
    page.click('button[type="submit"]'),
  ]);
});
