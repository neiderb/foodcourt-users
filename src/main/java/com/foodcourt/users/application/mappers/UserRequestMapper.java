package com.foodcourt.users.application.mappers;

import com.foodcourt.users.application.dto.request.UserRequest;
import com.foodcourt.users.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserRequestMapper {
	
	UserRequestMapper INSTANCE = Mappers.getMapper(UserRequestMapper.class);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "birthdate", dateFormat = "yyyy-MM-dd")
	@Mapping(target = "role", ignore = true)
	User toDomain(UserRequest userRequest);
	
}
