package org.solvd.recommendation.observer;


/**
 * Observer interface for rating change events.
 * Implementations will be notified when a movie rating is added, updated, or deleted.
 */
public interface IRatingChangedObserver {

    void onRatingChanged(Long movieId);
}