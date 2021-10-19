package com.rahi.moviecatelogservice.controller;

import com.rahi.moviecatelogservice.model.CatalogItem;
import com.rahi.moviecatelogservice.model.UserRating;
import com.rahi.moviecatelogservice.service.MovieInfo;
import com.rahi.moviecatelogservice.service.UserRatingInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private final MovieInfo movieInfo;
    private final UserRatingInfo userRatingInfo;

    public MovieCatalogResource( MovieInfo movieInfo, UserRatingInfo userRatingInfo ) {
        this.movieInfo = movieInfo;
        this.userRatingInfo = userRatingInfo;
    }

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog( @PathVariable("userId") String userId ) {
        UserRating userRating = userRatingInfo.getUserRating(userId);

        assert userRating != null;
        return userRating.getUserRatings().stream()
                .map(movieInfo::getCatalogItem)
                .collect(Collectors.toList());
    }
}
