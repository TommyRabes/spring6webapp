package mg.tommy.springboot.springbootwebapp.controller.api.testconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

// Intellij is definitely running tests his own way
// Dude is able to detect beans that Spring can't (but should) find when we run the maven surefire plugin
// Edit: After further investigation, the problem is actually the maven resources:testResources plugin
// not processing placeholders in test resources because filtering was not enabled in the POM for test resources
// As a result, we keep getting plain string '@spring.security.profile@', not the expected value as active profiles
// in the application.properties file in the target directory
// So none of the RequestPostProcessor beans defined below are going to be registered in the context
@TestConfiguration
public class MockedBean {

    // Set default value in case the 'http-basic' profile is not active
    // because only application-HTTP-BASIC.yml defines those properties
    // Otherwise you will have odd behaviour
    // For example, assuming that the 'http-basic' profile is not active, and we don't provide any default value here
    // if you run a @WebMvcTest slice via Intellij
    // the expression will not be evaluated
    // however, running a @SpringBootTest via Intellij will throw an error complaining that the property couldn't be resolved
    @Value("${spring.security.user.name:defaultUsername}")
    private String httpBasicUsername;

    @Value("${spring.security.user.password:defaultPassword}")
    private String httpBasicPassword;

    @Profile("http-basic")
    @Bean
    public RequestPostProcessor mvcHttpBasicProcessor() {
        return httpBasic(httpBasicUsername, httpBasicPassword);
    }

    @Profile("oauth2")
    @Bean
    public RequestPostProcessor mvcOAuth2Processor() {
        return jwt().jwt(jwtBuilder -> {
            jwtBuilder.claims(claims -> {
                        claims.put("scope", "message.read");
                        claims.put("scope", "message.write");
                    })
                    .subject("oidc-client")
                    .notBefore(Instant.now().minusSeconds(5L));
        });
    }

    @Profile("form-login")
    @Bean
    public RequestPostProcessor mvcLoginProcessor() {
        return user(User.builder()
                .username("admin@gmail.com")
                .password("{pbkdf2@SpringSecurity_v5_8}5070e01244772466c4b842cc367df8164deaa379b73d04bd36f524b94a9540e4b4014e9d3c6f2d8d52f936fb0c5b6823")
                .roles("ADMIN")
                .build());
    }

    //@Profile("oauth2")
    //@Bean
    public JwtDecoder jwtDecoder() {
        JwtDecoder jwtDecoder = mock(JwtDecoder.class);
        Jwt jwt = Jwt.withTokenValue("token")
                .claims(claims -> {
                    claims.put("scope", "message.read");
                    claims.put("scope", "message.write");
                })
                .header("alg", "none")
                .subject("oidc-client")
                .notBefore(Instant.now().minusSeconds(5L))
                .build();
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);
        return jwtDecoder;
    }
}
