package com.openclassrooms.SpringSecurityAuth.controllers;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class LoginController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    public LoginController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/user")
    public String getUser() {
        return "Welcome user";
    }

    @GetMapping("/admin")
    public String getAdmin() {
        return "Welcome admin";
    }

    @GetMapping("/")
    public String getUserInfo(Principal user, @AuthenticationPrincipal OidcUser oidcUser) {
        StringBuffer userInfo = new StringBuffer();
        if(user instanceof UsernamePasswordAuthenticationToken) {
            userInfo.append(getUsernamePasswordLoginInfo(user));
        } else if (user instanceof OAuth2AuthenticationToken) {
            userInfo.append(getOAuth2LoginInfo(user, oidcUser));
        }
        return userInfo.toString();
    }

    private StringBuffer getUsernamePasswordLoginInfo(Principal user) {
        StringBuffer usernameInfo = new StringBuffer();
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) user;
        if(token.isAuthenticated()) {
            User u = (User) token.getPrincipal();
            usernameInfo.append("Welcome, " + u.getUsername());
        } else {
            usernameInfo.append("NA");
        }
        return usernameInfo;
    }

    private StringBuffer getOAuth2LoginInfo(Principal user, OidcUser oidcUser) {
        StringBuffer protectedInfo = new StringBuffer();
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) user;
        OAuth2AuthorizedClient authClient = authorizedClientService.loadAuthorizedClient(token.getAuthorizedClientRegistrationId(), token.getName());
        if(token.isAuthenticated()) {
            Map<String, Object> userAttributes = ((DefaultOAuth2User) token.getPrincipal()).getAttributes();
            String userToken = authClient.getAccessToken().getTokenValue();
            protectedInfo.append("Welcome, " + userAttributes.get("name") + "<br></br>");
            protectedInfo.append("e-mail: " + userAttributes.get("email") + "<br></br>");
            protectedInfo.append("Access token: " + userToken);

            OidcIdToken idToken = oidcUser.getIdToken();
            if(idToken != null) {
                protectedInfo.append("idToken value: " + idToken.getTokenValue() + "<br></br>");
                protectedInfo.append("Token mapped values: <br>");
                Map<String, Object> claims = idToken.getClaims();
                for(String key: claims.keySet()) {
                    protectedInfo.append(" " + key + " : " + claims.get(key) + "<br>");
                }
            }
        } else {
            protectedInfo.append("NA");
        }
        return protectedInfo;
    }
}
