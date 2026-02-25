package com.myproject.ms.auth.mapper;


import com.myproject.ms.auth.dto.AccountDto;
import com.myproject.ms.auth.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountDto toDto(Account account);
}
