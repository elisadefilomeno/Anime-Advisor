package it.unipi.large_scale.anime_advisor.reviewManager;

import it.unipi.large_scale.anime_advisor.entity.Review;

public interface reviewManager {

    public void createReview(Review r);
    public void readReview(Review r);
    public void updateReview(Review r);
    public void deleteReview(Review r);

}
