package ru.alfabank.dmpr.repository.cards;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.BaseDataContextTests;

import static org.junit.Assert.*;

public class CommonFilterRepositoryTests extends BaseDataContextTests {
    @Autowired
    private CardsCommonFilterRepository repository;

    @Test
    public void testGetMacroRegions() throws Exception {
        assertNotEquals(0, repository.getMacroRegions().length);
    }

    @Test
    public void testGetRegions() throws Exception {
        assertNotEquals(0, repository.getRegions().length);
    }

    @Test
    public void testGetBranchTypes() throws Exception {
        assertNotEquals(0, repository.getBranchTypes().length);
    }

    @Test
    public void testGetCardCategories() throws Exception {
        assertNotEquals(0, repository.getCardCategories().length);
    }

    @Test
    public void testGetDebitOrCredits() throws Exception {
        assertNotEquals(0, repository.getDebitOrCredits().length);
    }

    @Test
    public void testGetClientSegments() throws Exception {
        assertNotEquals(0, repository.getClientSegments().length);
    }

    @Test
    public void testGetSystemUnits() throws Exception {
        assertNotEquals(0, repository.getSystemUnits().length);
    }

    @Test
    public void testGetCities() throws Exception {
        assertNotEquals(0, repository.getCities().length);
    }

    @Test
    public void testGetMvkSigns() throws Exception {
        assertNotEquals(0, repository.getMvkSigns().length);
    }

    @Test
    public void testGetReissueSpeeds() throws Exception {
        assertNotEquals(0, repository.getReissueSpeeds().length);
    }


    @Test
    public void testGetMacroRegionById() throws Exception {
        BaseEntity unit = repository.getMacroRegions()[0];
        assertEquals(unit.name, repository.getMacroRegionById(unit.id).name);
    }

    @Test
    public void testGetReissueSpeedById() throws Exception {
        BaseEntity unit = repository.getReissueSpeeds()[0];
        assertEquals(unit.name, repository.getReissueSpeedById(unit.id).name);
    }

    @Test
    public void testGetRegionById() throws Exception {
        ChildEntity region = repository.getRegions()[0];
        assertEquals(region.name, repository.getRegionById(region.id).name);
    }

    @Test
    public void testGetSystemUnit() throws Exception {
        BaseEntity unit = repository.getSystemUnits()[0];
        assertEquals(unit.name, repository.getSystemUnitById(unit.id).name);
    }
}