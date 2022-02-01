package it.unipi.large_scale.anime_advisor.reviewManager;

import it.unipi.large_scale.anime_advisor.entity.Review;

public interface reviewManager {

    void createReview(Review r);
    void readReview(Review r);
    void updateReview(Review r);
    void deleteReview(Review r);

}
