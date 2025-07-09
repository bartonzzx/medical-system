from mcp.server.fastmcp import FastMCP

mcp = FastMCP("medical-mcp", host='0.0.0.0', port=8081)

from DoctorManagement import *

# initialize 

# # 定义枚举类型
# class DoctorLevel(str, Enum):
#     """医师级别枚举"""
#     CHIEF = "主任医师"
#     GENERAL = "普通医师"
#     INTERN = "实习医师"

# class TreatmentType(str, Enum):
#     """主治部位枚举"""
#     SHOULDER = "肩部"
#     ANKLE = "踝部"
#     KNEE = "膝部"
#     WAIST = "腰部"
#     HEAD = "头部"
#     ELBOW = "肘部"
#     LEG = "腿部"

# class Gender(str, Enum):
#     """性别枚举"""
#     MALE = "男"
#     FEMALE = "女"

# level_mapping = {
#     DoctorLevel.CHIEF: 1,
#     DoctorLevel.GENERAL: 2,
#     DoctorLevel.INTERN: 3
# }

# type_mapping = {
#     TreatmentType.SHOULDER: 1,
#     TreatmentType.ANKLE: 2,
#     TreatmentType.KNEE: 3,
#     TreatmentType.WAIST: 9,
#     TreatmentType.HEAD: 10,
#     TreatmentType.ELBOW: 11,
#     TreatmentType.LEG: 12
# }

# @mcp.tool()
# async def getAllDoctorsCategories(token: str) -> Any:
#     '''获取医师的分类，包含主治的身体部位分类，如肩部、踝部、膝部；以及医师的级别分类，如普通医师、实习医师、主任医师。

#     Args:
#         token (str): 用户的token
#     '''
#     headers = {
#         'Authorization': token
#     }
#     url = config.API_BASE_URL + '/api/doctors/info'
#     async with httpx.AsyncClient() as client:
#         response = await client.get(url, headers=headers)
#         if response.status_code == 200:
#             response_json = response.json()
#             if response_json['success'] == True:
#                 allTreatType = response_json['data']['allTreatType']
#                 allLevel = response_json['data']['allLevel']
#                 return '按主治的身体部位分类：' + str(allTreatType) + '；\n 按医师的级别分类：' + str(allLevel)
#             else:
#                 return '获取医师分类失败：' + response_json.get('message', '未知错误')
#         else:
#             return 'API请求失败，状态码：' + str(response.status_code)

# @mcp.tool()
# async def getDoctorInfo(token: str, keyword: str) -> Any:
#     '''获取医师的详细信息。

#     Args:
#         token (str): 用户的token
#         keyword (str): 医师的姓名、主治部位、医师级别或其他关键字
#     '''
#     headers = {
#         'Authorization': token,
#     }
#     params = {
#         'pn': 1,
#         'size': 10,
#         'keyword': keyword
#     }
#     url = config.API_BASE_URL + '/api/doctors'
#     all_info = str()
#     async with httpx.AsyncClient() as client:
#         isLastPage = False
#         while not isLastPage:
#             response = await client.get(url, headers=headers, params=params)
#             if response.status_code == 200:
#                 response_json = response.json()
#                 if response_json['success'] == True:
#                     isLastPage = response_json['data']['doctorInfo']['isLastPage']
#                     params['pn'] += 1

#                     for item in response_json['data']['doctorInfo']['list']:
#                         item_doctor_info = f"姓名:{item['name']},年龄:{item['age']},性别:{'男' if item['sex']==1 else '女'},级别:{item['doctorLevel']},主治方向:{item['treatType']},医师ID:{item['id']},账户ID:{item['accountId']}\n"
#                         all_info += item_doctor_info
#                 else:
#                         return '获取医师信息失败：' + response_json.get('message', '未知错误')
#             else:
#                 return 'API请求失败，状态码：' + str(response.status_code)
#         return all_info if all_info else '没有找到相关医师信息。'

# @mcp.tool()
# async def addDoctor(
#     token: str, 
#     name: str, 
#     age: int, 
#     sex: Gender, 
#     level: DoctorLevel, 
#     phoneNumber: str, 
#     type: TreatmentType, 
#     pwd: str
# ) -> Any:
#     '''添加新的医师到系统。
    
#     Args:
#         token (str): 用户的token
#         name (str): 医师的姓名
#         age (int): 医师的年龄
#         sex (Gender): 医师的性别，可选值：男、女
#         level (DoctorLevel): 医师的级别，可选值：主任医师、普通医师、实习医师
#         phoneNumber (str): 医师的电话号码
#         type (TreatmentType): 医师的主治的身体部位，可选值：肩部、踝部、膝部、腰部、头部、肘部、腿部
#         pwd (str): 医师的密码
#     '''
#     headers = {
#         'Authorization': token
#     }
    
