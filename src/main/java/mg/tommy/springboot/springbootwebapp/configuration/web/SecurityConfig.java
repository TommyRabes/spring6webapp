package mg.tommy.springboot.springbootwebapp.configuration.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Configure WebSecurity to ignore h2 console path
     * See <a href="https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter">...</a>
     * A better alternative is to go through HttpSecurity's methods where we have more control of which role has access
     * to the console and fine-tuned the security checks that should be enabled/disabled
     */
    //@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/console/**"));
    }

    @Profile("H2")
    @Bean
    @Order(1)
    public SecurityFilterChain h2FilterChain(HttpSecurity securityBuilder) throws Exception {
        securityBuilder
                .securityMatcher(toH2Console())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(toH2Console()).permitAll()
                )
                // Allows access to H2 console without Spring Security extra checks
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(toH2Console())
                        // csrf protection is activated by default, but it's safe to disable for REST endpoints
                        // see: https://stackoverflow.com/questions/19468209/spring-security-configuration-http-403-error
                        // and https://spring.io/blog/2013/08/21/spring-security-3-2-0-rc1-highlights-csrf-protection
                        .ignoringRequestMatchers("/plans", "/plans/**")
                        .ignoringRequestMatchers("/api/**"))
                .headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin()));

        return securityBuilder.build();
    }

    @Bean
    public SecurityFilterChain routeAccessFilterChain(HttpSecurity securityBuilder) throws Exception {
        securityBuilder
                .authorizeHttpRequests(requests -> requests
                        // Must add those 3 values to allow access to index.html
                        // Spring forward any get request that match "" and "/" to "/index.html", if no explicit request mapping has been set
                        .requestMatchers("", "/", "/index.html").permitAll()

                        // Can't use suffix pattern matching along with '**' multi-segment matching (e.g: **/*.css)
                        // as of Spring 5.3 since PathPattern has been introduced as the new default pattern matching strategy
                        // Prior to Spring 5.3, it used to be AntPathMatcher. To toggle back to AntPatchMatcher,
                        // override the following property: 'spring.mvc.pathmatch.matching-strategy: ant_path_matcher'
                        // (From Spring 5.3 on, the default is 'path-pattern-parser')
                        .requestMatchers("/css/**", "/webjars/**").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/plans", "/plans/**").permitAll()

                        // An alternative to Spring's @PreAuthorize annotation
                        // But remember that setting this control here (in a more global context)
                        // rather than at the controller level results in not being able to handle any client access denial
                        // in an @ExceptionHandler annotated method
                        // This check is performed earlier in the FilterChain
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        //.hasRole("ADMIN")
                        // lockdown any other request
                        .anyRequest().permitAll()
                        // .authenticated()
                )

                // Overrides Spring's default form-based login
                // now we must explicitly handle the '/login' endpoint
                .formLogin(form -> form.loginPage("/login").usernameParameter("email").permitAll())

                // Looks like by default, Spring allows /login?error
                // But as for /login?logout, we need to explicitly permitAll on that route
                // otherwise it will redirect us to /login as Spring considers any request needs authentication
                // since we've called anyRequest().authenticated()
                .logout(configurer -> configurer.logoutSuccessUrl("/login?logout").permitAll())

                .csrf(csrf -> csrf
                        // csrf protection is activated by default, but it's safe to disable for REST endpoints
                        // see: https://stackoverflow.com/questions/19468209/spring-security-configuration-http-403-error
                        // and https://spring.io/blog/2013/08/21/spring-security-3-2-0-rc1-highlights-csrf-protection
                        .ignoringRequestMatchers("/plans", "/plans/**")
                        .ignoringRequestMatchers("/api/**"))
                .headers(headers -> headers.frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin()));

        return securityBuilder.build();
    }

/*
    @Bean
    public UserDetailsManager userDetailsManager() {
        UserDetails reader = User.builder()
                .username("reader@gmail.com")
                .password("{bcrypt}$2a$10$pB1fvMDW9HA6L4Y4ULAzy.9j1VRQZ3BXnXuXspH.FtanemnJXoxC2")
                .roles("READER")
                .build();
        UserDetails client = User.builder()
                .username("client@gmail.com")
                .password("{bcrypt}$2a$10$UWlGnleelzPZ2J0gJfBLNuvtQp6ty3SMwnnvltncES.exxIArIzfu")
                .roles("CLIENT")
                .build();
        UserDetails admin = User.builder()
                .username("admin@gmail.com")
                .password("{pbkdf2@SpringSecurity_v5_8}5070e01244772466c4b842cc367df8164deaa379b73d04bd36f524b94a9540e4b4014e9d3c6f2d8d52f936fb0c5b6823")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(reader, client, admin);
    }
*/
/*
    // Spring offers the possibility to define a custom PasswordEncoder instead of the default DelegatingPasswordEncoder
    // A DelegatingPasswordEncoder will try to figure out the encoding algorithm by reading the prefix of stored passwords
    // Here, we are defining a custom DelegatingPasswordEncoder
    // Usually, Spring Security's default DelegatingPasswordEncoder is OK
    // unless you want to handle a specific encoding algorithm for example
    @Bean
    public static PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("pbkdf2", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_5());
        encoders.put("pbkdf2@SpringSecurity_v5_8", Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v4_1());
        encoders.put("scrypt@SpringSecurity_v5_8", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("argon2", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_2());
        encoders.put("argon2@SpringSecurity_v5_8", Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoders.put("sha256", new StandardPasswordEncoder());

        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }
 */
}
