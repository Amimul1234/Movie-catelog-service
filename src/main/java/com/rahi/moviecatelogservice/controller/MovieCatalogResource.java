package com.rahi.moviecatelogservice.controller;

import com.rahi.moviecatelogservice.model.CatalogItem;
import com.rahi.moviecatelogservice.model.Movie;
import com.rahi.moviecatelogservice.model.UserRating;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

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
    private final WebClient.Builder webClientBuilder;
    private final DiscoveryClient discoveryClient;

    public MovieCatalogResource( RestTemplate restTemplate, WebClient.Builder webClientBuilder,
                                 DiscoveryClient discoveryClient ) {
        this.restTemplate = restTemplate;
        this.webClientBuilder = webClientBuilder;
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/getInstances")
    public List<ServiceInstance> getServiceInstances() {
        return discoveryClient.getInstances("ratings-data-service");
    }

    @GetMapping("/{userId}")
    public List<CatalogItem> getCatalog( @PathVariable("userId") String userId ) {

        //For each one call info service and get details
        //Put them all together

        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsData/user/" + userId,
                UserRating.class);

        assert ratings != null;
        return ratings.getUserRatings().stream()
                .map(rating -> {

//                    Movie movie = webClientBuilder.build()
//                            .get()
//                            .uri("http://127.0.0.1:8082/movies/" + rating.getMovieId())
//                            .retrieve()
//                            .bodyToMono(Movie.class)
//                            .block();


                    Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);

                    assert movie != null;
                    return new CatalogItem(movie.getName(), "Test", 4);
                })
                .collect(Collectors.toList());
    }
}
