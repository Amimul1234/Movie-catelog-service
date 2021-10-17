package com.rahi.moviecatelogservice.controller;

import com.rahi.moviecatelogservice.model.CatalogItem;
import com.rahi.moviecatelogservice.model.Movie;
import com.rahi.moviecatelogservice.model.Rating;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog( @PathVariable("userId") String userId ) {

        RestTemplate restTemplate = new RestTemplate();


        //get all related movie IDs
        //For each one call info service and get details
        //Put them all together

        List<Rating> ratings = List.of(
                new Rating("1234", 4),
                new Rating("5678", 3)
        );

        return ratings.stream()
                .map(rating -> {
                    Movie movie = restTemplate.getForObject("http://127.0.0.1:8082/movies/" + rating.getMovieId(), Movie.class);
                    assert movie != null;
                    return new CatalogItem(movie.getName(), "Test", 4);
                })
                .collect(Collectors.toList());

    }
}
