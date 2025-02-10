# Build stage
FROM node:18 as build
WORKDIR /app

# Copy package files first
COPY package*.json ./
COPY angular.json ./
COPY tsconfig*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application
COPY . .

# Build the application
RUN npm run build --configuration=production

# Development stage
FROM node:18 as development
WORKDIR /app

# Copy package files
COPY package*.json ./
COPY angular.json ./
COPY tsconfig*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application
COPY . .

# Expose port
EXPOSE 4200

# Start the application in development mode
CMD ["npm", "start", "--", "--host", "0.0.0.0", "--poll=2000"]

# Run stage
FROM node:18-slim
WORKDIR /app

# Copy built assets from build stage
COPY --from=build /app/dist/dropbox ./dist

# Install serve
RUN npm install -g serve

EXPOSE 4200
CMD ["serve", "-s", "dist", "-l", "4200"] 