package com.example.demo.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.Address;

@Repository("addressRepository")
public interface AddressRepository extends JpaRepository<Address, Serializable> {

}
