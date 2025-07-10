from typing import Any
import httpx
import config
from main import mcp

@mcp.tool()
async def getAllMedicalPolicy(token: str) -> Any:
    '''获取所有医疗政策的信息。包括政策ID、政策名称、政策内容、适用城市、信息创建时间、信息更新时间。
    
    Args:
        token (str): 用户的token
    '''
    headers = {
        'Authorization': token,
    }
    params = {
        'pn': 1,
        'size': 10,
    }

    url = config.API_BASE_URL + '/api/medical_policys'
    all_info = str()
    async with httpx.AsyncClient() as client:
        isLastPage = False
        while not isLastPage:
            response = await client.get(url, headers=headers, params=params)
            if response.status_code == 200:
                response_json = response.json()
                if response_json['success'] == True:
                    isLastPage = response_json['data']['policyInfo']['isLastPage']
                    params['pn'] += 1
                    for item in response_json['data']['policyInfo']['list']:
                        item_policy_info = f"政策ID: {item['id']}, 政策名称: {item['title']}, 内容: {item['message']}, 适用城市ID: {item['cityModel']['cityId']}, 适用城市: {item['cityModel']['city']}, 信息创建时间: {item['createTime']}, 信息更新时间: {item.get('updateTime','自创建后暂未更新')}\n"
                        all_info += item_policy_info
                else:
                    return '获取医疗政策信息失败：' + response_json.get('message', '未知错误')
            else:
                return 'API请求失败，状态码：' + str(response.status_code)
    return all_info if all_info else '没有找到相关医疗政策信息。'

@mcp.tool()
async def addMedicalPolicy(token: str, title: str, message: str, cityId: int) -> Any:
    '''添加新的医疗政策。将会创建一个新的医疗政策条目。由用户提供政策名称、内容和适用城市ID。

    Args:
        token (str): 用户的token
        title (str): 政策名称
        message (str): 政策内容
        cityId (int): 适用城市ID
    '''
    headers = {
        'Authorization': token
    }
    data = {
        'title': title,
        'message': message,
        'cityId': cityId,
    }
    
    url = config.API_BASE_URL + '/api/medical_policys'
    async with httpx.AsyncClient() as client:
        response = await client.post(url, headers=headers, json=data)
        if response.status_code == 200:
            response_json = response.json()
            if response_json['success'] == True:
                return '医疗政策添加成功！'
            else:
                return '添加医疗政策失败：' + response_json.get('message', '未知错误')
        else:
            return 'API请求失败，状态码：' + str(response.status_code)
        
@mcp.tool()
async def updateMedicalPolicy(token: str, policyId: int, title: str, message: str, cityId: int) -> Any:
    '''更新医疗政策的信息。由用户提供政策ID、名称、内容和适用城市ID。
    
    Args:
        token (str): 用户的token
        policyId (int): 政策ID
        title (str): 政策名称
        message (str): 政策内容
        cityId (int): 适用城市ID
    '''
    headers = {
        'Authorization': token,
    }
    data = {
        'title': title,
        'message': message,
        'cityId': cityId,
    }
    
    url = config.API_BASE_URL + f'/api/medical_policys/{policyId}'
    async with httpx.AsyncClient() as client:
        response = await client.put(url, headers=headers, json=data)
        if response.status_code == 200:
            response_json = response.json()
            if response_json['success'] == True:
                return '医疗政策更新成功！'
            else:
                return '更新医疗政策失败：' + response_json.get('message', '未知错误')
        else:
            return 'API请求失败，状态码：' + str(response.status_code)
        
@mcp.tool()
async def deleteMedicalPolicy(token: str, policyId: int) -> Any:
    '''删除医疗政策的信息。由用户提供政策ID。
    
    Args:
        token (str): 用户的token
        policyId (int): 政策ID
    '''
    headers = {
        'Authorization': token,
    }
    
    url = config.API_BASE_URL + f'/api/medical_policys/{policyId}'
    async with httpx.AsyncClient() as client:
        response = await client.delete(url, headers=headers)
        if response.status_code == 200:
            response_json = response.json()
            if response_json['success'] == True:
                return '医疗政策删除成功！'
            else:
                return '删除医疗政策失败：' + response_json.get('message', '未知错误')
        else:
            return 'API请求失败，状态码：' + str(response.status_code)