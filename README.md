# 🏋️ FitConnect – Full Stack Fitness Platform

A LinkedIn-style platform connecting Fitness Professionals with Clients.

---

## 📁 Project Structure

```
fitconnect/
├── backend/                          # Spring Boot Backend
│   ├── pom.xml                       # Maven dependencies
│   └── src/main/
│       ├── java/com/fitconnect/
│       │   ├── FitConnectApplication.java   # Main entry point
│       │   ├── model/               # JPA Entity classes
│       │   │   ├── User.java
│       │   │   ├── Trainer.java
│       │   │   ├── Booking.java
│       │   │   └── Review.java
│       │   ├── repository/          # Spring Data JPA repositories
│       │   │   ├── UserRepository.java
│       │   │   ├── TrainerRepository.java
│       │   │   ├── BookingRepository.java
│       │   │   └── ReviewRepository.java
│       │   ├── service/             # Business logic layer
│       │   │   ├── AuthService.java
│       │   │   ├── TrainerService.java
│       │   │   ├── BookingService.java
│       │   │   └── ReviewService.java
│       │   ├── controller/          # REST API Controllers
│       │   │   ├── AuthController.java
│       │   │   ├── TrainerController.java
│       │   │   ├── BookingController.java
│       │   │   └── ReviewController.java
│       │   ├── security/            # JWT Security
│       │   │   ├── JwtUtils.java
│       │   │   ├── JwtAuthFilter.java
│       │   │   ├── SecurityConfig.java
│       │   │   ├── UserDetailsImpl.java
│       │   │   └── UserDetailsServiceImpl.java
│       │   └── dto/                 # Data Transfer Objects
│       │       ├── AuthRequest.java
│       │       ├── AuthResponse.java
│       │       ├── RegisterRequest.java
│       │       ├── TrainerDTO.java
│       │       ├── BookingDTO.java
│       │       ├── ReviewDTO.java
│       │       └── ApiResponse.java
│       └── resources/
│           └── application.properties
│
└── frontend/                        # Vanilla HTML/CSS/JS Frontend
    ├── index.html                   # Landing page
    ├── login.html                   # Login page
    ├── signup.html                  # Registration page
    ├── dashboard.html               # User/Trainer dashboard
    ├── trainers.html                # Browse & search trainers
    ├── pages/
    │   └── trainer-profile-edit.html # Trainer profile editor
    ├── css/
    │   └── style.css                # Global styles
    └── js/
        └── api.js                   # API service + utilities
```

---

## 🛢️ MySQL Setup Guide

### Step 1: Install MySQL
- Download MySQL Community Server from https://dev.mysql.com/downloads/
- Or use: `brew install mysql` (Mac) / `sudo apt install mysql-server` (Linux)

### Step 2: Create Database
```sql
CREATE DATABASE fitconnect_db;
CREATE USER 'fitconnect_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON fitconnect_db.* TO 'fitconnect_user'@'localhost';
FLUSH PRIVILEGES;
```

### Step 3: Update application.properties
Edit `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fitconnect_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root          # or fitconnect_user
spring.datasource.password=your_password
```

> **Note:** Tables are created automatically by Hibernate (`spring.jpa.hibernate.ddl-auto=update`)

---

## 🚀 Running the Backend (IntelliJ IDEA)

### Prerequisites
- Java 17+ installed
- IntelliJ IDEA (Community or Ultimate)
- MySQL running on port 3306

### Steps:
1. **Open Project**
   - Open IntelliJ → `File` → `Open` → Select `fitconnect/backend/` folder

2. **Wait for Maven Sync**
   - IntelliJ will auto-import Maven dependencies (watch the progress bar)
   - If not: Right-click `pom.xml` → `Maven` → `Reload Project`

3. **Update Database Config**
   - Open `src/main/resources/application.properties`
   - Set your MySQL username and password

4. **Run the Application**
   - Open `FitConnectApplication.java`
   - Click the green ▶️ Run button
   - Or: Right-click → `Run 'FitConnectApplication'`

5. **Verify it's running**
   - Open browser → `http://localhost:8080/api/trainers`
   - Should return: `{"success":true,"message":"Trainers fetched","data":[]}`

---

## 🌐 Running the Frontend

### Option 1: VS Code Live Server (Recommended)
1. Install VS Code extension: **Live Server**
2. Open `fitconnect/frontend/` in VS Code
3. Right-click `index.html` → `Open with Live Server`
4. Opens at `http://127.0.0.1:5500`

