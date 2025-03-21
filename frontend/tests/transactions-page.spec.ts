import { test, expect } from "@playwright/test";

test("navigate transactions", async ({ page }) => {
  await page.goto("http://localhost:3000/signup");

  await page.fill("input#firstName", "John");
  await page.fill("input#lastName", "Doe");
  await page.fill("input#username", "mail@example.com");
  await page.fill("input#email", "mail@example.com");
  await page.fill("input#password", "yourpassword");

  await Promise.all([page.click('button[type="submit"]')]);

  await page.goto("http://localhost:3000/login");

  await page.fill("input#email", "mail@example.com");
  await page.fill("input#password", "yourpassword");

  await Promise.all([page.click('button[type="submit"]')]);

  await page.goto("http://localhost:3000/dashboard/transactions");

  await page.waitForLoadState("networkidle");

  await expect(page.getByText("Transactions")).toBeTruthy();

  await expect(page.locator("table")).toBeTruthy();
  await expect(page.locator('button:text("Add Transaction")')).toBeTruthy();

  await expect(page.getByRole('cell', { name: 'Date' }).isVisible()).toBeTruthy();
  await expect(page.getByRole('cell', { name: 'Name' }).isVisible()).toBeTruthy();
  await expect(page.getByRole('cell', { name: 'Category'}).isVisible()).toBeTruthy();
  await expect(page.getByRole('cell', { name: 'Description' }).isVisible()).toBeTruthy();
  const sidebarToggle = page.locator('button[name="Toggle Sidebar"]');
  
  if (await sidebarToggle.isVisible()) {
    await sidebarToggle.click();
  }
});