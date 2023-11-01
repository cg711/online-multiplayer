package com.service.security;

import com.service.entity.Account;
import com.service.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsImplService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    /**
     *
     * @param username the username identifying the user whose data is required.
     * @return returns a UserDetails object containing information about an account such as a username, password,
     *         and authorities.
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username).get(0);
        ArrayList<GrantedAuthority> roleList = new ArrayList<>();
        roleList.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return account.getRole();
            }
        });
        if (account != null) {
            //if account exists
            return new User(account.getUsername(), account.getPassword(), List.of(roleList.get(0)));

        }
        return null;
    }
}

