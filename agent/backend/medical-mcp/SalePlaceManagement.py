from typing import Any
import httpx
import config
from main import mcp

@mcp.tool()
async def getAllPlaceInfo(token: str) -> Any:
    '''获取所有销售点的信息。

    Args:
        token (str): 用户的token
    '''
    headers = {
        'Authorization': token,
    }
    pn = 1
    size = 10
    url = config.API_BASE_URL + '/api/sales/{pn}/{size}'
    all_info = str()
    async with httpx.AsyncClient() as client:
        isLastPage = False
        while not isLastPage:
            response = await client.get(url.format(pn=pn, size=size), headers=headers)
            if response.status_code == 200:
                response_json = response.json()
                if response_json['success'] == True:
                    isLastPage = response_json['data']['salePageInfo']['isLastPage']
                    pn += 1
                    for item in response_json['data']['salePageInfo']['list']:
                        item_place_info = f"销售点ID: {item['saleId']}, 销售点名称: {item['saleName']}, 联系电话: {item['salePhone']}, 地址: {item['address']}, 纬度: {item['lat']}, 经度: {item['lng']}, 信息创建时间: {item['createtime']}, 信息更新时间: {item.get('updatetime', '自创建后暂未更新')}\n"
                        all_info += item_place_info
                else:
                    return '获取销售点信息失败：' + response_json.get('message', '未知错误')
            else:
                return 'API请求失败，状态码：' + str(response.status_code)
    return all_info if all_info else '没有找到相关销售点信息。'
