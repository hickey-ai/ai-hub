# 截图工具（新增 13 个行业样板）

在仓库根目录，先完成 `./test-all.ps1`（Windows）或 `./test-all.sh`（macOS/Linux），再运行：

```sh
npm --prefix tools ci
npm --prefix tools exec -- playwright-core install chromium
npm --prefix tools run capture:sectors
```

需要 Java 21、Node.js 和 Chromium。脚本逐个启动本机 Java jar，访问真实页面并捕获总览、两个资源列表和新增表单各一张截图；遇到页面异常直接失败。可用环境变量 `CHROMIUM_PATH` 指向已有的 Chromium 可执行文件。请在干净的演示数据目录执行，避免把自己的数据截图提交到仓库；脚本不会验证业务交易流程或生产可用性。
