# Anime & Manga Content Manager

A Java-based desktop application for managing personal anime and manga collections with user authentication and external API integration.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Installation](#installation)
- [Usage](#usage)
- [API Integration](#api-integration)
- [Exception Handling](#exception-handling)
- [Data Persistence](#data-persistence)
- [Contributing](#contributing)

## 🎯 Overview

This application allows users to create accounts, log in, and manage their personal collections of anime and manga content. It integrates with external APIs to fetch anime and manga data, providing a comprehensive solution for content tracking and management.

## ✨ Features

### User Management

- **User Registration**: Create new accounts with validation
- **User Authentication**: Secure login system
- **Password Validation**: Enforced password security rules
- **Email Validation**: Valid email format checking

### Content Management

- **Anime Collection**: Add, remove, and track anime series
- **Manga Collection**: Add, remove, and track manga series
- **Viewing Status**: Mark content as watched/read or not
- **Content Search**: Search by ID or title
- **Duplicate Prevention**: Avoid adding duplicate content

### External API Integration

- **Jikan API**: Fetch anime data from MyAnimeList
- **Real-time Data**: Get up-to-date anime and manga information
- **Filtered Results**: Store relevant data locally

### Data Persistence

- **JSON Storage**: User data and collections stored in JSON format
- **Automatic Saving**: Changes are automatically persisted
- **Data Loading**: Application state restored on startup

## 🛠 Technologies Used

- **Java 8+**: Core programming language
- **JSON Library**: `json-20240303.jar` for JSON manipulation
- **HTTP Client**: Built-in Java HTTP client for API calls
- **File I/O**: Standard Java file operations for data persistence

## 📁 Project Structure

```
TP_FINAL/
├── src/
│   ├── api/                    # API integration
│   │   ├── GetAnime.java       # Anime API client
│   │   ├── GetManga.java       # Manga API client
│   │   └── IApis.java          # API interface
│   ├── contenido/              # Content models
│   │   ├── Anime.java          # Anime entity
│   │   ├── Contenido.java      # Base content class
│   │   ├── EstadoVisto.java    # Viewing status enum
│   │   └── Manga.java          # Manga entity
│   ├── excepciones/            # Custom exceptions
│   │   ├── ContenidoDuplicadoException.java
│   │   ├── ContenidoNoEncontradoException.java
│   │   ├── ContrasenaInvalidaException.java
│   │   ├── EmailInvalidoException.java
│   │   ├── LoginException.java
│   │   ├── PeticionApiException.java
│   │   ├── UsuarioNoEncontradoException.java
│   │   └── UsuarioRepetidoException.java
│   ├── gestores/               # Business logic managers
│   │   ├── GestorContenido.java    # Content management
│   │   ├── GestorExcepciones.java  # Exception handling
│   │   └── GestorUsuarios.java     # User management
│   ├── manejo_json/            # JSON utilities
│   │   ├── JsonUtil.java           # Base JSON utility
│   │   ├── JsonUtilAnime.java      # Anime JSON operations
│   │   ├── JsonUtilManga.java      # Manga JSON operations
│   │   └── JsonUtilUsuario.java    # User JSON operations
│   ├── menu/                   # User interface
│   │   ├── Main.java           # Application entry point
│   │   └── Menu.java           # Console menu system
│   └── usuario/                # User management
│       ├── Usuario.java            # User entity
│       └── ValidacionUsuario.java  # User validation
├── libs/
│   └── json-20240303.jar       # JSON library dependency
├── usuarios.json               # User data storage
├── pruebaAnime.json           # Anime data cache
└── pruebaManga.json           # Manga data cache
```

## 🚀 Installation

1. **Prerequisites**:

   - Java Development Kit (JDK) 8 or higher
   - IDE with Java support (IntelliJ IDEA, Eclipse, etc.)

2. **Clone the Repository**:

   ```bash
   git clone https://github.com/valentincerezuela04/TP_final_programacion_2.git
   cd TP_final_programacion_2
   ```

3. **Set up Dependencies**:

   - The project includes `json-20240303.jar` in the `libs/` directory
   - Ensure this library is added to your project's classpath

4. **Compile and Run**:
   ```bash
   cd TP_FINAL/src
   javac -cp "../../libs/json-20240303.jar" menu/Main.java
   java -cp ".:../../libs/json-20240303.jar" menu.Main
   ```

## 💻 Usage

### Starting the Application

Run the `Main.java` class to start the console application:

```java
// Entry point
public static void main(String[] args) {
    Menu menu = new Menu();
    menu.menuPrincipal();
}
```

### Main Menu Options

1. **Login**: Access existing user account
2. **Create Account**: Register new user
3. **Exit**: Close application

### User Registration

- Username: Unique identifier
- Password: Must meet security requirements:
  - At least 8 characters
  - One uppercase letter
  - One number
  - No spaces
  - Special characters allowed: period (.)
- Email: Valid email format required

### Content Management

Once logged in, users can:

- Browse available anime/manga from API
- Add content to personal collection
- Mark content as watched/read
- Remove content from collection
- Search personal collection

## 🌐 API Integration

### Jikan API (MyAnimeList)

The application integrates with the Jikan API to fetch anime and manga data:

```java
// Example API call
GetAnime apiClient = new GetAnime();
apiClient.obtenerYGuardarDataFiltrada("pruebaAnime.json");
```

**Endpoints Used**:

- `https://api.jikan.moe/v4/anime` - Anime data
- `https://api.jikan.moe/v4/manga` - Manga data

**Data Retrieved**:

- Title and ID
- Score and popularity
- Synopsis
- Episodes/Chapters
- Status (Airing, Completed, etc.)

## ⚠️ Exception Handling

The application includes comprehensive exception handling:

### User-Related Exceptions

- `UsuarioRepetidoException`: Duplicate username
- `UsuarioNoEncontradoException`: User not found
- `ContrasenaInvalidaException`: Invalid password format
- `EmailInvalidoException`: Invalid email format
- `LoginException`: Authentication failure

### Content-Related Exceptions

- `ContenidoDuplicadoException`: Duplicate content addition
- `ContenidoNoEncontradoException`: Content not found
- `EstadoRepetidoException`: Duplicate status change

### API-Related Exceptions

- `PeticionApiException`: API request failure
- `RespuestaApiException`: Invalid API response

## 💾 Data Persistence

### JSON File Structure

**Users (`usuarios.json`)**:

```json
{
  "usuarios": [
    {
      "id": 1,
      "nombre": "username",
      "contraseña": "password",
      "email": "user@example.com",
      "listaAnime": [...],
      "listaManga": [...]
    }
  ]
}
```

**Anime Data (`pruebaAnime.json`)**:

```json
[
  {
    "id": 1,
    "title": "Anime Title",
    "score": 8.5,
    "episodes": 26,
    "status": "Finished Airing",
    "synopsis": "Description..."
  }
]
```

### Automatic Persistence

- User data is automatically saved after each modification
- API data is cached locally to reduce external requests
- Application state is restored on startup

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Create a Pull Request

### Development Guidelines

- Follow Java naming conventions
- Add appropriate exception handling
- Include comprehensive comments
- Test new features thoroughly
- Update documentation as needed

## 📄 License

This project is developed as part of an academic assignment for Programming 2 at UTN (Universidad Tecnológica Nacional).

## 👥 Authors

- **Pablo** - Development and implementation
- **Valentín Cerezuela** - Project collaboration

## 🔗 Links

- [Jikan API Documentation](https://docs.api.jikan.moe/)
- [JSON Library Documentation](https://github.com/stleary/JSON-java)

---

**Note**: This application is designed for educational purposes and demonstrates object-oriented programming concepts, API integration, exception handling, and data persistence in Java.
