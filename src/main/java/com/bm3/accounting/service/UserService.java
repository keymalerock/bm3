package com.bm3.accounting.service;

import com.bm3.accounting.dto.UserRequestDTO;
import com.bm3.accounting.dto.UserResponseDTO;
import com.bm3.accounting.entity.Rol;
import com.bm3.accounting.entity.User;
import com.bm3.accounting.mappers.UserMapper;
import com.bm3.accounting.repository.RolRepository;
import com.bm3.accounting.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RolRepository rolRepository;
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
       return userRepository.findAll().stream()
                .map(userMapper::userToUserResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.userToUserResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User user = userMapper.userRequestDTOToUser(userRequestDTO);

        user.setKey(passwordEncoder.encode(user.getKey()));
        user.setEnabled(true);
        user.setUsername(user.getUsername());
        user.setEmail(user.getEmail());
        Rol defaultRole = rolRepository.findByName("user")
                .orElseThrow(() -> new RuntimeException("Role 'USER' not found. Please ensure it exists in the database."));

        user.getRolesMtM().add(defaultRole);
        User savedUser = userRepository.save(user);
        return userMapper.userToUserResponseDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO updateUser (Long id, UserRequestDTO requestDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user not found with id: " + id));

        existingUser.setUsername(requestDTO.getUsername());
        existingUser.setEmail(requestDTO.getEmail());
        existingUser.setEnabled(requestDTO.isEnabled());

        // Solo actualizar la contraseña si se proporciona una nueva
        if (requestDTO.getKey() != null && !requestDTO.getKey().isEmpty()) {
            existingUser.setKey(passwordEncoder.encode(requestDTO.getKey()));
        }

        User updatedUser = userRepository.save(existingUser);
        return userMapper.userToUserResponseDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user not found with id: " + id));
        existingUser.setEnabled(false);
        userRepository.save(existingUser);
    }

}
