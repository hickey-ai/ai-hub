# authcenter · 安全与身份中心

> 国家战略软件方向中的安全底座试点：为 ai-hub 业务项目提供可配置签发者、RSA JWT、公钥发布、受众隔离、登录与受保护的审计接口。它是可运行的演示与集成起点，不是等同于商用身份云或安全产品认证。

## 已实现

- `POST /api/auth/login`：演示账号登录并签发 RSA `RS256` Bearer 令牌。
- `GET /api/auth/me`：校验签名、签发者和过期时间后返回当前用户。
- `GET /.well-known/openid-configuration` 与 `GET /oauth2/jwks`：仅发布签发者和公钥元数据，供 Spring Resource Server 发现公钥；未实现 OIDC 完整协议。
- 受众白名单：默认 `ai-hub-manage`、`ai-hub-demo`，防止把一个应用令牌误用于未登记应用。
- `GET /api/audit`：仅管理员令牌可查看登录成功、失败和受众拒绝事件；不持久化。
- Vue 3 控制台：登录、会话、运行状态、发现文档和审计视图。

## 运行与集成

在仓库根目录运行 `./authcenter/run.ps1`（Windows）或 `./authcenter/run.sh`（macOS/Linux）；脚本完成依赖安装、前端构建、后端测试打包并启动。也可手动执行：

```powershell
cd authcenter
npm --prefix frontend ci
npm --prefix frontend run build -- --outDir ../backend/src/main/resources/static --emptyOutDir
mvn -f backend/pom.xml spring-boot:run
```

打开 `http://127.0.0.1:8182`。演示账号：`admin / admin123`；运营账号：`operator / operator123`。启动时生成内存 RSA 密钥，重启会使旧令牌失效。

以 `manage` 为例，在 `manage/backend/src/main/resources/application.properties` 增加：

```properties
aihub.auth.issuer-uri=http://127.0.0.1:8182
aihub.auth.audience=ai-hub-manage
```

然后调用 `POST /api/auth/login` 取得 `accessToken`，向管理后台 API 发送 `Authorization: Bearer <令牌>`。目前 `manage` 的浏览器页面会明确提示需要令牌；没有把演示登录伪装成完整授权码 + PKCE 流程。

## 真实页面截图

![安全与身份中心本机运行总览](./screenshots/overview.png)

![登录后会话与管理员审计](./screenshots/session.png)

## 边界与后续

当前为本机演示：用户和审计在内存中，密钥不持久化，`/api/auth/logout` 只记录事件并要求客户端清除令牌，**不撤销已签发 JWT**；未实现 MFA、密码重置、授权码 + PKCE、密钥轮换、跨实例会话撤销、设备信任和安全运营平台。生产部署前必须接入受控密钥管理、数据库、HTTPS、限流、审计留存和独立安全评审。

## 验证

```powershell
mvn -f backend/pom.xml test
npm --prefix frontend run build
```

5 项接口测试覆盖发现文档、公钥端点、登录与令牌校验、缺少/篡改令牌、运营员访问审计 403、错误密码、未登记受众，以及登出不撤销 JWT 的边界。另以本机 Chrome 验证登录后会话/审计显示、清除令牌调用后端及页面数据清理；不是跨浏览器或生产安全验收。
