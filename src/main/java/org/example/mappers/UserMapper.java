package org.example.mappers;

import org.example.dto.UserDTO;
import org.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface for converting {@link User} entity objects to {@link UserDTO} DTO objects and vice versa.
 */
@Mapper
public interface UserMapper {

    /**
     * Instance of the {@code UserMapper} created by MapStruct.
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Converts a {@link User} entity to a {@link UserDTO} DTO.
     *
     * @param user the {@link User} entity to convert
     * @return the corresponding {@link UserDTO} DTO
     */
    UserDTO toDTO(User user);

    /**
     * Converts a {@link UserDTO} DTO to a {@link User} entity.
     *
     * @param userDTO the {@link UserDTO} DTO to convert
     * @return the corresponding {@link User} entity
     */
    User toEntity(UserDTO userDTO);
}
