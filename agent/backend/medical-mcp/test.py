from typing import Any
import httpx
from mcp.server.fastmcp import FastMCP
import config

token = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtZWRpY2FsLXVzZXIiLCJpYXQiOjE3NTE5NTUxNTYsImV4cCI6MTc1MTk3Njc1NiwiaWQiOjEsInVuYW1lIjoiYWRtaW5fMSIsInJvbGUiOiJST0xFXzEifQ.QQ37r1eTmLmiXKRv3qPfFffFbxSHwXty_CM0LxO5kMA'

headers = {
    'Authorization': token
}
url = config.API_BASE_URL + '/doctors/info'
with httpx.Client() as client:
    response = client.get(url, headers=headers)
    if response.status_code == 200:
        response_json = response.json()
        if response_json['success'] == True:
            allTreatType = response_json['data']['allTreatType']
            allLevel = response_json['data']['allLevel']
            return 
