# 鉴权现状与接入边界（2026-10-04）

## 现状盘点

仓库有 102 个独立 Spring Boot 后端，均默认绑定 `127.0.0.1`；新增 `authcenter` 作为本机安全与身份底座试点；其余业务项目仍没有统一接入的 Spring Security 资源服务器依赖或服务端角色权限检查。`manage` 的“角色/菜单/权限标识”是展示数据，此前 `/api/profile` 和操作日志操作者是演示身份，**不是登录及真实身份审计**；本轮在鉴权模式下用令牌 `sub` 标记个人资料及后续写操作；`authcenter` 自身提供登录成功/失败及受众拒绝的内存审计。本轮 `manage` 仍是资源服务器试点，另有 `authcenter` 作为可运行签发者；其余 **100 个业务项目仍未接入**。绑定回环地址不等于鉴权，不要部署公网或录入真实敏感数据。

## manage 接入方式（仅 API）

`authcenter` 已公开发现文档与 JWKS，可直接作为本机 `manage` 试点的签发者；接入外部鉴权中心时仍须公开符合 OIDC 发现/JWKS 的签发者地址，使用签名 JWT，签发者 `iss` 与配置严格一致，令牌 `aud` 包含 `ai-hub-manage`。在启动前设置环境变量 `AIHUB_AUTH_ISSUER_URI`，或者 JVM 启动参数 `--aihub.auth.issuer-uri=https://idp.example/realms/ai-hub`；默认受众为 `ai-hub-manage`，中心使用不同受众时再设 `AIHUB_AUTH_AUDIENCE` / `--aihub.auth.audience=...`。**不是任意鉴权中心 URL 都能直接兼容**，需要中心侧注册受众、签发 JWT、提供发现文档和公钥。

```powershell
$env:AIHUB_AUTH_ISSUER_URI = 'https://idp.example/realms/ai-hub'
./manage/run.ps1
# 取得该中心合法 access token 后：
Invoke-RestMethod http://127.0.0.1:8082/api/users -Headers @{ Authorization = 'Bearer <access-token>' }
```

- 有签发者配置时，`/api/**` 一律要求有效 Bearer JWT；校验签名、签发者、有效期和受众；无/坏令牌返回 401。签发者不可用或配置错误不应退回演示模式。静态页面仍可打开，但 Vue 管理界面**尚无 OIDC 登录/令牌获取与刷新流程**，因此不能把此配置称为浏览器单点登录已完成。
- 未设置签发者时保留此前本机免登录演示行为。不要把它当生产安全默认值；直接对公网开放仍危险。
- 只做**身份认证**，任何有效且受众匹配的令牌都能调用所有 manage API；尚无角色、租户、数据级授权、登录事件审计、密码管理或登出。启用鉴权后，新写入的操作日志标记 JWT `sub`，原有演示/历史记录无法追溯真实身份。登录页面、Authorization Code + PKCE、角色/权限、跨项目统一身份以及 100 个业务项目迁移需要分阶段设计和测试。
- access token 请勿写入仓库、URL、截图或浏览器持久存储。外部中心生产连接应使用 HTTPS；配置和密钥由部署环境管理。

## 整理与测试策略

先区分完整流程项目、行业记录样板、纯小程序及工具项目；逐项目建立“公开静态资源 / 匿名业务 API / 需认证业务 API / 角色与数据隔离”的接口清单。复用经过验证的安全配置而非复制 `manage` 文件到 100 个项目：将来采用共享 starter 和版本化的 OAuth2 资源服务器配置，并配套 CI 401、错误签发者/受众、无权限 403、浏览器登录及真机回归。当前不能宣称一项配置已经保护整个生态矩阵。

本轮验证：`mvn -B -f authcenter/backend/pom.xml test`（安全底座接口与边界）；`mvn -B -f manage/backend/pom.xml test` 共 7 项，包含默认演示流程、启用鉴权时的未登录 GET/POST、错误签名/签发者/受众、过期/空主体拒绝、有效令牌访问及写入后主体标记；Vue 构建、jar 打包与根工具 16 项测试通过；2026-10-07 本机 `./test-all.ps1` 全量构建/打包 102/102 通过，四个小程序构建通过；118 份 Surefire 报告共 160 项测试，0 失败、0 错误、0 跳过。打包后的 `manage` jar 在本机临时端口冒烟：`GET /api/users` 与 `GET /` 均为 200。此处的全量是构建与自动测试，不是逐页面、微信真机或真实身份中心联调。本机 `authcenter` → `manage` 真实进程联调：签发受众为 `ai-hub-manage` 的令牌后，`GET /api/users` 无令牌返回 **401**、合法令牌返回 **200**、篡改令牌返回 **401**（身份中心 8182 / 管理后台临时端口 8282）。这是 API 校验的专项冒烟，不是浏览器单点登录，也不覆盖角色/数据级权限。本机 Chrome 页面验证登录后展示管理员审计、清除令牌调用 `/api/auth/logout`、清除页面及会话存储中的令牌与审计列表；该登出**不会撤销已签发令牌**。外部真实身份中心的端到端登录**未验证**。
