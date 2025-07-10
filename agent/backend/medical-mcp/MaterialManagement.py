from typing import Any
import httpx
import config
from main import mcp

@mcp.tool()
async def getAllMaterialInfo(token: str) -> Any:
    '''获取所有就医所需材料的信息。

    Args:
        token (str): 用户的token
    '''
    headers = {
        'Authorization': token,
    }
    parms = {
        'pn': 1,
        'size': 10,
    }
    url = config.API_BASE_URL + '/api/materials/'
    all_info = str()
    async with httpx.AsyncClient() as client:
        isLastPage = False
        while not isLastPage:
            response = await client.get(url, headers=headers, params=parms)
            if response.status_code == 200:
                response_json = response.json()
                if response_json['success'] == True:
                    isLastPage = response_json['data']['materialInfo']['isLastPage']
                    parms['pn'] += 1
                    for item in response_json['data']['materialInfo']['list']:
                        item_material_info = f"材料ID: {item['id']}, 材料名称: {item['tittle']}, 材料信息: {item['message']}\n"
                        all_info += item_material_info
                else:
                    return '获取材料信息失败：' + response_json.get('message', '未知错误')
            else:
                return 'API请求失败，状态码：' + str(response.status_code)
    return all_info if all_info else '没有找到相关材料信息。'