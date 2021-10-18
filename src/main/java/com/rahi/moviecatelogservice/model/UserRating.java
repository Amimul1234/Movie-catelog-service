package com.rahi.moviecatelogservice.model;

import java.util.List;

/**
 * @Author Amimul Ehsan
 * @Created at 10/18/21
 * @Project Movie Catelog Service
 */

public class UserRating {

    private List<Rating> userRatings;

    public UserRating() {
    }

    public UserRating( List<Rating> userRatings ) {
        this.userRatings = userRatings;
    }

    public List<Rating> getUserRatings() {
        return userRatings;
    }

    public void setUserRatings( List<Rating> userRatings ) {
        this.userRatings = userRatings;
    }
}
