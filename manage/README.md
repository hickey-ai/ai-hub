# manage · 通用管理工作台样板

Vue 3 + Java 21 Spring Boot 管理台演示。页面截图：[仪表盘](./screenshots/dashboard.png)、[用户管理](./screenshots/users.png)。

![仪表盘](./screenshots/dashboard.png)

![用户管理](./screenshots/users.png)

## 运行

1. `cd backend && mvn spring-boot:run`，API 在 `http://localhost:8082/api/users`。
2. `cd frontend && npm install && npm run dev`，访问 `http://127.0.0.1:5174`。
3. 后端测试：`cd backend && mvn test`；前端构建：`cd frontend && npm run build`。

## 功能与边界

- 仪表盘指标随用户数据变化；用户搜索/过滤/新建/编辑/删除；角色及成员数展示。
- 用户角色是**演示字段**，不包含登录、会话、RBAC 服务端校验、菜单配置、审计或持久化。数据在内存中，重启重置。**不能生产使用**。
- 图表趋势为静态示意，不是历史统计数据。
