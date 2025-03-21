# syntax=docker.io/docker/dockerfile:1

FROM node:18-alpine

# Install any system dependencies needed (e.g. libc6-compat for compatibility)
RUN apk add --no-cache libc6-compat

# Set working directory
WORKDIR /app

# Copy dependency manifests
COPY package.json yarn.lock* package-lock.json* pnpm-lock.yaml* .npmrc* ./

# Install dependencies based on the available lockfile
RUN \
  if [ -f yarn.lock ]; then yarn install; \
  elif [ -f package-lock.json ]; then npm install; \
  elif [ -f pnpm-lock.yaml ]; then corepack enable pnpm && pnpm install; \
  else echo "Lockfile not found." && exit 1; \
  fi

# Copy the rest of your application code
COPY . .

# Expose the port that Next.js uses
EXPOSE 3000

# Start Next.js in development mode
CMD ["npm", "run", "dev"]
