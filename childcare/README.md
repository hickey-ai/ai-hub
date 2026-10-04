# 托育机构 · childcare

班级、接送与日常照护交接。Vue 3 + Java 21 / Spring Boot 独立本机单用户演示，提供 托育班级 和 照护交接 两个关联的业务页面，支持检索、新增、修改、删除和本地 JSON 持久化。首次载入虚构数据，后续存储在 `data/childcare.json`。

## 直接体验

准备 Java 21、Maven、Node.js 20.19+/22.12+、npm。在仓库根目录运行 `./childcare/run.ps1`（Windows）或 `./childcare/run.sh`（macOS/Linux），打开 http://127.0.0.1:8181。首次构建会联网下载依赖，Ctrl+C 停止。

## 核心流程与限制

先维护班级，再用代称记录入园/离园交接、接送人代称和喂养午睡等备注；异常交接须有说明，已交接不可回退。**没有实名认证、监护授权核验、考勤设备或健康评估**；严禁存入真实儿童身份、健康及接送信息。

## API 与测试

`GET /api/{resource}`、`POST /api/{resource}`、`PUT /api/{resource}/{id}`、`DELETE /api/{resource}/{id}`。字段、必填、类别、数字、日期、关联关系均在服务端校验。无效输入 400、不存在 404、非法状态跳转或删除被引用档案 409。执行 `mvn -f childcare/backend/pom.xml test`，或根目录运行 `./test-all.ps1`。真实浏览器/API/重启验证见[三项目验证记录](../docs/priority-three-validation-2026-10-04.md)。

## 真实页面截图

![托育机构总览](./screenshots/overview.png)
![托育班级](./screenshots/primary.png)
![照护交接](./screenshots/secondary.png)
![新增表单](./screenshots/editor.png)

## 使用边界

本机单用户样板，不含正式鉴权、权限隔离、数据库、并发写入、外部设备、真实资金或生产流程控制；状态由用户手动维护。请勿录入敏感或真实业务数据，也不要直接部署到公网。
