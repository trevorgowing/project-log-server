package com.trevorgowing.projectlog.security;

import com.trevorgowing.projectlog.user.User;
import com.trevorgowing.projectlog.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
class UserDetailsServiceSupport implements UserDetailsService {

	private final UserRepository userRepository;

	UserDetailsServiceSupport(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = Optional.ofNullable(userRepository.findByEmail(email))
				.orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));

		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				new ArrayList<>());
	}
}
