# wellness · vital · 养生服务台

> 课程、会员和生活方式计划的轻量化运营工作台。这是 ai-hub 的业务系统样板，统一采用 Vue 3 + Java 21/Spring Boot，自主实现，不复制第三方源码。

## 页面

- 总览：核心指标、趋势图、快捷操作和动态
- 养生计划：查看、新增和状态展示
- 打卡记录：状态跟踪、处理和刷新

## 页面截图

![总览](./screenshots/overview.png)

![养生计划](./screenshots/primary.png)

![打卡记录](./screenshots/secondary.png)

## 运行

```powershell
npm --prefix frontend ci
npm --prefix frontend run build -- --outDir ../backend/src/main/resources/static --emptyOutDir
mvn -f backend/pom.xml package
java -jar backend/target/wellness-api-0.1.0.jar
```

访问 http://127.0.0.1:8087。API：`GET /api/metrics`、`GET /api/plans`、`POST /api/plans`、`GET /api/checkins`。

当前为本机演示版本，数据保存在进程内存；正式使用前应接入数据库、登录鉴权、审计和业务合规流程。金融、健康、医院、学校和门禁场景涉及敏感数据，未经安全评审不得直接用于真实生产。
