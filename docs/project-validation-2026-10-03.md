# 98 项逐项目验证记录 · 2026-10-03

本记录只描述实际执行的本机测试，不代表生产验收、安全认证或全部业务需求完成。命令在仓库根目录执行；Java 21、Maven、Node/npm、Chrome 已安装。

- `./test-all.ps1`：98/98 前端/小程序构建、Maven 测试、jar 静态资源检查通过；Surefire XML 共 137 个测试，0 失败、0 错误、0 跳过。另对改动后的 `crm`、`oa` 单独重建及测试通过。
- `./smoke-test.ps1`：98/98 项真实 Java 进程页面/静态 JS/API 冒烟通过（其中三个纯小程序项目验证 API，无电脑端页面）。
- `node tools/verify-sectors.cjs --report=...`：80/80 项逐一通过真实浏览器两类列表、新增/搜索/编辑/删除、API 校验、关联约束及 Java 重启持久化。原始批次 `testops`、`schedule` 因验收数据不满足必填 textarea/结束时间约束而失败；修正测试输入后两个项目独立重测通过。保留失败及重测 JSON，未涂改原始结果。
- `node tools/verify-special.cjs --report=...`：其余 18/18 项隔离服务/API 检查通过；深度因业务不同而异，见下表。六个旧记录台另通过浏览器增删改/刷新，shop 通过浏览器商品详情→购物车→演示订单/库存。

## 分项矩阵

**层级说明：**S=行业样板浏览器 CRUD/搜索、API 非法输入/关联、重启持久化；L=旧记录台浏览器 CRUD/刷新及专门的持久化/失败回滚单测；C=商城浏览器演示下单/库存及 Maven 搜索/订单测试；W=工具/工作台逐导航页面检查及服务/API；B=基础真实页面/API 和项目 Maven 测试；M=小程序构建页面产物/API（没有微信真机测试）。所有项目还运行了构建和 Maven 测试。

