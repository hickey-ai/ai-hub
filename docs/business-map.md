# 业务系统调研与建设清单

2026-10-01 从 GitHub/Gitee 可公开访问项目中抽样，侧重业务模块、交互模式和技术边界；参考链接**不是**复制授权，也不表示它们全部使用 Java 21。该调研非全网穷举。

| 领域 | 参考项目 | 可借鉴的领域能力 | ai-hub 状态 |
| --- | --- | --- | --- |
| 金融 | 金融经营台 | 账户、流水、风险提醒 | **finance 已实现演示闭环**；真实资金系统未做 |
| 健康 | 健康管理台 | 健康档案、随访、指标提醒 | **health 已实现演示闭环**；医疗建议与合规未做 |
| 养生 | 养生服务台 | 会员、计划、课程、打卡 | **wellness 已实现演示闭环**；真实服务履约未做 |
| 医院 | 医院运营台 | 患者、门诊、病区、医嘱 | **hospital 已实现演示闭环**；HIS/EMR 对接未做 |
| 学校 | 学校教务台 | 学生、课程、出勤、事务 | **school 已实现演示闭环**；教务数据合规未做 |
| 门禁 | 门禁安全台 | 门点、访客、通行、异常 | **access 已实现演示闭环**；真实硬件接入未做 |
| 理发 | 理发预约小程序 | 服务、设计师、时段、预约、取消 | **barber 已实现演示闭环**；真实排班、会员、收银未做 |
| 点餐 | 扫码点餐小程序 | 桌号、菜品、餐篮、备注、后厨状态 | **dining 已实现演示闭环**；真实后厨、支付、退款未做 |
| 自助购物 | 自助购物小程序 | 商品、搜索、扫码、库存、购物袋、结算 | **selfshop 已实现演示闭环**；支付、防损、电子小票未做 |
| 日程记录 | 日程与未来安排 | 分类、完成状态、搜索、时间校验 | **schedule 本机演示闭环**；提醒推送/多用户协作未做 |
| 汽车维修记录 | 车辆养护台 | 车辆档案、里程、保养维修和费用 | **carcare 本机演示闭环**；维修工单/配件库存未做 |
| 养娃记录 | 家庭成长记录 | 成长档案、日常与里程碑记录 | **parenting 本机演示闭环**；多人共享/医疗诊断未做 |
| 科研实验记录 | [GO FAIR 数据原则](https://www.go-fair.org/fair-principles/) | 跨学科实验、样本关联、版本快照与数据导出 | **labbook 本机演示闭环**；未达到 FAIR 或受监管电子记录合规要求 |
| 商城 | [mall (GitHub)](https://github.com/macrozheng/mall)、[mall (Gitee)](https://gitee.com/macrozheng/mall) | 商品、订单、库存、营销 | **shop 最小闭环**；支付/售后未做 |
| 管理后台 | [RuoYi-Vue-Plus (GitHub)](https://github.com/dromara/RuoYi-Vue-Plus)、[RuoYi-Vue-Plus (Gitee)](https://gitee.com/dromara/RuoYi-Vue-Plus) | 用户、角色、菜单、权限 | **manage 演示样板**；生产 RBAC 未做 |
| CRM | [悟空CRM (GitHub)](https://github.com/WuKongOpenSource/WukongCRM-11.0-JAVA)、[悟空CRM (Gitee)](https://gitee.com/wukongcrm) | 客户、线索、商机、跟进 | **crm 最小闭环**；正式鉴权/线索池未做 |
| ERP | [iDempiere (GitHub)](https://github.com/idempiere/idempiere)、[JshERP (Gitee)](https://gitee.com/jishenghua/JSH_ERP) | 采购、仓储、销售、财务 | **erp 本机档案/流水演示**；自动库存结转、财务闭环未做 |
| OA / 流程 | [O2OA (GitHub)](https://github.com/o2oa/o2oa)、[O2OA (Gitee)](https://gitee.com/o2oa/O2OA) | 审批、表单、流程、通知 | **oa 最小闭环**；审批身份/工作流引擎未做 |
| CMS | [Halo (GitHub)](https://github.com/halo-dev/halo)、[Halo (Gitee)](https://gitee.com/halo-dev/halo) | 内容、分类、发布、媒体 | 待建设 |
| HRM | [OrangeHRM (GitHub)](https://github.com/orangehrm/orangehrm) | 员工、假勤、招聘 | **hrm 本机员工/假勤记录演示**；排班薪酬、审批未做 |
| 工单 | [Zammad (GitHub)](https://github.com/zammad/zammad) | 工单、SLA、分配 | **service 本机客户/工单记录演示**；SLA、派单未做 |
| 制造 | 本仓新增场景，无指定参考仓库 | 物料、生产工单 | **manufacturing 本机记录演示**；MES 排程、领料、报工未做 |
| 物流 | 本仓新增场景，无指定参考仓库 | 车辆、运单 | **logistics 本机记录演示**；实时定位、调度未做 |
| 物业 | 本仓新增场景，无指定参考仓库 | 房源、报修 | **property 本机记录演示**；收费、派工未做 |
| 农业 | 本仓新增场景，无指定参考仓库 | 地块、农事 | **agriculture 本机记录演示**；传感器、溯源未做 |
| 建筑 | 本仓新增场景，无指定参考仓库 | 工程、施工日志 | **construction 本机记录演示**；进度成本、验收未做 |
| 酒店 | 本仓新增场景，无指定参考仓库 | 客房、预订 | **hospitality 本机记录演示**；房态冲突控制、支付未做 |
| 能源 | 本仓新增场景，无指定参考仓库 | 设备、巡检 | **energy 本机记录演示**；实时遥测、告警未做 |
| 法律 | 本仓新增场景，无指定参考仓库 | 委托人、案件 | **legal 本机记录演示**；文书、权限与保密未做 |
| 文旅 | 本仓新增场景，无指定参考仓库 | 场馆、活动 | **culture 本机记录演示**；票务、容量校验未做 |
| 社区 | 本仓新增场景，无指定参考仓库 | 服务项目、需求 | **community 本机记录演示**；居民身份、服务履约未做 |

> 参考仓库的许可证和技术栈各不相同。ai-hub 只采用公开产品思路，代码自写并统一 Vue + Java 21。新增系统需先明确业务实体、状态机、权限边界，再补接口测试和真实页面截图。

> 新增 13 个行业目录是独立的本机单用户 CRUD 演示，不等于对上述参考产品的完整复刻；此表保留尚未建设的 CMS 等领域，防止把行业覆盖误读为生产就绪。
