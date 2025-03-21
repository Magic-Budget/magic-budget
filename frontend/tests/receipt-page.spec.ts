import { test, expect } from "@playwright/test";

test("navigate receipt", async ({ page }) => {
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

  await page.goto("http://localhost:3000/dashboard/receipt");
  const sidebarToggle = page.locator('button[name="Toggle Sidebar"]');
  
  if (await sidebarToggle.isVisible()) {
    await sidebarToggle.click();
  }
  await expect(page.locator('button:text("Refresh")')).toBeTruthy();
  await expect(page.locator('button:text("Upolad file")')).toBeTruthy();
});
