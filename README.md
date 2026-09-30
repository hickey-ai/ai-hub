# ai-hub · Vue + Java 21 业务系统合集

> 独立、可运行的业务系统样板，不复制第三方项目源码。当前是起步版本，**不是**已覆盖所有行业的成品系统。

| 目录 | 类型 | 已实现的最小闭环 | 页面预览 |
| --- | --- | --- | --- |
| [shop](./shop/README.md) | 电商商城 | 商品浏览/搜索/分类、购物车、库存校验与下单 | [截图](./shop/screenshots/storefront.png) |
| [manage](./manage/README.md) | 通用管理脚手架 | 仪表盘、用户增删改、角色查看、基础统计 | [截图](./manage/screenshots/dashboard.png) |
| [crm](./crm/README.md) | 客户关系 CRM | 客户建档、商机跟进、阶段推进、赢单/流失 | [截图](./crm/screenshots/pipeline.png) |
| [oa](./oa/README.md) | 办公审批 OA | 请假/报销草稿、提交、同意/驳回 | [截图](./oa/screenshots/approvals.png) |

## 快速开始

依赖：Java 21、Maven 3.6.3+、Node.js 20.19+/22.12+、npm。每个项目各自运行，不共用数据库或端口。


默认后端数据为**内存演示数据**，重启即重置；不含正式认证、支付或生产级权限控制。切勿直接公网部署。详见各项目 README。

| 项目 | 后端 | 前端 |
| --- | --- | --- |
| shop | `cd shop/backend && mvn spring-boot:run`（8081） | `cd shop/frontend && npm install && npm run dev`（5173） |
| manage | `cd manage/backend && mvn spring-boot:run`（8082） | `cd manage/frontend && npm install && npm run dev`（5174） |
| crm | `cd crm/backend && mvn spring-boot:run`（8083） | `cd crm/frontend && npm ci && npm run dev`（5175） |
| oa | `cd oa/backend && mvn spring-boot:run`（8084） | `cd oa/frontend && npm ci && npm run dev`（5176） |

## 业务系统地图

[调研和建设清单](./docs/business-map.md)区分 **参考项目**、**已实现** 和 **待建设**。后续可选 ERP（采购/库存）、CMS（内容发布）、HRM（人事）、预约、工单、教育、医疗等。每个类型应有独立业务闭环、测试、截图后再改为“已实现”。

## 技术约定

- 前端 Vue 3 + Vite；后端 Java 21 + Spring Boot；REST JSON，开发环境由 Vite 代理 `/api`。
- 每个项目独立 `frontend/`、`backend/`、`screenshots/`，避免不相关业务耦合。
- 演示数据不应被误认为生产持久化；鉴权、迁移、审计、并发与支付是后续专题。
- Git 签名仅设置在本仓库：`user.email=3174667330@qq.com`；全局 Git 配置保持不变。

交流：QQ 邮箱 3174667330@qq.com。
