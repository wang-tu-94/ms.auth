package com.myproject.ms.auth.mapper;

import com.myproject.ms.auth.dto.ServiceAccountDto;
import com.myproject.ms.auth.model.ServiceAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceAccountMapper {
    ServiceAccountDto toDto(ServiceAccount serviceAccount);
}