#     # 将性别转换为数字，1表示男，2表示女
#     sex_value = 1 if sex == Gender.MALE else 2
    
#     # 将医师级别转换为数字
#     level_value = level_mapping[level]
    
#     # 将主治的身体部位转换为数字
#     type_value = type_mapping[type]
    
#     data = {
#         'name': name,
#         'age': age,
#         'sex': sex_value,
#         'levelId': level_value,
#         'phoneNumber': phoneNumber,
#         'typeId': type_value,
#         'pwd': pwd
#     }
    
#     url = config.API_BASE_URL + '/api/doctors'
#     async with httpx.AsyncClient() as client:
#         response = await client.post(url, headers=headers, json=data)
#         if response.status_code == 200:
#             response_json = response.json()
#             if response_json['success'] == True:
#                 return '添加医师成功'
#             else:
#                 return '添加医师失败：' + response_json.get('message', '未知错误')
#         else:
#             return 'API请求失败，状态码：' + str(response.status_code)

# @mcp.tool()
# async def modifyDoctor(
#     token: str, 
#     doctorId: int, 
#     name: str, 
#     age: int, 
#     sex: Gender, 
#     level: DoctorLevel, 
#     phoneNumber: str, 
#     type: TreatmentType, 
#     pwd: str):
#     '''修改医师的信息。
    
#     Args:
#         token (str): 用户的token
#         doctorId (int): 医师的ID, 可通过 getDoctorInfo 获取
#         name (str): 医师的姓名
#         age (int): 医师的年龄
#         sex (Gender): 医师的性别，可选值：男、女
#         level (DoctorLevel): 医师的级别，可选值：主任医师、普通医师、实习医师
#         phoneNumber (str): 医师的电话号码
#         type (TreatmentType): 医师的主治的身体部位，可选值：肩部、踝部、膝部、腰部、头部、肘部、腿部
#         pwd (str): 医师的密码
#     '''

#     headers = {
#         'Authorization': token
#     }

#     data = {
#         'name': name,
#         'age': age,
#         'sex': 1 if sex == Gender.MALE else 2,
#         'levelId': level_mapping[level],
#         'phoneNumber': phoneNumber,
#         'typeId': type_mapping[type],
#         'pwd': pwd
#     }

#     url = config.API_BASE_URL + f'/api/doctors/{doctorId}'
#     async with httpx.AsyncClient() as client:
#         response = await client.put(url, headers=headers, json=data)
#         if response.status_code == 200:
#             response_json = response.json()
#             if response_json['success'] == True or response_json["code"] == 10000:
#                 return '修改医师信息成功'
#             else:
#                 return '修改医师信息失败：' + response_json.get('message', '未知错误')
#         else:
#             return 'API请求失败，状态码：' + str(response.status_code)

# @mcp.tool()
# async def deleteDoctor(token: str, doctorId: int) -> Any:
#     '''删除医师的信息。
    
#     Args:
#         token (str): 用户的token
#         doctorId (int): 医师的ID, 可通过 getDoctorInfo 获取
#     '''
#     headers = {
#         'Authorization': token
#     }
#     url = config.API_BASE_URL + f'/api/doctors/{doctorId}'
#     async with httpx.AsyncClient() as client:
#         response = await client.delete(url, headers=headers)
#         if response.status_code == 200:
#             response_json = response.json()
#             if response_json['success'] == True:
#                 return '删除医师信息成功'
#             else:
#                 return '删除医师信息失败：' + response_json.get('message', '未知错误')
#         else:
#             return 'API请求失败，状态码：' + str(response.status_code)

# @mcp.tool()
# async def resetDoctorPassword(token: str, accountId: int) -> Any:
#     '''重置医师的密码为666666。
    
#     Args:
#         token (str): 用户的token
#         accountId (int): 账户的ID, 可通过 getDoctorInfo 获取
#     '''
#     headers = {
#         'Authorization': token
#     }
#     url = config.API_BASE_URL + f'/api/doctors/reset/{accountId}'
#     async with httpx.AsyncClient() as client:
#         response = await client.put(url, headers=headers)
#         if response.status_code == 200:
#             response_json = response.json()
#             if response_json['success'] == True:
#                 return '重置医师密码成功'
#             else:
#                 return '重置医师密码失败：' + response_json.get('message', '未知错误')
#         else:
#             return 'API请求失败，状态码：' + str(response.status_code)


if __name__ == "__main__":
    # 初始化并运行 server
    # mcp.run(transport='stdio')
    mcp.run(transport='streamable-http')