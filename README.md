## 端口信息
前端端口：3000
后端端口：8080
MCP服务器端口：8081

## 部署方法
1. 在仓库目录下，运行
```
docker compose up -d
```
2. 进入数据库容器
```
docker exec -it medical-system-db sh
```
3. 登陆mysql（密码:qwe!@#）
```
mysql -u root -p
```
4. 导入后端服务所需数据
```
use medical;
```
```
source /root/medical_newFunction.sql;
```

## 卸载方法
进入仓库目录，运行
```
docker compose down
```
