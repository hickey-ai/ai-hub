# ai-hub 文档导航 / Documentation guide

**[中文首页](../README.md) · [English home](../README.en.md)**

文档分工：README 是入口；分类目录回答“到哪里找”；截图索引给出逐项目页面证据；验证记录区分构建、浏览器流程和真实设备验收；调研清单是历史参考，不表示功能已经完整实现。

| 文档 | 用途 | 范围 |
| :--- | :--- | :--- |
| [项目分类目录](./project-catalog.md) / [English](./project-catalog.en.md) | 12 类、101 项的主分类、功能入口、端口 | 每个项目恰好归类一次；小程序为交付形态 |
| [产品矩阵](./product-matrix.md) / [English](./product-matrix.en.md) | 12 个业务场景 × 工信部 4 个软件业统计方向，逐项对应 101 个项目 | 未交付方向列出验收标准，不冒称已实现 |
| [截图索引](./screenshots.md) / [English](./screenshots.en.md) | 每项 README 与现有截图的直接链接 | 截图不等于真机或生产验收 |
| [架构图](./architecture.svg) | 各目录独立的 Web/Java/API/本机数据链路 | 本机演示，不是分布式生产拓扑 |
| [鉴权现状与接入试点](./auth-integration.md) | 101 项鉴权盘点、manage 的 JWT 配置与未完成项 | 仅 manage API 试点，非全局单点登录 |
| [新增三项目验证](./priority-three-validation-2026-10-04.md) | 快递驿站、再生资源回收、托育机构的测试证据 | 不等于全量 101 项重测或生产验收 |
| [逐项目验证](./project-validation-2026-10-03.md) | 98 行逐项目测试范围和原始记录 | 历史验证快照；后续轮次见账本 |
| [持续自查账本](./quality-loop.md) | 100 轮中已实际执行的轮次、证据与遗留 | 不把构建成功当作所有功能验收 |
| [业务系统调研](./business-map.md) | 参考方向与能力缺口 | 2026-10-01 抽样调研，非项目现状目录 |
| [公司官网调研](./company-research.md) | 外部产品信息与原创实现边界 | 仅供研究，不构成复制授权 |

**分类规则：**按核心用户任务而非代码形态分配唯一主分类。商城归零售、科研记录归教育科研、网格事件归公共服务；Web/微信小程序、业务深度、验收级别用额外说明表示，不重复计数。新增项目时同步更新中英文分类、截图索引和测试记录，运行 `python -m unittest discover -s tools -p 'test_*.py'` 检查链接及覆盖。

**English:** Use the English catalog and screenshot index above. Build checks, browser checks and device/production acceptance are different levels of evidence. Read each project's README before using it; do not expose local demos to the public internet.
