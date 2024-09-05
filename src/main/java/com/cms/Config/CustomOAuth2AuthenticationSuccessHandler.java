package com.cms.Config;

import com.cms.Helpers.JWTUtilHelper;
import com.cms.Models.Providers;
import com.cms.Models.User;
import com.cms.Services.Impl.SecurityCustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.cms.Repositories.userRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Component
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JWTUtilHelper jwtUtil;

    @Autowired
    private userRepository userRepository;

    @Autowired
    @Lazy
    private SecurityCustomUserDetailsService userDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
        System.out.println(authorizedClientRegistrationId);

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {
            //google authentication
            //google attributes
            String email = oAuth2User.getAttribute("email"); // Ensure this matches the email attribute from your OAuth provider
            User userDetails = null;
            try {
                userDetails = (User) userDetailsService.loadUserByUsername(email);
            }
            catch (Exception e) {
                String name = oAuth2User.getAttribute("name");
                String picture = oAuth2User.getAttribute("picture");

                userDetails = new User();
                userDetails.setUserId(UUID.randomUUID().toString());
                userDetails.setName(name);
                userDetails.setEmail(email);
                userDetails.setProfilePic(picture);
                userDetails.setPassword("password");
                userDetails.setProvider(Providers.GOOGLE);
                //set user role
                userDetails.setRoleList(List.of("ROLE_USER"));
                userDetails.setEnabled(true);
                userDetails.setEmailVerified(true);
                userDetails.setProviderUserId(oAuth2User.getName());
                userDetails.setAbout("This account is created using Github");

                this.userRepository.save(userDetails);
            }
            finally {
                //generate JWT token
                String token = jwtUtil.generateToken(userDetails);
                System.out.println(userDetails);

                // Log the generated details for debugging
                System.out.println("OAuth2 User Details: " + oAuth2User);
                System.out.println("Generated JWT Token: " + token);

                // Redirect to frontend with the token in the URL
                String redirectUrl = "http://localhost:5173/user/dashboard?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8.toString());
                response.sendRedirect(redirectUrl);
            }
        }
        else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {
            //GitHub's authentication
            //GitHub login
            String email = oAuth2User.getAttribute("email") != null ? oAuth2User.getAttribute("email") : oAuth2User.getAttribute("login") + "@github.com"; // Ensure this matches the email attribute from your OAuth provider
            User userDetails = null;
            try {
                userDetails = (User) userDetailsService.loadUserByUsername(email);
            }
            catch (Exception e) {
                String name = oAuth2User.getAttribute("login");
                String picture = oAuth2User.getAttribute("avatar_url");

                userDetails = new User();
                userDetails.setUserId(UUID.randomUUID().toString());
                userDetails.setName(name);
                userDetails.setEmail(email);
                userDetails.setProfilePic(picture);
                userDetails.setPassword("password");
                userDetails.setProvider(Providers.GITHUB);
                //set user role
                userDetails.setRoleList(List.of("ROLE_USER"));
                userDetails.setEnabled(true);
                userDetails.setEmailVerified(true);
                userDetails.setProviderUserId(oAuth2User.getName());
                userDetails.setAbout("This account is created using Gooogle");

                this.userRepository.save(userDetails);
            }
            finally {
                //generate JWT token
                String token = jwtUtil.generateToken(userDetails);
                System.out.println(userDetails);

                // Log the generated details for debugging
                System.out.println("OAuth2 User Details: " + oAuth2User);
                System.out.println("Generated JWT Token: " + token);

                // Redirect to frontend with the token in the URL
                String redirectUrl = "http://localhost:5173/user/dashboard?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8.toString());
                response.sendRedirect(redirectUrl);
            }
        }
    }
}
