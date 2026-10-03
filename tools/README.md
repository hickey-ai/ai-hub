# 截图工具（行业记录样板）

在仓库根目录，先完成 `./test-all.ps1`（Windows）或 `./test-all.sh`（macOS/Linux），再运行：

```sh
npm --prefix tools ci
npm --prefix tools exec -- playwright-core install chromium
npm --prefix tools run capture:sectors
```

需要 Java 21、Node.js 和 Chromium。脚本逐个启动本机 Java jar，访问真实页面并捕获总览、两个资源列表和新增表单各一张截图；遇到页面异常直接失败。可用环境变量 `CHROMIUM_PATH` 指向已有的 Chromium 可执行文件。请在干净的演示数据目录执行，避免把自己的数据截图提交到仓库；脚本不会验证业务交易流程或生产可用性。

如只需重新拍摄本轮三个平台的页面，先构建 testops、ticketops、bugtrack 的 jar，再运行 `node tools/capture-sectors.cjs --platforms`。每个项目生成总览、两个资源列表和表单截图。

本轮新增 10 个行业记录台的 jar 构建完成后，可运行 `node tools/capture-sectors.cjs --gaps`，逐项拍摄总览、两类列表与新增表单，并检查真实 Vue 表单创建。

## 逐项目运行验证（2026-10-03）

先从仓库根目录运行 `./test-all.ps1`，安装 `tools` 的依赖 `npm --prefix tools ci`；在本机安装 Chrome 时设置 `CHROMIUM_PATH`（如 Windows 的 `C:\Program Files\Google\Chrome\Application\chrome.exe`），然后执行：

```sh
node tools/verify-sectors.cjs --report=docs/deep-sectors.json
node tools/verify-special.cjs --report=docs/deep-special.json
```

`verify-sectors.cjs` 对自动发现的 80 个 `config.js` 行业样板逐项启用临时数据文件和真实 Java 进程，检查浏览器两列表导航、新增/搜索/编辑/删除、API 校验、关联约束及重启持久化。可追加项目名（如 `schedule`）只测该项。

`verify-special.cjs` 对其余 18 项逐一检查隔离 Java 服务、API 响应和真实页面；其中六项旧记录台另检查浏览器新增/编辑/删除/刷新持久化；四个小程序项目确认构建产物包含页面。此脚本对 `shop`、`manage`、`crm`、`oa` 等的业务深度仍以其 Maven 测试为主，**不声称完成真实支付、认证、消息推送、多用户或行业合规验收**。追加 `--screenshots` 会重拍六项旧记录台及 CRM/OA 的真实页面，切勿把自己的数据截图提交。

两脚本遇到失败会返回非零状态，并将每个项目的结果写入指定 JSON 报告；运行这些脚本需要已构建好的 jar 和浏览器。完整记录与未覆盖事项见 `docs/project-validation-2026-10-03.md`。
