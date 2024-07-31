package com.antipin.oauth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class OAuth2LogoutHandler implements LogoutHandler {

    private final OAuth2AuthorizedClientService authorizedClientService;

    private final RestTemplate restTemplate;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
            String clientRegistrationId = oauth2Token.getAuthorizedClientRegistrationId();

            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                    clientRegistrationId, oauth2Token.getName());

            String accessToken = authorizedClient.getAccessToken().getTokenValue();
            String clientId = authorizedClient.getClientRegistration().getClientId();
            String clientSecret = authorizedClient.getClientRegistration().getClientSecret();

            String body = "{\"access_token\":\"" + accessToken + "\"}";
            String revokeUrl = "https://api.github.com/applications/" + clientId + "/grant";
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(clientId, clientSecret);
            HttpEntity<String> entity = new HttpEntity<>(body, headers);

            restTemplate.exchange(revokeUrl, HttpMethod.DELETE, entity, Void.class);
        }
    }
}
