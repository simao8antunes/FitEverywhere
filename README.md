# FitEverywhere

Welcome to FitEverywhere! FitEverywhere is a fitness management platform designed to connect customers, personal trainers, and gym managers through a seamless digital experience. This README provides an overview of the deployed environments, steps to start the application, and a summary of the tech stack used.

## Table of Contents
- [Deployed Servers](#deployed-servers)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)

---

## Deployed Servers

FitEverywhere has two fully dockerized environments:

1. **Production Environment**
    - **URL**: [https://fiteverywhere.me](https://fiteverywhere.me)
    - **Port**: 80 (reverse-proxied via Nginx)
    - **Description**: The main production environment where stable and thoroughly tested features are available for end-users.

2. **Development Environment**
    - **URL**: [https://dev.fiteverywhere.me](https://dev.fiteverywhere.me)
    - **Port**: 80 (reverse-proxied via Nginx)
    - **Description**: A development environment for testing new features and ongoing development updates before they are released to production.

## Tech Stack

FitEverywhere uses a modern, robust stack designed to deliver efficient and scalable applications:

- **Frontend**:
    - **React** (TypeScript) - A fast and interactive UI framework.
    - **Vite** - Development server and build tool for optimized front-end performance.
    - **Google OAuth2** - Authentication for secure login and user management.
    - **Docker** - Containerization to ensure consistent environments across development, testing, and production.

- **Backend**:
    - **Spring Boot** (Java) - Backend framework for scalable and RESTful API development.
    - **MySQL** - Database for handling structured application data.
    - **Docker** - Containerization to ensure consistent environments across development, testing, and production.

- **DevOps**:
    - **Docker Compose** - Manages multi-container applications for isolated services.
    - **Nginx** - Reverse proxy for load balancing and serving the app.
    - **CI/CD** - GitHub Actions for continuous integration and delivery, ensuring code quality and automated deployments.
    - **Azure** - Cloud platform for hosting both the application and database.

## Getting Started

### Prerequisites

- **Docker** and **Docker Compose** installed on your local machine.

### Local Development Setup

To start the app in a development environment, follow these steps:

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/MESW-LES-2425/FitEverywhere-T2E.git
   cd FitEverywhere-T2E
   ```
2. **Start the Application**:
   ```bash
   docker compose up --build
   ```
---
Thank you for using FitEverywhere! For questions, bug reports, or feature requests, please reach out via our GitHub issues page.