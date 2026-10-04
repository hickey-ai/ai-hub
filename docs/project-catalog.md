# 项目分类目录 · 101 个独立项目

**简体中文** · [English](./project-catalog.en.md)

每个项目只归入**一个主分类**；4 个微信小程序已包含在 101 项中，不重复计数。分类是寻找业务的入口，**不代表生产成熟度**。多数行业方向是本机单用户记录样板；完整页面见[截图索引](./screenshots.md)，验证层级见[逐项目测试记录](./project-validation-2026-10-03.md)。

## 分类导航

- [零售餐饮与生活服务 · 13](#commerce)
- [企业经营与专业服务 · 13](#enterprise)
- [供应链、制造与质量 · 14](#supply)
- [医疗健康与养老 · 10](#care)
- [宠物服务 · 4](#pets)
- [教育、科研与文化 · 11](#education)
- [出行、文旅与活动 · 10](#mobility)
- [地产、园区与工程 · 4](#places)
- [农林渔矿、能源与环境 · 7](#resources)
- [公共服务与设施安全 · 6](#public)
- [研发协作与数字工具 · 7](#digital)
- [家庭与日程 · 2](#personal)

<a id="commerce"></a>
## 零售餐饮与生活服务 · 13

| 项目 | 本机可体验范围 | 入口 | 端口 | 截图 |
| :--- | :--- | :--- | ---: | :--- |
| [商城 · `shop`](../shop/README.md) | 电脑端与微信小程序、商品检索、详情、购物车、库存校验与下单 | 电脑端 + 小程序 | `8081` | [查看](../shop/screenshots/desktop-home.png) |
| [理发 · `barber`](../barber/README.md) | 小程序服务、设计师、时段预约与取消 | 小程序 | `8091` | [查看](../barber/screenshots/overview.png) |
| [点餐 · `dining`](../dining/README.md) | 小程序桌号、菜品、餐篮、备注与订单 | 小程序 | `8092` | [查看](../dining/screenshots/overview.png) |
| [自助购物 · `selfshop`](../selfshop/README.md) | 小程序搜索、扫码入口、库存、购物袋与结算 | 小程序 | `8093` | [查看](../selfshop/screenshots/overview.png) |
| [门店收银 · `pos`](../pos/README.md) | 收银台与手工交易台账 | 电脑端 | `8149` | [查看](../pos/screenshots/overview.png) |
| [会员运营 · `loyalty`](../loyalty/README.md) | 等级与会员登记 | 电脑端 | `8150` | [查看](../loyalty/screenshots/overview.png) |
| [洗衣门店 · `laundry`](../laundry/README.md) | 设备与洗护订单 | 电脑端 | `8151` | [查看](../laundry/screenshots/overview.png) |
| [健身房 · `gym`](../gym/README.md) | 课程与预约台账 | 电脑端 | `8152` | [查看](../gym/screenshots/overview.png) |
| [摄影工作室 · `photography`](../photography/README.md) | 套餐与拍摄预约 | 电脑端 | `8153` | [查看](../photography/screenshots/overview.png) |
| [婚庆策划 · `wedding`](../wedding/README.md) | 方案与婚礼档期 | 电脑端 | `8154` | [查看](../wedding/screenshots/overview.png) |
| [物品租赁 · `rental`](../rental/README.md) | 出租设备、租借记录 | 电脑端 | `8120` | [查看](../rental/screenshots/overview.png) |
| [上门服务 · `homeservice`](../homeservice/README.md) | 客户档案、上门任务 | 电脑端 | `8121` | [查看](../homeservice/screenshots/overview.png) |
| [快递驿站 · `parcelstation`](../parcelstation/README.md) | 货架、包裹入库、取件码、签收与异常件流转（本机演示） | 电脑端 | `8179` | [查看](../parcelstation/screenshots/overview.png) |

<a id="enterprise"></a>
## 企业经营与专业服务 · 13

| 项目 | 本机可体验范围 | 入口 | 端口 | 截图 |
| :--- | :--- | :--- | ---: | :--- |
| [通用后台 · `manage`](../manage/README.md) | 若依式控制台、用户、角色、菜单、部门、日志与个人中心 | 电脑端 | `8082` | [查看](../manage/screenshots/dashboard.png) |
| [客户关系 · `crm`](../crm/README.md) | 客户、商机、跟进和阶段流转 | 电脑端 | `8083` | [查看](../crm/screenshots/pipeline.png) |
| [协同办公 · `oa`](../oa/README.md) | 请假/报销草稿、提交与审批 | 电脑端 | `8084` | [查看](../oa/screenshots/approvals.png) |
| [人力资源 · `hrm`](../hrm/README.md) | 员工档案、假勤申请与状态 | 电脑端 | `8108` | [查看](../hrm/screenshots/overview.png) |
| [金融工作台 · `finance`](../finance/README.md) | 账户、流水、风险提醒与经营指标 | 电脑端 | `8085` | [查看](../finance/screenshots/overview.png) |
| [财务记账 · `accounting`](../accounting/README.md) | 账簿与手工记账 | 电脑端 | `8155` | [查看](../accounting/screenshots/overview.png) |
| [合同管理 · `contracts`](../contracts/README.md) | 模板与合同台账 | 电脑端 | `8156` | [查看](../contracts/screenshots/overview.png) |
| [项目执行 · `projectops`](../projectops/README.md) | 项目与里程碑跟踪 | 电脑端 | `8157` | [查看](../projectops/screenshots/overview.png) |
| [法律服务 · `legal`](../legal/README.md) | 委托档案、案件进度与下一步行动 | 电脑端 | `8111` | [查看](../legal/screenshots/overview.png) |
| [审计事务所项目 · `auditfirm`](../auditfirm/README.md) | 审计项目与底稿登记 | 电脑端 | `8178` | [查看](../auditfirm/screenshots/overview.png) |
| [保险服务 · `insurance`](../insurance/README.md) | 保单档案、理赔进度 | 电脑端 | `8119` | [查看](../insurance/screenshots/overview.png) |
| [采购招投标台账 · `procurement`](../procurement/README.md) | 采购需求与报价记录 | 电脑端 | `8176` | [查看](../procurement/screenshots/overview.png) |
| [售后工单 · `service`](../service/README.md) | 客户档案、服务工单与跟进 | 电脑端 | `8109` | [查看](../service/screenshots/overview.png) |

<a id="supply"></a>
## 供应链、制造与质量 · 14

| 项目 | 本机可体验范围 | 入口 | 端口 | 截图 |
| :--- | :--- | :--- | ---: | :--- |
| [进销存 · `erp`](../erp/README.md) | 商品档案、出入库流水、关联与检索 | 电脑端 | `8101` | [查看](../erp/screenshots/overview.png) |
| [仓储管理 · `wms`](../wms/README.md) | 仓库库位、作业记录 | 电脑端 | `8115` | [查看](../wms/screenshots/overview.png) |
| [批发采购 · `b2b`](../b2b/README.md) | 供应商档案、采购订单 | 电脑端 | `8116` | [查看](../b2b/screenshots/overview.png) |
| [制造 · `manufacturing`](../manufacturing/README.md) | 物料档案、生产工单、进度记录 | 电脑端 | `8102` | [查看](../manufacturing/screenshots/overview.png) |
| [物流 · `logistics`](../logistics/README.md) | 车辆档案、运输运单和状态记录 | 电脑端 | `8103` | [查看](../logistics/screenshots/overview.png) |
| [冷链运输 · `coldchain`](../coldchain/README.md) | 容器与运输登记 | 电脑端 | `8160` | [查看](../coldchain/screenshots/overview.png) |
| [生鲜配送 · `freshdelivery`](../freshdelivery/README.md) | 线路与配送任务 | 电脑端 | `8161` | [查看](../freshdelivery/screenshots/overview.png) |
| [跨境业务 · `crossborder`](../crossborder/README.md) | 商品与跨境运单 | 电脑端 | `8162` | [查看](../crossborder/screenshots/overview.png) |
| [退换货 · `returns`](../returns/README.md) | 商品与退换登记 | 电脑端 | `8163` | [查看](../returns/screenshots/overview.png) |
| [质量管理 · `qms`](../qms/README.md) | 标准与检查记录 | 电脑端 | `8159` | [查看](../qms/screenshots/overview.png) |
| [设备维保 · `maintenance`](../maintenance/README.md) | 设备与维保工单 | 电脑端 | `8158` | [查看](../maintenance/screenshots/overview.png) |
| [食品批次溯源 · `foodsafety`](../foodsafety/README.md) | 食品批次与流转记录 | 电脑端 | `8170` | [查看](../foodsafety/screenshots/overview.png) |
| [仪器计量校准 · `calibration`](../calibration/README.md) | 仪器与校准记录 | 电脑端 | `8177` | [查看](../calibration/screenshots/overview.png) |
| [再生资源回收 · `recycling`](../recycling/README.md) | 回收物料、称重单据、手工结算与状态校验（本机演示） | 电脑端 | `8180` | [查看](../recycling/screenshots/overview.png) |

<a id="care"></a>
## 医疗健康与养老 · 10

| 项目 | 本机可体验范围 | 入口 | 端口 | 截图 |
| :--- | :--- | :--- | ---: | :--- |
| [健康 · `health`](../health/README.md) | 健康档案、随访预约与指标提醒 | 电脑端 | `8086` | [查看](../health/screenshots/overview.png) |
| [养生 · `wellness`](../wellness/README.md) | 会员、养生计划、课程和打卡 | 电脑端 | `8087` | [查看](../wellness/screenshots/overview.png) |
| [医院 · `hospital`](../hospital/README.md) | 患者、门诊预约、病区与医嘱工作台 | 电脑端 | `8088` | [查看](../hospital/screenshots/overview.png) |
| [诊所管理 · `clinic`](../clinic/README.md) | 诊室与预约登记 | 电脑端 | `8135` | [查看](../clinic/screenshots/overview.png) |
| [口腔门诊 · `dental`](../dental/README.md) | 牙椅与就诊排期 | 电脑端 | `8136` | [查看](../dental/screenshots/overview.png) |
| [医美服务 · `aesthetics`](../aesthetics/README.md) | 项目与咨询跟进 | 电脑端 | `8137` | [查看](../aesthetics/screenshots/overview.png) |
| [康复训练 · `rehab`](../rehab/README.md) | 方案与训练记录 | 电脑端 | `8138` | [查看](../rehab/screenshots/overview.png) |
| [检验台账 · `lis`](../lis/README.md) | 项目与样本流转演示 | 电脑端 | `8139` | [查看](../lis/screenshots/overview.png) |
| [药店台账 · `pharmacy`](../pharmacy/README.md) | 药品目录、批次记录 | 电脑端 | `8118` | [查看](../pharmacy/screenshots/overview.png) |
| [养老护理 · `eldercare`](../eldercare/README.md) | 长者档案、照护记录 | 电脑端 | `8117` | [查看](../eldercare/screenshots/overview.png) |

<a id="pets"></a>
## 宠物服务 · 4

| 项目 | 本机可体验范围 | 入口 | 端口 | 截图 |
| :--- | :--- | :--- | ---: | :--- |
| [宠物照护 · `petcare`](../petcare/README.md) | 宠物与照护记录 | 电脑端 | `8145` | [查看](../petcare/screenshots/overview.png) |
| [宠物寄养 · `petboarding`](../petboarding/README.md) | 房间与寄养登记 | 电脑端 | `8146` | [查看](../petboarding/screenshots/overview.png) |
| [宠物美容 · `petgrooming`](../petgrooming/README.md) | 服务与预约登记 | 电脑端 | `8147` | [查看](../petgrooming/screenshots/overview.png) |
| [宠物诊所 · `veterinary`](../veterinary/README.md) | 诊室与到访台账 | 电脑端 | `8148` | [查看](../veterinary/screenshots/overview.png) |

<a id="education"></a>
## 教育、科研与文化 · 11

| 项目 | 本机可体验范围 | 入口 | 端口 | 截图 |
| :--- | :--- | :--- | ---: | :--- |
| [学校 · `school`](../school/README.md) | 学生、课程、出勤与校园事务 | 电脑端 | `8089` | [查看](../school/screenshots/overview.png) |
| [幼儿园管理 · `kindergarten`](../kindergarten/README.md) | 班级与活动记录 | 电脑端 | `8140` | [查看](../kindergarten/screenshots/overview.png) |
| [培训机构 · `training`](../training/README.md) | 课程与报名记录 | 电脑端 | `8141` | [查看](../training/screenshots/overview.png) |
| [在线学习 · `elearning`](../elearning/README.md) | 课程与学习记录 | 电脑端 | `8142` | [查看](../elearning/screenshots/overview.png) |
| [考试管理 · `exam`](../exam/README.md) | 场次与报考登记 | 电脑端 | `8143` | [查看](../exam/screenshots/overview.png) |
| [图书馆 · `library`](../library/README.md) | 图书与借阅台账 | 电脑端 | `8144` | [查看](../library/screenshots/overview.png) |
| [科研实验 · `labbook`](../labbook/README.md) | 八个学科模板、实验、样本、修订历史与 JSON 导出 | 电脑端 | `8097` | [查看](../labbook/screenshots/overview.png) |
| [博物馆藏品档案 · `museum`](../museum/README.md) | 藏品与借展记录 | 电脑端 | `8174` | [查看](../museum/screenshots/overview.png) |
| [内容发布 · `cms`](../cms/README.md) | 内容栏目、稿件记录 | 电脑端 | `8114` | [查看](../cms/screenshots/overview.png) |
| [文化体育 · `culture`](../culture/README.md) | 场馆空间、活动安排与状态 | 电脑端 | `8112` | [查看](../culture/screenshots/overview.png) |
| [托育机构 · `childcare`](../childcare/README.md) | 托育班级、儿童代称、接送与异常交接记录（本机演示） | 电脑端 | `8181` | [查看](../childcare/screenshots/overview.png) |

<a id="mobility"></a>
## 出行、文旅与活动 · 10

| 项目 | 本机可体验范围 | 入口 | 端口 | 截图 |
| :--- | :--- | :--- | ---: | :--- |
| [汽车养护 · `carcare`](../carcare/README.md) | 车辆、里程、维修保养与费用记录 | 电脑端 | `8095` | [查看](../carcare/screenshots/overview.png) |
| [汽车销售线索 · `autosales`](../autosales/README.md) | 车辆与意向客户记录 | 电脑端 | `8175` | [查看](../autosales/screenshots/overview.png) |
| [车队管理 · `fleet`](../fleet/README.md) | 车队与行车任务 | 电脑端 | `8133` | [查看](../fleet/screenshots/overview.png) |
| [公交地铁运营 · `transit`](../transit/README.md) | 线路与班次登记 | 电脑端 | `8169` | [查看](../transit/screenshots/overview.png) |
| [港口航运记录 · `maritime`](../maritime/README.md) | 港口泊位与靠港记录 | 电脑端 | `8173` | [查看](../maritime/screenshots/overview.png) |
| [停车场 · `parking`](../parking/README.md) | 车位与进出场登记 | 电脑端 | `8130` | [查看](../parking/screenshots/overview.png) |
| [充电站 · `charging`](../charging/README.md) | 充电站点与充电登记 | 电脑端 | `8131` | [查看](../charging/screenshots/overview.png) |
| [酒店旅游 · `hospitality`](../hospitality/README.md) | 客房档案、预订记录与房态 | 电脑端 | `8107` | [查看](../hospitality/screenshots/overview.png) |
| [景区管理 · `scenic`](../scenic/README.md) | 景点资源与游览登记 | 电脑端 | `8134` | [查看](../scenic/screenshots/overview.png) |
| [演出赛事票务台账 · `events`](../events/README.md) | 场馆与活动场次登记 | 电脑端 | `8172` | [查看](../events/screenshots/overview.png) |

<a id="places"></a>
## 地产、园区与工程 · 4

| 项目 | 本机可体验范围 | 入口 | 端口 | 截图 |
| :--- | :--- | :--- | ---: | :--- |
| [物业房产 · `property`](../property/README.md) | 房源档案、报修事项与处理记录 | 电脑端 | `8104` | [查看](../property/screenshots/overview.png) |
| [房产交易 · `realestate`](../realestate/README.md) | 房源与带看台账 | 电脑端 | `8164` | [查看](../realestate/screenshots/overview.png) |
| [建筑工程 · `construction`](../construction/README.md) | 工程项目、现场施工日志 | 电脑端 | `8106` | [查看](../construction/screenshots/overview.png) |
| [园区运营 · `parkops`](../parkops/README.md) | 园区设施与巡检台账 | 电脑端 | `8132` | [查看](../parkops/screenshots/overview.png) |

<a id="resources"></a>
## 农林渔矿、能源与环境 · 7

| 项目 | 本机可体验范围 | 入口 | 端口 | 截图 |
| :--- | :--- | :--- | ---: | :--- |
| [农业养殖 · `agriculture`](../agriculture/README.md) | 地块档案、农事活动记录 | 电脑端 | `8105` | [查看](../agriculture/screenshots/overview.png) |
| [林地巡护 · `forestry`](../forestry/README.md) | 林区档案、巡护记录 | 电脑端 | `8125` | [查看](../forestry/screenshots/overview.png) |
| [水产养殖 · `fishery`](../fishery/README.md) | 养殖池塘、投喂记录 | 电脑端 | `8126` | [查看](../fishery/screenshots/overview.png) |
| [矿山作业 · `mining`](../mining/README.md) | 作业区域、班次记录 | 电脑端 | `8124` | [查看](../mining/screenshots/overview.png) |
| [水务设施 · `water`](../water/README.md) | 设施档案、巡检记录 | 电脑端 | `8122` | [查看](../water/screenshots/overview.png) |
| [城市环卫 · `sanitation`](../sanitation/README.md) | 作业线路、清运记录 | 电脑端 | `8123` | [查看](../sanitation/screenshots/overview.png) |
| [能源环保 · `energy`](../energy/README.md) | 监测设备、巡检读数与结论 | 电脑端 | `8110` | [查看](../energy/screenshots/overview.png) |

<a id="public"></a>
## 公共服务与设施安全 · 6

| 项目 | 本机可体验范围 | 入口 | 端口 | 截图 |
| :--- | :--- | :--- | ---: | :--- |
| [门禁 · `access`](../access/README.md) | 门点、人员、访客与通行事件 | 电脑端 | `8090` | [查看](../access/screenshots/overview.png) |
| [社区公益 · `community`](../community/README.md) | 服务项目、需求登记与处理记录 | 电脑端 | `8113` | [查看](../community/screenshots/overview.png) |
| [公共服务 · `civic`](../civic/README.md) | 服务事项、办理登记 | 电脑端 | `8129` | [查看](../civic/screenshots/overview.png) |
| [网格事件 · `gridops`](../gridops/README.md) | 登记、处置、核验、办结与时间线（原创本机演示） | 电脑端 | `8168` | [查看](../gridops/screenshots/overview.png) |
| [应急消防巡检 · `emergency`](../emergency/README.md) | 巡检场所与隐患记录 | 电脑端 | `8171` | [查看](../emergency/screenshots/overview.png) |
| [通信设施 · `telecom`](../telecom/README.md) | 通信站点、维护任务 | 电脑端 | `8127` | [查看](../telecom/screenshots/overview.png) |

<a id="digital"></a>
## 研发协作与数字工具 · 7

| 项目 | 本机可体验范围 | 入口 | 端口 | 截图 |
| :--- | :--- | :--- | ---: | :--- |
| [AI 工作台 · `ai`](../ai/README.md) | CLI 工具箱、Skill 技能库、Prompt 工作台与安全命令预览 | 电脑端 | `8098` | [查看](../ai/screenshots/overview.png) |
| [HTML 创意工坊 · `html`](../html/README.md) | HTML/CSS/JS 实时预览、模板、小程序式小游戏与虚构黄页 | 电脑端 | `8099` | [查看](../html/screenshots/studio.png) |
| [网页抓取 · `crawler`](../crawler/README.md) | 公开网页单页抓取、摘要、链接与历史 | 电脑端 | `8100` | [查看](../crawler/screenshots/overview.png) |
| [IT 运维 · `itops`](../itops/README.md) | 设备资产、故障记录 | 电脑端 | `8128` | [查看](../itops/screenshots/overview.png) |
| [测试管理 · `testops`](../testops/README.md) | 测试用例与手工执行结果记录 | 电脑端 | `8165` | [查看](../testops/screenshots/overview.png) |
| [内部工单 · `ticketops`](../ticketops/README.md) | 队列、优先级、处理人与状态 | 电脑端 | `8166` | [查看](../ticketops/screenshots/overview.png) |
| [缺陷跟踪 · `bugtrack`](../bugtrack/README.md) | 项目、缺陷、复现步骤与验证状态 | 电脑端 | `8167` | [查看](../bugtrack/screenshots/overview.png) |

<a id="personal"></a>
## 家庭与日程 · 2

| 项目 | 本机可体验范围 | 入口 | 端口 | 截图 |
| :--- | :--- | :--- | ---: | :--- |
| [日程 · `schedule`](../schedule/README.md) | 日程、分类、完成状态与未来安排 | 电脑端 | `8094` | [查看](../schedule/screenshots/overview.png) |
| [养娃 · `parenting`](../parenting/README.md) | 成长档案、日常与里程碑记录 | 电脑端 | `8096` | [查看](../parenting/screenshots/overview.png) |

## 如何使用目录

1. 点击项目名查看独立 README、功能边界与运行步骤。
2. Windows 运行 `./<项目>/run.ps1`，macOS/Linux 运行 `./<项目>/run.sh`，打开对应端口；仅小程序项目需使用相应微信开发工具查看界面。
3. 查看[全部页面截图](./screenshots.md)和[逐项目验证矩阵](./project-validation-2026-10-03.md)。

**安全边界：**这些无统一鉴权的本机演示不可直接暴露公网，也不要写入真实敏感数据；构建通过不代表生产或真机验收。
