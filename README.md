# ai-hub · Vue + Java 21 业务系统合集

> 四个可在**本机单用户**运行的独立业务系统样板；不是复制第三方源码，也不是生产级 SaaS。

| 项目 | 功能闭环 | 本机地址 | 页面截图 |
| --- | --- | --- | --- |
| [shop](./shop/README.md) | 京东式商品检索、详情、购物车、库存校验与下单；电脑端 + 微信小程序 | http://127.0.0.1:8081 | [电脑端首页](./shop/screenshots/desktop-home.png) · [商品列表](./shop/screenshots/desktop-catalog.png) · [商品详情](./shop/screenshots/desktop-detail.png) · [购物车](./shop/screenshots/desktop-cart.png) · [订单结算](./shop/screenshots/desktop-checkout.png) · [小程序首页](./shop/screenshots/miniprogram-home.png) · [小程序详情](./shop/screenshots/miniprogram-detail.png) · [小程序购物车](./shop/screenshots/miniprogram-cart.png) |
| [manage](./manage/README.md) | 用户增删改查、角色查看和仪表盘 | http://127.0.0.1:8082 | [工作台](./manage/screenshots/dashboard.png) |
| [crm](./crm/README.md) | 客户、商机、跟进和阶段流转 | http://127.0.0.1:8083 | [销售漏斗](./crm/screenshots/pipeline.png) |
| [oa](./oa/README.md) | 请假/报销草稿、提交与审批 | http://127.0.0.1:8084 | [审批](./oa/screenshots/approvals.png) |

## shop 页面预览

- 电脑端：首页、商品列表、商品详情、购物车、订单结算：见 [shop/README.md](./shop/README.md)。
- 微信小程序：uni-app H5 运行截图覆盖首页、商品详情、购物车与订单信息；微信开发者工具产物位于 `shop/miniprogram/dist/build/mp-weixin/`。

## 直接运行

安装 **Java 21、Maven、Node.js 20.19+/22.12+、npm**，克隆仓库后在根目录执行其中一个项目的命令：

```powershell
./shop/run.ps1       # Windows PowerShell；其他项目换成 manage / crm / oa
```

```sh
./shop/run.sh        # macOS / Linux；其他项目换成 manage / crm / oa
```

脚本会运行 `npm ci`、构建前端、执行后端测试并打成单个可执行 jar，随后启动。打开上表地址即可使用，无需另开 Vite 开发服务器；首次运行要联网下载依赖。按 **Ctrl+C** 停止。若已有对应项目的 Vite 开发服务器，请先停止再执行脚本，避免 Windows 上 `npm ci` 遇到文件占用。四个项目端口互不冲突，可分别启动。

业务数据写在各项目的 `<项目>/data/<项目>.json`，数据文件不提交到 Git，**重启仍保留**。先停止服务，再复制 JSON 文件备份；要重置演示数据，停止服务后删除对应 JSON 文件并重启。若 JSON 损坏，服务拒绝启动而不是悄悄覆盖原数据。仅支持单进程访问同一数据文件，不支持多个实例共享写入。

## 验证

```powershell
./test-all.ps1       # Windows：四个前端构建 + 后端测试/打包 + jar 静态页检查
```

```sh
./test-all.sh        # macOS / Linux
```

Windows 还可运行 `./smoke-test.ps1`，启动四个真实 jar 验证页面、JS、API，并验证商城搜索、详情、下单后的进程重启数据；其临时数据及日志保留在系统临时目录。`test-all` 还会构建微信小程序产物。CI 对四个项目运行同样的构建测试；后端测试覆盖业务校验与重启后的 JSON 数据读取。单独测试可在各项目目录运行 `mvn -f backend/pom.xml test`。截图位于各项目 `screenshots/`，由运行页面取得。

**使用范围：**可在自己的电脑上体验、演示及改造；不含登录鉴权、服务端权限、正式支付、审计、数据库迁移及多实例并发保障。`server.address` 默认仅绑定 `127.0.0.1`，**不能直接用于公网或真实业务生产**，尤其 OA 审批和管理角色只是演示字段。

[业务系统调研与后续类型](./docs/business-map.md)区分参考项目、已实现及待建设。每个类型需完成业务闭环、测试和截图才列为已实现。联系邮箱：3174667330@qq.com；Git 签名仅配置在本仓库，不更改全局设置。
