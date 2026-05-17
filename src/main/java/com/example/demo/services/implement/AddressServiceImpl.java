package com.example.demo.services.implement;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Address;
import com.example.demo.entities.User;
import com.example.demo.models.address.AddressResponseDTO;
import com.example.demo.models.address.AddressSaveDTO;
import com.example.demo.repositories.AddressRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.AddressService;

@Service("addressService")
public class AddressServiceImpl implements AddressService {

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    @Qualifier("addressRepository")
    private AddressRepository addressRepository;

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Override
    public List<AddressResponseDTO> getAllAddressesByUser(Long userId) {
        List<AddressResponseDTO> addresses = new ArrayList<>();
        for (Address address : addressRepository.findByUserId(userId)) {
            addresses.add(toResponseDTO(address));
        }
        return addresses;
    }

    @Override
    public AddressResponseDTO createAddress(AddressSaveDTO addressSaveDTO) {
        User user = userRepository.findById(addressSaveDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist to create address"));

        Address address = new Address();
        address.setStreet(addressSaveDTO.getStreet());
        address.setCity(addressSaveDTO.getCity());
        address.setCountry(addressSaveDTO.getCountry());
        address.setZipCode(addressSaveDTO.getZipCode());
        address.setDescription(addressSaveDTO.getDescription());
        address.setUser(user);
        address.setPrimary(addressSaveDTO.isPrimary());
        addressRepository.save(address);

        return toResponseDTO(address);
    }

    @Override
    public AddressResponseDTO updateAddress(Long id, AddressSaveDTO addressSaveDTO) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Address does not exist to update"));

        User user = userRepository.findById(addressSaveDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User does not exist to assign address"));

        address.setStreet(addressSaveDTO.getStreet());
        address.setCity(addressSaveDTO.getCity());
        address.setCountry(addressSaveDTO.getCountry());
        address.setZipCode(addressSaveDTO.getZipCode());
        address.setDescription(addressSaveDTO.getDescription());
        address.setPrimary(addressSaveDTO.isPrimary());
        address.setUser(user);

        addressRepository.save(address);

        return toResponseDTO(address);
    }

    @Override
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Address does not exist to delete"));

        addressRepository.delete(address);
    }

    private AddressResponseDTO toResponseDTO(Address address) {
        AddressResponseDTO response = modelMapper.map(address, AddressResponseDTO.class);
        response.setUserId(address.getUser().getId());
        return response;
    }
}
