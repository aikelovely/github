package ru.alfabank.dmpr.mapper.authorization;

import org.apache.ibatis.annotations.Param;

import java.util.HashSet;

public interface UIAccessMapper {
    String[] getUserRoles(@Param("username") String username);
}
