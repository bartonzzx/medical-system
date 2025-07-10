package com.mobai.medical.controller;

import com.github.pagehelper.PageInfo;
import com.mobai.medical.domain.DrugCompany;
import com.mobai.medical.service.CompanyService;
import com.mobai.medical.utils.Msg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompanyControllerTest {

    @InjectMocks
    private CompanyController companyController;

    @Mock
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 测试 getCompanyWithPage
    @Test
    void testGetCompanyWithPage_Success() {
        PageInfo<DrugCompany> pageInfo = new PageInfo<>();
        pageInfo.setList(Collections.singletonList(new DrugCompany()));
        when(companyService.getCompanyWithPage(1, 10, "test")).thenReturn(pageInfo);

        Msg response = companyController.getCompanyWithPage(1, 10, "test");
        assertNotNull(response); // 不需要 getBody()
        assertTrue(response.isSuccess());
    }


    @Test
    void testGetCompanyWithPage_Fail() {
        when(companyService.getCompanyWithPage(1, 10, null)).thenReturn(null);

        Msg response = companyController.getCompanyWithPage(1, 10, null);
        assertNotNull(response);
        assertFalse(response.isSuccess());
    }

    // 测试 getCompanyById
    @Test
    void testGetCompanyById_Success() {
        Msg successMsg = Msg.success();
        when(companyService.getCompanyById(1)).thenReturn(successMsg);

        Msg response = companyController.getCompanyById(1);
        assertEquals(successMsg, response);
    }

    @Test
    void testGetCompanyById_Fail() {
        Msg failMsg = Msg.fail();
        when(companyService.getCompanyById(999)).thenReturn(failMsg);

        Msg response = companyController.getCompanyById(999);
        assertEquals(failMsg, response);
    }

    // 测试 saveCompany
    @Test
    void testSaveCompany_MissingName() {
        DrugCompany company = new DrugCompany();
        company.setCompanyPhone("123456");

        Msg response = companyController.saveCompany(company);
        assertFalse(response.isSuccess());
        assertEquals("填写信息不完整", response.getMessage());
    }

    @Test
    void testSaveCompany_Success() {
        DrugCompany company = new DrugCompany();
        company.setCompanyName("TestCo");
        company.setCompanyPhone("123456");

        when(companyService.saveCompany(company)).thenReturn(Msg.success());

        Msg response = companyController.saveCompany(company);
        assertTrue(response.isSuccess());
    }

    // 测试 updateCompanyById
    @Test
    void testUpdateCompanyById_MissingName() {
        DrugCompany company = new DrugCompany();
        company.setCompanyPhone("123456");

        Msg response = companyController.updateCompanyById(1L, company);
        assertFalse(response.isSuccess());
        assertEquals("公司名称不能为空", response.getMessage());
    }

    @Test
    void testUpdateCompanyById_Success() {
        DrugCompany company = new DrugCompany();
        company.setCompanyName("UpdatedCo");
        company.setCompanyPhone("654321");

        when(companyService.updateCompanyById(1L, company)).thenReturn(Msg.success());

        Msg response = companyController.updateCompanyById(1L, company);
        assertTrue(response.isSuccess());
    }

    // 测试 deleteCompanyById
    @Test
    void testDeleteCompanyById_Success() {
        when(companyService.deleteCompanyById(1)).thenReturn(Msg.success());

        Msg response = companyController.deleteCompanyById(1);
        assertTrue(response.isSuccess());
    }

    @Test
    void testDeleteCompanyById_Fail() {
        when(companyService.deleteCompanyById(999)).thenReturn(Msg.fail());

        Msg response = companyController.deleteCompanyById(999);
        assertFalse(response.isSuccess());
    }
}
