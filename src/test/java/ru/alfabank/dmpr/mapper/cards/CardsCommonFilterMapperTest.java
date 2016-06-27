package ru.alfabank.dmpr.mapper.cards;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.alfabank.dmpr.BaseDataContextTests;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.model.cards.Branch;

import static org.junit.Assert.*;

public class CardsCommonFilterMapperTest extends BaseDataContextTests {
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private CardsCommonFilterMapper mapper;

    @Test
    public void testGetMacroRegions() throws Exception {
        BaseEntity[] entities = mapper.getMacroRegions();
        assertNotEquals(0, entities.length);
    }

    @Test
    public void testGetRegions() throws Exception {
        ChildEntity[] entities = mapper.getRegions();
        assertNotEquals(0, entities.length);
    }

    @Test
    public void testGetCities() throws Exception {
        ChildEntity[] entities = mapper.getCities();
        assertNotEquals(0, entities.length);
    }

    @Test
    public void testGetBranches() throws Exception {
        Branch[] entities = mapper.getBranches();
        assertNotEquals(0, entities.length);
    }

    @Test
    public void testGetBranchTypes() throws Exception {
        BaseEntity[] entities = mapper.getBranchTypes();
        assertNotEquals(0, entities.length);
    }

    @Test
    public void testGetCardCategories() throws Exception {
        BaseEntity[] entities = mapper.getCardCategories();
        assertNotEquals(0, entities.length);
    }

    @Test
    public void testGetDebitOrCredits() throws Exception {
        BaseEntity[] entities = mapper.getDebitOrCredits();
        assertNotEquals(0, entities.length);
    }

    @Test
    public void testGetClientSegments() throws Exception {
        BaseEntity[] entities = mapper.getClientSegments();
        assertNotEquals(0, entities.length);
    }

    @Test
    public void testGetSystemUnits() throws Exception {
        BaseEntity[] entities = mapper.getSystemUnits();
        assertNotEquals(0, entities.length);
    }

    @Test
    public void testGetMvkSigns() throws Exception {
        BaseEntity[] entities = mapper.getMvkSigns();
        assertNotEquals(0, entities.length);
    }

    @Test
    public void testGetReissueSpeeds() throws Exception {
        BaseEntity[] entities = mapper.getReissueSpeeds();
        assertNotEquals(0, entities.length);
    }

    @Test
    public void testGetZPSigns() throws Exception {
        BaseEntity[] entities = mapper.getZPSigns();
        assertNotEquals(0, entities.length);
    }

    @Test
    public void testGetPYTypes() throws Exception {
        ChildEntity[] entities = mapper.getPYTypes();
        assertNotEquals(0, entities.length);
    }
}