from typing import Any
import httpx
from mcp.server.fastmcp import FastMCP
import config

token = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZWRpY2FsLXVzZXIiLCJpYXQiOjE3NTE5NTUxNTYsImV4cCI6MTc1MTk3Njc1NiwiaWQiOjEsInVuYW1lIjoiYWRtaW5fMSIsInJvbGUiOiJST0xFXzEifQ.QQ37r1eTmLmiXKRv3qPfFffFbxSHwXty_CM0LxO5kMA'

headers = {
    'Authorization': token,
}
params = {
    'pn': 1,
    'size': 10,
    'keyword': '医师'
}
url = config.API_BASE_URL + '/doctors'
all_info = str()
with httpx.Client() as client:
    isLastPage = False
    while not isLastPage:
        response = client.get(url, headers=headers, params=params)
        if response.status_code == 200:
            response_json = response.json()
            if response_json['success'] == True:
                isLastPage = response_json['data']['doctorInfo']['isLastPage']
                params['pn'] += 1
                # print("var"+"="*30+str(isLastPage))
                # print("url"+"="*30+str(response_json['data']['doctorInfo']['isLastPage']))

                for item in response_json['data']['doctorInfo']['list']:
                    item_doctor_info = f"姓名:{item['name']},年龄:{item['age']},性别:{'男' if item['sex']==1 else '女'},级别:{item['doctorLevel']},主治方向:{item['treatType']},医师ID:{item['accountId']}\n"
                    all_info += item_doctor_info
        #     else:
        #         return '获取医师信息失败：' + response_json.get('message', '未知错误')
        # else:
        #     return 'API请求失败，状态码：' + str(response.status_code)