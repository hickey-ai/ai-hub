# 上门服务 · homeservice

客户登记和上门任务，透明记录服务进度。Vue 3 + Java 21 / Spring Boot 独立本机单用户演示，提供 客户档案 和 上门任务 两个关联的业务页面，支持检索、新增、修改、删除和本地 JSON 持久化。首次载入虚构数据，后续存储在 `data/homeservice.json`。

## 直接体验

准备 Java 21、Maven、Node.js 20.19+/22.12+、npm。在仓库根目录运行 `./homeservice/run.ps1`（Windows）或 `./homeservice/run.sh`（macOS/Linux），打开 http://127.0.0.1:8121。首次构建会联网下载依赖，Ctrl+C 停止。

## API 与测试

`GET /api/{resource}`、`POST /api/{resource}`、`PUT /api/{resource}/{id}`、`DELETE /api/{resource}/{id}`。字段、必填、类别、数字、日期、关联关系均在服务端校验。无效输入 400、不存在 404、删除被引用档案 409。执行 `mvn -f homeservice/backend/pom.xml test`，或根目录运行 `./test-all.ps1`。

## 真实页面截图

![上门服务总览](./screenshots/overview.png)
![客户档案](./screenshots/primary.png)
![上门任务](./screenshots/secondary.png)

![编辑表单](./screenshots/editor.png)

## 使用边界

本机单用户档案/记录样板，字段状态由用户手动维护，不提供生产级事务或流程引擎。不含正式鉴权、权限隔离、数据库、并发写入、外部设备、真实资金或生产流程控制；状态由用户手动维护。请勿录入敏感或真实业务数据，也不要直接部署到公网。
