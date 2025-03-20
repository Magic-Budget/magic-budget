from locust import HttpUser, task, between
import random
import string

def random_string(length=8):
    return ''.join(random.choices(string.ascii_letters + string.digits, k=length))

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