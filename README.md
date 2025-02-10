# 📦 Dropbox  

A full-stack application that mimics basic Dropbox functionality using **Spring Boot** and **Angular**.

---

## 🏛 Architecture  

### 🔹 Backend (Spring Boot)  
- **Storage:** AWS S3 for file storage, MySQL for metadata  

#### **Key Components**  
- `FileService` → Handles file operations (upload/download)  
- `UserService` → Manages user authentication & profile  
- `StorageService` → AWS S3 integration for file storage  

### 🔹 Frontend (Angular)  
- **Modern Angular application** with component-based architecture  
- **Material UI** for styling  
- **State management** for user & file operations  

---

## 📌 Prerequisites  

Make sure you have the following installed:  

- **Docker & Docker Compose** 

---

## 🚀 Quick Start

### Build and Run:
```bash
docker-compose up --build
```

### Access:
- **Frontend:** [http://localhost:4200](http://localhost:4200)
- **Backend:** [http://localhost:8080](http://localhost:8080)
- **Swagger:** [swagger(api documentation)](http://localhost:8080/swagger-ui/index.html#/)

---

## ✨ Key Features
- File upload/download
- User management
- Paginated file listing
- Pre-signed URLs for S3 operations

