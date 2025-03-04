package org.solvd.recommendation.observer;

import org.solvd.recommendation.model.UserRating;

// Observer interface
public interface IRatingChangedObserver {
    void onRatingChanged(UserRating rating);
}