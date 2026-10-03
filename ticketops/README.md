# 内部工单平台 · ticketops

按队列记录需求、指派处理人与工单进度。Vue 3 + Java 21 / Spring Boot 独立本机单用户演示，提供 工单队列 和 内部工单 两个关联的业务页面，支持检索、新增、修改、删除和本地 JSON 持久化。首次载入虚构数据，后续存储在 `data/ticketops.json`。

## 直接体验

准备 Java 21、Maven、Node.js 20.19+/22.12+、npm。在仓库根目录运行 `./ticketops/run.ps1`（Windows）或 `./ticketops/run.sh`（macOS/Linux），打开 http://127.0.0.1:8166。首次构建会联网下载依赖，Ctrl+C 停止。

## 使用路径

先建立工单队列（例如 IT 服务台），再在「内部工单」登记标题、优先级、处理人和状态；处理过程中可编辑状态与备注。已有 `service` 面向客户售后，本项目面向内部请求。**状态为人工维护，不自动派单、计时或发送通知。**

## API 与测试

`GET /api/{resource}`、`POST /api/{resource}`、`PUT /api/{resource}/{id}`、`DELETE /api/{resource}/{id}`。字段、必填、类别、数字、日期、关联关系均在服务端校验。无效输入 400、不存在 404、删除被引用档案或编号重复 409；工单编号全局唯一（忽略大小写，首尾空格自动去除），编辑自身不冲突。执行 `mvn -f ticketops/backend/pom.xml test`，或根目录运行 `./test-all.ps1`。

## 真实页面截图

![内部工单平台总览](./screenshots/overview.png)
![工单队列](./screenshots/primary.png)
![内部工单](./screenshots/secondary.png)
![新增表单](./screenshots/editor.png)

## 使用边界

本机单用户样板，不含正式鉴权、权限隔离、数据库、并发写入、外部设备、真实资金或生产流程控制；状态由用户手动维护。请勿录入敏感或真实业务数据，也不要直接部署到公网。
