package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.address.AddressResponseDTO;
import com.example.demo.models.address.AddressSaveDTO;
import com.example.demo.models.response.ResponseApi;
import com.example.demo.services.AddressService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    @Qualifier("addressService")
    private AddressService addressService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getAddressByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(
                new ResponseApi<>(true, addressService.getAllAddressesByUser(userId),
                        "Address retrieved successfully"));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAddress(@RequestBody @Valid AddressSaveDTO addressSaveDTO) {
        try {
            AddressResponseDTO createdAddress = addressService.createAddress(addressSaveDTO);
            return ResponseEntity.ok(new ResponseApi<>(true, createdAddress, "Address created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @RequestBody @Valid AddressSaveDTO addressSaveDTO) {
        try {
            AddressResponseDTO updatedAddress = addressService.updateAddress(id, addressSaveDTO);
            return ResponseEntity.ok(new ResponseApi<>(true, updatedAddress, "Address updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        try {
            addressService.deleteAddress(id);
            return ResponseEntity.ok(new ResponseApi<>(true, null, "Address deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseApi<>(false, null, e.getMessage()));
        }
    }
}