| 项目 | Maven 测试数 | 深测层级 | 真实服务冒烟 |
| :--- | ---: | :--- | :---: |
| [access](../access/README.md) | 3 | L · PASS | PASS |
| [accounting](../accounting/README.md) | 1 | S · PASS | PASS |
| [aesthetics](../aesthetics/README.md) | 1 | S · PASS | PASS |
| [agriculture](../agriculture/README.md) | 1 | S · PASS | PASS |
| [ai](../ai/README.md) | 1 | W · PASS | PASS |
| [auditfirm](../auditfirm/README.md) | 1 | S · PASS | PASS |
| [autosales](../autosales/README.md) | 1 | S · PASS | PASS |
| [b2b](../b2b/README.md) | 1 | S · PASS | PASS |
| [barber](../barber/README.md) | 4 | M · PASS | PASS |
| [bugtrack](../bugtrack/README.md) | 2 | S · PASS | PASS |
| [calibration](../calibration/README.md) | 1 | S · PASS | PASS |
| [carcare](../carcare/README.md) | 2 | S · PASS | PASS |
| [charging](../charging/README.md) | 1 | S · PASS | PASS |
| [civic](../civic/README.md) | 1 | S · PASS | PASS |
| [clinic](../clinic/README.md) | 1 | S · PASS | PASS |
| [cms](../cms/README.md) | 1 | S · PASS | PASS |
| [coldchain](../coldchain/README.md) | 1 | S · PASS | PASS |
| [community](../community/README.md) | 1 | S · PASS | PASS |
| [construction](../construction/README.md) | 1 | S · PASS | PASS |
| [contracts](../contracts/README.md) | 1 | S · PASS | PASS |
| [crawler](../crawler/README.md) | 2 | W · PASS | PASS |
| [crm](../crm/README.md) | 5 | B · PASS | PASS |
| [crossborder](../crossborder/README.md) | 1 | S · PASS | PASS |
| [culture](../culture/README.md) | 1 | S · PASS | PASS |
| [dental](../dental/README.md) | 1 | S · PASS | PASS |
| [dining](../dining/README.md) | 2 | M · PASS | PASS |
| [eldercare](../eldercare/README.md) | 1 | S · PASS | PASS |
| [elearning](../elearning/README.md) | 1 | S · PASS | PASS |
| [emergency](../emergency/README.md) | 1 | S · PASS | PASS |
| [energy](../energy/README.md) | 1 | S · PASS | PASS |
| [erp](../erp/README.md) | 1 | S · PASS | PASS |
| [events](../events/README.md) | 1 | S · PASS | PASS |
| [exam](../exam/README.md) | 1 | S · PASS | PASS |
| [finance](../finance/README.md) | 3 | L · PASS | PASS |
| [fishery](../fishery/README.md) | 1 | S · PASS | PASS |
| [fleet](../fleet/README.md) | 1 | S · PASS | PASS |
| [foodsafety](../foodsafety/README.md) | 1 | S · PASS | PASS |
| [forestry](../forestry/README.md) | 1 | S · PASS | PASS |
| [freshdelivery](../freshdelivery/README.md) | 1 | S · PASS | PASS |
| [gridops](../gridops/README.md) | 1 | W · PASS | PASS |
| [gym](../gym/README.md) | 1 | S · PASS | PASS |
| [health](../health/README.md) | 3 | L · PASS | PASS |
| [homeservice](../homeservice/README.md) | 1 | S · PASS | PASS |
| [hospital](../hospital/README.md) | 3 | L · PASS | PASS |
| [hospitality](../hospitality/README.md) | 1 | S · PASS | PASS |
| [hrm](../hrm/README.md) | 1 | S · PASS | PASS |
| [html](../html/README.md) | 2 | W · PASS | PASS |
| [insurance](../insurance/README.md) | 1 | S · PASS | PASS |
| [itops](../itops/README.md) | 1 | S · PASS | PASS |
| [kindergarten](../kindergarten/README.md) | 1 | S · PASS | PASS |
| [labbook](../labbook/README.md) | 2 | W · PASS | PASS |
| [laundry](../laundry/README.md) | 1 | S · PASS | PASS |
| [legal](../legal/README.md) | 1 | S · PASS | PASS |
| [library](../library/README.md) | 1 | S · PASS | PASS |
| [lis](../lis/README.md) | 1 | S · PASS | PASS |
| [logistics](../logistics/README.md) | 1 | S · PASS | PASS |
| [loyalty](../loyalty/README.md) | 1 | S · PASS | PASS |
| [maintenance](../maintenance/README.md) | 1 | S · PASS | PASS |
| [manage](../manage/README.md) | 4 | B · PASS | PASS |
| [manufacturing](../manufacturing/README.md) | 1 | S · PASS | PASS |
| [maritime](../maritime/README.md) | 1 | S · PASS | PASS |
| [mining](../mining/README.md) | 1 | S · PASS | PASS |
| [museum](../museum/README.md) | 1 | S · PASS | PASS |
| [oa](../oa/README.md) | 5 | B · PASS | PASS |
| [parenting](../parenting/README.md) | 2 | S · PASS | PASS |
| [parking](../parking/README.md) | 1 | S · PASS | PASS |
| [parkops](../parkops/README.md) | 1 | S · PASS | PASS |
| [petboarding](../petboarding/README.md) | 1 | S · PASS | PASS |
| [petcare](../petcare/README.md) | 1 | S · PASS | PASS |
| [petgrooming](../petgrooming/README.md) | 1 | S · PASS | PASS |
| [pharmacy](../pharmacy/README.md) | 1 | S · PASS | PASS |
| [photography](../photography/README.md) | 1 | S · PASS | PASS |
| [pos](../pos/README.md) | 1 | S · PASS | PASS |
| [procurement](../procurement/README.md) | 1 | S · PASS | PASS |
| [projectops](../projectops/README.md) | 1 | S · PASS | PASS |
| [property](../property/README.md) | 1 | S · PASS | PASS |
| [qms](../qms/README.md) | 1 | S · PASS | PASS |
| [realestate](../realestate/README.md) | 1 | S · PASS | PASS |
| [rehab](../rehab/README.md) | 1 | S · PASS | PASS |
| [rental](../rental/README.md) | 1 | S · PASS | PASS |
| [returns](../returns/README.md) | 1 | S · PASS | PASS |
| [sanitation](../sanitation/README.md) | 1 | S · PASS | PASS |
| [scenic](../scenic/README.md) | 1 | S · PASS | PASS |
| [schedule](../schedule/README.md) | 2 | S · PASS | PASS |
| [school](../school/README.md) | 3 | L · PASS | PASS |
| [selfshop](../selfshop/README.md) | 2 | M · PASS | PASS |
| [service](../service/README.md) | 1 | S · PASS | PASS |
| [shop](../shop/README.md) | 5 | C + M · PASS | PASS |
| [telecom](../telecom/README.md) | 1 | S · PASS | PASS |
| [testops](../testops/README.md) | 2 | S · PASS | PASS |
| [ticketops](../ticketops/README.md) | 2 | S · PASS | PASS |
| [training](../training/README.md) | 1 | S · PASS | PASS |
| [transit](../transit/README.md) | 1 | S · PASS | PASS |
| [veterinary](../veterinary/README.md) | 1 | S · PASS | PASS |
| [water](../water/README.md) | 1 | S · PASS | PASS |
| [wedding](../wedding/README.md) | 1 | S · PASS | PASS |
| [wellness](../wellness/README.md) | 3 | L · PASS | PASS |
| [wms](../wms/README.md) | 1 | S · PASS | PASS |

