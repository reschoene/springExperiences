package br.com.reschoene.basicAuth1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {
    @Bean
    PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    UserDetailsService getUserDetailsService() {
        var udm = new InMemoryUserDetailsManager();

        var u = User.builder()
                .username("renato")
                .password("12345")
                .roles("read")
                .build();
        udm.createUser(u);

        return udm;
    }
}
