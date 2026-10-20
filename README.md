# Resourcify — Campus Resource Booking & Facility Management System

<p align="center">
  <img src="https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 17+" />
  <img src="https://img.shields.io/badge/GUI-Java%20Swing%20(Modern%20UI)-2563EB?style=for-the-badge" alt="Java Swing Modern UI" />
  <img src="https://img.shields.io/badge/Architecture-MVC%20%2F%20OOP-10B981?style=for-the-badge" alt="Architecture" />
  <img src="https://img.shields.io/badge/Dependencies-Zero%20(Pure%20Java%20SE)-8B5CF6?style=for-the-badge" alt="Zero Dependencies" />
  <img src="https://img.shields.io/badge/Tests-37%2F37%20Passed-10B981?style=for-the-badge" alt="Tests Passed" />
</p>

---

## 📌 Overview

**Resourcify** is a modern, enterprise-grade desktop campus resource reservation system engineered with pure **Java SE (Swing & AWT)**. Built from the ground up demonstrating clean **Object-Oriented Programming (OOP)** and **Model-View-Service** architectural principles, Resourcify eliminates manual campus resource contention through automated conflict detection, role-governed reservation pipelines, and facility utilization analytics.

---

## 🎨 Modern UI/UX Design System

Unlike conventional introductory Swing applications with flat grey beveled windows, Resourcify features a custom-built, lightweight **Design System (`UITheme.java`)** that delivers a contemporary SaaS dashboard experience with **zero external JARs**:

* 🌟 **Dual-Pane Hero Login Screen (`LoginFrame`)**:
  * Deep navy-to-blue gradient banner with brand monogram `[R]` and feature highlights.
  * Elevated white form card with active focus glow inputs.
  * One-click demo chip buttons (`Student`, `Faculty`, `Admin`) for rapid login testing.
* 🛡️ **Executive Top Navbar**:
  * Dark slate theme (`#0F172A`) across all user and admin views.
  * Circular user avatar badge rendering user initials color-coded by role (Student: Cyan, Faculty: Emerald, Admin: Purple).
  * Styled sign-out button with subtle hover feedback.
* 🏷️ **Status & Availability Pill Badges**:
  * Dynamic rounded pill renderers for table cells:
    * `APPROVED` / `Available` ➜ **Emerald Pill** (`#DCFCE7`)
    * `PENDING` ➜ **Amber Pill** (`#FEF3C7`)
    * `REJECTED` / `CANCELLED` / `Maintenance` ➜ **Rose Red Pill** (`#FEE2E2`)
* 📊 **Elevated Admin Metric Cards**:
  * Visual KPI stat cards on the Admin dashboard featuring bold 26pt metrics and color-coded accent bars.
* 🪟 **Spacious Tables & Styled Modals**:
  * 36px table row spacing with zebra striping and custom headers.
  * Clean, padded modal dialogs for new facility bookings and resource additions.

---

## 🚀 Key Features by Role

### 1. 🎓 Student & Faculty Portal (`UserFrame`)
* **Browse Resources**:
  * Real-time search by location or facility name.
  * Filter by category (*Computer Lab, Seminar Hall, Laboratory, Meeting Room, Classroom*).
  * Check live capacity and operational availability.
* **Instant Conflict-Guarded Booking**:
  * Select facility, date, start time, and end time.
  * Time-overlap algorithms prevent double bookings and past-date reservations.
* **My Bookings Dashboard**:
  * Track reservation lifecycle (`PENDING` ➜ `APPROVED` / `REJECTED` / `CANCELLED`).
  * View administrator feedback and remarks.
  * Cancel existing pending or approved requests with automatic resource release.

### 2. ⚡ Administrator Command Center (`AdminFrame`)
* **Booking Requests & Approvals**:
  * Centralized queue of all campus requests with applicant details and purpose.
  * One-click **Approve** and **Reject** actions with custom administrative remarks.
  * Dynamic pending requests counter.
