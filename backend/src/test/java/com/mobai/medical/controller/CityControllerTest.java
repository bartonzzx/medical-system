package com.mobai.medical.controller;

import com.github.pagehelper.PageInfo;
import com.mobai.medical.domain.City;
import com.mobai.medical.model.CityModel;
import com.mobai.medical.service.CityService;
import com.mobai.medical.utils.Msg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CityControllerTest {

    @InjectMocks
    private CityController cityController;

    @Mock
    private CityService cityService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 测试 getCityWithPage 成功情况（name = null）
    @Test
    void testGetCityWithPage_WithNameNull() {
        PageInfo<CityModel> pageInfo = new PageInfo<>();
        pageInfo.setList(Collections.singletonList(new CityModel()));
        when(cityService.getCityWithPage(1, 10, null)).thenReturn(pageInfo);

        Msg result = cityController.getCityWithPage(1, 10, null);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(pageInfo, result.getData()); // 使用 getData()
    }

    // 测试 getCityWithPage 成功情况（name != null）
    @Test
    void testGetCityWithPage_WithNameNotNull() {
        PageInfo<CityModel> pageInfo = new PageInfo<>();
        pageInfo.setList(Collections.singletonList(new CityModel()));
        when(cityService.getCityWithPage(2, 5, "北京")).thenReturn(pageInfo);

        Msg result = cityController.getCityWithPage(2, 5, "北京");
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(pageInfo, result.getData());
    }

    // 测试 getCityWithPage 返回 null 的情况
    @Test
    void testGetCityWithPage_ReturnsNull() {
        when(cityService.getCityWithPage(1, 10, null)).thenReturn(null);

        Msg result = cityController.getCityWithPage(1, 10, null);
        assertNotNull(result);
        assertFalse(result.isSuccess());
    }

    // 测试 getCityDefault 调用 getCityWithPage 默认参数
    @Test
    void testGetCityDefault_InvokeCorrectly() {
        PageInfo<CityModel> pageInfo = new PageInfo<>();
        pageInfo.setList(Collections.singletonList(new CityModel()));
        when(cityService.getCityWithPage(null, null, null)).thenReturn(pageInfo);

        Msg result = cityController.getCityDefault(null);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(pageInfo, result.getData());
    }

    // 测试 getCityDefault 带 name 参数
    @Test
    void testGetCityDefault_WithName() {
        PageInfo<CityModel> pageInfo = new PageInfo<>();
        pageInfo.setList(Collections.singletonList(new CityModel()));
        when(cityService.getCityWithPage(null, null, "上海")).thenReturn(pageInfo);

        Msg result = cityController.getCityDefault("上海");
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(pageInfo, result.getData());
    }

    // 测试 getCityById 成功情况
    @Test
    void testGetCityById_Success() {
        Msg successMsg = Msg.success().data("city", new City());
        when(cityService.getCityById(1)).thenReturn(successMsg);

        Msg result = cityController.getCityById(1);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertNotNull(result.get("city"));
    }

    // 测试 getCityById 失败情况
    @Test
    void testGetCityById_Fail() {
        Msg failMsg = Msg.fail().mess("没有找到");
        when(cityService.getCityById(999)).thenReturn(failMsg);

        Msg result = cityController.getCityById(999);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("没有找到", result.getMessage());
    }

    // 测试 saveCity 成功情况（城市编号唯一）
    @Test
    void testSaveCity_Success() {
        when(cityService.checkCityByName(110100)).thenReturn(0); // 不存在该城市
        when(cityService.saveCity(110100)).thenReturn(Msg.success().mess("添加成功"));

        Msg result = cityController.saveCity(110100);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("添加成功", result.getMessage());
    }

    // 测试 saveCity 失败 - 城市已存在
    @Test
    void testSaveCity_CityAlreadyExists() {
        when(cityService.checkCityByName(110100)).thenReturn(1); // 已存在

        Msg result = cityController.saveCity(110100);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("城市已经存在", result.getMessage());
        assertEquals(10004, result.getCode());
    }

    // 测试 deleteCityById 成功情况
    @Test
    void testDeleteCityById_Success() {
        when(cityService.deleteCityById(1)).thenReturn(Msg.success().mess("删除成功"));

        Msg result = cityController.deleteCityById(1);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals("删除成功", result.getMessage());
    }

    // 测试 deleteCityById 失败情况
    @Test
    void testDeleteCityById_Fail() {
        when(cityService.deleteCityById(999)).thenReturn(Msg.fail().mess("删除失败"));

        Msg result = cityController.deleteCityById(999);
        assertNotNull(result);
        assertFalse(result.isSuccess());
        assertEquals("删除失败", result.getMessage());
    }
}
