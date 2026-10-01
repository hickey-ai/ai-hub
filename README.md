# ai-hub · Vue + Java 21 业务系统合集

> 十个可在**本机单用户**运行的独立业务系统样板；不是复制第三方源码，也不是生产级 SaaS。

| 项目 | 功能闭环 | 本机地址 | 页面截图 |
| --- | --- | --- | --- |
| [shop](./shop/README.md) | 京东式商品检索、详情、购物车、库存校验与下单；电脑端 + 微信小程序 | http://127.0.0.1:8081 | [电脑端首页](./shop/screenshots/desktop-home.png) · [商品列表](./shop/screenshots/desktop-catalog.png) · [商品详情](./shop/screenshots/desktop-detail.png) · [购物车](./shop/screenshots/desktop-cart.png) · [订单结算](./shop/screenshots/desktop-checkout.png) · [小程序首页](./shop/screenshots/miniprogram-home.png) · [小程序详情](./shop/screenshots/miniprogram-detail.png) · [小程序购物车](./shop/screenshots/miniprogram-cart.png) |
| [manage](./manage/README.md) | 若依式通用后台：控制台、用户、角色、菜单、部门、日志、个人中心 | http://127.0.0.1:8082 | [控制台](./manage/screenshots/dashboard.png) · [用户](./manage/screenshots/users.png) · [角色](./manage/screenshots/roles.png) · [菜单](./manage/screenshots/menus.png) · [部门](./manage/screenshots/departments.png) · [日志](./manage/screenshots/logs.png) · [个人中心](./manage/screenshots/profile.png) |
| [crm](./crm/README.md) | 客户、商机、跟进和阶段流转 | http://127.0.0.1:8083 | [销售漏斗](./crm/screenshots/pipeline.png) |
| [oa](./oa/README.md) | 请假/报销草稿、提交与审批 | http://127.0.0.1:8084 | [审批](./oa/screenshots/approvals.png) |
| [finance](./finance/README.md) | 金融账户、流水、风险提醒与经营指标 | http://127.0.0.1:8085 | [总览](./finance/screenshots/overview.png) · [账户](./finance/screenshots/primary.png) · [流水](./finance/screenshots/secondary.png) |
| [health](./health/README.md) | 健康档案、随访预约与指标提醒 | http://127.0.0.1:8086 | [总览](./health/screenshots/overview.png) · [档案](./health/screenshots/primary.png) · [预约](./health/screenshots/secondary.png) |
| [wellness](./wellness/README.md) | 养生计划、课程运营与会员打卡 | http://127.0.0.1:8087 | [总览](./wellness/screenshots/overview.png) · [计划](./wellness/screenshots/primary.png) · [打卡](./wellness/screenshots/secondary.png) |
| [hospital](./hospital/README.md) | 患者、门诊预约、病区和医嘱工作台 | http://127.0.0.1:8088 | [总览](./hospital/screenshots/overview.png) · [患者](./hospital/screenshots/primary.png) · [预约](./hospital/screenshots/secondary.png) |
| [school](./school/README.md) | 学生、课程、出勤与校园事务管理 | http://127.0.0.1:8089 | [总览](./school/screenshots/overview.png) · [学生](./school/screenshots/primary.png) · [课程](./school/screenshots/secondary.png) |
| [access](./access/README.md) | 门点、人员、访客与通行事件管理 | http://127.0.0.1:8090 | [总览](./access/screenshots/overview.png) · [门点](./access/screenshots/primary.png) · [通行](./access/screenshots/secondary.png) |

## manage 页面预览

`manage` 参考 Gitee 上 RuoYi-Vue、RuoYi-Vue-Plus 的后台信息架构，以 Vue 3 + Java 21/Spring Boot 自主实现：

- [控制台](./manage/screenshots/dashboard.png)
- [用户管理](./manage/screenshots/users.png)
- [角色管理](./manage/screenshots/roles.png)
- [菜单管理](./manage/screenshots/menus.png)
- [部门管理](./manage/screenshots/departments.png)
- [操作日志](./manage/screenshots/logs.png)
- [个人中心](./manage/screenshots/profile.png)

每个项目只有在业务页面、后端接口、测试和实际运行截图都完成后，才会在合集文档中标记为已实现。

## shop 页面预览

- 电脑端：首页、商品列表、商品详情、购物车、订单结算：见 [shop/README.md](./shop/README.md)。
- 微信小程序：uni-app H5 运行截图覆盖首页、商品详情、购物车与订单信息；微信开发者工具产物位于 `shop/miniprogram/dist/build/mp-weixin/`。

## 直接运行

安装 **Java 21、Maven、Node.js 20.19+/22.12+、npm**，克隆仓库后在根目录执行其中一个项目的命令：

```powershell
./shop/run.ps1       # Windows PowerShell；其他项目换成 manage / crm / oa / finance / health / wellness / hospital / school / access
```

```sh
./shop/run.sh        # macOS / Linux；其他项目换成 manage / crm / oa / finance / health / wellness / hospital / school / access
```

脚本会运行 `npm ci`、构建前端、执行后端测试并打成单个可执行 jar，随后启动。打开上表地址即可使用，无需另开 Vite 开发服务器；首次运行要联网下载依赖。按 **Ctrl+C** 停止。若已有对应项目的 Vite 开发服务器，请先停止再执行脚本，避免 Windows 上 `npm ci` 遇到文件占用。十个项目端口互不冲突，可分别启动。

业务数据写在各项目的 `<项目>/data/<项目>.json`，数据文件不提交到 Git，**重启仍保留**。先停止服务，再复制 JSON 文件备份；要重置演示数据，停止服务后删除对应 JSON 文件并重启。若 JSON 损坏，服务拒绝启动而不是悄悄覆盖原数据。仅支持单进程访问同一数据文件，不支持多个实例共享写入。

## 验证

```powershell
./test-all.ps1       # Windows：四个前端构建 + 后端测试/打包 + jar 静态页检查
```

```sh
./test-all.sh        # macOS / Linux
```

Windows 还可运行 `./smoke-test.ps1`，启动十个真实 jar 验证页面、JS、API，并验证商城搜索、详情、下单后的进程重启数据；其临时数据及日志保留在系统临时目录。`test-all` 会构建十个项目和微信小程序产物。后端测试覆盖业务校验与重启后的 JSON 数据读取。单独测试可在各项目目录运行 `mvn -f backend/pom.xml test`。截图位于各项目 `screenshots/`，由运行页面取得。

**使用范围：**可在自己的电脑上体验、演示及改造；不含登录鉴权、服务端权限、正式支付、审计、数据库迁移及多实例并发保障。`server.address` 默认仅绑定 `127.0.0.1`，**不能直接用于公网或真实业务生产**，尤其 OA 审批和管理角色只是演示字段。

[业务系统调研与后续类型](./docs/business-map.md)区分参考项目、已实现及待建设。每个类型需完成业务闭环、测试和截图才列为已实现。联系邮箱：3174667330@qq.com；Git 签名仅配置在本仓库，不更改全局设置。
## 新增行业系统

当前已补充金融、健康、养生、医院、学校和门禁六个行业工作台。它们统一采用 Vue 3 + Java 21/Spring Boot，均提供总览、核心业务管理、记录处理页面、JSON API、前端构建和后端测试。

这些行业涉及资金、健康、医疗、未成年人、身份和出入权限等敏感数据，当前版本定位为本机演示与二次开发样板。正式上线前必须补充数据库、登录鉴权、细粒度 RBAC、操作审计、脱敏、加密、备份、合规评审和灾备方案。

完整行业清单见 [业务系统调研与后续类型](./docs/business-map.md)。
