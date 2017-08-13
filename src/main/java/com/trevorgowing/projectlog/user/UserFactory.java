package com.trevorgowing.projectlog.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.trevorgowing.projectlog.user.DuplicateEmailException.causedDuplicateEmailException;
import static com.trevorgowing.projectlog.user.User.unidentifiedUser;

@Service
public class UserFactory {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserFactory(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	User createUser(String email, String password, String firstName, String lastName) {
		try {
			return userRepository.save(unidentifiedUser(email, passwordEncoder.encode(password), firstName, lastName));
		} catch (DataIntegrityViolationException dataIntegrityViolationException) {
			throw causedDuplicateEmailException(email, dataIntegrityViolationException);
		}
	}
}
