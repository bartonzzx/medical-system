from typing import Any
from enum import Enum
import httpx
import config
from main import mcp

@mcp.tool()
async def getCompantById(token: str, companyId: int) -> Any:
    '''根据公司ID获取公司的信息。

    Args:
        token (str): 用户的token
        companyId (int): 公司的ID
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
        
