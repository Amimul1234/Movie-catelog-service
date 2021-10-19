package com.rahi.moviecatelogservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rahi.moviecatelogservice.model.Rating;
import com.rahi.moviecatelogservice.model.UserRating;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @Author Amimul Ehsan
 * @Created at 10/19/21
 * @Project Movie-catelog-service
 */

@Service
public class UserRatingInfo {

    private final RestTemplate restTemplate;

    public UserRatingInfo( RestTemplate restTemplate ) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "getFallBackUserRating")
    public UserRating getUserRating( String userId ) {
        return restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);
    }

    public UserRating getFallBackUserRating( String userId ) {

        UserRating userRating = new UserRating();

        userRating.setUserId(userId);
        userRating.setUserRatings(List.of(
                new Rating("0", 0)
        ));

        return userRating;
    }
}
