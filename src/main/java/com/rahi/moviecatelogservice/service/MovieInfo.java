package com.rahi.moviecatelogservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rahi.moviecatelogservice.model.CatalogItem;
import com.rahi.moviecatelogservice.model.Movie;
import com.rahi.moviecatelogservice.model.Rating;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Author Amimul Ehsan
 * @Created at 10/19/21
 * @Project Movie-catelog-service
 */

@Service
public class MovieInfo {

    private final RestTemplate restTemplate;

    public MovieInfo( RestTemplate restTemplate ) {
        this.restTemplate = restTemplate;
    }

    @HystrixCommand(fallbackMethod = "getFallBackCatalogItem")
    public CatalogItem getCatalogItem( Rating rating ) {
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
        assert movie != null;
        return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
    }

    public CatalogItem getFallBackCatalogItem( Rating rating ) {
        return new CatalogItem("Movie name not found", "", rating.getRating());
    }
}
