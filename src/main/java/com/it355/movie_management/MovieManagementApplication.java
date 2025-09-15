package com.it355.movie_management;

import com.it355.movie_management.middlewares.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MovieManagementApplication {
    private final AuthFilter authFilter;

    public MovieManagementApplication(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

	public static void main(String[] args) {
		SpringApplication.run(MovieManagementApplication.class, args);
	}

    @Bean
    public FilterRegistrationBean<AuthFilter> jwtFilter() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(authFilter);
        registrationBean.addUrlPatterns("/logout", "/check-auth", "/movies/*", "/movies/*/*", "/watch-list/*", "/watch-list/*/*", "/users/*", "/users/*/*", "/user-logs/*", "/user-logs/*/*");
        return registrationBean;
    }
}