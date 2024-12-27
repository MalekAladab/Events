package com.example.demo.security;

import com.example.demo.repository.UserRepository;
import com.example.demo.model.User;  // Assurez-vous que vous avez une entité User
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepository.findByUsername(username);  
            if (user == null) {
                throw new UsernameNotFoundException("Utilisateur non trouvé");
            }

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())  
                    .roles(user.getRole()) 
                    .build();
        };
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/home","/webjars/**","/uploads/**").permitAll()
                .anyRequest().authenticated() 
            )
            .formLogin(form -> form
                    .loginPage("/login")
                    .permitAll() 
                    .defaultSuccessUrl("/list", true) 
              
                    )
                    .logout(logout -> logout
                    .logoutUrl("/logout")           
                    .logoutSuccessUrl("/")      
                    .invalidateHttpSession(true)    
                    .clearAuthentication(true)     
                    .permitAll()
                );
        return http.build();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder encoder, UserDetailsService uds) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(uds);
        authenticationProvider.setPasswordEncoder(encoder);
        return authenticationProvider;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///C:/Users/USER/Desktop/events/uploads/");
    }
    
  
}