### Option 2: Python HTTP Server
```bash
cd fitconnect/frontend
python3 -m http.server 5500
# Open http://localhost:5500
```

### Option 3: Direct File Opening
- Open any HTML file directly in browser
- Note: Some browsers restrict fetch() for `file://` URLs; use Option 1 or 2 instead

---

## 📡 API Endpoints Reference

### 🔐 Authentication
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/register` | Register new user | ❌ |
| POST | `/api/auth/login` | Login & get JWT | ❌ |

**Register body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "secret123",
  "role": "USER",
  "location": "Mumbai",
  "fitnessGoal": "Weight Loss"
}
```

**Login body:**
```json
{ "email": "john@example.com", "password": "secret123" }
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGci...",
    "userId": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER"
  }
}
```

---

### 🏋️ Trainers
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/trainers` | Get all trainers | ❌ |
| GET | `/api/trainers/{id}` | Get trainer by ID | ❌ |
| GET | `/api/trainers/search?location=Mumbai&skill=Yoga&maxRate=2000&minExp=2` | Search trainers | ❌ |
| POST | `/api/trainers/profile` | Create trainer profile | ✅ TRAINER |
| PUT | `/api/trainers/profile` | Update trainer profile | ✅ TRAINER |
| GET | `/api/trainers/my-profile` | Get my trainer profile | ✅ TRAINER |

---

### 📅 Bookings
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/bookings` | Create booking | ✅ USER |
| GET | `/api/bookings/my-bookings` | Get user's bookings | ✅ |
| GET | `/api/bookings/trainer-bookings` | Get trainer's bookings | ✅ TRAINER |
| PUT | `/api/bookings/{id}/status?status=ACCEPTED` | Accept/Reject booking | ✅ TRAINER |
| PUT | `/api/bookings/{id}/cancel` | Cancel booking | ✅ USER |

**Booking body:**
```json
{
  "trainerId": 1,
  "sessionDate": "2024-03-15",
  "sessionTime": "10:00 AM",
  "sessionType": "In-Person",
  "notes": "Need help with weight loss"
}
```

---

### ⭐ Reviews
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/reviews/trainer/{trainerId}` | Get trainer reviews | ❌ |
| POST | `/api/reviews` | Submit a review | ✅ USER |
| DELETE | `/api/reviews/{id}` | Delete your review | ✅ |

---

## 🔑 JWT Authentication (Frontend Usage)

The JWT token is stored in `localStorage` as `fitconnect_token`.

All protected API calls include the header:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

This is handled automatically by `api.js`:
```javascript
// Login
const res = await AuthAPI.login({ email, password });
Auth.setSession(res.data.token, { id, name, email, role });

// Protected call (token auto-attached)
const trainers = await TrainerAPI.getAll();
```

---

## 🎨 Frontend Pages

| Page | File | Description |
|------|------|-------------|
| Home | `index.html` | Landing page with featured trainers |
| Login | `login.html` | JWT-based login |
| Sign Up | `signup.html` | Register as User or Trainer |
| Dashboard | `dashboard.html` | Booking management & stats |
| Find Trainers | `trainers.html` | Search, filter, book, review |
| Trainer Profile | `pages/trainer-profile-edit.html` | Edit trainer profile |

---

## 🔧 Troubleshooting

### Backend won't start
- Ensure MySQL is running: `mysql -u root -p`
- Check password in `application.properties`
- Verify Java 17: `java -version`

### CORS errors in browser
- Make sure backend is running on port 8080
- CORS is configured to allow all origins in `SecurityConfig.java`

### 401 Unauthorized
- Token expired – log out and log back in
- Check the Authorization header is sent

### "Cannot connect to server"
- Start the Spring Boot backend first
- Frontend calls `http://localhost:8080/api` – ensure this is reachable

---

## 🛠️ Tech Stack Summary

| Layer | Technology |
|-------|------------|
| Frontend | HTML5, CSS3, Vanilla JavaScript |
| Backend | Java 17, Spring Boot 3.2 |
| Database | MySQL 8.0 |
| ORM | Spring Data JPA / Hibernate |
| Security | Spring Security + JWT (JJWT 0.12.3) |
| Build Tool | Maven |
| Fonts | Google Fonts (Bebas Neue + DM Sans) |

---

*Built with ❤️ for the fitness community*
