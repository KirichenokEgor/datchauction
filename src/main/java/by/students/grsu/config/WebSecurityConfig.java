package by.students.grsu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;



@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    DataSource dataSource;

    @PostConstruct
    void pc(){
        dataSource = new MyDataSource();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                //.antMatchers("/", "/home").permitAll()
                .antMatchers("/registration", "/confirmRegistration").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    //.successForwardUrl("/home")
                    .permitAll()
                    .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .permitAll();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

//        userDetailsManager.createUser(User.withDefaultPasswordEncoder()
//                        .username("KiriEg")
//                        .password("1111")
//                        .roles("ADMIN")
//                        .build());

        return userDetailsManager;
    }
}