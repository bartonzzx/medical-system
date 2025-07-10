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
