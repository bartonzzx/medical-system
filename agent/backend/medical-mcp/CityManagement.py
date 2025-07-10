from typing import Any
import httpx
import config
from main import mcp

@mcp.tool()
async def getAllCityInfo(token: str) -> Any:
    '''获取所有城市的信息。包括城市ID、城市邮编、所在省份和城市名称。

    Args:
        token (str): 用户的token
    '''
    headers = {
        'Authorization': token,
    }
    url = config.API_BASE_URL + '/api/citys'
    async with httpx.AsyncClient() as client:
        response = await client.get(url, headers=headers)
        if response.status_code == 200:
            response_json = response.json()
            if response_json['success'] == True:
                all_info = str()
                for item in response_json['data']['cityPageInfo']['list']:
                    item_city_info = f"城市ID: {item['cityId']}, 城市邮编: {item['cityNumber']}, 省份: {item['province']}, 城市:{item['city']}\n"
                    all_info += item_city_info
                return all_info if all_info else '没有找到相关城市信息。'
            else:
                return '获取城市信息失败：' + response_json.get('message', '未知错误')
        else:
            return 'API请求失败，状态码：' + str(response.status_code)
        
@mcp.tool()
async def getCityInfoById(token: str, cityId: int) -> Any:
    '''通过城市ID获取城市的信息。包括城市ID、城市邮编、信息创建时间和信息更新时间。

    Args:
        token (str): 用户的token
        cityId (int): 城市的ID
    '''
    headers = {
        'Authorization': token,
    }
    url = config.API_BASE_URL + f'/api/citys/{cityId}'
    async with httpx.AsyncClient() as client:
        response = await client.get(url, headers=headers)
        if response.status_code == 200:
            response_json = response.json()
            if response_json['success'] == True:
                item = response_json['data']['city']
                city_info = f"城市ID: {item['cityId']}, 城市邮编: {item['cityNumber']}, 信息创建时间: {item['createtime']}, 信息更新时间: {item['updatetime']}\n"
                return city_info
            else:
                return '获取城市信息失败：' + response_json.get('message', '未知错误')
        else:
            return 'API请求失败，状态码：' + str(response.status_code)
        
@mcp.tool()
async def deleteCity(token: str, cityId: int) -> Any:
    '''删除城市的信息。

    Args:
        token (str): 用户的token
        cityId (int): 城市的ID
    '''
    headers = {
        'Authorization': token,
    }
    url = config.API_BASE_URL + f'/api/citys/{cityId}'
    async with httpx.AsyncClient() as client:
        response = await client.delete(url, headers=headers)
        if response.status_code == 200:
            response_json = response.json()
            if response_json['success'] == True:
                return '城市信息删除成功。'
            else:
                return '删除城市信息失败：' + response_json.get('message', '未知错误')
        else:
            return 'API请求失败，状态码：' + str(response.status_code)