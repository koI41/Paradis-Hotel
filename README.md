# Paradis Hotel Management System

A desktop-based Hotel Management System built using **Java 11**, **JavaFX 21**, and **SQLite**, designed to streamline hotel operations including room management, customer registration, booking processing, billing, and checkout workflows through an intuitive graphical user interface.

---

## Overview

Paradis Hotel Management System (PHMS) is a standalone desktop application that enables hotel staff to efficiently manage daily hotel operations from a single dashboard. The application follows the **Model-View-Controller (MVC)** architecture and uses an embedded **SQLite** database, eliminating the need for external database servers or web-based deployment.

The system provides dedicated modules for:

* Room Management
* Customer Registration
* Booking Management
* Billing & Checkout
* Revenue Tracking
* Dashboard Analytics

---

## Features

### Dashboard

* Live hotel statistics
* Total rooms overview
* Available room tracking
* Active booking monitoring
* Quick action shortcuts

<img width="1247" height="808" alt="image" src="https://github.com/user-attachments/assets/2495c5b1-6a49-4727-833f-18414c8aee29" />

---

### Room Management

* Add new rooms
* Define room categories (Single, Double, Deluxe)
* Set room pricing
* Track room availability
* Filter rooms by status
* Cancelling room allotment

<img width="1248" height="956" alt="image" src="https://github.com/user-attachments/assets/211d873d-b966-442b-a517-61e514425052" />

---

### Booking Management

* Register customer bookings
* Assign available rooms
* Check-in / Check-out date management
* Automatic room occupancy updates
* Active booking monitoring


<img width="1238" height="789" alt="image" src="https://github.com/user-attachments/assets/d16951f0-4445-4704-af3f-a6eacbc5cbf0" />


---

### Billing & Checkout

* Automated bill generation
* 5% tax calculation
* Detailed invoice generation
* Revenue tracking
* One-click checkout workflow


![Uploading image.png…]()


---

### Database Management

* Embedded SQLite database
* Automatic database initialization
* Foreign key constraints
* Persistent data storage
* Secure database operations using PreparedStatements

---

## Technology Stack

### Frontend

* JavaFX 21
* JavaFX Controls
* JavaFX Graphics
* JavaFX FXML

### Backend

* Java 11
* JDBC

### Database

* SQLite 3

### Build & Testing

* Apache Maven
* JUnit 4

### Architecture

* MVC (Model-View-Controller)
* DAO (Data Access Object)

---

## System Architecture

The application follows a layered MVC architecture:

```text
Paradis Hotel Management System
│
├── Model Layer
│   ├── Room
│   ├── Customer
│   ├── Booking
│   └── Bill
│
├── DAO Layer
│   ├── RoomDAO
│   ├── CustomerDAO
│   ├── BookingDAO
│   └── BillDAO
│
├── Controller Layer
│   ├── MainController
│   ├── RoomController
│   ├── BookingController
│   └── BillingController
│
└── Utility Layer
    └── DatabaseUtil
```

---

## Database Schema

The application uses four normalized tables:

### Rooms

Stores:

* Room Number
* Room Type
* Price Per Day
* Availability Status

### Customers

Stores:

* Customer Details
* Contact Information
* Check-In and Check-Out Dates

### Bookings

Stores:

* Booking Details
* Room Assignments
* Booking Status
* Total Cost

### Bills

Stores:

* Invoice Information
* Tax Calculation
* Payment Status
* Revenue Records

---

## Key Technical Highlights

* MVC-based architecture implementation
* DAO pattern for database abstraction
* JDBC database connectivity
* Automatic SQLite database initialization
* Foreign key relationships and data integrity
* PreparedStatements for secure SQL operations
* Dynamic JavaFX UI generation
* Modular and scalable code structure
* Embedded database deployment
* Automated invoice generation system

---

## OOP Concepts Demonstrated

### Encapsulation

Private fields with controlled access through getters and setters across all domain models.

### Inheritance

Application lifecycle managed through JavaFX's Application class.

### Polymorphism

Event-driven interactions using JavaFX EventHandlers and lambda expressions.

### Abstraction

DAO layer abstracts database operations from controllers and business logic.

---

## Getting Started

### Prerequisites

* Java 11 or later
* Maven 3.x

### Clone Repository

```bash
git clone <repository-url>
cd paradis-hotel
```

### Build Project

```bash
mvn clean install
```

### Run Application

```bash
mvn javafx:run
```

---

## Project Structure

```text
paradis-hotel/
│
├── pom.xml
├── README.md
├── paradis_hotel.db
│
└── src
    └── main
        └── java
            └── com
                └── paradis
                    └── hotel
                        ├── model
                        ├── dao
                        ├── controller
                        ├── util
                        └── main
```

---

## Future Enhancements

* User Authentication and Authorization
* Role-Based Access Control
* PDF Invoice Export
* Occupancy and Revenue Analytics
* Data Visualization Dashboard
* Online Reservation System
* MySQL/PostgreSQL Integration
* Multi-User Network Deployment

---

## Author

**Sujan Shetty**
Information Technology, MIT Manipal

---

## License

This project is developed for educational and academic purposes.
