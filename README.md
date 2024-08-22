# Seleksi 3 Asisten Laboratorium Programming 2024

Develop a movie streaming website to uplift and entertain the weary agents of OWCA after a grueling semester and intensive coursework. This website aims to provide a fun and relaxing environment to help rejuvenate the agents and boost their morale.

## Identity
|    Nama  | NIM      |
|----------|----------|
| Elbert Chailes    | 13522045     |

### How to Run

Follow these steps to get the project up and running on your local machine:

1. **Clone the repository**
   ```
   git clone https://github.com/ChaiGans/Seleksi3_Labpro.git
   ```

2. **Open Docker Desktop**
   Ensure Docker Desktop is running on your machine. You can download and install it from [Docker's official website](https://www.docker.com/products/docker-desktop).

3. **Navigate to the Project Root**
   Change directory to the root of the `Seleksi3_Labpro` project:
   ```
   cd Seleksi3_Labpro
   ```

4. To rebuild the JAR file specified in the `Dockerfile`, you can use the `mvn clean install` command. This process allows you to freshly remake the JAR file. You can perform this action using IntelliJ, which comes pre-loaded with Maven, or through the terminal. Detailed terminal commands are not discussed here. Note that, developer will leave `target` folder in repository, so there is no mandatory to run `mvn install` separately.

5. **Start the Project**
   Use Docker Compose to build and run the project:
   ```
   docker compose up --build
   ```
   To stop the project, use:
   ```
   docker compose stop
   ```
   To remove the containers completely, use:
   ```
   docker compose down
   ```

### Note
This repository contains two Docker Compose files:
- `docker-compose.yml`: For local development. It configures the database and web application to run on the same machine.
- `docker-compose-prod.yml`: Not needed for local runs. This file is intended for deployment purposes and will be explained in future documentation.

## Design Patterns and Why?
In the development of the OWCA web application, which enables users to conduct film transactions and view content similar to platforms like Netflix, several key design patterns are employed to enhance the architecture and maintainability of the system. These patterns facilitate clear roles for different parts of the application and support a robust, scalable design.

### 1. Model-View-Controller (MVC) Pattern
The MVC pattern is a fundamental design approach used in web development with frameworks such as Java Spring Boot. This pattern divides the application into three interconnected components:
- **Model**: Represents the data and the business logic of the application. In OWCA, models are implemented as `Entities` which encapsulate the core data relevant to the application's functionality.
- **View**: Handles the display of information to the user, based on the data received from the models.
- **Controller**: Acts as an intermediary between the Model and the View, handling user input and converting it into commands for the Model or View.

**Reason**: The MVC pattern is chosen for its well-documented benefits in promoting separation of concerns, enhancing modularity, and facilitating scalable application development. By segregating the application logic from the user interface, MVC makes the application more maintainable and adaptable to changing user requirements or technology landscapes.

### 2. Repository Pattern
The Repository pattern is utilized as a part of the data access layer to abstract the interaction with the underlying database. It provides a collection-like interface for accessing domain objects.

**Reason**: This pattern is particularly effective in encapsulating the set of data operations performed on the database, thereby decoupling the data access code from the business logic. This abstraction allows for cleaner, more readable code and simplifies data manipulation and access throughout the application.

### 3. Service Layer Pattern
The Service layer pattern is used to organize the business logic of an application into distinct services that expose a set of public methods available to the client.

**Reason**: Implementing a Service layer facilitates transaction management and enhances the reusability and isolation of code that performs business logic. This layer acts as a bridge to ensure that the Controllers remain lean, focusing primarily on request handling by delegating complex business rules and operations to the services.

### 4. Builder Pattern
The Builder pattern is employed to construct complex objects. This pattern is particularly useful in creating instances of `ResponseEntity` and other entity models within the application.

**Reason**: The Builder pattern provides a clear and flexible way to construct various objects, allowing for better control over the construction process. It enhances type safety by using method chaining (fluent interface) and improves code readability and maintainability. It also prevents errors by ensuring that objects are built in a consistent state, especially useful in environments with complex object creation scenarios.

## Tech Stack
This section details the technologies used in the OWCA web application, including frameworks, languages, servers, and tools for deployment and media handling.

- **Thymeleaf**: A server-side Java template engine for web applications, used for rendering views.
- **TailwindCSS**: A utility-first CSS framework for rapid UI development.
- **Java 17**: The programming language used, providing robust features for enterprise applications.
- **Spring Boot**: Framework for building stand-alone, production-grade Spring based Applications that you can "just run".
- **Apache Tomcat 10**: The web server and servlet container that provides the environment to run Java code.
- **CSS Vanilla**: Standard CSS used to style the application without additional frameworks.
- **Aiven**: Cloud service used to deploy and manage the application's database in a scalable manner.
- **Cloudflare R2**: Storage solution used for hosting images and videos efficiently, integrating seamlessly with the web application for content delivery.
- **Azure**: Comprehensive cloud platform used for deploying and managing the application's services, enabling scalable hosting, automated management, and integration of various cloud resources.

## API Endpoints
For a more interactive view and to test these endpoints, please visit the Swagger UI documentation at: [OWCA API Documentation](https://owca-please.azurewebsites.net/swagger-ui/index.html).

### Film REST Controller
Operations related to films in the database:

- **GET `/films/{id}`**: Retrieves detailed information about a film by its ID.
- **PUT `/films/{id}`**: Updates the information of a specific film by its ID.
- **DELETE `/films/{id}`**: Deletes a specific film by its ID.
- **POST `/films`**: Adds a new film to the database.
- **GET `/films`**: Fetches information about all films stored in the database.

### Wishlist REST Controller
Manage user wishlists for films:

- **POST `/wishlists/{id}`**: Creates a new wishlist entry where `{id}` is the film's ID. Requires `userId` in the request body.
- **DELETE `/wishlists/{id}`**: Removes a wishlist entry based on the film's ID and `userId` provided in the request body.

### User REST Controller
Operations related to users:

- **POST `/users/{id}/balance`**: Adds balance to a user's account using the user's ID in `{id}`.
- **GET `/users`**: Retrieves information about all users (typically for admin use).
- **GET `/users/{id}`**: Retrieves detailed information about a specific user by their ID.
- **DELETE `/users/{id}`**: Deletes a specific user by their ID.

### Review REST Controller
Handling of film reviews:

- **POST `/reviews`**: Submits a new review using the request body provided.
- **DELETE `/reviews/{id}`**: Deletes a specific review by its ID.

### Authentication REST Controller
Endpoints for authentication and user session management:

- **POST `/login`**: Authenticates a user, intended primarily for admin accounts.
- **GET `/self`**: Checks if a user is currently logged in and retrieves their session information.

### Registration and Login Controller
User account management:

- **POST `/api/v1/auth/register`**: Registers a new user account.
- **POST `/api/v1/auth/logout`**: Logs out a user and clears the session cookie.
- **POST `/api/v1/auth/login-with-email`**: Allows users to log in using either an email or a username. Initially intended for email only but adapted to accept usernames as well.

### Action REST Controller
User actions on films:

- **POST `/action/buy/{filmId}`**: Processes the purchase of a film by a logged-in user, identified by their user ID.

## Bonus Feature
### B02 - Deployment
This application is deployed on [Azure](https://azure.microsoft.com/en-us/) using the following architecture:

- **Database**: We utilize a PostgreSQL database hosted on [Aiven's](https://aiven.io/) free service, ensuring our application can manage data in the cloud efficiently.
- **Containerization**: The application is containerized using Docker, with images built locally and pushed to [Docker Hub](https://hub.docker.com/). This approach facilitates easy updates and consistency across different environments.
- **Configuration**: Deployment on Azure utilizes a `docker-compose.prod.yml` file found in the repository, which orchestrates the deployment process using Docker Compose.

**Deployment URL**: [https://owca-please.azurewebsites.net/](https://owca-please.azurewebsites.net/)

**Region**: Korea Central on a Free plan. Please note that the performance may be intermittent and laggy due to the limitations of the free service tier.

### B05 - Lighthouse
`Google Lighthouse` is an open-source automated tool developed by Google to audit or measure the quality of web applications. This tool aids web developers in improving their projects. In this bonus feature, we guarantee a performance score of **at least 95**. However, certain aspects, such as accessibility, may not reach this threshold, **achieving scores between 92 and 94**. This is often due to choices in color contrast that, while aesthetically pleasing, may not meet Lighthouse's strictest standards. Additionally, certain intentional sizing and positioning of icons and buttons are designed to enhance UI aesthetics at the expense of Lighthouse scores. Despite these deviations, the average score **remains above 95**, with **no scores lower than 90**, which should be considered as `PASS`.
![Screenshot (2364)](https://github.com/user-attachments/assets/35b67474-0e19-476c-96ad-732a749ba31c)
![Screenshot (2365)](https://github.com/user-attachments/assets/44657236-36c5-4041-b363-52091d75b315)
![Screenshot (2366)](https://github.com/user-attachments/assets/024f8b68-623e-4ea4-8102-6d8cb30a13a5)
![Screenshot (2367)](https://github.com/user-attachments/assets/24f8ff84-d440-429a-ab75-f1b6d8a3e204)
![Screenshot (2368)](https://github.com/user-attachments/assets/bcda6194-3491-4dff-82bb-2db3d744fb88)
![Screenshot (2363)](https://github.com/user-attachments/assets/4bdbe59d-30eb-4bdc-b770-89fa52d5dbbd)

### B06 - Responsive Layout
A responsive layout ensures that your web application adapts seamlessly across different screen sizes and devices, providing an optimal viewing and interaction experience. This feature is crucial in modern web design, where users access sites from a wide array of devices including smartphones, tablets, and desktop computers.

Developer's implementation utilizes flexible grid layouts, media queries, and dynamic CSS styles that adjust based on the device's screen properties. The goal is to maintain functionality and aesthetics, ensuring that elements such as navigation bars, sidebars, images, and text are legible and accessible regardless of the device used.

### B07 - API Documentation

Good developers document their work to ensure it is readable and easily understandable by others. API documentation is essential for providing clear guidelines on how to interact with the backend services of an application. We use Swagger for documenting both our single service and monolithic service architectures, making it easy for developers to get up to speed quickly.

Our comprehensive API documentation includes detailed descriptions of API endpoints, expected request formats, possible response codes, and interactive examples. This helps in reducing the learning curve and accelerating the integration process for developers working with our APIs.

To explore our API documentation, please visit our Swagger UI:
[Swagger API Documentation](https://owca-please.azurewebsites.net/swagger-ui/index.html)

### B10 - Fitur Tambahan
#### 1. Top 5 Bought Films
#### 2. Rating and Film Review
#### 3. Wishlist

### B11 - Ember
In our application, we utilize bucket storage to separate the storage of our source code from user-uploaded files such as films, images, videos, and binaries. This approach enhances security and scalability by isolating static resources from the application logic.

We have chosen **Cloudflare R2** as our cloud object storage service due to its cost-effectiveness—it offers a free plan. Cloudflare R2 is highly scalable and provides reliable storage solutions without egress fees, making it an ideal choice for startups and independent developers.

**Storage Limitations:**
- **Capacity**: Our Cloudflare R2 plan is limited to 1GB of storage.
- **Shared Usage**: The bucket is shared with a colleague, which might occasionally lead to capacity issues. Users of this application should be aware that the storage might reach its full capacity, potentially impacting the ability to upload new films.

**DO CONTACT `elbertchailes888@gmail.com` if the storage of bucket is full. Or there is issue that causing errors.**

# Important Notice: Clear Browser Storage After Account Deletion

Please ensure to clear your browser's cookies and local storage if you encounter issues when attempting to log in. Additionally, it is crucial to note that if you are logged in with an account that is subsequently deleted, the associated session cookies may remain in your browser's storage. To resolve this and continue using the application without interruption, you must manually remove these cookies and local storage entries. Please inform the administrator if you experience this issue.
