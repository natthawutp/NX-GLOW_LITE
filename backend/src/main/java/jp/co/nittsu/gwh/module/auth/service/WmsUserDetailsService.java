package jp.co.nittsu.gwh.module.auth.service;

import jp.co.nittsu.gwh.module.auth.entity.WmsUser;
import jp.co.nittsu.gwh.module.auth.repository.WmsUserRepository;
import jp.co.nittsu.gwh.security.WmsUserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class WmsUserDetailsService implements UserDetailsService {

    @Autowired
    private WmsUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        WmsUser user = userRepository.findByEmailAndDelFlg(email, 0)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return new WmsUserPrincipal(
                user.getUserId(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getDisplayName(),
                user.getIsActive(),
                Arrays.asList(user.getUserRole().split(","))
        );
    }
}
