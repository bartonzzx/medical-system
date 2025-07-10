package com.mobai.medical.controller;

import com.mobai.medical.domain.CompanyPolicy;
import com.mobai.medical.param.CompanyPolicyParam;
import com.mobai.medical.service.CompanyPolicyService;
import com.mobai.medical.utils.Msg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CompanyPolicyControllerTest {

    @InjectMocks
    private CompanyPolicyController companyPolicyController;

    @Mock
    private CompanyPolicyService companyPolicyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 测试 getPolicyWithPage 成功情况
    @Test
    void testGetPolicyWithPage_Success() {
        CompanyPolicyParam param = new CompanyPolicyParam();
        param.setPn(1);
        param.setSize(10);

        Msg mockMsg = Msg.success().data("policyInfo", new Object());
        when(companyPolicyService.getAllPolicyWithPage(param)).thenReturn(mockMsg);

        Msg result = companyPolicyController.getPolicyWithPage(param);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("响应成功", result.getMessage());
        assertNotNull(result.get("policyInfo"));
    }

    // 测试 saveMedicalPolicy 成功情况（参数合法）
    @Test
    void testSaveMedicalPolicy_Success() {
        CompanyPolicyParam param = new CompanyPolicyParam();
        param.setTitle("新政策");
        param.setMessage("这是新的医药公司政策内容");
        param.setCompanyId(1L);
        param.setCreateTime(new Date());
        param.setUpdateTime(new Date());

        when(companyPolicyService.savePolicy(param)).thenReturn(Msg.success().mess("添加成功"));

        Msg result = companyPolicyController.saveMedicalPolicy(param);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("添加成功", result.getMessage());
    }

    // 测试 saveMedicalPolicy 标题为空
    @Test
    void testSaveMedicalPolicy_TitleEmpty() {
        CompanyPolicyParam param = new CompanyPolicyParam();
        param.setMessage("内容不能为空");
        param.setCompanyId(1L);

        Msg result = companyPolicyController.saveMedicalPolicy(param);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("标题不能为空", result.getMessage());
    }

    // 测试 saveMedicalPolicy 内容为空
    @Test
    void testSaveMedicalPolicy_MessageEmpty() {
        CompanyPolicyParam param = new CompanyPolicyParam();
        param.setTitle("标题不能为空");
        param.setCompanyId(1L);

        Msg result = companyPolicyController.saveMedicalPolicy(param);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("内容不能为空", result.getMessage());
    }

    // 测试 saveMedicalPolicy 公司ID为空
    @Test
    void testSaveMedicalPolicy_CompanyIdEmpty() {
        CompanyPolicyParam param = new CompanyPolicyParam();
        param.setTitle("政策标题");

        Msg result = companyPolicyController.saveMedicalPolicy(param);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("公司ID不能为空", result.getMessage());
    }

    // 测试 updateMedicalPolicy 成功情况
    @Test
    void testUpdateMedicalPolicy_Success() {
        CompanyPolicyParam param = new CompanyPolicyParam();
        param.setTitle("更新后的标题");
        param.setMessage("更新后的内容");
        param.setCompanyId(1L);

        when(companyPolicyService.updatePolicy(1L, param)).thenReturn(Msg.success().mess("修改成功"));

        Msg result = companyPolicyController.updateMedicalPolicy(1L, param);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("修改成功", result.getMessage());
    }

    // 测试 updateMedicalPolicy 标题为空
    @Test
    void testUpdateMedicalPolicy_TitleEmpty() {
        CompanyPolicyParam param = new CompanyPolicyParam();
        param.setMessage("内容不为空");
        param.setCompanyId(1L);

        Msg result = companyPolicyController.updateMedicalPolicy(1L, param);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("标题不能为空", result.getMessage());
    }

    // 测试 updateMedicalPolicy 内容为空
    @Test
    void testUpdateMedicalPolicy_MessageEmpty() {
        CompanyPolicyParam param = new CompanyPolicyParam();
        param.setTitle("标题不为空");
        param.setCompanyId(1L);

        Msg result = companyPolicyController.updateMedicalPolicy(1L, param);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("内容不能为空", result.getMessage());
    }

    // 测试 updateMedicalPolicy 公司ID为空
    @Test
    void testUpdateMedicalPolicy_CompanyIdEmpty() {
        CompanyPolicyParam param = new CompanyPolicyParam();
        param.setTitle("标题不为空");
        param.setMessage("内容不为空");

        Msg result = companyPolicyController.updateMedicalPolicy(1L, param);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("公司ID不能为空", result.getMessage());
    }

    // 测试 deletePolicy 成功情况
    @Test
    void testDeletePolicy_Success() {
        when(companyPolicyService.deletePolicy(1L)).thenReturn(Msg.success().mess("删除成功"));

        Msg result = companyPolicyController.deletePolicy(1L);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("删除成功", result.getMessage());
    }

    // 测试 deletePolicy 失败情况
    @Test
    void testDeletePolicy_Fail() {
        when(companyPolicyService.deletePolicy(999L)).thenReturn(Msg.fail().mess("删除失败"));

        Msg result = companyPolicyController.deletePolicy(999L);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("删除失败", result.getMessage());
    }
}
