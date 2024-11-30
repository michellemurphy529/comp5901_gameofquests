# A Game of Quests

## Overview
A Game of Quests is a Spring Boot web application that simulates a multiplayer quest game. Players interact through a web interface, while the backend handles game logic and manages the display. The application supports real-time interaction between 4 players.

---

## Dependencies
- **Java JDK 22**  
- **Maven**  
- **Google Chrome**: Version 131.0.6778.86 (Official Build) (x86_64)  
- **ChromeDriver**: Ensure compatibility with your installed Chrome version. Download it from [Chrome for Testing](https://googlechromelabs.github.io/chrome-for-testing/#stable).  
  - **MacOS Note**: You may need to allow ChromeDriver to run from an unidentified developer in **System Preferences > Security & Privacy**.

---

## Setup and Running the Application

1. **Clone the Repository**  
    Open a terminal and run the following commands:
    ```bash
    git clone <https://github.com/michellemurphy529/comp5901_gameofquests.git>
    cd comp5901_gameofquests
    git checkout comp5901_web_based_implementation
    ```

2. **Build the Project**  
    Use the Maven wrapper to clean and build the project:
    ```bash
    ./mvnw clean install
    ```

3. **Run the Spring Boot Application**  
    Start the application with:
    ```bash
    ./mvnw spring-boot:run
    ```

4. **Access the Application**  
    Open **Google Chrome** and navigate to `http://localhost:8080` in a browser window. Immediately click on the **Register** button to register a single Player ID. 
    
    Do this another 3 times (total of 4 browser windows) for correct setup of the game. 
    
    Player 1 will be prompted to click on **Start Game** button to begin game.

---

## Running Selenium Tests

### Acceptance Tests
This project includes Selenium WebDriver tests to validate web-based functionalities in a multiplayer environment.

1. **Set Up ChromeDriver**  
    Ensure the ChromeDriver executable is in your system's `PATH` or provide its location in the test configuration.
    - AcceptanceTests.java (line 50)

2. **Execute the Acceptance Tests**  
    Run the following Maven command:
    ```bash
    ./mvnw test -Dtest=AcceptanceTests
    ```

---
