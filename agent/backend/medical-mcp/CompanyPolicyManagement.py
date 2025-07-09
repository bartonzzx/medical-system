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
                        item_policy_info = f"公司ID:{item['drugCompanyModel']['companyId']},公司名称:{item['drugCompanyModel']['companyName']},政策ID:{item['id']},政策名称:{item['title']},政策内容:{item['message']},修改时间:{item['updateTime']}\n"
                        all_info += item_policy_info
                else:
                    return '获取公司政策信息失败：' + response_json.get('message', '未知错误')
            else:
                return 'API请求失败，状态码：' + str(response.status_code)
        return all_info if all_info else '没有找到相关公司政策信息。'