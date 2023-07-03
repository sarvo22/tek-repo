package com.tekfilo.sso.company;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompanyServiceTest {

    @InjectMocks
    CompanyService companyService;
    CompanyDto companyDto = new CompanyDto();
    CompanyEntity companyEntity = new CompanyEntity();
    CompanyEntity saveCompanyEntity = new CompanyEntity();
    @Mock
    private CompanyRepository companyRepository;

    @Before
    public void setUp() throws Exception {
        companyDto.setId(100001);

        companyDto.setCompanyName("Test Company");
        companyDto.setDisplayName("Test Company");
        companyDto.setBusinessType("Test");
        companyDto.setDefaultCurrency("USD");
        companyDto.setCountryId(1);
        companyDto.setState("TN");
        companyDto.setCity("Cbe");


        companyEntity.setId(companyDto.getId());
        companyEntity.setCompanyName("Test Company");
        saveCompanyEntity.setCompanyName("Test Company");
    }

    @Test
    public void findByIdFoundTest() {
        when(companyRepository.findById(companyDto.getId())).thenReturn(Optional.ofNullable(companyEntity));
        CompanyEntity actual = companyService.findById(companyDto.getId());
        Assert.assertEquals(companyEntity.getId(), actual.getId());

    }

    @Test
    public void findByIdNotFounTest() {
        when(companyRepository.findById(companyDto.getId())).thenReturn(Optional.ofNullable(new CompanyEntity()));
        CompanyEntity actual = companyService.findById(companyDto.getId());
        Assert.assertEquals(null, actual.getId());
    }

    @Test
    public void saveTest() throws Exception {

    }
}