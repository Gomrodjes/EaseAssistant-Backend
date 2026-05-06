package com.example.demo.services;

import java.util.List;

import com.example.demo.models.address.AddressResponseDTO;
import com.example.demo.models.address.AddressSaveDTO;

public interface AddressService {
    List<AddressResponseDTO> getAllAddressesByUser(Long userId);

    AddressResponseDTO createAddress(AddressSaveDTO addressSaveDTO);

    AddressResponseDTO updateAddress(Long id, AddressSaveDTO addressSaveDTO);

    void deleteAddress(Long id);
}
