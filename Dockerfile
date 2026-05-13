# --- STAGE 1: Build ---
# Start with the official JDK 25 image
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Install Maven, Chrome dependencies and Chrome itself
RUN apt-get update && apt-get install -y \
    maven \
    wget \
    gnupg \
    ca-certificates \
    --no-install-recommends \
    && wget -qO- https://dl-ssl.google.com/linux/linux_signing_key.pub | gpg --dearmor > /usr/share/keyrings/google-chrome.gpg \
    && echo "deb [arch=amd64 signed-by=/usr/share/keyrings/google-chrome.gpg] http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google.list \
    && apt-get update && apt-get install -y google-chrome-stable \
    && rm -rf /var/lib/apt/lists/*

COPY pom.xml .
RUN mvn dependency:go-offline

# Build the app
COPY . .
RUN mvn clean test
RUN ls -la target/

# Stage 2: Serve with Nginx
FROM nginx:alpine

# Copy the build output to Nginx's html folder
COPY --from=build /app/target /usr/share/nginx/html

# Copy custom Nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