* **Resource Management**:
  * Add new campus facilities with ID, category, location, and capacity.
  * Toggle availability between **Available** and **Maintenance**.
  * Guarded deletion preventing removal of resources with active schedules.
* **Utilization & Analytics Reports**:
  * Executive overview KPI cards (Total Resources, Total Bookings, Pending, Approved).
  * Per-facility utilization rates and allocation breakdown table.

---

## 💡 OOP Principles & Architecture

Resourcify implements clean architectural separation between Data, Business Logic, and Presentation:

```
src/
└── resourcify/
    ├── Main.java                      # Application launcher & theme bootstrapper
    ├── UITheme.java                   # Custom Modern SaaS design system & components
    │
    ├── [MODELS]
    │   ├── User.java                  # User entity with encapsulated credentials & role
    │   ├── Resource.java              # Campus facility entity with capacity & status
    │   ├── Booking.java               # Booking model with temporal validation
    │   ├── Role.java                  # Enum: STUDENT, FACULTY, ADMIN
    │   └── BookingStatus.java         # Enum: PENDING, APPROVED, REJECTED, CANCELLED
    │
    ├── [DATA & SERVICES]
    │   ├── DataStore.java             # In-memory mock database seeded with realistic data
    │   └── BookingService.java        # Core business rules, validation, conflict detection
    │
    ├── [VIEWS & CONTROLLERS]
    │   ├── LoginFrame.java            # Dual-pane authentication & demo fill
    │   ├── UserFrame.java             # Student/Faculty browsing & booking portal
    │   └── AdminFrame.java            # Admin approval queue & facility management
    │
    └── [VERIFICATION]
        └── ComponentVerificationTest.java # 37-point automated GUI & business logic test suite
```

| OOP Concept | Implementation Details |
|---|---|
| **Encapsulation** | Strict `private` fields across `User`, `Booking`, and `Resource` with defensive getters, setters, and state guards. |
| **Separation of Concerns** | `BookingService` handles business logic and validations independently from Swing presentation views. |
| **Polymorphism & Roles** | Role-based routing dispatching distinct capabilities and views according to `Role`. |
| **Defensive Exception Handling** | Custom `IllegalArgumentException` validation guarding against time overlaps, negative capacity, and invalid dates. |
| **Modern Java Streams** | Stream filtering and lambda reductions for dynamic utilization calculations and analytics. |

---

## 🔑 Demo Credentials

| Role | Email | Password | Quick Access |
|---|---|---|---|
| **Student** | `student@resourcify.demo` | `student123` | Click **Student** chip on Login screen |
| **Faculty** | `faculty@resourcify.demo` | `faculty123` | Click **Faculty** chip on Login screen |
| **Admin** | `admin@resourcify.demo` | `admin123` | Click **Admin** chip on Login screen |

---

## 🛠️ How to Run

### Prerequisites
* **Java Development Kit (JDK 17 or higher)**
* No build tools (Maven/Gradle) or external libraries required.

### Option 1: Run via VS Code / IntelliJ (Recommended)
1. Open the project folder in your IDE.
2. Open [`Main.java`](src/resourcify/Main.java).
3. Click the **Run** button right above `public static void main` (or press `Shift + F10` / Play button).

### Option 2: Run via Terminal (PowerShell / Command Prompt)
Navigate to the `src` directory and run:

```powershell
# Navigate to the source folder
cd src

# Compile all source files
javac resourcify/*.java

# Run the Resourcify application
java resourcify.Main
```

### Running the Automated Test Suite
To execute the comprehensive 37-point automated verification suite:

```powershell
cd src
java resourcify.ComponentVerificationTest
```
*(Expected output: `Passed: 37, Failed: 0, Status: ALL COMPONENTS FUNCTIONAL & VERIFIED!`)*

---

## 📄 License
Academic PBL Project — Developed for Object-Oriented Programming (OOP) coursework.
