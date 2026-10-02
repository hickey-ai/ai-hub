# 能源环保 · energy

设备与巡检指标并排可见，异常线索及时记录。Vue 3 + Java 21 / Spring Boot 独立本机单用户演示，提供 监测设备 和 巡检记录 两个关联的业务页面，支持检索、新增、修改、删除和本地 JSON 持久化。首次载入虚构数据，后续存储在 `data/energy.json`。

## 直接体验

准备 Java 21、Maven、Node.js 20.19+/22.12+、npm。在仓库根目录运行 `./energy/run.ps1`（Windows）或 `./energy/run.sh`（macOS/Linux），打开 http://127.0.0.1:8110。首次构建会联网下载依赖，Ctrl+C 停止。

## API 与测试

`GET /api/{resource}`、`POST /api/{resource}`、`PUT /api/{resource}/{id}`、`DELETE /api/{resource}/{id}`。字段、必填、类别、数字、日期、关联关系均在服务端校验。无效输入 400、不存在 404、删除被引用档案 409。执行 `mvn -f energy/backend/pom.xml test`，或根目录运行 `./test-all.ps1`。

## 真实页面截图

![能源环保总览](./screenshots/overview.png)
![监测设备](./screenshots/primary.png)
![巡检记录](./screenshots/secondary.png)

![energy编辑表单](./screenshots/editor.png)

## 使用边界

本机单用户样板，不含正式鉴权、权限隔离、数据库、并发写入、外部设备、真实资金或生产流程控制；状态由用户手动维护。请勿录入敏感或真实业务数据，也不要直接部署到公网。
