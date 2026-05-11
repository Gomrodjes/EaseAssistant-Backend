package com.example.demo.services.implement;

import com.example.demo.entities.User;
import com.example.demo.models.users.UserResponseDTO;
import com.example.demo.models.users.UserUpdateDTO;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<UserResponseDTO> users = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            users.add(modelMapper.map(user, UserResponseDTO.class));
        }
        return users;
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {
        if (!userRepository.findById(id).isPresent()) {
            throw new IllegalArgumentException("The user not exist to update");
        }

        if (userRepository.existsByEmailAndIdNot(userUpdateDTO.getEmail(), id)) {
            throw new IllegalArgumentException("Email already in use");
        }

        if (userRepository.existsByPhoneNumberAndIdNot(userUpdateDTO.getPhoneNumber(), id)) {
            throw new IllegalArgumentException("Phone number already in use");
        }

        User user = userRepository.findById(id).get();

        user.setEmail(userUpdateDTO.getEmail());
        user.setFullName(userUpdateDTO.getFullName());
        user.setDateOfBirth(userUpdateDTO.getDateOfBirth());
        user.setGender(userUpdateDTO.getGender());
        user.setNationality(userUpdateDTO.getNationality());
        user.setBiography(userUpdateDTO.getBiography());
        user.setPhoneNumber(userUpdateDTO.getPhoneNumber());

        userRepository.save(user);

        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO updateUserActiveStatus(Long id, boolean isActive) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("The user not exist to update"));

        user.setActive(isActive);
        userRepository.save(user);

        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("The user not exist to delete"));

        userRepository.delete(user);
    }
}
