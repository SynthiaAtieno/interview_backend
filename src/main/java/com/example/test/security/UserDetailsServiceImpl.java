package com.example.test.security;

import com.example.test.entities.Users;
import com.example.test.service.user.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Users> appUser = userService.findByEmail(username);
        if (appUser.isPresent()) {
            Users user = appUser.get();
            var list = new ArrayList<String>();
            list.add(user.getRole().name());

            List<SimpleGrantedAuthority> grantedAuthorities = list.stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(appUser.get().getEmail(), appUser.get().getPassword(), grantedAuthorities);

        } else {
            throw new UsernameNotFoundException("Invalid user");
        }

    }

}
