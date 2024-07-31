package com.antipin.oauth.controller;

import com.antipin.oauth.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private static OAuth2User principal;

    private String contextPath = "http://localhost";

    @Test
    @WithMockUser(authorities = "SCOPE_read:user")
    void whenAccessProtectedResourceWithAuthThenAllowForAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk());
    }

    @Test
    void whenAccessProtectedResourceAnonymouslyThenRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/user"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(contextPath + "/login"));
    }

    /*@Test
    void whenSignInByOAuth2ThenLoadRolesFromDatabase() throws Exception {
        mockMvc.perform(get("/userinfo")
                        .with(authentication(getOauth2AuthenticationFor(principal))))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.authorities..role").isArray());
    }

    @BeforeAll
    public static void setUpUser() {
        principal = createOAuth2User();
    }

    private static OAuth2User createOAuth2User() {
        *//*Collection<? extends GrantedAuthority> authorities = Set.of(
                new SimpleGrantedAuthority("OAUTH2_USER"),
                new SimpleGrantedAuthority("SCOPE_read:user"));*//*


        Map<String, Object> authorityAttributes = new HashMap<>();
        authorityAttributes.put("email", "mailantipin@gmail.com");
        authorityAttributes.put("id", "1234567890");

        GrantedAuthority authority = new OAuth2UserAuthority(authorityAttributes);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", "1234567890");
        attributes.put("name", "Sergey-Antipin");
        attributes.put("email", "mailantipin@gmail.com");

        return new DefaultOAuth2User(List.of(authority), attributes, "id");
    }

    public static Authentication getOauth2AuthenticationFor(OAuth2User principal) {

        Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();

        return new OAuth2AuthenticationToken(principal, authorities, "github");
    }*/
}
