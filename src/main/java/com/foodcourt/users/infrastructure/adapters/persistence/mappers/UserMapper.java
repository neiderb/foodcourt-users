package com.foodcourt.users.infrastructure.adapters.persistence.mappers;

import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.infrastructure.adapters.persistence.UserData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
	
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
	
	@Mapping(target = "role", ignore = true)
	@Mapping(target = "phoneNumber", source = "phone")
	User toDomain(UserData userData);
	
	@Mapping(target = "role", ignore = true)
	@Mapping(target = "phone", source = "phoneNumber")
	UserData toData(User user);
	
}
