package ma.enset.hospitalapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Bean
    public UserDetailsService inMemoryUserDetailsManager(){
        return new InMemoryUserDetailsManager(
                User.withUsername("secretaire").password(passwordEncoder.encode("1234")).
                        roles("ADMIN").build(),
                User.withUsername("medecin").password(passwordEncoder.encode("1234")).
                        roles("USR").build()

        )    ;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.formLogin();
        httpSecurity.authorizeHttpRequests().requestMatchers("/user/**").hasRole("USR");
//httpSecurity.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN");
        httpSecurity.authorizeHttpRequests()
                .requestMatchers("/admin/**").hasRole("ADMIN");
        httpSecurity.authorizeHttpRequests()
                .requestMatchers("/index/**").hasAnyRole("ADMIN","USR");

        httpSecurity.authorizeHttpRequests().anyRequest().authenticated();
        //ila bghit ntconncta m3a chi ressource ma3endich lih le droit
        return httpSecurity.build();
    }


}