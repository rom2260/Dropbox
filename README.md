# 📦 Dropbox  

A full-stack application that mimics basic Dropbox functionality using **Spring Boot** and **Angular**.

---

## 🎥 Demo Video

Check out the [demo video](https://drive.google.com/file/d/1RCEac_ewo8e5CH4N4ApXpWvLHInWTOPY/view?usp=sharing) to see the project in action!

---

## 🏛 Architecture  

### 🔹 Backend (Spring Boot)  
- **Storage:** AWS S3 for file storage, MySQL for metadata  

#### **Key Components**  
- `FileService` → Handles file operations (upload/download)  
- `UserService` → Manages user profile  
- `StorageService` → AWS S3 integration for file storage  

### 🔹 Frontend (Angular)  
- **Modern Angular application** with component-based architecture  
- **Material UI** for styling  
- **State management** for user & file operations  

---

## 📌 Prerequisites  

Make sure you have the following installed:  

- **Docker** 

---

## 🚀 Quick Start
### Initial Setup Instructions

When cloning the repository for the **first time**, please follow these steps:

1. **Navigate to the code base** in your terminal.
2. Run the following commands:

   ```bash
   # Remove the existing Docker volume to ensure a fresh start
   docker-compose down -v

   # Build and run the application using Docker Compose
   docker-compose up --build
   ```

**Note:** For subsequent runs, you only need to execute:

```bash
docker-compose up --build
```

This will start your application without needing to remove the Docker volume again.


### Access:
- **Frontend:** [http://localhost:4200](http://localhost:4200)
- **Backend:** [http://localhost:8080](http://localhost:8080)
- **Swagger (api documentation):** [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

---

## ✨ Key Features
- File upload/download
- User management
- Paginated file listing
- Pre-signed URLs for S3 operations

