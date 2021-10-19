package com.rahi.moviecatelogservice.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rahi.moviecatelogservice.model.CatalogItem;
import com.rahi.moviecatelogservice.model.Movie;
import com.rahi.moviecatelogservice.model.Rating;
import com.rahi.moviecatelogservice.model.UserRating;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Amimul Ehsan
 * @Created at 10/17/21
 * @Project Movie Catalog Service
 */

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    private final RestTemplate restTemplate;

    public MovieCatalogResource( RestTemplate restTemplate ) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog( @PathVariable("userId") String userId ) {
        UserRating userRating = getUserRating(userId);

        assert userRating != null;
        return userRating.getUserRatings().stream()
                .map(this::getCatalogItem)
                .collect(Collectors.toList());
    }

    @HystrixCommand(fallbackMethod = "getFallBackCatalogItem")
    private CatalogItem getCatalogItem( Rating rating ) {
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
        assert movie != null;
        return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
    }

    private CatalogItem getFallBackCatalogItem( Rating rating ) {
        return new CatalogItem("Movie name not found", "", rating.getRating());
    }

    @HystrixCommand(fallbackMethod = "getFallBackUserRating")
    private UserRating getUserRating( String userId ) {
        return restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);
    }

    private UserRating getFallBackUserRating( String userId ) {

        UserRating userRating = new UserRating();

        userRating.setUserId(userId);
        userRating.setUserRatings(List.of(
                new Rating("0", 0)
        ));

        return userRating;
    }
}
