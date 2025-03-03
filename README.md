# Movie Recommendation System

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![MyBatis](https://img.shields.io/badge/MyBatis-3.5.16-red.svg)](https://mybatis.org/mybatis-3/)

A sophisticated movie recommendation engine built with Java, demonstrating SOLID principles and enterprise-level design patterns. This application offers personalized movie suggestions through multiple recommendation algorithms and comprehensive user preference analysis.

## Table of Contents

- [Overview](#overview)
- [System Architecture](#system-architecture)
- [Database Model](#database-model)
- [Key Features](#key-features)
- [Recommendation Algorithms](#recommendation-algorithms)
- [Design Patterns](#design-patterns)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Usage Examples](#usage-examples)
- [Future Improvements](#future-improvements)

## Overview

This recommendation system provides a complete system for managing movie data, user profiles, viewing history, ratings, and generating tailored movie recommendations. The system leverages multiple recommendation algorithms to balance suggesting familiar content with discovering new engaging titles.

## System Architecture

The system follows a layered architecture:

```
┌─────────────────────────────────────────────────────────────┐
│                      Service Layer                          │
│  ┌─────────────────┐ ┌─────────────────┐ ┌───────────────┐  │
│  │ Entity Services │ │Recommendation   │ │Relationship   │  │
│  │                 │  Algorithms       │ │Services       │  │
│  └─────────────────┘ └─────────────────┘ └───────────────┘  │
├─────────────────────────────────────────────────────────────┤
│                      Data Access Layer                      │
│  ┌─────────────────┐ ┌─────────────────┐ ┌───────────────┐  │
│  │ DAO Interfaces  │ │ MyBatis DAO     │ │ Mappers       │  │
│  │                 │ │ Implementations │ │               │  │
│  └─────────────────┘ └─────────────────┘ └───────────────┘  │
├─────────────────────────────────────────────────────────────┤
│                      Domain Model Layer                     │
│  ┌─────────────────┐ ┌─────────────────┐ ┌───────────────┐  │
│  │ Entity Models   │ │ Utility Classes │ │ Enums         │  │
│  │                 │ │                 │ │               │  │
│  └─────────────────┘ └─────────────────┘ └───────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

## Database Model

The system uses a relational database with the following key entities:

- **Movies**: Core content with attributes like title, duration, and average rating
- **Users**: System users derived from Person entities
- **Genres**: Categories for classifying movies
- **Ratings**: User ratings for movies
- **Viewing History**: Record of movie watches
- **Interactions**: Different ways users interact with content (like, dislike, etc.)
- **People**: Actors, directors, and other contributors
- **Roles**: Types of contributions to movies (director, actor, etc.)

<!-- Add your database diagram here -->
![Database Model](https://github.com/aldoushux503/MovieRecSystem/blob/main/src/main/resources/database/MovieRecSystem.png)

## Key Features

- **Multiple Recommendation Algorithms**:
  - Collaborative filtering based on user similarity
  - Content-based filtering using movie attributes
  - Hybrid approach combining both methods
  
- **Comprehensive Movie Information**:
  - Basic movie details (title, duration)
  - Genre classification
  - Content contributor tracking
  - Dynamic rating calculation

- **User Profile Management**:
  - Viewing history tracking
  - Rating collection
  - Genre preferences
  - User interactions (likes, dislikes, favorites)

- **Advanced Algorithm Features**:
  - Cold-start problem handling for new users
  - Similarity calculations using multiple methods
  - Trending movie detection
  - Similar movie identification

## Recommendation Algorithms

### Collaborative Filtering

Recommends movies based on similar users' preferences. The system:
1. Identifies users with similar taste profiles
2. Analyzes their ratings and viewing history
3. Recommends highly-rated movies from similar users

**Similarity calculation methods include**:
- Pearson Correlation (default): Measures linear correlation between user rating patterns
- Cosine Similarity: Calculates the cosine of the angle between user rating vectors

```java
// Sample workflow
Map<Long, Double> userSimilarities = findSimilarUsers(userId, userRatings);
for (Long movieId : candidateMovies) {
    Double predictedRating = calculateWeightedRating(movieId, userSimilarities);
    predictions.put(movieId, predictedRating);
}
```

### Content-Based Filtering

Recommends movies based on attributes (primarily genres) of movies the user has enjoyed. The system:
1. Builds a user profile based on rated movie genres
2. Creates genre profiles for candidate movies
3. Calculates similarity between user and movie profiles

This approach is especially useful for new users and niche preferences, handling the "cold start" problem effectively.

### Hybrid Recommendation

Combines collaborative and content-based approaches for balanced recommendations. It:
1. Gets predictions from both algorithms
2. Weights results (configurable, default 50/50)
3. Dynamically adjusts weights based on user history
4. Handles cold-start scenarios by prioritizing content-based recommendations

## Design Patterns

The project implements numerous design patterns to ensure maintainability and extensibility:

- **Factory Pattern**: Centralized creation of service and DAO objects
  - `ServiceFactory`: Creates service instances
  - `DAOFactory`: Creates DAO objects
  - `RecommendationAlgorithmFactory`: Creates algorithm instances

- **Singleton Pattern**: Ensures single instances of factories
  - `ServiceFactory.getInstance()`
  - `RecommendationAlgorithmFactory.getInstance()`

- **Strategy Pattern**: Pluggable algorithms
  - `SimilarityCalculator` interface with multiple implementations
  - `RecommendationAlgorithm` interface with various strategies

- **Template Method**: Common operations in abstract classes
  - `AbstractService` defines CRUD operations
  - `AbstractRecommendationAlgorithm` standardizes recommendation flow
  - 
- **Observer Pattern**: Decouples components through event notification
  - `RatingChangedObserver`: Interface for rating change notifications
  - `MovieRatingObserver`: Updates movie average ratings when user ratings change
  - `UserRatingService`: Notifies observers of rating changes

- **Data Access Object (DAO)**: Separates business logic from data access
  - Interface and implementation separation
  - Type-safe generic parameterization

- **Builder Pattern**: Simplified object creation
  - Leverages Lombok `@Builder` for clean construction

- **Composite Pattern**: Composite keys for relationship entities
  - `CompositeKey2` and `CompositeKey3` for multi-field identifiers

## Project Structure

```
src/main/java/org/solvd/recommendation/
├── Main.java                     # Application entry point
├── RecommendationDemo.java       # Demo showcasing algorithms
├── algorithm/                    # Recommendation algorithms
│   ├── IRecommendationAlgorithm.java
│   ├── AbstractRecommendationAlgorithm.java
│   ├── CollaborativeFilteringAlgorithm.java
│   ├── ContentBasedFilteringAlgorithm.java
│   ├── HybridRecommendationAlgorithm.java
│   ├── RecommendationAlgorithmFactory.java
│   └── similarity/               # Similarity calculation algorithms
│       ├── ISimilarityCalculator.java
│       ├── CosineSimilarityCalculator.java
│       ├── PearsonCorrelationCalculator.java
│       ├── SimilarityCalculatorFactory.java
│       └── SimilarityMethod.java
├── dao/                          # Data Access Objects
│   ├── IDAO.java                 # Generic DAO interface
│   ├── IGenreDAO.java            # And other entity-specific interfaces
│   ├── DAOFactory.java           # Factory for DAOs
│   └── mybatis/                  # MyBatis implementation
│       ├── AbstractMyBatisDAO.java
│       └── entity-specific implementations
├── model/                        # Domain models
│   ├── Movie.java
│   ├── User.java
│   ├── Genre.java
│   └── other entity models
├── service/                      # Business logic
│   ├── AbstractService.java       
│   ├── ServiceFactory.java
│   ├── RecommendationService.java
│   ├── entity-specific services
│   └── interfaces/               # Service interfaces
├── util/                         # Utility classes  
└── exception/                    # Custom exceptions
```

## Getting Started

### Prerequisites

- Java 17
- MySQL 8.0+
- Maven

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/aldoushux503/MovieRecSystem.git
   cd MovieRecSystem
   ```

2. Configure database connection in `src/main/resources/config.properties`:
   ```properties
   db.user=your_username
   db.password=your_password
   db.url=jdbc:mysql://localhost:3306/MovieRecSystem
   ```

3. Create the database schema:
   ```bash
   mysql -u your_username -p < src/main/resources/database/MovieRecSystem.sql
   ```

4. Build the project:
   ```bash
   mvn clean install
   ```

5. Run the demo:
   ```bash
   mvn exec:java -Dexec.mainClass="org.solvd.recommendation.RecommendationDemo"
   ```

## Configuration

The system is configurable through various parameters:

- Database connection in `config.properties`
- Similarity method selection in algorithm creation

## Usage Examples

### Basic Recommendation

```java
// Get the recommendation service
ServiceFactory serviceFactory = ServiceFactory.getInstance();
IRecommendationService recommendationService = serviceFactory.getRecommendationService();

// Get personalized recommendations for a user
Long userId = 123L;
List<Movie> recommendations = recommendationService.recommendMoviesForUser(userId, 10);

// Print recommendations
recommendations.forEach(movie -> {
    System.out.println(movie.getTitle() + " (" + movie.getAverageRating() + ")");
});
```

### Specific Algorithm Selection

```java
// Get content-based recommendations specifically
List<Movie> contentBasedRecs = recommendationService.getContentBasedRecommendations(userId, 5);

// Get collaborative filtering recommendations
List<Movie> collaborativeRecs = recommendationService.getCollaborativeFilteringRecommendations(userId, 5);
```

### Custom Algorithm Configuration

```java
// Create a factory
RecommendationAlgorithmFactory factory = RecommendationAlgorithmFactory.getInstance();

// Configure collaborative filtering with Cosine similarity and 15 neighbors
IRecommendationAlgorithm algorithm = factory.createCollaborativeAlgorithm(
    SimilarityMethod.COSINE, 15);

// Generate recommendations
List<Movie> recommendations = algorithm.recommendMovies(userId, 10);
```

