# 快递驿站 · parcelstation

入库、取件码与异常件处理。Vue 3 + Java 21 / Spring Boot 独立本机单用户演示，提供 货架档案 和 包裹流转 两个关联的业务页面，支持检索、新增、修改、删除和本地 JSON 持久化。首次载入虚构数据，后续存储在 `data/parcelstation.json`。

## 直接体验

准备 Java 21、Maven、Node.js 20.19+/22.12+、npm。在仓库根目录运行 `./parcelstation/run.ps1`（Windows）或 `./parcelstation/run.sh`（macOS/Linux），打开 http://127.0.0.1:8179。首次构建会联网下载依赖，Ctrl+C 停止。

## 核心流程与限制

包裹先以「待取件」入库，可登记签收或异常；异常件须填写处理备注，可退回待取件。包裹编号不能重复，同一待取件取件码不能重复；已签收不能回退。这里只记录人工确认，**不向收件人发送短信，也不核验取件人身份**。

## API 与测试

`GET /api/{resource}`、`POST /api/{resource}`、`PUT /api/{resource}/{id}`、`DELETE /api/{resource}/{id}`。字段、必填、类别、数字、日期、关联关系均在服务端校验。无效输入 400、不存在 404、编号冲突/非法状态跳转或删除被引用档案 409。执行 `mvn -f parcelstation/backend/pom.xml test`，或根目录运行 `./test-all.ps1`。真实浏览器/API/重启验证见[三项目验证记录](../docs/priority-three-validation-2026-10-04.md)。

## 真实页面截图

![快递驿站总览](./screenshots/overview.png)
![货架档案](./screenshots/primary.png)
![包裹流转](./screenshots/secondary.png)
![新增表单](./screenshots/editor.png)

## 使用边界

本机单用户样板，不含正式鉴权、权限隔离、数据库、并发写入、外部设备、真实资金或生产流程控制；状态由用户手动维护。请勿录入敏感或真实业务数据，也不要直接部署到公网。
