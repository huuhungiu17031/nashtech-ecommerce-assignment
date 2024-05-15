package com.nashtech.cellphonesfake.provider;

import com.nashtech.cellphonesfake.exception.NotFoundException;
import com.nashtech.cellphonesfake.model.User;
import com.nashtech.cellphonesfake.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CustomAuthedProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    public CustomAuthedProvider(@Lazy PasswordEncoder passwordEncoder,
                                @Lazy UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userService.findByEmail(email);
        if (passwordEncoder.matches(password, user.getPassword())) {
            List<GrantedAuthority> roles = new ArrayList<>();
            user.getListRole().forEach(role -> {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName());
                roles.add(authority);
            });
            return new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    roles);
        }
        throw new NotFoundException("Wrong email or password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean valid = authentication.equals(UsernamePasswordAuthenticationToken.class);
        if (valid)
            return true;
        throw new UnsupportedOperationException("Unimplemented method 'supports'");
    }
}
