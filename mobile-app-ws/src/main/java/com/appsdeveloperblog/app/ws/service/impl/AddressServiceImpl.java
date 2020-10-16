package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.io.entity.AddressEntity;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.AddressRepository;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
@Service
public class AddressServiceImpl implements AddressService {
	@Autowired
	UserRepository repo;
	
	@Autowired
	AddressRepository addressRepo;

	@Override
	public List<AddressDto> getAddresses(String userId) {
		List<AddressDto> returnValue = new ArrayList<>();
		ModelMapper model = new ModelMapper();
		UserEntity userEntity = repo.findByUserId(userId);

		List<AddressEntity> addressEntity = addressRepo.findAllByUserDetails(userEntity);
		
		for(AddressEntity ae : addressEntity) {
			returnValue.add(model.map(ae, AddressDto.class));
		}
		
		return returnValue;
	}

	@Override
	public AddressDto getAddress(String addressId) {
		AddressDto addressDto = null;
		AddressEntity addressEntity = addressRepo.findByAddressId(addressId);
		if(addressEntity!=null) {
			return new ModelMapper().map(addressEntity, AddressDto.class);
		}
		
		return addressDto;
	}

}
