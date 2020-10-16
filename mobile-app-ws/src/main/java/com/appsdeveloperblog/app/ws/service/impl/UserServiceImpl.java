package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.exception.UserServiceException;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.shared.dto.Utils;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	@Autowired
	UserRepository repo;
	@Autowired
	Utils utils;
	@Autowired
	BCryptPasswordEncoder pe;

	/*
	 * @Override public UserDto createUser(UserDto dto) { UserEntity checkemail =
	 * repo.findByEmail(dto.getEmail()); if (checkemail != null) throw new
	 * RuntimeException("User Already Exists");
	 * 
	 * 
	 * for(int i=0;i<dto.getAddresses().size();i++) { AddressDto address =
	 * dto.getAddresses().get(i); address.setUserDetails(dto);
	 * address.setAddressId(utils.randomString(30)); dto.getAddresses().set(i,
	 * address); }
	 * 
	 * 
	 * String randomString = utils.randomString(10); // UserEntity ue = new
	 * UserEntity(); // UserDto dtoObj = new UserDto();
	 * 
	 * ModelMapper model = new ModelMapper(); UserEntity ue = model.map(dto,
	 * UserEntity.class);
	 * 
	 * 
	 * // BeanUtils.copyProperties(dto, ue);
	 * ue.setEncryptedPassword(pe.encode(dto.getPassword()));
	 * ue.setUserId(randomString); UserEntity savedProp = repo.save(ue); //
	 * BeanUtils.copyProperties(savedProp, dtoObj); UserDto dtoObj =
	 * model.map(savedProp, UserDto.class); return dtoObj; }
	 */

	@Override
	public UserDto createUser(UserDto user) {

		if (repo.findByEmail(user.getEmail()) != null)
			throw new UserServiceException("Record already exists");

		for (int i = 0; i < user.getAddresses().size(); i++) {
			AddressDto address = user.getAddresses().get(i);
			address.setUserDetails(user);
			address.setAddressId(utils.randomString(30));
			// user.getAddresses().set(i,address);
		}

		// BeanUtils.copyProperties(user, userEntity);
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(user, UserEntity.class);

		String publicUserId = utils.randomString(30);
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword(pe.encode(user.getPassword()));
		// userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));

		UserEntity storedUserDetails = repo.save(userEntity);

		// BeanUtils.copyProperties(storedUserDetails, returnValue);
		UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);

		// Send an email message to user to verify their email address
		// amazonSES.verifyEmail(returnValue);

		return returnValue;
	}

	@Override // UserDetailsService inherited abstract method
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = repo.findByEmail(email);
		if (userEntity == null)
			throw new UsernameNotFoundException(email);
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());

	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = repo.findByEmail(email);
		if (userEntity == null)
			throw new UsernameNotFoundException(email);
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
	}

	@Override
	public UserDto getUserByUserId(String id) {
		UserEntity userEntity = repo.findByUserId(id);
		if (userEntity == null)
			throw new UsernameNotFoundException("User with Id " + id + " not found !!");
		UserDto dtoObj = new UserDto();
		BeanUtils.copyProperties(userEntity, dtoObj);
		return dtoObj;
	}

	@Override
	public UserDto updateUser(String id, UserDto userDto) {
		UserEntity userEntity = repo.findByUserId(id);
		UserDto dtoObj = new UserDto();
		if (userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());
		UserEntity updatedUserEntity = repo.save(userEntity);
		BeanUtils.copyProperties(updatedUserEntity, dtoObj);
		return dtoObj;
	}

	@Override
	public void deleteUser(String id) {
		UserEntity userEntity = repo.findByUserId(id);
		if (userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		repo.delete(userEntity);

	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> returnDto = new ArrayList<>();
		if (page > 0)
			page = page - 1;
		Pageable pageable = PageRequest.of(page, limit);

		Page<UserEntity> pageUserEntity = repo.findAll(pageable);
		List<UserEntity> userEntityList = pageUserEntity.getContent();

		for (UserEntity userEntity : userEntityList) {
			UserDto dto = new UserDto();
			BeanUtils.copyProperties(userEntity, dto);
			returnDto.add(dto);
		}

		return returnDto;
	}

}
