<div align="center">

**[简体中文](./README.md) · [English](./README.en.md)**

<img src="./docs/ai-hub-hero.gif" alt="ai-hub：让更多职业的软件创意变成可运行的项目" width="1200" />

### 把每一种真实需求，变成看得见的软件。

**87 个独立可运行项目** &nbsp;·&nbsp; **4 个微信小程序构建** &nbsp;·&nbsp; **Vue 3 + Java 21** &nbsp;·&nbsp; **本机优先**

[探索生态矩阵](#-ai-hub-生态矩阵) · [一键体验](#-快速开始) · [全部项目](#-项目宇宙) · [页面截图](#-真实页面) · [架构与边界](#-系统架构)

</div>

> **ai-hub 是持续生长的业务软件合集。** 从有完整交互流程的精选项目，到覆盖更多职业的轻量记录样板，再到小程序和创意工具：选一个目录，运行、查看页面、验证功能，然后按自己的业务继续改造。**“让每种职业都有软件”是愿景，不是已经覆盖所有行业流程的承诺。**

## ✨ 从这里进入

<table>
<tr>
<td width="25%" align="center"><a href="./shop/README.md"><img src="./shop/screenshots/desktop-home.png" alt="shop 电脑端商城首页截图" width="100%" /></a><br/><b>01 · 交易体验</b><br/><sub>shop · 商品 / 购物车 / 订单</sub></td>
<td width="25%" align="center"><a href="./manage/README.md"><img src="./manage/screenshots/dashboard.png" alt="manage 管理后台截图" width="100%" /></a><br/><b>02 · 企业运营</b><br/><sub>manage · 控制台 / 用户 / 角色</sub></td>
<td width="25%" align="center"><a href="./labbook/README.md"><img src="./labbook/screenshots/overview.png" alt="labbook 实验记录截图" width="100%" /></a><br/><b>03 · 科研记录</b><br/><sub>labbook · 实验 / 修订 / 导出</sub></td>
<td width="25%" align="center"><a href="./scenic/README.md"><img src="./scenic/screenshots/overview.png" alt="scenic 景区管理记录样板截图" width="100%" /></a><br/><b>04 · 行业样板</b><br/><sub>scenic · 景点 / 游客记录</sub></td>
</tr>
</table>

<div align="center"><sub>以上均为实际运行页面，而非效果图。点击图片查看项目说明与更多截图。</sub></div>

## 🧩 ai-hub 生态矩阵

![ai-hub 生态矩阵：业务流程、小程序触点、行业记录样板、创意工具，以及独立本机技术链路](./docs/ecosystem-matrix.svg)

| 生态层 | 代表项目 / 入口 | 你能体验到什么 | 当前边界 |
| :--- | :--- | :--- | :--- |
| **业务流程应用** | [shop](./shop/README.md) · [manage](./manage/README.md) · [labbook](./labbook/README.md) | 商城购买链路、管理控制台、实验记录等各有侧重的独立体验 | 功能深度不一致；不等于生产级全功能产品 |
| **移动端触点** | [shop](./shop/README.md) · [barber](./barber/README.md) · [dining](./dining/README.md) · [selfshop](./selfshop/README.md) | 四个项目的 uni-app 微信小程序构建，连接各自 Java API | **4 个构建目标包含在 87 个项目内**，不是额外四套系统 |
| **行业记录样板** | [scenic](./scenic/README.md) · [realestate](./realestate/README.md) · [health](./health/README.md) · [erp](./erp/README.md) · [更多行业 ↓](#-项目宇宙) | 行业档案、关联记录、搜索和表单；用来讨论具体业务需求 | 多数为本机单用户记录演示，不含真实票务、诊断、交易或监管流程 |
| **创意与效率工具** | [ai](./ai/README.md) · [html](./html/README.md) · [crawler](./crawler/README.md) | CLI / skill 工作台、HTML 小游戏与黄页样板、公开页面抓取 | 本机工具与演示，不是托管 AI 平台或大规模爬虫服务 |

**共同的交付方式，不是共享单体服务：**各项目独立目录、独立端口、独立数据文件；Vue 3 页面经构建由 Java 21 / Spring Boot 服务提供，配有测试脚本和实际截图。[看完整架构](#-系统架构) · [看使用边界](#-验证与使用边界)

<details>
<summary><b>展开 ai-hub 业务星图动效</b></summary>
<br/>
<img src="./docs/ai-hub-constellation.gif" alt="ai-hub 业务领域星图动效" width="1200" />
</details>

> 💬 **你的行业还缺合适的软件？** 可加 QQ **3174667330** 讨论定制开发：告诉我们业务场景、使用人数与期待的流程。现有项目是可以运行和验证的起点，正式上线需要进一步设计、开发与验收。

## ⚡ 快速开始

> 需要 **Java 21、Maven、Node.js 20.19+/22.12+ 和 npm**；首次运行会联网下载依赖。以下命令会安装前端依赖、构建页面、运行后端测试、打包并启动服务。

<table>
<tr><th>Windows PowerShell</th><th>macOS / Linux</th></tr>
<tr><td><code>./shop/run.ps1</code></td><td><code>./shop/run.sh</code></td></tr>
<tr><td colspan="2">浏览器打开 <code>http://127.0.0.1:8081</code>。想体验其它项目？把 <code>shop</code> 换成下方项目名，并打开对应端口。按 <code>Ctrl+C</code> 停止。</td></tr>
</table>

也可以从 [科研实验记录](./labbook/README.md) 开始：运行 `./labbook/run.ps1`（Windows）或 `./labbook/run.sh`（macOS/Linux），打开 `http://127.0.0.1:8097`。**这些是本机单用户演示系统，不可直接部署到公网。**

## 🪐 项目宇宙

每个项目名称都可进入独立 README，了解功能、运行方式与完整截图。地址默认仅在当前电脑可访问。

| 场景 | 项目 | 已实现能力 | 本机端口 |
| :--- | :--- | :--- | :--- |
| **交易与服务** | [shop · 商城](./shop/README.md) | 电脑端与微信小程序、商品检索、详情、购物车、库存校验与下单 | `8081` |
| | [barber · 理发](./barber/README.md) | 小程序服务、设计师、时段预约与取消 | `8091` |
| | [dining · 点餐](./dining/README.md) | 小程序桌号、菜品、餐篮、备注与订单 | `8092` |
| | [selfshop · 自助购物](./selfshop/README.md) | 小程序搜索、扫码入口、库存、购物袋与结算 | `8093` |
| **企业运营** | [manage · 通用后台](./manage/README.md) | 若依式控制台、用户、角色、菜单、部门、日志与个人中心 | `8082` |
| | [crm · 客户关系](./crm/README.md) | 客户、商机、跟进和阶段流转 | `8083` |
| | [oa · 协同办公](./oa/README.md) | 请假/报销草稿、提交与审批 | `8084` |
| | [finance · 金融工作台](./finance/README.md) | 账户、流水、风险提醒与经营指标 | `8085` |
| **行业场景** | [health · 健康](./health/README.md) | 健康档案、随访预约与指标提醒 | `8086` |
| | [wellness · 养生](./wellness/README.md) | 会员、养生计划、课程和打卡 | `8087` |
| | [hospital · 医院](./hospital/README.md) | 患者、门诊预约、病区与医嘱工作台 | `8088` |
| | [school · 学校](./school/README.md) | 学生、课程、出勤与校园事务 | `8089` |
| | [access · 门禁](./access/README.md) | 门点、人员、访客与通行事件 | `8090` |
| **生活与研究** | [schedule · 日程](./schedule/README.md) | 日程、分类、完成状态与未来安排 | `8094` |
| | [carcare · 汽车养护](./carcare/README.md) | 车辆、里程、维修保养与费用记录 | `8095` |
| | [parenting · 养娃](./parenting/README.md) | 成长档案、日常与里程碑记录 | `8096` |
| | [labbook · 科研实验](./labbook/README.md) | 八个学科模板、实验、样本、修订历史与 JSON 导出 | `8097` |
| **AI 与创意** | [ai · AI 工作台](./ai/README.md) | CLI 工具箱、Skill 技能库、Prompt 工作台与安全命令预览 | `8098` |
| | [html · HTML 创意工坊](./html/README.md) | HTML/CSS/JS 实时预览、模板、小程序式小游戏与虚构黄页 | `8099` |
| | [crawler · 网页抓取](./crawler/README.md) | 公开网页单页抓取、摘要、链接与历史 | `8100` |
| **供应链与制造** | [erp · 进销存](./erp/README.md) | 商品档案、出入库流水、关联与检索 | `8101` |
| | [manufacturing · 制造](./manufacturing/README.md) | 物料档案、生产工单、进度记录 | `8102` |
| | [logistics · 物流](./logistics/README.md) | 车辆档案、运输运单和状态记录 | `8103` |
| **地产与农业** | [property · 物业房产](./property/README.md) | 房源档案、报修事项与处理记录 | `8104` |
| | [agriculture · 农业养殖](./agriculture/README.md) | 地块档案、农事活动记录 | `8105` |
| | [construction · 建筑工程](./construction/README.md) | 工程项目、现场施工日志 | `8106` |
| **服务行业** | [hospitality · 酒店旅游](./hospitality/README.md) | 客房档案、预订记录与房态 | `8107` |
| | [hrm · 人力资源](./hrm/README.md) | 员工档案、假勤申请与状态 | `8108` |
| | [service · 售后工单](./service/README.md) | 客户档案、服务工单与跟进 | `8109` |
| | [energy · 能源环保](./energy/README.md) | 监测设备、巡检读数与结论 | `8110` |
| | [legal · 法律服务](./legal/README.md) | 委托档案、案件进度与下一步行动 | `8111` |
| | [culture · 文化体育](./culture/README.md) | 场馆空间、活动安排与状态 | `8112` |
| | [community · 社区公益](./community/README.md) | 服务项目、需求登记与处理记录 | `8113` |
| **内容与交易** | [cms · 内容发布](./cms/README.md) | 内容栏目、稿件记录的本机记录样板 | `8114` |
|  | [wms · 仓储管理](./wms/README.md) | 仓库库位、作业记录的本机记录样板 | `8115` |
|  | [b2b · 批发采购](./b2b/README.md) | 供应商档案、采购订单的本机记录样板 | `8116` |
| **照护与服务** | [eldercare · 养老护理](./eldercare/README.md) | 长者档案、照护记录的本机记录样板 | `8117` |
|  | [pharmacy · 药店台账](./pharmacy/README.md) | 药品目录、批次记录的本机记录样板 | `8118` |
|  | [insurance · 保险服务](./insurance/README.md) | 保单档案、理赔进度的本机记录样板 | `8119` |
|  | [rental · 物品租赁](./rental/README.md) | 出租设备、租借记录的本机记录样板 | `8120` |
|  | [homeservice · 上门服务](./homeservice/README.md) | 客户档案、上门任务的本机记录样板 | `8121` |
| **资源与环境** | [water · 水务设施](./water/README.md) | 设施档案、巡检记录的本机记录样板 | `8122` |
|  | [sanitation · 城市环卫](./sanitation/README.md) | 作业线路、清运记录的本机记录样板 | `8123` |
|  | [mining · 矿山作业](./mining/README.md) | 作业区域、班次记录的本机记录样板 | `8124` |
|  | [forestry · 林地巡护](./forestry/README.md) | 林区档案、巡护记录的本机记录样板 | `8125` |
|  | [fishery · 水产养殖](./fishery/README.md) | 养殖池塘、投喂记录的本机记录样板 | `8126` |
| **基础设施与公共事务** | [telecom · 通信设施](./telecom/README.md) | 通信站点、维护任务的本机记录样板 | `8127` |
|  | [itops · IT 运维](./itops/README.md) | 设备资产、故障记录的本机记录样板 | `8128` |
|  | [civic · 公共服务](./civic/README.md) | 服务事项、办理登记的本机记录样板 | `8129` |
| **质量与内部支持** | [testops · 测试管理](./testops/README.md) | 测试用例与手工执行结果记录（本机样板） | `8165` |
|  | [ticketops · 内部工单](./ticketops/README.md) | 队列、优先级、处理人与状态（本机样板） | `8166` |
|  | [bugtrack · 缺陷跟踪](./bugtrack/README.md) | 项目、缺陷、复现步骤与验证状态（本机样板） | `8167` |

## 🧪 测试、工单与缺陷 · 新增 3 个平台

这三个平台是**独立的本机单用户工作台**，不是同一套账号下联动的 SaaS。测试平台记录用例与人工执行结果；内部工单记录队列、指派和处理进度；缺陷平台记录问题、复现步骤和状态。已有 [service 售后工单](./service/README.md) 面向客户售后，不与内部工单混为一谈。

<table><tr>
<td width="33%" align="center"><a href="./testops/README.md"><img src="./testops/screenshots/overview.png" alt="测试管理平台真实页面" width="100%" /></a><br/><b>testops · 测试管理</b><br/><sub>用例 → 手工执行记录</sub></td>
<td width="33%" align="center"><a href="./ticketops/README.md"><img src="./ticketops/screenshots/overview.png" alt="内部工单平台真实页面" width="100%" /></a><br/><b>ticketops · 内部工单</b><br/><sub>队列 → 工单处理</sub></td>
<td width="33%" align="center"><a href="./bugtrack/README.md"><img src="./bugtrack/screenshots/overview.png" alt="缺陷跟踪平台真实页面" width="100%" /></a><br/><b>bugtrack · 缺陷跟踪</b><br/><sub>项目 → 问题记录</sub></td>
</tr></table>

例如运行 `./testops/run.ps1`（Windows）或 `./testops/run.sh`（macOS/Linux），打开 `http://127.0.0.1:8165`。另两个项目使用各自目录和上方端口。**不提供自动化测试执行、CI 集成、跨项目同步、多用户协作、SLA 计时或通知。**

## 🆕 新增 35 个细分行业入口

景区管理、房产交易及出行、医疗、教育、宠物、门店、企业和供应链等方向均可单独启动。**每个新增项目是可操作的本机单用户记录样板**：两类关联资源、字段校验、检索／增删改、本地 JSON 持久化；不是完整行业生产系统。特别是景区票务／容量控制、房产交易／合同签署、医疗检验、停车计费、跨境合规和真实收付款均未实现。

| **出行、园区与景区** | [parking · 停车场](./parking/README.md) | 停车区域／停车记录 · 车位与进出场登记（本机记录样板） | `8130` |
|  | [charging · 充电站](./charging/README.md) | 充电站点／充电记录 · 充电站点与充电登记（本机记录样板） | `8131` |
|  | [parkops · 园区运营](./parkops/README.md) | 园区档案／巡检记录 · 园区设施与巡检台账（本机记录样板） | `8132` |
|  | [fleet · 车队管理](./fleet/README.md) | 车队车辆／行车任务 · 车队与行车任务（本机记录样板） | `8133` |
|  | [scenic · 景区管理](./scenic/README.md) | 景点档案／游览登记 · 景点资源与游览登记（本机记录样板） | `8134` |
| **医疗服务细分** | [clinic · 诊所管理](./clinic/README.md) | 诊室档案／预约登记 · 诊室与预约登记（本机记录样板） | `8135` |
|  | [dental · 口腔门诊](./dental/README.md) | 牙椅档案／就诊登记 · 牙椅与就诊排期（本机记录样板） | `8136` |
|  | [aesthetics · 医美服务](./aesthetics/README.md) | 服务项目／咨询登记 · 项目与咨询跟进（本机记录样板） | `8137` |
|  | [rehab · 康复训练](./rehab/README.md) | 训练方案／训练记录 · 方案与训练记录（本机记录样板） | `8138` |
|  | [lis · 检验台账](./lis/README.md) | 检验项目／样本登记 · 项目与样本流转演示（本机记录样板） | `8139` |
| **教育与学习** | [kindergarten · 幼儿园管理](./kindergarten/README.md) | 班级档案／班级活动 · 班级与活动记录（本机记录样板） | `8140` |
|  | [training · 培训机构](./training/README.md) | 培训课程／报名登记 · 课程与报名记录（本机记录样板） | `8141` |
|  | [elearning · 在线学习](./elearning/README.md) | 线上课程／学习进度 · 课程与学习记录（本机记录样板） | `8142` |
|  | [exam · 考试管理](./exam/README.md) | 考试场次／报考登记 · 场次与报考登记（本机记录样板） | `8143` |
|  | [library · 图书馆](./library/README.md) | 馆藏图书／借阅登记 · 图书与借阅台账（本机记录样板） | `8144` |
| **宠物服务** | [petcare · 宠物照护](./petcare/README.md) | 宠物档案／照护记录 · 宠物与照护记录（本机记录样板） | `8145` |
|  | [petboarding · 宠物寄养](./petboarding/README.md) | 寄养空间／寄养登记 · 房间与寄养登记（本机记录样板） | `8146` |
|  | [petgrooming · 宠物美容](./petgrooming/README.md) | 美容服务／美容预约 · 服务与预约登记（本机记录样板） | `8147` |
|  | [veterinary · 宠物诊所](./veterinary/README.md) | 诊室档案／到访记录 · 诊室与到访台账（本机记录样板） | `8148` |
| **门店与生活服务** | [pos · 门店收银](./pos/README.md) | 收银台档案／收银记录 · 收银台与手工交易台账（本机记录样板） | `8149` |
|  | [loyalty · 会员运营](./loyalty/README.md) | 会员等级／会员档案 · 等级与会员登记（本机记录样板） | `8150` |
|  | [laundry · 洗衣门店](./laundry/README.md) | 洗护设备／洗护订单 · 设备与洗护订单（本机记录样板） | `8151` |
|  | [gym · 健身房](./gym/README.md) | 团体课程／课程预约 · 课程与预约台账（本机记录样板） | `8152` |
|  | [photography · 摄影工作室](./photography/README.md) | 拍摄套餐／拍摄预约 · 套餐与拍摄预约（本机记录样板） | `8153` |
|  | [wedding · 婚庆策划](./wedding/README.md) | 婚礼方案／婚礼档期 · 方案与婚礼档期（本机记录样板） | `8154` |
| **企业经营** | [accounting · 财务记账](./accounting/README.md) | 账簿分类／记账条目 · 账簿与手工记账（本机记录样板） | `8155` |
|  | [contracts · 合同管理](./contracts/README.md) | 合同模板／合同登记 · 模板与合同台账（本机记录样板） | `8156` |
|  | [projectops · 项目执行](./projectops/README.md) | 项目档案／项目里程碑 · 项目与里程碑跟踪（本机记录样板） | `8157` |
|  | [maintenance · 设备维保](./maintenance/README.md) | 设备档案／维保工单 · 设备与维保工单（本机记录样板） | `8158` |
|  | [qms · 质量管理](./qms/README.md) | 检验标准／质量检查 · 标准与检查记录（本机记录样板） | `8159` |
| **供应链与房产** | [coldchain · 冷链运输](./coldchain/README.md) | 冷链箱档案／运输记录 · 容器与运输登记（本机记录样板） | `8160` |
|  | [freshdelivery · 生鲜配送](./freshdelivery/README.md) | 配送线路／配送任务 · 线路与配送任务（本机记录样板） | `8161` |
|  | [crossborder · 跨境业务](./crossborder/README.md) | 跨境商品／跨境运单 · 商品与跨境运单（本机记录样板） | `8162` |
|  | [returns · 退换货](./returns/README.md) | 商品档案／退换申请 · 商品与退换登记（本机记录样板） | `8163` |
|  | [realestate · 房产交易](./realestate/README.md) | 房源档案／带看记录 · 房源与带看台账（本机记录样板） | `8164` |

[scenic 景区管理](./scenic/README.md) 与 [realestate 房产交易](./realestate/README.md) 可从各自 README 启动；已有 [property 物业房产](./property/README.md) 侧重房源与报修。

<table>
<tr><td width="50%" align="center"><a href="./scenic/README.md"><img src="./scenic/screenshots/overview.png" alt="景区景点与游览登记总览" width="100%" /></a><br/><b>景区管理 · scenic</b></td><td width="50%" align="center"><a href="./realestate/README.md"><img src="./realestate/screenshots/primary.png" alt="房产房源与参考挂牌价列表" width="100%" /></a><br/><b>房产管理 · realestate</b></td></tr>
</table>

## 🆕 上一轮新增 · 16 个行业记录台

> 从内容、采购到养老、环境和公共服务，每个入口都是**可在本机运行的独立样板**：两类关联记录、检索与编辑、Java API、本地 JSON 持久化及实际页面截图。先体验具体场景，再按真实业务需求扩展；**不等同于完整生产系统**。

| 方向 | 新增项目 | 从这里开始 |
| :--- | :--- | :--- |
| 内容与交易 | [cms 内容发布](./cms/README.md) · [wms 仓储管理](./wms/README.md) · [b2b 批发采购](./b2b/README.md) | 栏目/稿件、库位/作业、供应商/采购单 |
| 照护与服务 | [eldercare 养老护理](./eldercare/README.md) · [pharmacy 药店台账](./pharmacy/README.md) · [insurance 保险服务](./insurance/README.md) · [rental 物品租赁](./rental/README.md) · [homeservice 上门服务](./homeservice/README.md) | 档案与服务、批次、理赔进度、租借、上门任务 |
| 资源与环境 | [water 水务设施](./water/README.md) · [sanitation 城市环卫](./sanitation/README.md) · [mining 矿山作业](./mining/README.md) · [forestry 林地巡护](./forestry/README.md) · [fishery 水产养殖](./fishery/README.md) | 设施/巡检、清运、班次、巡护、投喂记录 |
| 基础设施与公共事务 | [telecom 通信设施](./telecom/README.md) · [itops IT 运维](./itops/README.md) · [civic 公共服务](./civic/README.md) | 站点/维护、资产/故障、事项/办理登记 |

<table>
<tr><td width="50%" align="center"><a href="./cms/README.md"><img src="./cms/screenshots/overview.png" alt="cms 内容发布本机记录台总览" width="100%" /></a><br/><b>内容发布 · cms</b></td><td width="50%" align="center"><a href="./wms/README.md"><img src="./wms/screenshots/primary.png" alt="wms 仓储库位列表" width="100%" /></a><br/><b>仓储管理 · wms</b></td></tr>
<tr><td width="50%" align="center"><a href="./eldercare/README.md"><img src="./eldercare/screenshots/secondary.png" alt="eldercare 养老照护记录" width="100%" /></a><br/><b>养老护理 · eldercare</b></td><td width="50%" align="center"><a href="./civic/README.md"><img src="./civic/screenshots/editor.png" alt="civic 公共服务办理登记表单" width="100%" /></a><br/><b>公共服务 · civic</b></td></tr>
</table>

**如何体验新增项目？** 例如 Windows 运行 `./cms/run.ps1`，macOS/Linux 运行 `./cms/run.sh`，打开 `http://127.0.0.1:8114`；其他项目替换目录名并使用上表对应端口。每个项目 README 都有启动说明和四张截图（总览、两类列表、编辑表单）。

## 🖼️ 真实页面

以下画面来自实际运行的项目，不是设计稿。点击图片进入对应项目的完整说明。

<table>
<tr><td width="50%" align="center"><a href="./shop/README.md"><img src="./shop/screenshots/desktop-home.png" alt="shop 商城电脑端首页" width="100%" /></a><br/><b>shop · 电脑端商城</b></td><td width="50%" align="center"><a href="./manage/README.md"><img src="./manage/screenshots/dashboard.png" alt="manage 通用管理后台" width="100%" /></a><br/><b>manage · 通用管理后台</b></td></tr>
<tr><td width="50%" align="center"><a href="./labbook/README.md"><img src="./labbook/screenshots/overview.png" alt="labbook 科研实验记录总览" width="100%" /></a><br/><b>labbook · 跨学科实验记录</b></td><td width="50%" align="center"><a href="./barber/README.md"><img src="./barber/screenshots/overview.png" alt="barber 理发小程序" width="100%" /></a><br/><b>barber · 预约小程序</b></td></tr>
<tr><td width="50%" align="center"><a href="./ai/README.md"><img src="./ai/screenshots/overview.png" alt="ai 本机 AI 工作台" width="100%" /></a><br/><b>ai · 本机 AI 工作台</b></td><td width="50%" align="center"><a href="./html/README.md"><img src="./html/screenshots/studio.png" alt="html 创意工坊工作台" width="100%" /></a><br/><b>html · HTML 创意工坊</b></td></tr>
</table>

<details>
<summary><b>展开全部 87 个项目的页面截图索引</b></summary>

| 项目 | 已演示能力 | 本机地址 | 页面截图 |
| :--- | :--- | :--- | :--- |
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
| [barber](./barber/README.md) | 理发服务、设计师、时段、预约与取消；微信小程序 | http://127.0.0.1:8091 | [预约首页](./barber/screenshots/overview.png) · [预约页](./barber/screenshots/primary.png) · [我的](./barber/screenshots/secondary.png) |
| [dining](./dining/README.md) | 桌号、菜品、餐篮、备注与点餐订单；微信小程序 | http://127.0.0.1:8092 | [点餐页](./dining/screenshots/overview.png) · [餐篮](./dining/screenshots/primary.png) · [订单](./dining/screenshots/secondary.png) |
| [selfshop](./selfshop/README.md) | 商品、搜索、扫码入口、库存、购物袋与自助结算；微信小程序 | http://127.0.0.1:8093 | [商品首页](./selfshop/screenshots/overview.png) · [购物袋](./selfshop/screenshots/primary.png) · [订单](./selfshop/screenshots/secondary.png) |
| [schedule](./schedule/README.md) | 日程、分类、完成状态与未来安排 | http://127.0.0.1:8094 | [总览](./schedule/screenshots/overview.png) · [日程清单](./schedule/screenshots/primary.png) · [未来安排](./schedule/screenshots/secondary.png) |
| [carcare](./carcare/README.md) | 车辆档案、保养维修、里程与费用记录 | http://127.0.0.1:8095 | [总览](./carcare/screenshots/overview.png) · [车辆](./carcare/screenshots/primary.png) · [维修记录](./carcare/screenshots/secondary.png) |
| [parenting](./parenting/README.md) | 成长档案、成长记录与分类检索 | http://127.0.0.1:8096 | [总览](./parenting/screenshots/overview.png) · [成长档案](./parenting/screenshots/primary.png) · [成长记录](./parenting/screenshots/secondary.png) |
| [labbook](./labbook/README.md) | 跨学科实验、样本、修订历史与 JSON 导出 | http://127.0.0.1:8097 | [总览](./labbook/screenshots/overview.png) · [实验](./labbook/screenshots/experiments.png) · [样本](./labbook/screenshots/samples.png) · [学科模板](./labbook/screenshots/templates.png) · [详情](./labbook/screenshots/detail.png) · [编辑](./labbook/screenshots/editor.png) |
| [ai](./ai/README.md) | CLI 工具箱、Skill 技能库、Prompt 工作台 | http://127.0.0.1:8098 | [总览](./ai/screenshots/overview.png) · [CLI](./ai/screenshots/cli.png) · [技能库](./ai/screenshots/skills.png) · [Prompt](./ai/screenshots/prompt.png) · [技能详情](./ai/screenshots/skill-detail.png) |
| [html](./html/README.md) | HTML/CSS/JS 工作台、模板、小游戏与黄页目录 | http://127.0.0.1:8099 | [工作台](./html/screenshots/studio.png) · [模板](./html/screenshots/templates.png) · [小游戏](./html/screenshots/games.png) · [黄页](./html/screenshots/directory.png) · [黄页详情](./html/screenshots/directory-detail.png) |
| [crawler](./crawler/README.md) | 公开网页单页抓取、结构化摘要、链接提取与抓取历史 | http://127.0.0.1:8100 | [总览](./crawler/screenshots/overview.png) · [抓取结果](./crawler/screenshots/crawl.png) · [抓取历史](./crawler/screenshots/history.png) |
| [erp](./erp/README.md) | 商品与出入库，本机演示样板 | http://127.0.0.1:8101 | [总览](./erp/screenshots/overview.png) · [商品档案](./erp/screenshots/primary.png) · [出入库流水](./erp/screenshots/secondary.png) · [编辑表单](./erp/screenshots/editor.png) |
| [manufacturing](./manufacturing/README.md) | 物料与生产工单，本机演示样板 | http://127.0.0.1:8102 | [总览](./manufacturing/screenshots/overview.png) · [物料档案](./manufacturing/screenshots/primary.png) · [生产工单](./manufacturing/screenshots/secondary.png) · [编辑表单](./manufacturing/screenshots/editor.png) |
| [logistics](./logistics/README.md) | 车辆与运单，本机演示样板 | http://127.0.0.1:8103 | [总览](./logistics/screenshots/overview.png) · [运输车辆](./logistics/screenshots/primary.png) · [运输运单](./logistics/screenshots/secondary.png) · [编辑表单](./logistics/screenshots/editor.png) |
| [property](./property/README.md) | 房源档案与报修工单，本机演示样板 | http://127.0.0.1:8104 | [总览](./property/screenshots/overview.png) · [房源档案](./property/screenshots/primary.png) · [报修工单](./property/screenshots/secondary.png) · [编辑表单](./property/screenshots/editor.png) |
| [agriculture](./agriculture/README.md) | 地块档案与农事记录，本机演示样板 | http://127.0.0.1:8105 | [总览](./agriculture/screenshots/overview.png) · [地块档案](./agriculture/screenshots/primary.png) · [农事记录](./agriculture/screenshots/secondary.png) · [编辑表单](./agriculture/screenshots/editor.png) |
| [construction](./construction/README.md) | 工程项目与施工日志，本机演示样板 | http://127.0.0.1:8106 | [总览](./construction/screenshots/overview.png) · [工程项目](./construction/screenshots/primary.png) · [施工日志](./construction/screenshots/secondary.png) · [编辑表单](./construction/screenshots/editor.png) |
| [hospitality](./hospitality/README.md) | 客房档案与预订记录，本机演示样板 | http://127.0.0.1:8107 | [总览](./hospitality/screenshots/overview.png) · [客房档案](./hospitality/screenshots/primary.png) · [预订记录](./hospitality/screenshots/secondary.png) · [编辑表单](./hospitality/screenshots/editor.png) |
| [hrm](./hrm/README.md) | 员工档案与假勤申请，本机演示样板 | http://127.0.0.1:8108 | [总览](./hrm/screenshots/overview.png) · [员工档案](./hrm/screenshots/primary.png) · [假勤申请](./hrm/screenshots/secondary.png) · [编辑表单](./hrm/screenshots/editor.png) |
| [service](./service/README.md) | 客户档案与服务工单，本机演示样板 | http://127.0.0.1:8109 | [总览](./service/screenshots/overview.png) · [客户档案](./service/screenshots/primary.png) · [服务工单](./service/screenshots/secondary.png) · [编辑表单](./service/screenshots/editor.png) |
| [energy](./energy/README.md) | 监测设备与巡检记录，本机演示样板 | http://127.0.0.1:8110 | [总览](./energy/screenshots/overview.png) · [监测设备](./energy/screenshots/primary.png) · [巡检记录](./energy/screenshots/secondary.png) · [编辑表单](./energy/screenshots/editor.png) |
| [legal](./legal/README.md) | 委托人档案与案件进度，本机演示样板 | http://127.0.0.1:8111 | [总览](./legal/screenshots/overview.png) · [委托人档案](./legal/screenshots/primary.png) · [案件进度](./legal/screenshots/secondary.png) · [编辑表单](./legal/screenshots/editor.png) |
| [culture](./culture/README.md) | 场馆空间与活动安排，本机演示样板 | http://127.0.0.1:8112 | [总览](./culture/screenshots/overview.png) · [场馆空间](./culture/screenshots/primary.png) · [活动安排](./culture/screenshots/secondary.png) · [编辑表单](./culture/screenshots/editor.png) |
| [community](./community/README.md) | 服务项目与服务需求，本机演示样板 | http://127.0.0.1:8113 | [总览](./community/screenshots/overview.png) · [服务项目](./community/screenshots/primary.png) · [服务需求](./community/screenshots/secondary.png) · [编辑表单](./community/screenshots/editor.png) |

| [cms](./cms/README.md) | 内容栏目与稿件记录的本机演示 | http://127.0.0.1:8114 | [总览](./cms/screenshots/overview.png) · [内容栏目](./cms/screenshots/primary.png) · [稿件记录](./cms/screenshots/secondary.png) · [编辑表单](./cms/screenshots/editor.png) |
| [wms](./wms/README.md) | 仓库库位与作业记录的本机演示 | http://127.0.0.1:8115 | [总览](./wms/screenshots/overview.png) · [仓库库位](./wms/screenshots/primary.png) · [作业记录](./wms/screenshots/secondary.png) · [编辑表单](./wms/screenshots/editor.png) |
| [b2b](./b2b/README.md) | 供应商档案与采购订单的本机演示 | http://127.0.0.1:8116 | [总览](./b2b/screenshots/overview.png) · [供应商档案](./b2b/screenshots/primary.png) · [采购订单](./b2b/screenshots/secondary.png) · [编辑表单](./b2b/screenshots/editor.png) |
| [eldercare](./eldercare/README.md) | 长者档案与照护记录的本机演示 | http://127.0.0.1:8117 | [总览](./eldercare/screenshots/overview.png) · [长者档案](./eldercare/screenshots/primary.png) · [照护记录](./eldercare/screenshots/secondary.png) · [编辑表单](./eldercare/screenshots/editor.png) |
| [pharmacy](./pharmacy/README.md) | 药品目录与批次记录的本机演示 | http://127.0.0.1:8118 | [总览](./pharmacy/screenshots/overview.png) · [药品目录](./pharmacy/screenshots/primary.png) · [批次记录](./pharmacy/screenshots/secondary.png) · [编辑表单](./pharmacy/screenshots/editor.png) |
| [insurance](./insurance/README.md) | 保单档案与理赔进度的本机演示 | http://127.0.0.1:8119 | [总览](./insurance/screenshots/overview.png) · [保单档案](./insurance/screenshots/primary.png) · [理赔进度](./insurance/screenshots/secondary.png) · [编辑表单](./insurance/screenshots/editor.png) |
| [rental](./rental/README.md) | 出租设备与租借记录的本机演示 | http://127.0.0.1:8120 | [总览](./rental/screenshots/overview.png) · [出租设备](./rental/screenshots/primary.png) · [租借记录](./rental/screenshots/secondary.png) · [编辑表单](./rental/screenshots/editor.png) |
| [homeservice](./homeservice/README.md) | 客户档案与上门任务的本机演示 | http://127.0.0.1:8121 | [总览](./homeservice/screenshots/overview.png) · [客户档案](./homeservice/screenshots/primary.png) · [上门任务](./homeservice/screenshots/secondary.png) · [编辑表单](./homeservice/screenshots/editor.png) |
| [water](./water/README.md) | 设施档案与巡检记录的本机演示 | http://127.0.0.1:8122 | [总览](./water/screenshots/overview.png) · [设施档案](./water/screenshots/primary.png) · [巡检记录](./water/screenshots/secondary.png) · [编辑表单](./water/screenshots/editor.png) |
| [sanitation](./sanitation/README.md) | 作业线路与清运记录的本机演示 | http://127.0.0.1:8123 | [总览](./sanitation/screenshots/overview.png) · [作业线路](./sanitation/screenshots/primary.png) · [清运记录](./sanitation/screenshots/secondary.png) · [编辑表单](./sanitation/screenshots/editor.png) |
| [mining](./mining/README.md) | 作业区域与班次记录的本机演示 | http://127.0.0.1:8124 | [总览](./mining/screenshots/overview.png) · [作业区域](./mining/screenshots/primary.png) · [班次记录](./mining/screenshots/secondary.png) · [编辑表单](./mining/screenshots/editor.png) |
| [forestry](./forestry/README.md) | 林区档案与巡护记录的本机演示 | http://127.0.0.1:8125 | [总览](./forestry/screenshots/overview.png) · [林区档案](./forestry/screenshots/primary.png) · [巡护记录](./forestry/screenshots/secondary.png) · [编辑表单](./forestry/screenshots/editor.png) |
| [fishery](./fishery/README.md) | 养殖池塘与投喂记录的本机演示 | http://127.0.0.1:8126 | [总览](./fishery/screenshots/overview.png) · [养殖池塘](./fishery/screenshots/primary.png) · [投喂记录](./fishery/screenshots/secondary.png) · [编辑表单](./fishery/screenshots/editor.png) |
| [telecom](./telecom/README.md) | 通信站点与维护任务的本机演示 | http://127.0.0.1:8127 | [总览](./telecom/screenshots/overview.png) · [通信站点](./telecom/screenshots/primary.png) · [维护任务](./telecom/screenshots/secondary.png) · [编辑表单](./telecom/screenshots/editor.png) |
| [itops](./itops/README.md) | 设备资产与故障记录的本机演示 | http://127.0.0.1:8128 | [总览](./itops/screenshots/overview.png) · [设备资产](./itops/screenshots/primary.png) · [故障记录](./itops/screenshots/secondary.png) · [编辑表单](./itops/screenshots/editor.png) |
| [civic](./civic/README.md) | 服务事项与办理登记的本机演示 | http://127.0.0.1:8129 | [总览](./civic/screenshots/overview.png) · [服务事项](./civic/screenshots/primary.png) · [办理登记](./civic/screenshots/secondary.png) · [编辑表单](./civic/screenshots/editor.png) |
| [parking](./parking/README.md) | 停车区域与停车记录的本机记录演示 | http://127.0.0.1:8130 | [总览](./parking/screenshots/overview.png) · [停车区域](./parking/screenshots/primary.png) · [停车记录](./parking/screenshots/secondary.png) · [编辑表单](./parking/screenshots/editor.png) |
| [charging](./charging/README.md) | 充电站点与充电记录的本机记录演示 | http://127.0.0.1:8131 | [总览](./charging/screenshots/overview.png) · [充电站点](./charging/screenshots/primary.png) · [充电记录](./charging/screenshots/secondary.png) · [编辑表单](./charging/screenshots/editor.png) |
| [parkops](./parkops/README.md) | 园区档案与巡检记录的本机记录演示 | http://127.0.0.1:8132 | [总览](./parkops/screenshots/overview.png) · [园区档案](./parkops/screenshots/primary.png) · [巡检记录](./parkops/screenshots/secondary.png) · [编辑表单](./parkops/screenshots/editor.png) |
| [fleet](./fleet/README.md) | 车队车辆与行车任务的本机记录演示 | http://127.0.0.1:8133 | [总览](./fleet/screenshots/overview.png) · [车队车辆](./fleet/screenshots/primary.png) · [行车任务](./fleet/screenshots/secondary.png) · [编辑表单](./fleet/screenshots/editor.png) |
| [scenic](./scenic/README.md) | 景点档案与游览登记的本机记录演示 | http://127.0.0.1:8134 | [总览](./scenic/screenshots/overview.png) · [景点档案](./scenic/screenshots/primary.png) · [游览登记](./scenic/screenshots/secondary.png) · [编辑表单](./scenic/screenshots/editor.png) |
| [clinic](./clinic/README.md) | 诊室档案与预约登记的本机记录演示 | http://127.0.0.1:8135 | [总览](./clinic/screenshots/overview.png) · [诊室档案](./clinic/screenshots/primary.png) · [预约登记](./clinic/screenshots/secondary.png) · [编辑表单](./clinic/screenshots/editor.png) |
| [dental](./dental/README.md) | 牙椅档案与就诊登记的本机记录演示 | http://127.0.0.1:8136 | [总览](./dental/screenshots/overview.png) · [牙椅档案](./dental/screenshots/primary.png) · [就诊登记](./dental/screenshots/secondary.png) · [编辑表单](./dental/screenshots/editor.png) |
| [aesthetics](./aesthetics/README.md) | 服务项目与咨询登记的本机记录演示 | http://127.0.0.1:8137 | [总览](./aesthetics/screenshots/overview.png) · [服务项目](./aesthetics/screenshots/primary.png) · [咨询登记](./aesthetics/screenshots/secondary.png) · [编辑表单](./aesthetics/screenshots/editor.png) |
| [rehab](./rehab/README.md) | 训练方案与训练记录的本机记录演示 | http://127.0.0.1:8138 | [总览](./rehab/screenshots/overview.png) · [训练方案](./rehab/screenshots/primary.png) · [训练记录](./rehab/screenshots/secondary.png) · [编辑表单](./rehab/screenshots/editor.png) |
| [lis](./lis/README.md) | 检验项目与样本登记的本机记录演示 | http://127.0.0.1:8139 | [总览](./lis/screenshots/overview.png) · [检验项目](./lis/screenshots/primary.png) · [样本登记](./lis/screenshots/secondary.png) · [编辑表单](./lis/screenshots/editor.png) |
| [kindergarten](./kindergarten/README.md) | 班级档案与班级活动的本机记录演示 | http://127.0.0.1:8140 | [总览](./kindergarten/screenshots/overview.png) · [班级档案](./kindergarten/screenshots/primary.png) · [班级活动](./kindergarten/screenshots/secondary.png) · [编辑表单](./kindergarten/screenshots/editor.png) |
| [training](./training/README.md) | 培训课程与报名登记的本机记录演示 | http://127.0.0.1:8141 | [总览](./training/screenshots/overview.png) · [培训课程](./training/screenshots/primary.png) · [报名登记](./training/screenshots/secondary.png) · [编辑表单](./training/screenshots/editor.png) |
| [elearning](./elearning/README.md) | 线上课程与学习进度的本机记录演示 | http://127.0.0.1:8142 | [总览](./elearning/screenshots/overview.png) · [线上课程](./elearning/screenshots/primary.png) · [学习进度](./elearning/screenshots/secondary.png) · [编辑表单](./elearning/screenshots/editor.png) |
| [exam](./exam/README.md) | 考试场次与报考登记的本机记录演示 | http://127.0.0.1:8143 | [总览](./exam/screenshots/overview.png) · [考试场次](./exam/screenshots/primary.png) · [报考登记](./exam/screenshots/secondary.png) · [编辑表单](./exam/screenshots/editor.png) |
| [library](./library/README.md) | 馆藏图书与借阅登记的本机记录演示 | http://127.0.0.1:8144 | [总览](./library/screenshots/overview.png) · [馆藏图书](./library/screenshots/primary.png) · [借阅登记](./library/screenshots/secondary.png) · [编辑表单](./library/screenshots/editor.png) |
| [petcare](./petcare/README.md) | 宠物档案与照护记录的本机记录演示 | http://127.0.0.1:8145 | [总览](./petcare/screenshots/overview.png) · [宠物档案](./petcare/screenshots/primary.png) · [照护记录](./petcare/screenshots/secondary.png) · [编辑表单](./petcare/screenshots/editor.png) |
| [petboarding](./petboarding/README.md) | 寄养空间与寄养登记的本机记录演示 | http://127.0.0.1:8146 | [总览](./petboarding/screenshots/overview.png) · [寄养空间](./petboarding/screenshots/primary.png) · [寄养登记](./petboarding/screenshots/secondary.png) · [编辑表单](./petboarding/screenshots/editor.png) |
| [petgrooming](./petgrooming/README.md) | 美容服务与美容预约的本机记录演示 | http://127.0.0.1:8147 | [总览](./petgrooming/screenshots/overview.png) · [美容服务](./petgrooming/screenshots/primary.png) · [美容预约](./petgrooming/screenshots/secondary.png) · [编辑表单](./petgrooming/screenshots/editor.png) |
| [veterinary](./veterinary/README.md) | 诊室档案与到访记录的本机记录演示 | http://127.0.0.1:8148 | [总览](./veterinary/screenshots/overview.png) · [诊室档案](./veterinary/screenshots/primary.png) · [到访记录](./veterinary/screenshots/secondary.png) · [编辑表单](./veterinary/screenshots/editor.png) |
| [pos](./pos/README.md) | 收银台档案与收银记录的本机记录演示 | http://127.0.0.1:8149 | [总览](./pos/screenshots/overview.png) · [收银台档案](./pos/screenshots/primary.png) · [收银记录](./pos/screenshots/secondary.png) · [编辑表单](./pos/screenshots/editor.png) |
| [loyalty](./loyalty/README.md) | 会员等级与会员档案的本机记录演示 | http://127.0.0.1:8150 | [总览](./loyalty/screenshots/overview.png) · [会员等级](./loyalty/screenshots/primary.png) · [会员档案](./loyalty/screenshots/secondary.png) · [编辑表单](./loyalty/screenshots/editor.png) |
| [laundry](./laundry/README.md) | 洗护设备与洗护订单的本机记录演示 | http://127.0.0.1:8151 | [总览](./laundry/screenshots/overview.png) · [洗护设备](./laundry/screenshots/primary.png) · [洗护订单](./laundry/screenshots/secondary.png) · [编辑表单](./laundry/screenshots/editor.png) |
| [gym](./gym/README.md) | 团体课程与课程预约的本机记录演示 | http://127.0.0.1:8152 | [总览](./gym/screenshots/overview.png) · [团体课程](./gym/screenshots/primary.png) · [课程预约](./gym/screenshots/secondary.png) · [编辑表单](./gym/screenshots/editor.png) |
| [photography](./photography/README.md) | 拍摄套餐与拍摄预约的本机记录演示 | http://127.0.0.1:8153 | [总览](./photography/screenshots/overview.png) · [拍摄套餐](./photography/screenshots/primary.png) · [拍摄预约](./photography/screenshots/secondary.png) · [编辑表单](./photography/screenshots/editor.png) |
| [wedding](./wedding/README.md) | 婚礼方案与婚礼档期的本机记录演示 | http://127.0.0.1:8154 | [总览](./wedding/screenshots/overview.png) · [婚礼方案](./wedding/screenshots/primary.png) · [婚礼档期](./wedding/screenshots/secondary.png) · [编辑表单](./wedding/screenshots/editor.png) |
| [accounting](./accounting/README.md) | 账簿分类与记账条目的本机记录演示 | http://127.0.0.1:8155 | [总览](./accounting/screenshots/overview.png) · [账簿分类](./accounting/screenshots/primary.png) · [记账条目](./accounting/screenshots/secondary.png) · [编辑表单](./accounting/screenshots/editor.png) |
| [contracts](./contracts/README.md) | 合同模板与合同登记的本机记录演示 | http://127.0.0.1:8156 | [总览](./contracts/screenshots/overview.png) · [合同模板](./contracts/screenshots/primary.png) · [合同登记](./contracts/screenshots/secondary.png) · [编辑表单](./contracts/screenshots/editor.png) |
| [projectops](./projectops/README.md) | 项目档案与项目里程碑的本机记录演示 | http://127.0.0.1:8157 | [总览](./projectops/screenshots/overview.png) · [项目档案](./projectops/screenshots/primary.png) · [项目里程碑](./projectops/screenshots/secondary.png) · [编辑表单](./projectops/screenshots/editor.png) |
| [maintenance](./maintenance/README.md) | 设备档案与维保工单的本机记录演示 | http://127.0.0.1:8158 | [总览](./maintenance/screenshots/overview.png) · [设备档案](./maintenance/screenshots/primary.png) · [维保工单](./maintenance/screenshots/secondary.png) · [编辑表单](./maintenance/screenshots/editor.png) |
| [qms](./qms/README.md) | 检验标准与质量检查的本机记录演示 | http://127.0.0.1:8159 | [总览](./qms/screenshots/overview.png) · [检验标准](./qms/screenshots/primary.png) · [质量检查](./qms/screenshots/secondary.png) · [编辑表单](./qms/screenshots/editor.png) |
| [coldchain](./coldchain/README.md) | 冷链箱档案与运输记录的本机记录演示 | http://127.0.0.1:8160 | [总览](./coldchain/screenshots/overview.png) · [冷链箱档案](./coldchain/screenshots/primary.png) · [运输记录](./coldchain/screenshots/secondary.png) · [编辑表单](./coldchain/screenshots/editor.png) |
| [freshdelivery](./freshdelivery/README.md) | 配送线路与配送任务的本机记录演示 | http://127.0.0.1:8161 | [总览](./freshdelivery/screenshots/overview.png) · [配送线路](./freshdelivery/screenshots/primary.png) · [配送任务](./freshdelivery/screenshots/secondary.png) · [编辑表单](./freshdelivery/screenshots/editor.png) |
| [crossborder](./crossborder/README.md) | 跨境商品与跨境运单的本机记录演示 | http://127.0.0.1:8162 | [总览](./crossborder/screenshots/overview.png) · [跨境商品](./crossborder/screenshots/primary.png) · [跨境运单](./crossborder/screenshots/secondary.png) · [编辑表单](./crossborder/screenshots/editor.png) |
| [returns](./returns/README.md) | 商品档案与退换申请的本机记录演示 | http://127.0.0.1:8163 | [总览](./returns/screenshots/overview.png) · [商品档案](./returns/screenshots/primary.png) · [退换申请](./returns/screenshots/secondary.png) · [编辑表单](./returns/screenshots/editor.png) |
| [realestate](./realestate/README.md) | 房源档案与带看记录的本机记录演示 | http://127.0.0.1:8164 | [总览](./realestate/screenshots/overview.png) · [房源档案](./realestate/screenshots/primary.png) · [带看记录](./realestate/screenshots/secondary.png) · [编辑表单](./realestate/screenshots/editor.png) |

| [testops](./testops/README.md) | 测试用例与手工执行记录的本机演示 | http://127.0.0.1:8165 | [总览](./testops/screenshots/overview.png) · [测试用例](./testops/screenshots/primary.png) · [执行记录](./testops/screenshots/secondary.png) · [编辑表单](./testops/screenshots/editor.png) |
| [ticketops](./ticketops/README.md) | 队列与内部工单的本机演示 | http://127.0.0.1:8166 | [总览](./ticketops/screenshots/overview.png) · [队列](./ticketops/screenshots/primary.png) · [工单](./ticketops/screenshots/secondary.png) · [编辑表单](./ticketops/screenshots/editor.png) |
| [bugtrack](./bugtrack/README.md) | 项目与缺陷记录的本机演示 | http://127.0.0.1:8167 | [总览](./bugtrack/screenshots/overview.png) · [项目](./bugtrack/screenshots/primary.png) · [缺陷](./bugtrack/screenshots/secondary.png) · [编辑表单](./bugtrack/screenshots/editor.png) |

</details>

## 🧭 系统架构

![ai-hub 独立项目架构：用户入口、Vue 交互层、Java 服务、本地 JSON 与构建验证链路](./docs/architecture.svg)

**关键设计：**

1. **项目独立：**87 个系统各自拥有目录、Java 服务、端口和本地数据文件；不需要先启动一个公共网关或数据库。
2. **Web 一体交付：**Vue 页面由 Vite 构建后放入 Spring Boot 的静态资源目录，随可执行 jar 一起提供；浏览器通过同源 `/api` 请求业务接口。
3. **小程序单独构建：**shop、barber、dining、selfshop 的 uni-app 构建微信小程序产物；小程序和电脑端的代码形态不同，但对应 Java API 保持独立。
4. **数据本机持久化：**业务数据位于各项目的 `data/<项目>.json`，重启仍保留。停服后复制 JSON 文件备份；停服后移走它可重置演示数据。文件损坏时服务拒绝启动，避免覆盖原数据。
5. **测试再交付：**前端构建、后端测试、jar 静态页检查和真实进程冒烟都纳入根目录脚本。架构图表达的是当前本机演示形态，**不是生产高可用架构**。

## ✅ 验证与使用边界

```powershell
./test-all.ps1      # Windows：87 项目构建、后端测试、jar 检查；包含 4 个微信小程序构建
./smoke-test.ps1    # Windows：启动 87 个真实服务，检查页面、JS 与 API
```

**验证记录（2026-10-02）：**此前 84 个项目完成前端构建、后端测试、打包和真实 Java 进程的页面/API 冒烟；本轮新增的 testops、ticketops、bugtrack 分别通过前端构建、后端测试/打包、真实进程冒烟及浏览器表单检查，各附 4 张实际截图。87 个项目的完整脚本已经纳入新入口，但**本轮未重新跑完 87 项全量检查**。这些检查验证本机演示，不代表生产环境的性能、安全或行业合规认证。

macOS / Linux 可运行 `./test-all.sh`；各项目也可单独运行 `mvn -f <项目>/backend/pom.xml test`。**每个项目都提供 `run.ps1` 和可执行的 `run.sh`**；截图保存在各项目的 `screenshots/`。GitHub Actions 对推送/PR 自动挑选有变更的项目构建与运行后端测试；共享构建脚本变更和每周定时任务覆盖完整 87 项。CI 仅检查构建、后端测试与打包静态页面，**不等于 87 项浏览器/业务流程测试**。[查看 100 轮质量自查账本](./docs/quality-loop.md)。

> [!IMPORTANT]
> **这是可直接在本机体验、学习和二次开发的样板，不是可直接上生产的 SaaS。** 默认绑定 `127.0.0.1`；没有统一登录鉴权、正式权限隔离、支付、加密、不可篡改审计、数据库迁移与多实例并发保障。金融、医疗、未成年人、门禁、科研等敏感场景尤其不能直接处理真实业务数据。正式部署前须完成安全、隐私、合规与灾备设计。

## 🌱 下一站

“让所有职业都能找到软件”是方向，而非当前覆盖范围。本轮的 35 个入口也是行业概念样板，不能直接处理真实个人、健康或财务数据。新增 29 个行业目录是**可运行的档案／记录演示**，并非已经实现 ERP 自动库存结转、生产 MES、物流调度、酒店房态冲突控制、正式审批等完整行业系统。CMS、仓储、采购、照护、公共服务等新增目录同样仅覆盖本机档案/记录功能，不具备出版审核、真实仓储结转、保险理赔或政务身份校验等完整流程。更多候选类型与参考项目见 [业务系统调研与建设清单](./docs/business-map.md)。欢迎从一个真实业务问题出发，提出下一款值得认真做的软件。

**定制开发联系：**QQ **3174667330** · 邮箱 **3174667330@qq.com**。Git 提交邮箱仅设置在此仓库，不修改全局配置。
