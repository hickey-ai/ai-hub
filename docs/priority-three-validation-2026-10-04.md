# 新增三项目验证 · 2026-10-04

本记录只对应新增的 `parcelstation`、`recycling`、`childcare`，不修改此前 [98 项验证快照](./project-validation-2026-10-03.md)，不把本机样板称为生产系统。

| 项目 | Maven 单测 | 前端与 jar | 浏览器 / API / 重启 | 专项规则 |
| :--- | ---: | :--- | :--- | :--- |
| [快递驿站](../parcelstation/README.md) | 3/3 | `npm ci`、Vite、Maven package 通过 | [PASS](./priority-three-validation.json) | 重复包裹编号/取件码、异常备注、签收终态、写盘失败回滚 |
| [再生资源回收](../recycling/README.md) | 3/3 | 同上 | [PASS](./priority-three-validation.json) | 重量与金额大于零、重复单号、结算终态、关联保护 |
| [托育机构](../childcare/README.md) | 3/3 | 同上 | [PASS](./priority-three-validation.json) | 异常交接备注、完成后禁止回退、异常恢复 |

浏览器测试命令：`CHROMIUM_PATH=<本机 Chrome 路径> node tools/verify-sectors.cjs parcelstation recycling childcare --report=docs/priority-three-validation.json`；验证真实 Vue 的两个列表、新增、检索、编辑、删除、API 400/404/409、Java 重启与 JSON 持久化。截图用 `node tools/capture-sectors.cjs --priority` 在隔离演示数据下拍摄每项四张。上述检查在 Windows 本机完成，**没有**对全部 101 项重新运行全量构建和浏览器验收，也没有设备、短信、支付、实名或生产安全验证。

额外护栏：`python -m unittest discover -s tools -p "test_*.py" -q` 为 14/14 通过；`./smoke-test.ps1 -PriorityOnly` 对三个新项目的打包页面、JS 资源和 API 做实际进程检查，3/3 通过。全量 `test-all.ps1` / `test-all.sh`、CI 项目发现与默认冒烟项目表已纳入 101 项，但本轮**未实际执行 101 项全量构建或远端 CI**。
