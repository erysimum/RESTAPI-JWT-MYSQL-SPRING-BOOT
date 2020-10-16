package com.appsdeveloperblog.app.ws.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.app.ws.exception.UserServiceException;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.AddressResponse;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;
import com.appsdeveloperblog.app.ws.ui.model.response.OperationName;
import com.appsdeveloperblog.app.ws.ui.model.response.OperationResult;
import com.appsdeveloperblog.app.ws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	UserService userService;
	@Autowired
	AddressService addressService;

	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public UserRest getUsers(@PathVariable String id) {
		UserRest userRest = new UserRest();
		UserDto userDto = userService.getUserByUserId(id);
		BeanUtils.copyProperties(userDto, userRest);

		return userRest;
	}

	@PostMapping()
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		if (userDetails.getFirstName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		// UserRest userRest = new UserRest();
		// UserDto userDto = new UserDto();
		// BeanUtils.copyProperties(userDetails, userDto);

		ModelMapper model = new ModelMapper();
		UserDto userDto = model.map(userDetails, UserDto.class);

		UserDto dtofrmService = userService.createUser(userDto);
		// BeanUtils.copyProperties(dtofrmService, userRest);
		UserRest userRest = model.map(dtofrmService, UserRest.class);
		return userRest;

	}

	@PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE })
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
		UserRest userRest = new UserRest();
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		UserDto updateUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updateUser, userRest);
		return userRest;

	}

	@DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public OperationStatusModel deleteUser(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(OperationName.DELETE.name());
		userService.deleteUser(id);

		returnValue.setOperationResult(OperationResult.SUCCESS.name());
		return returnValue;
	}

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<UserRest> getAllUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "3") int limit) {
		List<UserRest> returnValue = new ArrayList<>();
		List<UserDto> userDto = userService.getUsers(page, limit);
		// UserRest userRestModel = new UserRest();

		for (UserDto users : userDto) {
			UserRest userRestModel = new UserRest();
			BeanUtils.copyProperties(users, userRestModel);
			returnValue.add(userRestModel);
		}
		return returnValue;
	}

	// http://localhost:8080/users/xvgsh6d6dhdhdhdks/addresses
	@GetMapping(value = "/{id}/addresses", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public List<AddressResponse> getAddresses(@PathVariable String id) {
		List<AddressResponse> returnValue = new ArrayList<>();

		List<AddressDto> addressDto = addressService.getAddresses(id);

		// Converting AddressDto to AddressResponse through Generic's ModelMapper
		Type listType = new TypeToken<List<AddressResponse>>() {
		}.getType();
		returnValue = new ModelMapper().map(addressDto, listType);
		return returnValue;
	}

	@GetMapping(value = "/{id}/addresses/{addressId}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public AddressResponse getAddress(@PathVariable String addressId) {
		AddressResponse returnValue = null;

		AddressDto addressDto = addressService.getAddress(addressId);

		returnValue =  new ModelMapper().map(addressDto,AddressResponse.class);
		return returnValue;
	}

}
