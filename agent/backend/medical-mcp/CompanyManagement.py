from typing import Any
import httpx
import config
from main import mcp

@mcp.tool()
async def getCompantById(token: str, companyId: int) -> Any:
    '''根据公司ID获取公司的信息。

    Args:
        token (str): 用户的token
        companyId (int): 公司的ID, 可通过 getAllCompanyInfo 获取所有公司的ID, 
                        或者通过 getCompanyInfoByKeyword 由关键词搜索公司并获取公司ID
    '''
    headers = {
        'Authorization': token
    }
    url = config.API_BASE_URL + f'/api/companys/{companyId}'
    async with httpx.AsyncClient() as client:
        response = await client.get(url, headers=headers)
        if response.status_code == 200:
            response_json = response.json()
            if response_json['success'] == True:
                item = response_json['data']['company']
                return f"公司ID:{item['companyId']},公司名称:{item['companyName']},联系电话:{item['companyPhone']},信息创建时间:{item['createtime']},信息修改时间:{item['updatetime']}\n"
            else:
                return '获取公司信息失败：' + response_json.get('message', '未知错误')
        else:
            return 'API请求失败，状态码：' + str(response.status_code)
        
@mcp.tool()
async def getAllCompanyInfo(token: str) -> Any:
    '''获取所有公司的信息。

    Args:
        token (str): 用户的token
    '''
    headers = {
        'Authorization': token,
    }
    
    pn = 1
    size = 10
    url = config.API_BASE_URL + '/api/companys/{pn}/{size}'
    all_info = str()
    async with httpx.AsyncClient() as client:
        isLastPage = False
        while not isLastPage:
            response = await client.get(url.format(pn=pn, size=size), headers=headers)
            if response.status_code == 200:
                response_json = response.json()
                if response_json['success'] == True:
                    isLastPage = response_json['data']['pageInfo']['isLastPage']
                    pn += 1
                    for item in response_json['data']['pageInfo']['list']:
                        item_company_info =  f"公司ID:{item['companyId']},公司名称:{item['companyName']},联系电话:{item['companyPhone']},信息创建时间:{item['createtime']},信息修改时间:{item['updatetime']}\n"
                        all_info += item_company_info
                else:
                    return '获取公司信息失败：' + response_json.get('message', '未知错误')
            else:
                return 'API请求失败，状态码：' + str(response.status_code)
        return all_info if all_info else '没有找到相关公司信息。'
    
@mcp.tool()
async def getCompanyInfoByKeyword(token: str, keyword: str) -> Any:
    '''根据关键字获取公司的信息。

    Args:
        token (str): 用户的token
        keyword (str): 公司的名称或其他关键字
    '''
    headers = {
        'Authorization': token,
    }
    params = {
        'name': keyword
    }
    pn = 1
    size = 10
    url = config.API_BASE_URL + '/api/companys/{pn}/{size}'
    all_info = str()
    async with httpx.AsyncClient() as client:
        isLastPage = False
        while not isLastPage:
            response = await client.get(url.format(pn=pn, size=size), headers=headers, params=params)
            if response.status_code == 200:
                response_json = response.json()
                if response_json['success'] == True:
                    isLastPage = response_json['data']['pageInfo']['isLastPage']
                    pn += 1
                    for item in response_json['data']['pageInfo']['list']:
                        item_company_info = f"公司ID:{item['companyId']},公司名称:{item['companyName']},联系电话:{item['companyPhone']},信息创建时间:{item['createtime']},信息修改时间:{item['updatetime']}\n"
                        all_info += item_company_info
                else:
                    return '获取公司信息失败：' + response_json.get('message', '未知错误')
            else:
                return 'API请求失败，状态码：' + str(response.status_code)
        return all_info if all_info else '没有找到相关公司信息。'

@mcp.tool()
async def addCompany(token: str, companyName: str, companyPhone: str) -> Any:
    '''添加公司的信息。

    Args:
        token (str): 用户的token
        companyName (str): 公司的名称
        companyPhone (str): 公司的联系电话
    '''
    headers = {
        'Authorization': token,
    }
    url = config.API_BASE_URL + '/api/companys'
    data = {
        'companyName': companyName,
        'companyPhone': companyPhone
    }
    async with httpx.AsyncClient() as client:
        response = await client.post(url, headers=headers, json=data)
        if response.status_code == 200:
            response_json = response.json()
            if response_json['success'] == True:
                return '公司信息添加成功。'
            else:
                return '添加公司信息失败：' + response_json.get('message', '未知错误')
        else:
            return 'API请求失败，状态码：' + str(response.status_code)

@mcp.tool()
async def updateCompany(token: str, companyId: int, companyName: str, companyPhone: str) -> Any:
    '''更新公司的信息。

    Args:
        token (str): 用户的token
        companyId (int): 公司的ID, 可通过 getAllCompanyInfo 获取所有公司的ID,
                        或者通过 getCompanyInfoByKeyword 由关键词搜索公司并获取公司ID
        companyName (str): 公司的名称
        companyPhone (str): 公司的联系电话
    '''
    headers = {
        'Authorization': token,
    }
    url = config.API_BASE_URL + f'/api/companys/{companyId}'
    data = {
        'companyName': companyName,
        'companyPhone': companyPhone
    }
    async with httpx.AsyncClient() as client:
        response = await client.put(url, headers=headers, json=data)
        if response.status_code == 200:
            response_json = response.json()
            if response_json['success'] == True:
                return '公司信息更新成功。'
            else:
                return '更新公司信息失败：' + response_json.get('message', '未知错误')
        else:
            return 'API请求失败，状态码：' + str(response.status_code)
        
@mcp.tool()
async def deleteCompany(token: str, companyId: int) -> Any:
    '''删除公司的信息。

    Args:
        token (str): 用户的token
        companyId (int): 公司的ID, 可通过 getAllCompanyInfo 获取所有公司的ID,
                        或者通过 getCompanyInfoByKeyword 由关键词搜索公司并获取公司ID
    '''
    headers = {
        'Authorization': token,
    }
    url = config.API_BASE_URL + f'/api/companys/{companyId}'
    async with httpx.AsyncClient() as client:
        response = await client.delete(url, headers=headers)
        if response.status_code == 200:
            response_json = response.json()
            if response_json['success'] == True:
                return '公司信息删除成功。'
            else:
                if "成功" in response_json.get('message'):
                    return '公司信息已不存在,无需删除。'
                return '删除公司信息失败：' + response_json.get('message', '未知错误')
        else:
            return 'API请求失败，状态码：' + str(response.status_code)