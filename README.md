# Resourcify — Campus Resource Booking & Utilization System

A desktop Java application developed with standard **Java Swing** demonstrating core **Object-Oriented Programming (OOP)** principles.

---

## 📌 Project Overview
**Resourcify** is a campus resource management and reservation system. It enables students and faculty to browse college resources (labs, seminar halls, classrooms, meeting rooms), check real-time availability, and submit booking requests with conflict prevention. Administrators can review, approve or reject requests, manage campus resources, and analyze resource utilization statistics.

---

## 🎯 Key Features

### 1. Authentication & Role-Based Access
- Secure login supporting multiple roles: `STUDENT`, `FACULTY`, and `ADMIN`.
- **Quick Demo Fill buttons** on the login screen for rapid presentation and teacher demonstration.

### 2. Student & Faculty Portal (`UserFrame`)
- **Browse Resources Tab**:
  - Live keyword search by resource name or location.
  - Category filtering (Computer Lab, Seminar Hall, Laboratory, Meeting Room, Classroom).
  - Clean table displaying capacity, location, and availability status.
- **Booking Dialog**:
  - Dedicated modal dialog with date input and start/end time slot selection.
  - Automatic time-conflict validation (prevents double bookings).
- **My Bookings Tab**:
  - Full tracking table showing Booking ID, slot, purpose, status (`PENDING`, `APPROVED`, `REJECTED`, `CANCELLED`), and admin remarks.
  - Action to cancel existing pending or approved bookings.

### 3. Administrator Dashboard (`AdminFrame`)
- **Booking Requests & Approvals**:
  - View all incoming requests with applicant information, role, and purpose.
  - Filter requests by status (`PENDING`, `APPROVED`, `REJECTED`, `CANCELLED`).
  - One-click **Approve** and **Reject** actions with custom administrative remarks.
  - Pending requests counter badge.
- **Resource Management**:
  - Add new campus resources (ID, name, type, location, capacity).
  - Toggle resource availability (Available ⟷ Maintenance).
  - Safe resource deletion (guarded against active bookings).
- **Utilization & Reports**:
  - Key metric summary cards: Total Resources, Total Bookings, Pending Approvals, Approved Bookings.
  - Per-resource utilization report and allocation rate table.

---

## 💡 OOP Concepts Demonstrated

| Concept | Implementation in Resourcify |
|---|---|
| **Encapsulation** | Model classes (`Booking`, `Resource`, `User`) use `private` fields with validated getters and setters. |
| **Separation of Concerns** | Clear architectural separation between Data Store (`DataStore`), Business Service (`BookingService`), and Presentation Views (`LoginFrame`, `UserFrame`, `AdminFrame`). |
| **Polymorphism & Roles** | Role-based logic (`Role.STUDENT`, `Role.FACULTY`, `Role.ADMIN`) determining portal access and operational permissions. |
| **Exception Handling** | Custom validation in `BookingService` throwing `IllegalArgumentException` caught gracefully by Swing UI dialogs to prevent application crashes. |
| **Collections & Streams** | Dynamic tracking and reporting using Java `List`, lambda expressions, and Streams API. |

---

## 🔑 Demo Credentials

| Role | Email | Password |
|---|---|---|
| **Student** | `student@resourcify.demo` | `student123` |
| **Faculty** | `faculty@resourcify.demo` | `faculty123` |
| **Admin** | `admin@resourcify.demo` | `admin123` |

*(Tip: You can also click the quick demo buttons on the login screen to auto-fill these credentials instantly!)*

---

## 🚀 How to Run

### Requirements
- **Java SE Development Kit (JDK 17 or higher)**
- No external dependencies or third-party libraries needed (uses standard Java SE & Swing).

### Compilation & Execution
From the root of the project directory:

```bash
# Compile all Java source files into out/ directory
javac -d out src/resourcify/*.java

# Run the application
java -cp out resourcify.Main

# Run the automated component test suite
java -cp out resourcify.ComponentVerificationTest
```
