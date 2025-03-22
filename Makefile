# Variables
FRONTEND_IMAGE = magicbudget-frontend
BACKEND_IMAGE = magicbudget-backend

# Default target
all: build-frontend build-backend

# Build frontend Docker image
build-frontend:
	docker build -t $(FRONTEND_IMAGE) -f frontend/Dockerfile ./frontend

# Build backend Docker image
build-backend:
	docker build -t $(BACKEND_IMAGE) -f backend/Dockerfile ./backend

# Clean up Docker images
clean:
	docker rmi $(FRONTEND_IMAGE) $(BACKEND_IMAGE)

.PHONY: all build-frontend build-backend clean