# ai-hub 增长复盘记录

本记录区分公开可见的计数、实际执行的检查与待验证的增长假设；没有访问后台数据时，不把文档改动当成转化提升的证据。

## 2026-10-14：把首次启动入口移到截图旁

- **本轮触发：**2026-10-14 09:34（北京时间）；这是增长复盘，不计入已取消的 100 轮质量自查。
- **公开证据：**本轮读取 [GitHub 官方仓库 API](https://api.github.com/repos/hickey-ai/ai-hub)，返回 0 Star、0 Fork、0 个未关闭 Issue/PR；仓库描述仍只提到商城、管理后台与建设清单，topics 为空。仅作本轮快照，未获得访问量、来源、克隆量或 README 点击数据，不能计算转化率或归因。
- **同类项目参考：**读取 [RuoYi-Vue-Plus 官方 README](https://github.com/dromara/RuoYi-Vue-Plus/blob/master/README.md) 时，演示入口位于第 23 行、文档入口位于第 31 行。它提供了一个可核验的入口布局参考，不是该布局带来关注增长的因果证据。litemall README 本轮读取超时；[Gitee 仓库公开页](https://gitee.com/codingdb/ai-hub) 本轮 HTTP 读取返回 404，无法核对公开指标，不能据此判断仓库已删除或不可用。
- **观察：**改动前，中英文 README 的启动命令位于生态矩阵之后，访客看完项目截图仍需继续寻找运行入口。
- **假设：**将启动命令、端口与首次构建说明紧挨真实截图放置，可以减少首次体验的查找步骤；是否改善使用量或 Star 转化尚待观察。
- **修改：**将唯一的“快速开始”章节前移到截图下，保留导航锚点。代码块只启动商城一个服务，管理后台与安全中心放在可选项目表格中，避免复制多条阻塞式启动命令。中英文同步，列出三个端口、Windows/macOS/Linux 命令，并链接完整体验指南。移除了本轮初稿未经测量的“60 秒启动”表述，首次执行耗时仍取决于下载与构建。
- **验证：**`python -m unittest discover -s tools -p 'test_*.py' -q` 通过 17 项；现有启动命令/端口检查扩展到两份根 README。`python tools/generate_product_matrix.py --check` 通过，`git diff --check` 通过。仅验证文档与工具，没有重跑 102 个项目或测量真实转化。
- **下一次观察：**优先核对公开仓库描述和 topics 与当前定位是否一致；有仓库管理能力时再处理。继续记录可见 Star/Fork 和实际用户反馈，访问或克隆数据不可取得时保持缺口说明；不以单日计数或一次排版调整声称增长成功。
- **知识库状态：**首页及项目/产品索引的 `updated` 为 2026-08-21，超过 30 天，属于明显过期索引；本轮未找到 ai-hub 对应档案，未写入知识库。

## 2026-10-15：先检查工具，减少首次构建受阻

- **本轮触发：**2026-10-15 09:53（北京时间），仅执行本轮增长复盘；未恢复旧质量自查，也未继续提交待人工审阅的 Spring 报告。
- **公开快照：**[GitHub 官方 API](https://api.github.com/repos/hickey-ai/ai-hub) 返回 0 Star、0 Fork、0 个未关闭 Issue/PR，topics 仍为空，描述仍只提商城、后台与建设清单；[Gitee 官方 API](https://gitee.com/api/v5/repos/codingdb/ai-hub) 本轮可读取，返回 **1 Star、0 Fork、0 个未关闭 Issue**。这是首次拿到的 Gitee 数字，上轮网页 404 并不是 0，不能声称本日增加 1 Star。没有取得访问量、访问来源、克隆量、README 点击或留存数据，无法把计数归因于近期改动。
- **同类项目：**本轮读取 [litemall 官方 README](https://github.com/linlinjava/litemall/blob/master/README.md)，其快速启动按后端与不同前端拆成步骤，并说明真实微信登录/支付需另外配置。只参考说明的具体程度，不将其布局或热度当作增长因果证据。
- **观察与假设：**现有快速开始列了 Java、Maven、Node.js、npm 版本要求，却没有让新用户先核对终端实际使用的版本；即使安装 Java 21，Maven 仍可能使用另一套 JDK。提前给出只读版本检查与判读方式，可以让用户在下载依赖前发现环境差异；本轮没有真实用户失败工单，因此这仍是待验证的使用障碍假设。
- **实际修改：**在中英文快速开始的启动命令表前新增 `java -version`、`mvn -v`、`node --version`、`npm --version`；说明 Java 与 Maven 的 JDK 都应为 21，命令不可用时先修正本机工具配置，并明确版本检查不代表应用验收通过。只改两份指南与此记录，不新增应用、依赖、遥测或系统配置。
- **执行限制：**最初拟同时补充下载代码步骤的写入命令被自动审批拒绝，仅返回 `blocked by policy`，没有更细原因，未执行该修改；本轮改为范围更小的只读工具检查说明，该写入已获执行。没有绕过审批或请求额外权限。
- **验证：**本机实际执行四条只读命令：Java 21，Maven 3.6.3 且使用 Java 21，Node.js v24.14.1，npm 11.11.0；只验证当前 Windows 环境，未运行 macOS/Linux。`python -m unittest discover -s tools -p 'test_*.py' -q`：17 项通过；`python tools/generate_product_matrix.py --check` 通过；`git diff --check` 通过。没有重跑 102 项构建，也没有证明转化提升。
- **下一次观察：**区分首次获得的 Gitee 计数与后续真实变化；继续关注启动环境相关用户反馈。有可用仓库管理能力时再核对 About/topics，当前没有可用 `gh` 命令；不把单日排版或说明改动当作获星的原因。
- **知识库：**首页、项目及产品索引仍为 2026-08-21，超过 30 天，明显过期；目录文件名未找到对应 ai-hub 档案，本轮不写入知识库。

## 2026-10-16：减少首次体验的目录判断

- **本轮触发：**2026-10-16 09:31（北京时间）；继续围绕真实流量与首次激活，不恢复已取消的质量自查。
- **公开证据：**GitHub API 仍返回 0 Star、0 Fork、0 个未关闭 Issue/PR，topics 为空；Gitee API 仍返回 1 Star、0 Fork、0 个未关闭 Issue。昨日在 GitHub Traffic 页面看到最近 14 天 **14 次页面访问、2 个独立访客、2,404 次克隆、198 个独立克隆者**；本轮没有把克隆者当真实用户，也没有重复声称它们全部来自 CI。Traffic 页面证据保存在仓库外的 `E:\ai\upstream-reviews\ai-hub-growth-20261015`。
- **观察与假设：**用户从仓库根目录开始时，仍需先理解 `shop` 子目录才能运行第一个页面；根目录的一个可执行演示入口可以减少目录判断和命令复制错误。它不能解决曝光不足，也不代表会增加 Star。
- **实际修改：**新增 `run-demo.ps1` 与 `run-demo.sh`，从仓库根目录转发到商城的现有启动脚本；中英文 README 的首个命令改为根目录演示入口；文档测试增加脚本存在性与引用检查。没有新增依赖、遥测、网络服务或业务功能。
- **验证：**`python -m unittest discover -s tools -p 'test_*.py' -q` 通过 **18 项**；`python tools/generate_product_matrix.py --check` 通过；`git diff --check` 通过。`run-demo.sh` 在当前 Windows PowerShell 中没有可用的 `sh` 命令，因此未运行 shell 语法检查；脚本内容保持 POSIX `sh` 语法，未声称 Linux 执行已验证。未运行完整 102 项构建。
- **下一次观察：**重新查看 Traffic 的页面访问、独立访客和来源；如果访问仍低，优先处理 GitHub About/topics、许可证和一个可下载/可直接体验的旗舰版本，而不是继续添加行业目录。
- **知识库：**知识库首页、项目与产品索引的 `updated` 仍为 2026-08-21，已过期；本轮未找到 ai-hub 对应知识库档案，未写入。
