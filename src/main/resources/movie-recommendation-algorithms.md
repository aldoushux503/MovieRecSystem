# Movie Recommendation System: Algorithm Analysis

## Overview

The movie recommendation system implements a comprehensive set of algorithms designed to suggest relevant movies to users based on their preferences. The system uses three distinct recommendation approaches:

1. **Collaborative Filtering**: Recommends movies based on similar users' preferences
2. **Content-Based Filtering**: Recommends movies based on movie attributes and user preferences
3. **Hybrid Recommendation**: Combines both approaches with adaptive weighting

Let's examine how each algorithm works in detail.

## Core Architecture

The system uses the **Template Method Pattern** with an abstract base class (`AbstractRecommendationAlgorithm`) that defines the common workflow:

1. Get all available movies
2. Filter out movies already rated or watched by the user
3. Predict ratings for remaining movies using algorithm-specific logic
4. Sort by predicted rating and return top recommendations

```java
public abstract class AbstractRecommendationAlgorithm implements IRecommendationAlgorithm {
    @Override
    public List<Movie> recommendMovies(Long userId, int limit) {
        // Validate input
        // Get all movies
        // Filter out rated and watched movies
        // Predict ratings using algorithm-specific logic
        // Sort and return top recommendations
    }

    // Template method to be implemented by concrete algorithms
    @Override
    public abstract Map<Long, Double> predictRatings(Long userId, List<Long> movieIds);
}
```

## Collaborative Filtering Algorithm

This algorithm operates on the principle that users who agreed in the past will likely agree in the future.

### How It Works

1. **Find Similar Users (Neighbors)**:
   - Calculate similarity between target user and other users based on their rating patterns
   - Select top N most similar users as the "neighborhood"

2. **Predict Ratings**:
   - For each candidate movie, calculate a weighted average of ratings from similar users
   - Weights are based on user similarity scores

```java
private Map<Long, Double> findSimilarUsers(Long targetUserId, Map<Long, Double> targetUserRatings) {
    // Calculate similarity with each user
    // Filter users with similarity above threshold
    // Sort by similarity and take top N neighbors
}

private Double predictRating(Long movieId, Map<Long, Double> similarUsers,
                            Map<Long, Double> targetUserRatings) {
    // Calculate weighted sum of ratings from similar users
    // Return normalized weighted average
}
```

### Example

Let's say we have the following user ratings (on a scale of 1-10):

| User    | Star Wars | Titanic | Jurassic Park | The Godfather | Inception |
|---------|-----------|---------|---------------|---------------|-----------|
| Alice   | 9         | 6       | 8             | ?             | ?         |
| Bob     | 8         | 5       | 7             | 9             | 6         |
| Charlie | 4         | 8       | 5             | 7             | ?         |
| David   | 9         | 5       | 9             | 8             | 7         |

To predict Alice's rating for "The Godfather":

1. **Calculate user similarities** with Alice:
   - Similarity(Alice, Bob) = 0.95 (high)
   - Similarity(Alice, Charlie) = 0.25 (low)
   - Similarity(Alice, David) = 0.90 (high)

2. **Calculate weighted prediction**:
   - Bob rated "The Godfather" as 9, with similarity 0.95
   - Charlie rated it as 7, with similarity 0.25
   - David rated it as 8, with similarity 0.90
   - Weighted sum: (9 × 0.95) + (7 × 0.25) + (8 × 0.90) = 16.4
   - Sum of weights: 0.95 + 0.25 + 0.90 = 2.1
   - Predicted rating: 16.4 ÷ 2.1 ≈ 7.8

The algorithm would predict Alice would rate "The Godfather" around 7.8.

## Content-Based Filtering Algorithm

This algorithm recommends items based on movie features and user preferences, rather than community behavior.

### How It Works

1. **Build User Profile**:
   - Analyze genres of movies the user has rated
   - Create a weight profile showing preferences for each genre
   - Normalize ratings around the midpoint (5.0) to identify likes and dislikes

2. **Match New Movies**:
   - For each candidate movie, extract its genre profile
   - Calculate similarity between user's genre preferences and movie's genres
   - Convert similarity score to a predicted rating

```java
private Map<Long, Double> buildUserGenreProfile(Long userId) {
    // Get user's ratings
    // Calculate weights for each genre based on ratings
    // Normalize weights by genre occurrence count
    // Return user's genre preference profile
}

private Map<Long, Double> getMovieGenreProfile(Long movieId) {
    // Get movie's genres
    // Create a uniform distribution of weights across genres
    // Return movie's genre profile
}
```

### Example

Using the same user Alice, let's analyze her genre preferences:

1. **Movies Alice has rated**:
   - Star Wars (9/10): {Sci-Fi: 1, Action: 1, Adventure: 1}
   - Titanic (6/10): {Romance: 1, Drama: 1}
   - Jurassic Park (8/10): {Adventure: 1, Sci-Fi: 1, Thriller: 1}

2. **Calculate genre weights** (normalized around 5.0):
   - Sci-Fi: ((9-5)/5 + (8-5)/5)/2 = 0.7
   - Action: (9-5)/5 = 0.8
   - Adventure: ((9-5)/5 + (8-5)/5)/2 = 0.7
   - Romance: (6-5)/5 = 0.2
   - Drama: (6-5)/5 = 0.2
   - Thriller: (8-5)/5 = 0.6

