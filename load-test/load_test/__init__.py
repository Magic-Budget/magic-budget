from locust import HttpUser, task, between
import random
import string
import json
from datetime import datetime, timedelta


def random_string(length=8):
    return "".join(random.choices(string.ascii_letters + string.digits, k=length))


class AuthLoadTest(HttpUser):
    wait_time = between(3, 10.5)

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
            "email": username,
        }

        headers = {"Content-Type": "application/json"}
        register_response = self.client.post(
            "/api/auth/register",
            json=register_payload,
            headers=headers,
            name="/api/auth/register",
        )

        # Then attempt to login with the same credentials
        login_payload = {"username": username, "password": password}

        self.client.post(
            "/api/auth/sign-in",
            json=login_payload,
            headers=headers,
            name="/api/auth/sign-in",
        )


class GoalsLoadTest(HttpUser):
    wait_time = between(3, 10.5)

    def on_start(self):
        # Register a new user
        self.username = f"{random_string()}@example.com"
        self.password = "password"

        headers = {"Content-Type": "application/json"}
        register_payload = {
            "username": self.username,
            "password": self.password,
            "firstName": "Goal",
            "lastName": "Tester",
            "email": self.username,
        }

        register_response = self.client.post(
            "/api/auth/register",
            json=register_payload,
            headers=headers,
            name="/api/auth/register",
        )

        # Extract token and user ID from response
        response_data = json.loads(register_response.text)
        self.token = response_data["token"]
        self.user_id = response_data["userId"]

        # Set auth headers for future requests
        self.auth_headers = {
            "Content-Type": "application/json",
            "Authorization": f"Bearer {self.token}",
        }

        # Track created goal IDs
        self.goal_ids = []

    @task(3)
    def create_goal(self):
        # Create a random goal
        future_date = (
            datetime.now() + timedelta(days=random.randint(30, 365))
        ).strftime("%Y-%m-%d")
        goal_payload = {
            "name": f"Goal {random_string(4)}",
            "target": random.randint(1000, 10000),
            "currentAmount": random.randint(0, 500),
            "due": future_date,
        }

        response = self.client.post(
            f"/api//{self.user_id}/goals",
            json=goal_payload,
            headers=self.auth_headers,
            name="/api/{userId}/goals [POST]",
        )

        if response.status_code == 200:
            goal_data = json.loads(response.text)
            self.goal_ids.append(goal_data["id"])

    @task(5)
    def get_all_goals(self):
        self.client.get(
            f"/api/{self.user_id}/goals",
            headers=self.auth_headers,
            name="/api/{userId}/goals [GET]",
        )

    @task(2)
    def get_specific_goal(self):
        if self.goal_ids:
            goal_id = random.choice(self.goal_ids)
            self.client.get(
                f"/api/{self.user_id}/goals/{goal_id}",
                headers=self.auth_headers,
                name="/api/{userId}/goals/{goalId} [GET]",
            )

    @task(1)
    def update_goal(self):
        if self.goal_ids:
            goal_id = random.choice(self.goal_ids)
            future_date = (
                datetime.now() + timedelta(days=random.randint(30, 365))
            ).strftime("%Y-%m-%d")
            update_payload = {
                "name": f"Updated Goal {random_string(4)}",
                "target": random.randint(1000, 10000),
                "currentAmount": random.randint(500, 2000),
                "due": future_date,
            }

            self.client.patch(
                f"/api/{self.user_id}/goals/{goal_id}",
                json=update_payload,
                headers=self.auth_headers,
                name="/api/{userId}/goals/{goalId} [PATCH]",
            )
