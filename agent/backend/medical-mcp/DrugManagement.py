from typing import Any
import httpx
import config
from main import mcp

@mcp.tool()
async def getAllDrugInfo(token: str) -> Any:
    '''获取所有药品的信息。

    Args:
        token (str): 用户的token
    '''
    headers = {
        'Authorization': token,
    }
    
    pn = 1
    size = 10
    url = config.API_BASE_URL + '/api/drugs/{pn}/{size}'
    all_info = str()
    async with httpx.AsyncClient() as client:
        isLastPage = False
        while not isLastPage:
            response = await client.get(url.format(pn=pn, size=size), headers=headers)
            if response.status_code == 200:
                response_json = response.json()
                if response_json['success'] == True:
                    isLastPage = response_json['data']['drugPageInfo']['isLastPage']
                    pn += 1
                    for item in response_json['data']['drugPageInfo']['list']:
                        item_drug_info = f"药品ID: {item['drugId']}, 药品名称: {item['drugName']}, 药品信息: {item['drugInfo']}, 药品效用: {item['drugEffect']}, 信息更新时间: {item.get('updatetime', '自创建后暂未更新')}\n"
                        sale_place_info = str()
                        for item_sale in item.get('drugSales', []):
                            item_sale_info = f"销售点ID: {item_sale['saleId']}, 销售点名称: {item_sale['saleName']}\n"
                            sale_place_info += item_sale_info
                        all_info += item_drug_info + sale_place_info
                else:
                    return '获取药品信息失败：' + response_json.get('message', '未知错误')
            else:
                return 'API请求失败，状态码：' + str(response.status_code)
    return all_info if all_info else '没有找到相关药品信息。'