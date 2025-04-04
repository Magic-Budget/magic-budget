import datetime
from locust import HttpUser, task, between
import random
import string

def random_string(length=8):
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

class AuthLoadTest(HttpUser):
    wait_time = between(1, 3)

    @task
    def register_and_login(self):
        username = f"{random_string()}@example.com"
        password = "password"

        # Register the user
        register_payload = {
            "username": username,
            "password": password,
            "firstName": "John",
            "lastName": "Doe",
            "email": username
        }

        headers = {"Content-Type": "application/json"}
        register_response = self.client.post("/api/auth/register", 
                                           json=register_payload, 
                                           headers=headers,
                                           name="/api/auth/register")
        
        # Then attempt to login with the same credentials
        login_payload = {
            "username": username,
            "password": password
        }
        
        self.client.post("/api/auth/sign-in", 
                        json=login_payload, 
                        headers=headers,
                        name="/api/auth/sign-in")

class StatsLoadTest(HttpUser):
    wait_time = between(1, 3)

    @task
    def register_and_login(self):
        username = f"{random_string()}@example.com"
        password = "password"

        # Register the user
        login_payload= {
            "username": username,
            "password": password,
            "firstName": "John",
            "lastName": "Doe",
            "email": username
        }

        headers = {"Content-Type": "application/json"}
        login_response = self.client.post("/api/auth/sign-in", 
                        json=login_payload, 
                        headers=headers,
                        name="/api/auth/sign-in")
        
        if login_response.status_code == 200:
            login_data = login_response.json()
            self.token = login_data.get("token")
            self.user_id = login_data.get("userId")
            # Update headers with Authorization for subsequent requests
            self.headers = {
                "Content-Type": "application/json",
                "Authorization": f"Bearer {self.token}"
            }

    @task
    def get_stats(self):
        # Only proceed if we have a token and user_id from successful login
        if hasattr(self, 'token') and hasattr(self, 'user_id'):
            # Call the stats endpoint with the user's ID and authorization token
            response = self.client.get(
                f"/api/{self.user_id}/stats",
                headers=self.headers,
                name="/api/{userId}/stats"
            )

class SavingGoalLoadTest(HttpUser):
    wait_time = between(1, 3)

    def on_start(self):
        username = f"{random_string()}@example.com"
        password = "password"

        # Register the user first
        register_payload = {
            "username": username,
            "password": password,
            "firstName": "John",
            "lastName": "Doe",
            "email": username
        }

        headers = {"Content-Type": "application/json"}
        self.client.post("/api/auth/register", 
                        json=register_payload, 
                        headers=headers,
                        name="/api/auth/register")
        
        # Then login
        login_payload = {
            "username": username,
            "password": password
        }
        
        login_response = self.client.post("/api/auth/sign-in", 
                        json=login_payload, 
                        headers=headers,
                        name="/api/auth/sign-in")
        
        if login_response.status_code == 200:
            login_data = login_response.json()
            self.token = login_data.get("token")
            self.user_id = login_data.get("userId")
            # Update headers with Authorization for subsequent requests
            self.headers = {
                "Content-Type": "application/json",
                "Authorization": f"Bearer {self.token}"
            }

    @task(3)
    def get_saving_goals(self):
        if hasattr(self, 'user_id') and hasattr(self, 'headers'):
            self.client.get(
                f"/api/saving-goals/user/{self.user_id}",
                headers=self.headers,
                name="/api/saving-goals/user/{userId}"
            )
    
    @task(1)
    def create_saving_goal(self):
        if hasattr(self, 'user_id') and hasattr(self, 'headers'):
            goal_payload = {
                "name": f"Goal {random_string(6)}",
                "amount": random.randint(100, 10000)
            }
            
            response = self.client.post(
                f"/api/saving-goals/{self.user_id}",
                json=goal_payload,
                headers=self.headers,
                name="/api/saving-goals/{userId}"
            )
            
            # Store the goal ID for potential future requests
            if response.status_code == 201:
                goal_data = response.json()
                self.goal_id = goal_data.get("id")
    
    @task(2)
    def get_saving_goal_by_id(self):
        if hasattr(self, 'headers') and hasattr(self, 'goal_id'):
            self.client.get(
                f"/api/saving-goals/{self.goal_id}",
                headers=self.headers,
                name="/api/saving-goals/{goalId}"
            )

class ExpenseLoadTest(HttpUser):
    wait_time = between(1, 3)

    def on_start(self):
        username = f"{random_string()}@example.com"
        password = "password"

        # Register the user first
        register_payload = {
            "username": username,
            "password": password,
            "firstName": "John",
            "lastName": "Doe",
            "email": username
        }

        headers = {"Content-Type": "application/json"}
        self.client.post("/api/auth/register", 
                        json=register_payload, 
                        headers=headers,
                        name="/api/auth/register")
        
        # Then login
        login_payload = {
            "username": username,
            "password": password
        }
        
        login_response = self.client.post("/api/auth/sign-in", 
                        json=login_payload, 
                        headers=headers,
                        name="/api/auth/sign-in")
        
        if login_response.status_code == 200:
            login_data = login_response.json()
            self.token = login_data.get("token")
            self.user_id = login_data.get("userId")
            # Update headers with Authorization for subsequent requests
            self.headers = {
                "Content-Type": "application/json",
                "Authorization": f"Bearer {self.token}"
            }
            # Initialize a list to store created expense IDs
            self.expense_ids = []

    @task(1)
    def add_expense(self):
        if hasattr(self, 'user_id') and hasattr(self, 'headers'):
            # Create a random expense
            expense_payload = {
                "amount": random.randint(10, 1000),
                "expenseDate": datetime.datetime.now().isoformat(),
                "expenseName": f"Expense {random_string(6)}",
                "category": random.choice(["GROCERY", "TRAVEL", "HEALTH", "FITNESS"]),
                "expenseDescription": f"Description {random_string(10)}",
                "shopName": f"Shop {random_string(5)}"
            }
            
            response = self.client.post(
                f"/api/{self.user_id}/expense/add-expense",
                json=expense_payload,
                headers=self.headers,
                name="/api/{userId}/expense/add-expense"
            )
            
            # After adding an expense, get all expenses to update our IDs
            if response.status_code == 201:
                self.get_expense_ids()

    def get_expense_ids(self):
        """Helper method to fetch expense IDs after creation"""
        response = self.client.get(
            f"/api/{self.user_id}/expense/view-all",
            headers=self.headers,
            name="/api/{userId}/expense/view-all (for IDs)"
        )
        
        if response.status_code == 200:
            try:
                expenses = response.json()
                self.expense_ids = [expense.get("id") for expense in expenses if expense.get("id")]
            except:
                pass

    @task(3)
    def get_all_expenses(self):
        if hasattr(self, 'user_id') and hasattr(self, 'headers'):
            self.client.get(
                f"/api/{self.user_id}/expense/view-all",
                headers=self.headers,
                name="/api/{userId}/expense/view-all"
            )
    
    @task(2)
    def get_expense_by_id(self):
        if hasattr(self, 'user_id') and hasattr(self, 'headers') and hasattr(self, 'expense_ids') and self.expense_ids:
            # Get a random expense ID from the list
            expense_id = random.choice(self.expense_ids)
            self.client.get(
                f"/api/{self.user_id}/expense/view/{expense_id}",
                headers=self.headers,
                name="/api/{userId}/expense/view/{expenseId}"
            )