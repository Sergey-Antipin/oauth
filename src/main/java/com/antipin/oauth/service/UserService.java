package com.antipin.oauth.service;

import com.antipin.oauth.model.Role;
import com.antipin.oauth.model.User;
import com.antipin.oauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService extends DefaultOAuth2UserService {

    private final UserRepository repository;

    private final RoleService roleService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        return getUserFromDatabase(user);
    }

    private OAuth2User getUserFromDatabase(OAuth2User oauth2User) {
        Optional<User> userFromDb = repository.findByEmail(oauth2User.getAttribute("email"));
        if (userFromDb.isPresent()) {
            Collection<? extends GrantedAuthority> authoritiesFromDb = userFromDb.get().getAuthorities();
            Set<GrantedAuthority> auths = new HashSet<>(authoritiesFromDb);
            auths.addAll(oauth2User.getAuthorities());
            return new DefaultOAuth2User(auths, oauth2User.getAttributes(), "id");
        } else {
            return saveNewUser(oauth2User);
        }
    }

    private OAuth2User saveNewUser(OAuth2User user) {
        Set<Role> roles = user.getAuthorities().stream()
                .map(authority -> new Role(null, authority.getAuthority()))
                .collect(Collectors.toSet());
        roles.add(roleService.getByRole("USER"));

        repository.save(new User(null,
                user.getName(),
                user.getAttribute("email"),
                null,
                roles));

        Set<GrantedAuthority> auths = new HashSet<>(roles);
        return new DefaultOAuth2User(auths, user.getAttributes(), "id");
    }
}
