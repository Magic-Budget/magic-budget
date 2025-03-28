import { test, expect } from "@playwright/test";

test("has title", async ({ page }) => {
  await page.goto("http://localhost:3000/signup");

  // Expect the page title to be exactly "Create Account".
  await expect(page).toHaveTitle("Create Account");
});

test("signup form submission works correctly", async ({ page }) => {
  await page.goto("http://localhost:3000/signup");

  // Fill in the username field using its id.
  await page.fill("input#username", "yourusername");

  // Fill in the email field using its id.
  await page.fill("input#email", "mail@example.com");

  await page.fill("input#password", "yourpassword");

  await Promise.all([page.click('button[type="submit"]')]);
});
