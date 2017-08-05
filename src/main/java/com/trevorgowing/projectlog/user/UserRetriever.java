package com.trevorgowing.projectlog.user;

import org.springframework.stereotype.Service;

import java.util.List;

import static com.trevorgowing.projectlog.user.UserNotFoundException.identifiedUserNotFoundException;
import static java.util.Optional.ofNullable;

@Service
public class UserRetriever {

	private final UserRepository userRepository;

	public UserRetriever(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User findUser(long userId) {
		return ofNullable(userRepository.findOne(userId))
				.orElseThrow(() -> identifiedUserNotFoundException(userId));
	}

	List<IdentifiedUserDTO> findIdentifiedUserDTOs() {
		return userRepository.findIdentifiedUserDTOs();
	}

	IdentifiedUserDTO findIdentifiedUserDTOById(long userId) {
		return ofNullable(userRepository.findIdentifiedUserDTOById(userId))
				.orElseThrow(() -> identifiedUserNotFoundException(userId));
	}
}