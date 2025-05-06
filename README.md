# ChatterBox – Post Service

The **Post Service** is one of the core microservices of the **ChatterBox** platform – a minimal Twitter-style social micro-posting app.

## 📦 Overview

The Post Service allows users to:
- Create text-based posts
- View all posts by a specific user
- View a specific post by its ID

This service is stateless, exposing REST APIs and internally handling data via mock implementations (placeholder for database integration).

## 🚀 Features

- Create a new post (authentication to be integrated)
- Fetch posts by user
- Fetch a single post by its ID

## 📘 Exposed APIs

| Method | Endpoint                 | Description                               |
|--------|--------------------------|-------------------------------------------|
| POST   | `/posts`                 | Create a new text post (auth to be added) |
| GET    | `/posts/user/{username}` | Get all posts by a specific user          |
| GET    | `/posts/post/{postId}`   | Get a specific post by its ID             |

## 🛠 Technology Stack

- Java 21
- Spring Boot 3.x
- Maven
- REST APIs
- [To be integrated] MongoDB (or other DB)
- [To be integrated] JWT Auth & Messaging (Kafka or RabbitMQ)

## 📁 Package Structure
