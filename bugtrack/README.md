# 缺陷跟踪平台 · bugtrack

把项目缺陷、复现步骤和修复状态整理在同一工作台。Vue 3 + Java 21 / Spring Boot 独立本机单用户演示，提供 项目档案 和 缺陷记录 两个关联的业务页面，支持检索、新增、修改、删除和本地 JSON 持久化。首次载入虚构数据，后续存储在 `data/bugtrack.json`。

## 直接体验

准备 Java 21、Maven、Node.js 20.19+/22.12+、npm。在仓库根目录运行 `./bugtrack/run.ps1`（Windows）或 `./bugtrack/run.sh`（macOS/Linux），打开 http://127.0.0.1:8167。首次构建会联网下载依赖，Ctrl+C 停止。

## 使用路径

先建立项目档案，再登记缺陷编号、严重程度、复现步骤、报告人和状态；修复后可手动更新为待验证或已关闭。**本项目不与测试执行记录自动关联，也不连接 Git 或 CI。**

## API 与测试

`GET /api/{resource}`、`POST /api/{resource}`、`PUT /api/{resource}/{id}`、`DELETE /api/{resource}/{id}`。字段、必填、类别、数字、日期、关联关系均在服务端校验。无效输入 400、不存在 404、删除被引用档案或编号重复 409；同一项目内缺陷编号唯一（忽略大小写，首尾空格自动去除），编辑自身不冲突。执行 `mvn -f bugtrack/backend/pom.xml test`，或根目录运行 `./test-all.ps1`。

## 真实页面截图

![缺陷跟踪平台总览](./screenshots/overview.png)
![项目档案](./screenshots/primary.png)
![缺陷记录](./screenshots/secondary.png)
![新增表单](./screenshots/editor.png)

## 使用边界

本机单用户样板，不含正式鉴权、权限隔离、数据库、并发写入、外部设备、真实资金或生产流程控制；状态由用户手动维护。请勿录入敏感或真实业务数据，也不要直接部署到公网。
