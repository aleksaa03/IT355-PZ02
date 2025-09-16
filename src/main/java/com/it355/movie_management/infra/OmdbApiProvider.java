package com.it355.movie_management.infra;

import com.it355.movie_management.common.config.AppConfig;
import com.it355.movie_management.dtos.movie.MovieDto;
import com.it355.movie_management.exceptions.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Component
public class OmdbApiProvider {
    private final String apiUrl;
    private final String apiKey;

    public OmdbApiProvider(AppConfig config) {
        this.apiUrl = "http://www.omdbapi.com";
        this.apiKey = config.getApiKey();
    }

    public Map<String, Object> searchMovies(String search, String type, int page) {
        if (search.length() < 3) {
            throw new BadRequestException("Search query must be at least 3 char long.");
        }

        String query = String.format("apikey=%s&s=%s&page=%d", this.apiKey, search, page);
        if (type != null && !type.isEmpty()) {
            query += "&type=" + type;
        }

        String url = this.apiUrl + "?" + query;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BadRequestException("Error fetching data from OMDB API.");
        }

        Map<String, Object> content = response.getBody();

        if ("False".equals(content.get("Response"))) {
            throw new BadRequestException(content.get("Error").toString());
        }

        Map<String, Object> result = Map.of(
                "movies", content.get("Search"),
                "totalResults", Integer.parseInt(content.get("totalResults").toString())
        );

        return result;
    }

    public MovieDto getMovieByImdbId(String imdbId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = this.apiUrl + "?i=" + imdbId + "&apikey=" + this.apiKey;

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BadRequestException("Error fetching data from OMDB API.");
        }

        Map<String, Object> content = response.getBody();

        if ("False".equals(content.get("Response"))) {
            throw new BadRequestException(content.get("Error").toString());
        }

        String releasedStr = (String) content.get("Released");
        Date released = null;
        if (releasedStr != null && !releasedStr.equalsIgnoreCase("N/A")) {
            try {
                released = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).parse(releasedStr);
            } catch (ParseException e) {
                throw new BadRequestException("Invalid date format for released date.");
            }
        }

        String imdbRatingStr = (String) content.get("imdbRating");
        BigDecimal imdbRating = null;
        try {
            imdbRating = new BigDecimal(imdbRatingStr);
        } catch (Exception ignored) {}

        MovieDto movie = new MovieDto();
        movie.setTitle((String) content.get("Title"));
        movie.setImg((String) content.get("Poster"));
        movie.setImdbId(imdbId);
        movie.setType((String) content.get("Type"));
        movie.setReleased(LocalDate.ofInstant(released.toInstant(), ZoneId.systemDefault()));
        movie.setImdbRating(imdbRating);
        movie.setPlot((String) content.get("Plot"));
        movie.setActors((String) content.get("Actors"));
        movie.setGenre((String) content.get("Genre"));

        return movie;
    }
}