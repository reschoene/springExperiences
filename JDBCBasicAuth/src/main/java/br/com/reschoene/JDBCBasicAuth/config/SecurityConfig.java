package br.com.reschoene.JDBCBasicAuth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {
    //@Autowired
    //DataSource dataSource;

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    UserDetailsService userDetailsService(){
        //return new JdbcUserDetailsManager(dataSource);
        return new JdbcUserDetailsManager(dataSource());
    }

    @Bean
    DataSource dataSource(){
        //Aqui posso usar tanto desta forma, onde crio uma instancia de DriverManagerDataSource para
        //retornar um bean DataSource, ou entao, no Application.properties configurar o data source por la
        //Se fizer via arq config, entao teria que descomentar o trecho acima que injeta e comentar este metodo
        var driverManager = new DriverManagerDataSource();

        driverManager.setUrl("jdbc:mysql://localhost/springTests");
        driverManager.setUsername("root");
        driverManager.setPassword("root");

        return driverManager;
    }
}
