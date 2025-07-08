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

@mcp.tool()
async def getDoctorInfo(token: str, keyword: str) -> Any:
    '''获取医师的详细信息。

    Args:
        token (str): 用户的token
        keyword (str): 医师的姓名、主治部位、医师级别或其他关键字
    '''
    headers = {
        'Authorization': token,
    }
    params = {
        'pn': 1,
        'size': 10,
        'keyword': keyword
    }
    url = config.API_BASE_URL + '/doctors'
    all_info = str()
    async with httpx.AsyncClient() as client:
        isLastPage = False
        while not isLastPage:
            response = await client.get(url, headers=headers, params=params)
            if response.status_code == 200:
                response_json = response.json()
                if response_json['success'] == True:
                    isLastPage = response_json['data']['doctorInfo']['isLastPage']
                    params['pn'] += 1

                    for item in response_json['data']['doctorInfo']['list']:
                        item_doctor_info = f"姓名:{item['name']},年龄:{item['age']},性别:{'男' if item['sex']==1 else '女'},级别:{item['doctorLevel']},主治方向:{item['treatType']},医师ID:{item['accountId']}\n"
                        all_info += item_doctor_info
                else:
                        return '获取医师信息失败：' + response_json.get('message', '未知错误')
            else:
                return 'API请求失败，状态码：' + str(response.status_code)
        return all_info if all_info else '没有找到相关医师信息。'

@mcp.tool()
async def addDoctor(token: str, name: str, age: int, sex: str, level: str, phoneNumber: str, type: str, pwd: str) -> Any:
    '''添加新的医师到系统。
    
    Args:
        token (str): 用户的token
        name (str): 医师的姓名
        age (int): 医师的年龄
        sex (str): 医师的性别
        level (str): 医师的级别, 如普通医师、实习医师、主任医师等
        phoneNumber (str): 医师的电话号码
        type (str): 医师的主治的身体部位, 如肩部、踝部、膝部等
        pwd (str): 医师的密码
    '''
    headers = {
        'Authorization': token
    }
    # 将性别转换为数字，1表示男，2表示女
    sex = 1 if sex == '男' else 2
    # 将医师级别转换为数字，1表示主任医师，2表示普通医师，3表示实习医师等
    level_mapping = {
        '主任医师': 1,
        '普通医师': 2,
        '实习医师': 3
    }
    level = level_mapping.get(level, 2)  # 默认级别为普通医师
    # 将主治的身体部位转换为数字，1表示肩部，
    data = {
        'name': name,
        'age': age,
        'sex': sex,
        'level': level,
        'phoneNumber': phoneNumber,
        'type': type,
        'pwd': pwd
    }
    url = config.API_BASE_URL + '/doctors'
    async with httpx.AsyncClient() as client:
        response = await client.post(url, headers=headers, json=data)
        if response.status_code == 200:
            response_json = response.json()
            if response_json['success'] == True:
                return '添加医师成功：' + str(response_json['data'])
            else:
                return '添加医师失败：' + response_json.get('message', '未知错误')
        else:
            return 'API请求失败，状态码：' + str(response.status_code)

if __name__ == "__main__":
    # 初始化并运行 server
    # mcp.run(transport='stdio')
    mcp.run(transport='streamable-http')