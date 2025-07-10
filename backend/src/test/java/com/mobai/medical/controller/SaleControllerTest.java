package com.mobai.medical.controller;

import com.github.pagehelper.PageInfo;
import com.mobai.medical.domain.Sale;
import com.mobai.medical.service.SaleService;
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

public class SaleControllerTest {

    @InjectMocks
    private SaleController saleController;

    @Mock
    private SaleService saleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 测试 getSaleWithPage 成功情况（name = null）
    @Test
    void testGetSaleWithPage_WithNameNull() {
        PageInfo<Sale> pageInfo = new PageInfo<>();
        pageInfo.setList(Collections.singletonList(new Sale()));
        when(saleService.getSaleWithPage(1, 10, null)).thenReturn(pageInfo);

        Msg result = saleController.getSaleWithPage(1, 10, null);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(pageInfo, result.getData()); // 使用 getData()
    }

    // 测试 getSaleWithPage 成功情况（name != null）
    @Test
    void testGetSaleWithPage_WithNameNotNull() {
        PageInfo<Sale> pageInfo = new PageInfo<>();
        pageInfo.setList(Collections.singletonList(new Sale()));
        when(saleService.getSaleWithPage(2, 5, "同仁堂")).thenReturn(pageInfo);

        Msg result = saleController.getSaleWithPage(2, 5, "同仁堂");
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(pageInfo, result.getData());
    }

    // 测试 getSaleWithPage 返回 null 的情况
    @Test
    void testGetSaleWithPage_ReturnsNull() {
        when(saleService.getSaleWithPage(1, 10, null)).thenReturn(null);

        Msg result = saleController.getSaleWithPage(1, 10, null);
        assertNotNull(result);
        assertFalse(result.isSuccess());
    }

    // 测试无路径变量时调用 getSaleWithPage 默认参数
    @Test
    void testGetMappingWithoutPathVariables_InvokeDefault() {
        PageInfo<Sale> pageInfo = new PageInfo<>();
        pageInfo.setList(Collections.singletonList(new Sale()));
        when(saleService.getSaleWithPage(null, null, null)).thenReturn(pageInfo);

        Msg result = saleController.getSaleWithPage(null, null, null);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(pageInfo, result.getData());
    }

    // 测试 getSaleById 成功情况
    @Test
    void testGetSaleById_Success() {
        Sale sale = new Sale();
        sale.setSaleId(1L);
        sale.setSaleName("同仁堂药店");
        sale.setSalePhone("13800000000");

        Msg successMsg = Msg.success().data("sale", sale);
        when(saleService.getSaleById(1)).thenReturn(successMsg);

        Msg result = saleController.getSaleById(1);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.get("sale"));
        assertEquals("同仁堂药店", ((Sale) result.get("sale")).getSaleName());
    }

    // 测试 getSaleById 失败情况
    @Test
    void testGetSaleById_Fail() {
        Msg failMsg = Msg.fail().mess("没有找到");
        when(saleService.getSaleById(999)).thenReturn(failMsg);

        Msg result = saleController.getSaleById(999);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("没有找到", result.getMessage());
    }

    // 测试 saveSale 成功情况（信息完整）
    @Test
    void testSaveSale_Success() {
        Sale sale = new Sale();
        sale.setSaleName("同仁堂");
        sale.setSalePhone("13800000000");
        sale.setCreatetime(new Date());
        sale.setUpdatetime(new Date());

        when(saleService.saveSale(sale)).thenReturn(Msg.success().mess("添加成功"));

        Msg result = saleController.saveSale(sale);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("添加成功", result.getMessage());
    }

    // 测试 saveSale 失败 - 信息不完整
    @Test
    void testSaveSale_MissingFields() {
        Sale sale = new Sale();
        sale.setSaleName(""); // 空值触发校验失败

        Msg result = saleController.saveSale(sale);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("填写信息不完整", result.getMessage());
    }

    // 测试 updateSaleById 成功情况
    @Test
    void testUpdateSaleById_Success() {
        Sale sale = new Sale();
        sale.setSaleId(1L);
        sale.setSaleName("更新后的药店");
        sale.setSalePhone("13800000000");
        sale.setUpdatetime(new Date());

        when(saleService.updateSaleById(1L, sale)).thenReturn(Msg.success().mess("修改成功"));

        Msg result = saleController.updateSaleById(1L, sale);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("修改成功", result.getMessage());
    }

    // 测试 updateSaleById 名称为空
    @Test
    void testUpdateSaleById_NameEmpty() {
        Sale sale = new Sale();
        sale.setSaleId(1L);
        sale.setSalePhone("13800000000");

        Msg result = saleController.updateSaleById(1L, sale);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("药店名称不能为空", result.getMessage());
    }

    // 测试 updateSaleById 电话为空
    @Test
    void testUpdateSaleById_PhoneEmpty() {
        Sale sale = new Sale();
        sale.setSaleId(1L);
        sale.setSaleName("同仁堂");

        Msg result = saleController.updateSaleById(1L, sale);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("药店电话不能为空", result.getMessage());
    }

    // 测试 deleteSaleById 成功情况
    @Test
    void testDeleteSaleById_Success() {
        when(saleService.deleteSaleById(1)).thenReturn(Msg.success().mess("删除成功"));

        Msg result = saleController.deleteSaleById(1);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("删除成功", result.getMessage());
    }

    // 测试 deleteSaleById 失败情况
    @Test
    void testDeleteSaleById_Fail() {
        when(saleService.deleteSaleById(999)).thenReturn(Msg.fail().mess("删除失败"));

        Msg result = saleController.deleteSaleById(999);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("删除失败", result.getMessage());
    }
}
