# Movie Recommendation System

## Overview
This project is a Movie Recommendation System built using Java, SQL, and Maven. It demonstrates CRUD operations and various relationships between entities such as Users, Movies, Genres, and Ratings.

It also uses MyBatis to interact with the database and perform operations such as inserting, updating, deleting, and querying data.

## Technologies Used
- Java
- SQL
- Maven
- MyBatis

## Project Structure
- `src/main/java/org/solvd/recommendation`: Contains the main Java source code.
- `src/main/resources/org/solvd/recommendation/mapper`: Contains MyBatis XML mapper files.

## Setup and Installation
1. Clone the repository:
    ```sh
    git clone https://github.com/aldoushux503/MovieRecSystem.git
    ```
2. Navigate to the project directory:
    ```sh
    cd <project-directory>
    ```
3. Build the project using Maven:
    ```sh
    mvn clean install
    ```

## Running the Application
To run the application, execute the `Main` class located in `src/main/java/org/solvd/recommendation/Main.java`.

## Usage
The application demonstrates the following operations:
- Creating and managing users, movies, genres, and ratings.
- Associating genres with movies.
- Adding user ratings and viewing history.
- Performing CRUD operations on the entities.
