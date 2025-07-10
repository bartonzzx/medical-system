package com.mobai.medical.controller;

import com.github.pagehelper.PageInfo;
import com.mobai.medical.model.DrugModel;
import com.mobai.medical.param.DrugParam;
import com.mobai.medical.service.DrugService;
import com.mobai.medical.utils.Msg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DrugControllerTest {

    @InjectMocks
    private DrugController drugController;

    @Mock
    private DrugService drugService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 测试 getDrugWithPage 成功情况（name = null）
    @Test
    void testGetDrugWithPage_WithNameNull() {
        PageInfo<DrugModel> pageInfo = new PageInfo<>();
        pageInfo.setList(Collections.singletonList(new DrugModel()));
        when(drugService.getDrugWithPage(1, 10, null)).thenReturn(pageInfo);

        Msg result = drugController.getDrugWithPage(1, 10, null);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(pageInfo, result.getData()); // 使用 getData()
    }

    // 测试 getDrugWithPage 成功情况（name != null）
    @Test
    void testGetDrugWithPage_WithNameNotNull() {
        PageInfo<DrugModel> pageInfo = new PageInfo<>();
        pageInfo.setList(Collections.singletonList(new DrugModel()));
        when(drugService.getDrugWithPage(2, 5, "阿莫西林")).thenReturn(pageInfo);

        Msg result = drugController.getDrugWithPage(2, 5, "阿莫西林");
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(pageInfo, result.getData());
    }

    // 测试 getDrugWithPage 返回 null 的情况
    @Test
    void testGetDrugWithPage_ReturnsNull() {
        when(drugService.getDrugWithPage(1, 10, null)).thenReturn(null);

        Msg result = drugController.getDrugWithPage(1, 10, null);
        assertNotNull(result);
        assertFalse(result.isSuccess());
    }

    // 测试 saveDrug 成功情况
    @Test
    void testSaveDrug_Success() {
        DrugParam param = new DrugParam();
        param.setDrugName("头孢");
        param.setDrugInfo("抗生素");
        param.setDrugEffect("消炎杀菌");
        param.setDrugImg("http://example.com/cephalexin.jpg");
        param.setDrugPublisher("制药公司A");
        param.setSaleIds(new Long[]{1L, 2L});
        param.setCreatetime(new Date());
        param.setUpdatetime(new Date());

        when(drugService.saveDrug(param)).thenReturn(Msg.success().data("pages", 1).mess("添加成功"));

        Msg result = drugController.saveDrug(param);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("添加成功", result.getMessage());
    }

    // 测试 saveDrug 失败情况
    @Test
    void testSaveDrug_Fail() {
        DrugParam param = new DrugParam();
        param.setDrugName("头孢");

        when(drugService.saveDrug(param)).thenReturn(Msg.fail().mess("添加失败"));

        Msg result = drugController.saveDrug(param);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("添加失败", result.getMessage());
    }

    // 测试 updateDrug 成功情况
    @Test
    void testUpdateDrug_Success() {
        DrugParam param = new DrugParam();
        param.setDrugId(1L);
        param.setDrugName("更新后的药品");
        param.setUpdatetime(new Date());

        when(drugService.updateDrug(1L, param)).thenReturn(Msg.success().mess("修改成功"));

        Msg result = drugController.updateDrug(1L, param);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("修改成功", result.getMessage());
    }

    // 测试 updateDrug 失败情况
    @Test
    void testUpdateDrug_Fail() {
        DrugParam param = new DrugParam();
        param.setDrugId(999L); // 无效ID

        when(drugService.updateDrug(999L, param)).thenReturn(Msg.fail().mess("修改失败"));

        Msg result = drugController.updateDrug(999L, param);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("修改失败", result.getMessage());
    }

    // 测试 deleteDrug 成功情况
    @Test
    void testDeleteDrug_Success() {
        when(drugService.deleteDrug(1L)).thenReturn(Msg.success().mess("删除成功"));

        Msg result = drugController.deleteDrug(1L);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("删除成功", result.getMessage());
    }

    // 测试 deleteDrug 失败情况
    @Test
    void testDeleteDrug_Fail() {
        when(drugService.deleteDrug(999L)).thenReturn(Msg.fail().mess("删除失败"));

        Msg result = drugController.deleteDrug(999L);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("删除失败", result.getMessage());
    }
}
