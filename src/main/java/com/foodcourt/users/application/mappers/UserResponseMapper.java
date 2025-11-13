package com.foodcourt.users.application.mappers;

import com.foodcourt.users.application.dto.response.UserResponse;
import com.foodcourt.users.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserResponseMapper {
	
	UserResponseMapper INSTANCE = Mappers.getMapper(UserResponseMapper.class);
	
	@Mapping(target = "phone", source = "phoneNumber")
	UserResponse toResponse(User user);
	
}
