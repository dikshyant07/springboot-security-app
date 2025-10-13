package com.authentication.system.security_project.mappers;

import com.authentication.system.security_project.dtos.AdminsUpdateResponseDto;
import com.authentication.system.security_project.dtos.SignupDto;
import com.authentication.system.security_project.dtos.SignupResponseDto;
import com.authentication.system.security_project.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toUser(SignupDto signupDto);

    @Mapping(source = "id", target = "userId")
    SignupResponseDto toDto(User user);

    AdminsUpdateResponseDto toAdminUpdateDto(User user);

    List<SignupResponseDto> toList(List<User> users);

}
