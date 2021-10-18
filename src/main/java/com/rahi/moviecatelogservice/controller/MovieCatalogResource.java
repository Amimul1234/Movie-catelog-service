package com.rahi.moviecatelogservice.controller;

import com.rahi.moviecatelogservice.model.CatalogItem;
import com.rahi.moviecatelogservice.model.Movie;
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
 * @Project Movie Catelog Service
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

        UserRating userRating = restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);

        return userRating.getUserRatings().stream()
                .map(rating -> {
                    Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
                    return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
                })
                .collect(Collectors.toList());

    }
}
