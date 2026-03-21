# CarRental App SpringBoot  
# Technical Description  
A comprehensive enterprise-grade Client/Server web application built with Spring Boot, designed to manage the entire lifecycle of a car rental business. It features a robust Role-Based Access Control (RBAC) system, secure authentication and a hybrid communication architecture combining RESTful APIs, Thymeleaf server-side rendering and asynchronous Socket-based messaging.  

https://github.com/user-attachments/assets/594d1075-2446-4410-8c55-30ce8a705c74  

# Architecture and logic control  
* The system follows a strict Model-View-Controller (MVC) architectural pattern. The backend utilizes Spring Data JPA (Hibernate) to interface with a MySQL relational database, abstracting complex SQL queries through an Object-Relational Mapping (ORM) layer.  
* The presentation layer uses HTML5/CSS3 and JavaScript (Fetch API) integrated with Thymeleaf templates for dynamic data rendering. Additionally, a standalone multithreaded Socket Server runs concurrently within the Spring context, handling serialized Java objects (`Message`) for direct, low-level client-server interactions.
