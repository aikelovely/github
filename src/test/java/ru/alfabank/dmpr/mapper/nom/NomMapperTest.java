package ru.alfabank.dmpr.mapper.nom;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.alfabank.dmpr.BaseDataContextTests;
import ru.alfabank.dmpr.model.BaseEntity;

import static org.junit.Assert.assertNotEquals;

public class NomMapperTest extends BaseDataContextTests {
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private NomMapper mapper;
}