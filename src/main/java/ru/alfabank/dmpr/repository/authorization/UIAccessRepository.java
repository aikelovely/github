package ru.alfabank.dmpr.repository.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.mapper.authorization.UIAccessMapper;

@Repository
public class UIAccessRepository {
    @Autowired
    UIAccessMapper mapper;

    public String[] getUserRoles(String username) {
        return mapper.getUserRoles(username);
    }
}
