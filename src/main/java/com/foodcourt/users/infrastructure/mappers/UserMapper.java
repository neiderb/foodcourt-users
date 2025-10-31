package com.foodcourt.users.infrastructure.mappers;

import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.infrastructure.adapters.UserData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
	
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
	
	@Mapping(target = "role", ignore = true)
	User toDomain(UserData userData);
	
	@Mapping(target = "role", ignore = true)
	UserData toData(User user);
	
}
