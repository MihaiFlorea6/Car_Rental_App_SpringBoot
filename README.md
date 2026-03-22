# CarRental App SpringBoot  
# Technical Description  
A comprehensive enterprise-grade Client/Server web application built with Spring Boot, designed to manage the entire lifecycle of a car rental business. It features a robust Role-Based Access Control (RBAC) system, secure authentication and a hybrid communication architecture combining RESTful APIs, Thymeleaf server-side rendering and asynchronous Socket-based messaging.  

<div align="center">
  <video src="https://github.com/user-attachments/assets/594d1075-2446-4410-8c55-30ce8a705c74" width="800" controls></video>
</div>  
  
# Architecture and logic control  
* The system follows a strict Model-View-Controller (MVC) architectural pattern. The backend utilizes Spring Data JPA (Hibernate) to interface with a MySQL relational database, abstracting complex SQL queries through an Object-Relational Mapping (ORM) layer.  
* The presentation layer uses HTML5/CSS3 and JavaScript (Fetch API) integrated with Thymeleaf templates for dynamic data rendering. Additionally, a standalone multithreaded Socket Server runs concurrently within the Spring context, handling serialized Java objects (`Message`) for direct, low-level client-server interactions.

# Objectives  
The primary objective was to design a secure, full-stack environment capable of:  
* **Secure Authorization:** Implementing Spring Security with BCrypt password hashing and session management for 4 distinct hierarchical roles (Admin, Manager, Employee, Client).
* **Data Integrity & Validation:** Preventing rental booking conflicts by executing custom JPQL queries that validate overlapping date intervals at the database level.
* **Workflow Automation:** Enabling internal communication through an integrated notification system and a bidirectional feedback loop between clients and management.
* **Scalable Data Modeling:** Designing a normalized relational database schema (MySQL) mapped seamlessly to Java Entities using Hibernate annotations (`@OneToMany`, `@ManyToOne`), while carefully managing Lazy/Eager fetching strategies to prevent `LazyInitializationException` and infinite JSON recursion.  

# User Roles & Bussiness Workflow  
The platform implements a strict hierarchical Role-Based Access Control (RBAC) system, modeling the real-world operational flow of a car rental agency.  
The business logic is divided into 4 distinct actor profiles:  
**1. The Administrator** (`ROLE_ADMIN`)  
* **Responsibilities:** IT & System Management.
* **Capabilities:** Possesses full CRUD (Create, Read, Update, Delete) access over all user accounts across the platform. The Admin can dynamically toggle user account statuses (Active/Inactive), immediately revoking system access for any compromised or suspended accounts.
  
