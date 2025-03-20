import { test, expect } from "@playwright/test";

test("navigate goals", async ({ page }) => {
  await page.goto("http://localhost:3000/signup");

  await page.fill("input#firstName", "John");
  await page.fill("input#lastName", "Doe");
  await page.fill("input#username", "mail1@example.com");
  await page.fill("input#email", "mail1@example.com");
  await page.fill("input#password", "yourpassword");

  await Promise.all([page.click('button[type="submit"]')]);

  await page.goto("http://localhost:3000/login");

  await page.fill("input#email", "mail@example.com");
  await page.fill("input#password", "yourpassword");

  await Promise.all([page.click('button[type="submit"]')]);

  await page.goto("http://localhost:3000/dashboard/receipt");

  await expect(page).toHaveTitle("Create Next App");
});
