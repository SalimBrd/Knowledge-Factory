package com.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.api.model.Address;
import com.api.repositories.AddressRepository;

@Service
public class AddressService implements IModelService<Address> {

	@Autowired
	private AddressRepository addressRepository;
	
	@Override
	public List<Address> getList(Integer page, Integer limit) {
		PageRequest pageable = PageRequest.of(page, limit);
		return addressRepository.GetListByPage(pageable);
	}

	@Override
	public Address getOneById(Integer id) {
		Optional<Address> address = addressRepository.findById(id);
		
		if (address.isEmpty())
			return null;
		
		return address.get();
	}
	
	public List<Address> getListForUser(Integer page, Integer limit, Integer userId) {
		PageRequest pageable = PageRequest.of(page, limit);
		return addressRepository.GetListByPageForUser(pageable, userId);
	}

	public Address getOneByIdForUser(Integer id, Integer userId) {
		Optional<Address> address = addressRepository.GetByIdForUser(id, userId);
		if (address.isEmpty())
			return null;
		
		return address.get();
	}

	@Override
	public Address create(Address entity) {
		addressRepository.save(entity);
		return entity;
	}

	@Override
	public Address update(Integer id, Address entity) {
		Optional<Address> address = addressRepository.findById(id);
		
		if (address.isEmpty())
			return null;
		
		Address addressFound = address.get();
		if(entity.getCity() != null)
			addressFound.setCity(entity.getCity());
		if(entity.getPostalCode() != null)
			addressFound.setPostalCode(entity.getPostalCode());
		if(entity.getCountry() != null)
			addressFound.setCountry(entity.getCountry());
		if(entity.getStreet() != null)
			addressFound.setStreet(entity.getStreet());
		
		addressRepository.save(addressFound);
		return addressFound;
	}

	@Override
	public boolean delete(Integer id) {
		Optional<Address> address = addressRepository.findById(id);
		
		if (address.isEmpty())
			return false;
		
		try {
			addressRepository.delete(address.get());
		}
		catch(Exception e) {
			return false;
		}
		
		return true;
	}

}
