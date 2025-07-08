from typing import Any
import httpx
from mcp.server.fastmcp import FastMCP
import config

# initialize 

mcp = FastMCP("medical-mcp", host='0.0.0.0', port=8081)

token = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZWRpY2FsLXVzZXIiLCJpYXQiOjE3NTE5NTUxNTYsImV4cCI6MTc1MTk3Njc1NiwiaWQiOjEsInVuYW1lIjoiYWRtaW5fMSIsInJvbGUiOiJST0xFXzEifQ.QQ37r1eTmLmiXKRv3qPfFffFbxSHwXty_CM0LxO5kMA'

@mcp.tool()
async def getAllDoctorsCategories(token: str) -> Any:
    '''获取医师的分类，包含主治的身体部位分类，如肩部、踝部、膝部；以及医师的级别分类，如普通医师、实习医师、主任医师。

    Args:
        token (str): 用户的token
    '''
    headers = {
        'Authorization': token
    }
    url = config.API_BASE_URL + '/doctors/info'
    async with httpx.AsyncClient() as client:
        response = await client.get(url, headers=headers)
        if response.status_code == 200:
            response_json = response.json()
            if response_json['success'] == True:
                allTreatType = response_json['data']['allTreatType']
                allLevel = response_json['data']['allLevel']
                return '按主治的身体部位分类：' + str(allTreatType) + '；\n 按医师的级别分类：' + str(allLevel)
            else:
                return '获取医师分类失败：' + response_json.get('message', '未知错误')
        else:
            return 'API请求失败，状态码：' + str(response.status_code)

async def getDoctorInfo(token: str, keyword: str) -> Any:
    '''获取医师的详细信息。

    Args:
        token (str): 用户的token
        keyword (str): 医师的姓名、主治部位、医师级别或其他关键字
    '''
    headers = {
        'Authorization': token,
        
    }
    url = config.API_BASE_URL + '/doctors/search'
    params = {'keyword': keyword}
    async with httpx.AsyncClient() as client:
        response = await client.get(url, headers=headers, params=params)
        if response.status_code == 200:
            response_json = response.json()
            if response_json['success'] == True:
                return response_json['data']
            else:
                return '获取医师信息失败：' + response_json.get('message', '未知错误')
        else:
            return 'API请求失败，状态码：' + str(response.status_code)

if __name__ == "__main__":
    # 初始化并运行 server
    # mcp.run(transport='stdio')
    mcp.run(transport='streamable-http')