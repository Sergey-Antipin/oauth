package com.antipin.oauth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final RestTemplate restTemplate;

    @GetMapping("/userinfo")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<OAuth2User> onlyForAuthenticated(@AuthenticationPrincipal OAuth2User user) {
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> forUsualUser() {
        return ResponseEntity.ok("for all users");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> forUserWithPrivileges() {
        return ResponseEntity.ok("only for admins");
    }

    @GetMapping("/")
    public String index() {
        return "redirect:index.html";
    }

    @GetMapping("/repos")
    public ResponseEntity<String> getRepos(@RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient client) {
        String accessToken = client.getAccessToken().getTokenValue();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "https://api.github.com/user/repos";
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

}
