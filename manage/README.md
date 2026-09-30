# manage · 通用管理工作台样板

![仪表盘](./screenshots/dashboard.png)
![用户管理](./screenshots/users.png)

用户搜索/过滤、新建、编辑、删除；仪表盘指标和角色成员数随数据更新。 Vue 3 + Java 21/Spring Boot；页面截图来自本地实际运行。

## 一键运行

需要 Java 21、Maven、Node.js 20.19+/22.12+ 和 npm；在本目录执行：

```powershell
./run.ps1           # Windows PowerShell
```

```sh
./run.sh            # macOS / Linux
```

首次执行会下载依赖、构建前端、运行后端测试并启动单个 jar。浏览器打开 **http://127.0.0.1:8082**，前后端同一端口，无需另开终端。按 Ctrl+C 停止。Windows 如有旧 Vite 开发服务占用 `frontend/node_modules`，请先关闭再运行。

数据保存在本项目 `data/manage.json`，停止服务后可复制备份；如需恢复默认演示数据，停止后删除该文件并重启。文件损坏时启动会报错，不会自动清空。不要多个进程共用此文件。

## 测试

```sh
mvn -f backend/pom.xml test
npm --prefix frontend ci
npm --prefix frontend run build
```

在仓库根目录执行 `./test-all.ps1`（Windows）或 `./test-all.sh`（macOS / Linux）会验证全部四个项目及 jar 中的页面资源。前端截图参见上方。

**使用边界：**角色是演示字段，不含登录、会话、RBAC 校验或真实审计；趋势图为静态示意。 默认只监听本机 `127.0.0.1`；这是可本地直接使用的样板，**不适合未经改造部署到公网或生产环境**。