3. **Predict rating for "Inception"** with genres {Sci-Fi: 1, Action: 1, Thriller: 1}:
   - Similarity calculation using cosine similarity between user profile and movie genres
   - High match with user preferences: similarity = 0.9
   - Predicted rating = 5 + (0.9 × 5) = 9.5

The algorithm would predict Alice would rate "Inception" around 9.5.

## Hybrid Recommendation Algorithm

This algorithm combines the strengths of both collaborative and content-based approaches.

### How It Works

1. **Get predictions from both algorithms** separately
2. **Combine predictions with weights**:
   - Default weights are 0.5 for each algorithm
   - For new users (< 5 ratings), weights automatically adjust to favor content-based filtering
3. **Calculate final predictions** as weighted averages

```java
@Override
public Map<Long, Double> predictRatings(Long userId, List<Long> movieIds) {
    // Get predictions from both algorithms
    Map<Long, Double> collaborativePredictions = collaborativeAlgorithm.predictRatings(userId, movieIds);
    Map<Long, Double> contentBasedPredictions = contentBasedAlgorithm.predictRatings(userId, movieIds);

    // Combine predictions with weights
    Map<Long, Double> hybridPredictions = new HashMap<>();
    
    // Process collaborative predictions
    // Process content-based predictions and combine with collaborative
    
    // Handle cold start (new users with few ratings)
    if (isNewUser(userId) && !contentBasedPredictions.isEmpty()) {
        // Increase reliance on content-based predictions
    }

    return hybridPredictions;
}
```

### Example

For Alice's rating of "Inception":

1. **Collaborative prediction**: 7.0
2. **Content-based prediction**: 9.5
3. **Standard weights** (0.5 each):
   - Final prediction = (7.0 × 0.5) + (9.5 × 0.5) = 8.25

If Alice is a new user with just these 3 ratings:
1. **Adjusted weights** (collaborative: 0.3, content-based: 0.7):
   - Final prediction = (7.0 × 0.3) + (9.5 × 0.7) = 8.75

The system would recommend "Inception" with a predicted rating of 8.75.

## Similarity Calculations

The system supports two similarity metrics:

### Cosine Similarity

Measures the cosine of the angle between two vectors, focusing on orientation rather than magnitude.

```java
@Override
public double calculateSimilarity(Map<Long, Double> vector1, Map<Long, Double> vector2) {
    // Calculate dot product
    // Calculate magnitudes
    // Return dot product divided by product of magnitudes
}
```

**Example**: 
- Vector1: {Action: 0.8, Comedy: 0.3}
- Vector2: {Action: 0.9, Comedy: 0.2}
- Dot product = (0.8 × 0.9) + (0.3 × 0.2) = 0.78
- Magnitudes = √(0.8² + 0.3²) × √(0.9² + 0.2²) = 0.85 × 0.92 = 0.78
- Similarity = 0.78 ÷ 0.78 = 0.99 (very similar)

### Pearson Correlation

Measures the linear relationship between vectors by comparing deviations from their means.

```java
@Override
public double calculateSimilarity(Map<Long, Double> vector1, Map<Long, Double> vector2) {
    // Find common keys (items rated by both users)
    // Calculate means
    // Calculate numerator and denominators for correlation
    // Return Pearson correlation
}
```

**Example**:
- User1 ratings: {Movie1: 4, Movie2: 5, Movie3: 3}
- User2 ratings: {Movie1: 8, Movie2: 9, Movie3: 7}
- Mean1 = 4, Mean2 = 8
- Both users rate movies consistently 4 points apart
- Correlation = 1.0 (perfect correlation despite different scales)

## Handling Special Cases

### Cold Start Problem

For new users with few ratings:
- The hybrid algorithm increases the weight of content-based predictions
- This provides meaningful recommendations even without much rating history

```java
private boolean isNewUser(Long userId) {
    int ratingCount = ratingService.getAllUserRatings(userId).size();
    return ratingCount < NEW_USER_THRESHOLD; // Users with fewer than 5 ratings
}
```

### Sparse Data

When users have few overlapping rated movies:
- Collaborative filtering may struggle to find strong similarities
- The system falls back on content-based recommendations

## Factory Pattern Implementation

The system uses factory classes to create algorithm instances:

1. **RecommendationAlgorithmFactory**: Creates and configures the three algorithm types
2. **SimilarityCalculatorFactory**: Creates similarity calculator instances

```java
public IRecommendationAlgorithm createAlgorithm(AlgorithmType type) {
    return switch (type) {
        case COLLABORATIVE_FILTERING -> new CollaborativeFilteringAlgorithm();
        case CONTENT_BASED_FILTERING -> new ContentBasedFilteringAlgorithm();
        case HYBRID_RECOMMENDATION -> new HybridRecommendationAlgorithm();
        default -> throw new IllegalArgumentException("Unknown algorithm type: " + type);
    };
}
```

This allows for easy switching between algorithms and similarity methods without changing client code.

## Conclusion

This recommendation system demonstrates a sophisticated approach that:

1. Uses multiple recommendation strategies to complement each other
2. Adapts to different user scenarios (new users vs. established users)
3. Follows good software design principles (Template Method, Factory, Strategy patterns)
4. Provides explainable recommendations based on either user community patterns or content features

By combining these approaches, the system can deliver personalized movie recommendations while addressing common challenges like the cold-start problem and sparse user data.
