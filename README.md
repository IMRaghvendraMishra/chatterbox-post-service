# ChatterBox Post Service

The ChatterBox Post Service is a core component of the ChatterBox platform, which allows users to create, manage, and retrieve posts. This service is part of a microservices-based architecture designed to provide social media functionalities, similar to Twitter, but limited to text posts only. The service communicates with other services (like User Service) and utilizes Kafka for event-driven processing.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies](#technologies)
- [Endpoints](#endpoints)
- [Installation](#installation)
- [Configuration](#configuration)
- [License](#license)

## Overview

The ChatterBox Post Service manages all operations related to posts within the platform. It exposes RESTful endpoints for creating, updating, retrieving, and deleting posts. Additionally, it integrates with Kafka to emit post creation events.

### Key Features:
- **Create a Post**: Allows users to create a new post with a specific username and content.
- **Update a Post**: Allows users to update an existing post by its ID.
- **Retrieve Posts**: Fetches posts by a specific user or by post ID.
- **Delete Posts**: Supports deleting a single post, all posts from a specific user, or all posts in the system.
- **Kafka Integration**: Emits events to a Kafka topic (`chatterbox-post-events`) when a new post is created.
- **Validation**: Ensures that posts meet validation rules (e.g., content length, user existence).

## Features

### Exposed Endpoints

- **POST /api/posts** - Create a new post and publish a Kafka event.
- **PUT /api/posts/{postId}** - Update an existing post by post ID.
- **GET /api/posts/user/{username}** - Retrieve all posts by a specific user.
- **GET /api/posts/{postId}** - Retrieve a post by its ID.
- **GET /api/posts** - Retrieve all posts in the system.
- **DELETE /api/posts/{postId}** - Delete a post by its ID.
- **DELETE /api/posts/user/{username}** - Delete all posts by a specific user.
- **DELETE /api/posts** - Delete all posts from the system.

### Kafka Event Handling

- The service emits a `post-created` event to Kafka whenever a new post is created. This event is handled by other services that subscribe to the Kafka topic (`chatterbox-post-events`).

## Technologies

- **Java 21**: The core language used for development.
- **Spring Boot**: Used to create the microservice architecture and handle HTTP requests.
- **MongoDB**: Used as the database for storing posts.
- **Kafka**: Used for event-driven architecture and communication between services.
- **Reactor Netty**: Used for non-blocking HTTP requests to the User Service.
- **Log4j2**: Used for logging operations.

## Installation

To run the `chatterbox-post-service`, ensure you have the following prerequisites installed:
- **Java 21**
- **Maven** or **Gradle** (for building the project)
- **MongoDB** running locally on `mongodb://localhost:27017/posts`
- **Kafka** running locally on `localhost:9092`

### Steps to run the service:

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/chatterbox-post-service.git
