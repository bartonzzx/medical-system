from typing import Any
import httpx
import config
from main import mcp

@mcp.tool()
async def getCompanyPolicyByKeyword(token: str, keyword: str) -> Any:
    '''根据关键词获取公司的政策信息。

    Args:
        token (str): 用户的token
        keyword (str): 关键词，用于搜索公司政策
    '''
    headers = {
        'Authorization': token,
    }
    body = {
        'pn': 1,
        'size': 10,
        'keyword': keyword
    }
    url = config.API_BASE_URL + f'/api/company_policys'
    all_info = str()
    async with httpx.AsyncClient() as client:
        isLastPage = False
        while not isLastPage:
            response = await client.get(url, headers=headers, params=body)
            if response.status_code == 200:
                response_json = response.json()
                if response_json['success'] == True:
                    isLastPage = response_json['data']['policyInfo']['isLastPage']
                    body['pn'] += 1

                    for item in response_json['data']['policyInfo']['list']:
                        item_policy_info = f"公司ID:{item['drugCompanyModel'].get('companyId','暂无')},公司名称:{item['drugCompanyModel'].get('companyName','')},政策ID:{item.get('id','')},政策名称:{item.get('title','')},政策内容:{item.get('message','')},修改时间:{item.get('updateTime','自创建后暂未更新')}\n"
                        all_info += item_policy_info
                else:
                    return '获取公司政策信息失败：' + response_json.get('message', '未知错误')
            else:
                return 'API请求失败，状态码：' + str(response.status_code)
        return all_info if all_info else '没有找到相关公司政策信息。'

@mcp.tool()
async def addCompanyPolicy(
    token: str, 
    companyId: int, 
    title: str, 
    message: str
) -> Any:
    '''添加公司的政策信息。

    Args:
        token (str): 用户的token
        companyId (int): 公司ID
        title (str): 政策标题
        message (str): 政策内容
    '''
    headers = {
        'Authorization': token,
    }
    body = {
        'companyId': companyId,
        'title': title,
        'message': message
    }
    url = config.API_BASE_URL + f'/api/company_policys'
    async with httpx.AsyncClient() as client:
        response = await client.post(url, headers=headers, json=body)
        if response.status_code == 200:
            response_json = response.json()
            if response_json['success'] == True:
                return '公司政策信息添加成功。'
            else:
                return '添加公司政策信息失败：' + response_json.get('message', '未知错误')
        else:
            return 'API请求失败，状态码：' + str(response.status_code)

@mcp.tool()
async def updateCompanyPolicy(
    token: str, 
    policyId: int, 
    companyId: int, 
    title: str, 
    message: str
) -> Any:
    '''更新公司的政策信息。

    Args:
        token (str): 用户的token
        policyId (int): 政策ID
        companyId (int): 公司ID
        title (str): 政策标题
        message (str): 政策内容
    '''
    headers = {
        'Authorization': token,
    }
    body = {
        'companyId': companyId,
        'title': title,
        'message': message
    }
    url = config.API_BASE_URL + f'/api/company_policys/{policyId}'
    async with httpx.AsyncClient() as client:
        response = await client.put(url.format(policyId), headers=headers, json=body)
        if response.status_code == 200:
            response_json = response.json()
            if response_json['success'] == True:
                return '公司政策信息更新成功。'
            else:
                return '更新公司政策信息失败：' + response_json.get('message', '未知错误')
        else:
            return 'API请求失败，状态码：' + str(response.status_code)
        
@mcp.tool()
async def deleteCompanyPolicy(token: str, policyId: int) -> Any:
    '''删除公司的政策信息。

    Args:
        token (str): 用户的token
        policyId (int): 政策ID
    '''
    headers = {
        'Authorization': token,
    }
    url = config.API_BASE_URL + f'/api/company_policys/{policyId}'
    async with httpx.AsyncClient() as client:
        response = await client.delete(url.format(policyId), headers=headers)
        if response.status_code == 200:
            response_json = response.json()
            if response_json['success'] == True:
                return '删除公司政策信息成功'
            else:
                return '删除公司政策信息失败：' + response_json.get('message', '未知错误')
        else:
            return 'API请求失败，状态码：' + str(response.status_code)