![Screenshot 2026-03-22 182124](https://github.com/user-attachments/assets/7c5834e7-cae5-47cf-ba7f-8c4e78531122)  

**2. The Manager** (`ROLE_MANAGER`)  
* **Responsabilities:** Fleet Oversight & Business Operations.
* **Capabilities:** Acts as the central supervisor. The Manager views the entire fleet status, monitors all historical and active rentals, and manages employee specific attributes (e.g., assigning specific roles like "contabilitate" or "ajutor clienti").  
  They also drive internal communication by sending direct `EmailNotifications` to employees or clients and act upon received `Feedback.`
  
  ![Screenshot 2026-03-22 182830](https://github.com/user-attachments/assets/4a912218-0559-45b1-9f8b-3e935e0c1ce6)
  
  ![Screenshot 2026-03-22 182941](https://github.com/user-attachments/assets/f99da211-d136-4703-b81e-8517bb445e5a)

  ![Screenshot 2026-03-22 183103](https://github.com/user-attachments/assets/8529e15e-86e4-4cd7-8282-8121fa54460a)

**3. The Employee** (`ROLE_EMPLOYEE`)  
* **Responsabilities:** Day-to-Day Operations & Request Processing.
* **Capabilities:** The operational core of the agency. Employees monitor the real-time queue of `PENDING` rental requests, having the authority to `APPROVE` or `REJECT` them based on physical car availability.  
  They are also responsible for updating live telemetry for each car, such as current `fuel_level` and operational availability.

![Screenshot 2026-03-22 183816](https://github.com/user-attachments/assets/80da88ec-547e-417c-ae4f-bf7639cbe465)  

**4. The Client** (`ROLE_CLIENT`)  
* **Responsabilities:** The End-User
* **Capabilities:** Can browse the entire catalog of vehicles, filtering by category. Clients submit `RentalRequests` for specific date intervals. Post-rental, they can submit detailed `Feedback` (1-5 star ratings and text) which is routed directly to the Management dashboard.  

![Screenshot 2026-03-22 184313](https://github.com/user-attachments/assets/48eee6f1-6e50-44e2-8e78-91f6e4190b5a)

![Screenshot 2026-03-22 184455](https://github.com/user-attachments/assets/9dbddcdf-699c-46b8-8511-7d0e8d1f67d8)  

<img width="1298" height="972" alt="Screenshot (128)" src="https://github.com/user-attachments/assets/75ce6796-71a5-4a60-80c2-cb4bbf7b4500" />   


**⚙️ The Core Workflow**  
1. **Booking & Validation:** A `Client` selects a car and submits a start/end date. The system's JPA repository executes a custom query (`findConflictingRentals`) to ensure the dates do not overlap with existing approved requests, preventing double-booking at the database level.
2. **Operational Processing:** The request enters a `PENDING` state. An `Employee` reviews the request in their dashboard and updates the status to `APPROVED`.
3. **Communication Loop:** If issues arise (e.g., car requires maintenance), the `Manager` can dispatch instant internal messages to the `Employee` or update the `Client` via the integrated Email/Notification system.
4. **Quality Assurance:** After the rental period, the `Client` leaves a rating. The system automatically recalculates the `Average Rating` for that specific car, allowing the `Manager` to make data-driven fleet decisions.  
   
# System Logic & OOP Design  
The application's reliability relies on two main software engineering assets:  
## 1. Security & Role-Based Access Control (RBAC)  
  - The application leverages `SecurityConfig` to restrict endpoint access based on user authorities. Passwords are never stored in plain text;
  - The `PasswordHashGenerator` utility ensures all credentials in the MySQL database are securely hashed using the BCrypt algorithm.
   ## BCryptPasswordEncoder
      import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

      public class PasswordHashGenerator {
         public static void main(String[] args) {
         BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
         String password = "parola123";
         String hash = encoder.encode(password);
        
         System.out.println("HASH PENTRU PAROLA: " + password);
         System.out.println("===========================================");
         System.out.println(hash);

  
  - The CustomUserDetailsService bridges the database records with the Spring Security context.

## 2. Hybrid Communication Protocol  
* **REST & MVC:** Standard HTTP requests map to dedicated controllers (`ClientController`, `AdminController`, etc.), serving both JSON payloads for asynchronous JavaScript fetch calls and fully rendered Thymeleaf HTML views.
* **TCP Sockets:** The `SocketServer` component runs asynchronously on a dedicated thread, accepting standard TCP connections. It routes serialized `Message` objects based on their `type` attribute, bypassing the HTTP overhead for specific high-speed internal communications.  

# Skills  
This project demonstrates proficiency in:  
**→ `Enterprise Java (Spring Boot)` architecture, configuration and dependency injection.**  
**→ `Relational Database Design (MySQL)` and `ORM (Hibernate/JPA)`, including custom JPQL queries and complex entity mappings.**  
**→ `Web Security`, implementing authentication flows, BCrypt hashing and endpoint authorization.**  
**→ `Full-Stack Integration`, connecting a robust backend with dynamic frontend interfaces using Thymeleaf, JavaScript and CSS.**  
**→ `Network Programming`, utilizing Java Sockets for serialized object transmission over TCP/IP.**  

# Testing  
The platform logic was extensively validated against real-world scenarios. Custom SQL datasets (`car_rental_*.sql`) were created to test edge cases such as: overlapping rental dates, unauthorized endpoint access attempts, dynamic calculation of average car ratings based on user feedback, and role-specific UI rendering.  

# Key Technologies  
`Java 17`, `Spring Boot`, `Spring Security`, `Spring Data JPA (Hibernate)`, `MySQL`, `Thymeleaf`, `Java Sockets (TCP/IP)`, `JavaScript (Fetch API)`, `HTML5/CSS3`.