## 修复与不足

1. `finance`、`health`、`wellness`、`hospital`、`school`、`access` 原先仅内存保存且缺少编辑/删除。现增加本机 JSON 原子保存、重启恢复、错误文件拒绝启动、输入/资源校验（包含 null 请求体）、服务端 ID、更新/删除及失败写回滚，并补六套 JUnit 和浏览器回归；真实截图重拍三页。
2. `crm`、`oa` 后端已持久化，但界面仍误写“仅内存”；已修正提示并重新构建验证。README 中泛化的“每项目 JSON 文件”改为按业务项目说明，避免误导工具/静态项目。
3. **未解决的安全与验收缺口：**四个小程序 `npm ci` 各报告 45 个依赖漏洞（其中 20 个 high）；单独执行 `npm audit --omit=dev`，shop 仍报告 37 个（其中 17 个 high），需要审查升级链及复测。未做微信开发者工具/真机联调、真实支付、并发负载、多用户鉴权、备份恢复演练、无障碍/兼容性与金融/医疗/未成年人等法规认证。多数目录是本机单用户记录样板，不能直接部署处理敏感生产数据。

## 可复现证据

- [80 项最终深测结果](./deep-sectors-final-2026-10-03.json) · [原始 70 项批次（含两项失败）](./deep-sectors-rest-2026-10-03.json) · [两项修正重测](./deep-sectors-retest-2026-10-03.json)
- [18 项特殊项目最终结果](./deep-special-final-2026-10-03.json) · [六项旧记录台浏览器/截图](./deep-legacy-2026-10-03.json) · [CRM/OA 修正复测](./deep-crm-oa-2026-10-03.json)
- 构建原始日志 `test-all-2026-10-03.log`、冒烟原始日志 `smoke-test-confirm-2026-10-03.log`（脚本进程退出码 0）保留在本机且被 Git 忽略；复现脚本见 [tools/README](../tools/README.md)。


## 2026-10-04 增量验证（第 3 轮）

本轮按照“先补可直接使用的业务闭环，再扩展行业样板”的优先级，处理了三个高频项目的持久化边界：

- `manage`：用户新增、编辑、删除与操作日志改为同一份本地状态原子提交；写盘失败时用户状态、日志和下一个日志编号都保持不变。
- `shop`：保留按邮箱查询订单的演示接口，并明确该接口不是登录鉴权。
- `barber`：预约创建与取消改为先写入完整快照、成功后再替换内存；新增预约详情接口，要求预约编号和手机号同时匹配。

验证命令及结果：

```text
mvn -B -f manage/backend/pom.xml test  -> 4 tests, 0 failures, 0 errors
mvn -B -f shop/backend/pom.xml test    -> 5 tests, 0 failures, 0 errors
mvn -B -f barber/backend/pom.xml test  -> 4 tests, 0 failures, 0 errors
git diff --check                         -> 通过
真实 Java 进程（临时端口 18082/18081/18091） -> 首页、创建、查询/手机号隔离、重启持久化均通过
```

本轮仍未完成微信开发者工具/真机联调、真实支付、登录鉴权、并发部署及生产安全验收；因此不能将上述本机 JSON 演示项目宣称为生产系统。

## 2026-10-04 增量验证（第 4 轮）

本轮没有新增业务页面，改进的是“全量测试入口不会漏项目”的可复现性：

- `test-all.ps1` 与 `test-all.sh` 不再复制维护项目名称列表，而是扫描含 `backend/pom.xml` 的目录；按锁文件自动执行 4 个微信小程序构建、95 个 Vue 前端构建和 3 个纯后端打包。目录数量仍由 98 项护栏保护。
- 从仓库根目录执行 `./test-all.ps1`，98/98 项通过；109 个 Surefire 报告共 139 个测试，0 失败、0 错误、0 跳过。`veterinary` 也从 clean package 通过，未复现首次远端失败。
- 本轮只验证了构建、后端测试、jar 静态资源和小程序产物；不新增浏览器深测、微信开发者工具真机、真实支付、登录鉴权或生产安全结论。
- 推送后 GitHub Actions 完整运行 `37177762753` 共 99 个 job（discover + 98 个项目），99/99 成功；这仍只代表 CI 构建/测试/打包检查通过。
