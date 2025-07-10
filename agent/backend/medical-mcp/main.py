from mcp.server.fastmcp import FastMCP

mcp = FastMCP("medical-mcp", host='0.0.0.0', port=8081)

from DoctorManagement import *
from CompanyManagement import *
from CompanyPolicyManagement import *
from CityManagement import *
from MedicalPolicyManagement import *
from SalePlaceManagement import *

if __name__ == "__main__":
    # 初始化并运行 server
    # mcp.run(transport='stdio')
    mcp.run(transport='streamable-http')