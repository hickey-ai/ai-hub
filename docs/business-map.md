# 业务系统调研与建设清单

2026-09-30 从 GitHub/Gitee 可公开访问项目中抽样，侧重业务模块、交互模式和技术边界；参考链接**不是**复制授权，也不表示它们全部使用 Java 21。该调研非全网穷举。

| 领域 | 参考项目 | 可借鉴的领域能力 | ai-hub 状态 |
| --- | --- | --- | --- |
| 商城 | [mall (GitHub)](https://github.com/macrozheng/mall)、[mall (Gitee)](https://gitee.com/macrozheng/mall) | 商品、订单、库存、营销 | **shop 最小闭环**；支付/售后未做 |
| 管理后台 | [RuoYi-Vue-Plus (GitHub)](https://github.com/dromara/RuoYi-Vue-Plus)、[RuoYi-Vue-Plus (Gitee)](https://gitee.com/dromara/RuoYi-Vue-Plus) | 用户、角色、菜单、权限 | **manage 演示样板**；生产 RBAC 未做 |
| CRM | [悟空CRM (GitHub)](https://github.com/WuKongOpenSource/WukongCRM-11.0-JAVA)、[悟空CRM (Gitee)](https://gitee.com/wukongcrm) | 客户、线索、商机、跟进 | **crm 最小闭环**；正式鉴权/线索池未做 |
| ERP | [iDempiere (GitHub)](https://github.com/idempiere/idempiere)、[JshERP (Gitee)](https://gitee.com/jishenghua/JSH_ERP) | 采购、仓储、销售、财务 | 待建设 |
| OA / 流程 | [O2OA (GitHub)](https://github.com/o2oa/o2oa)、[O2OA (Gitee)](https://gitee.com/o2oa/O2OA) | 审批、表单、流程、通知 | **oa 最小闭环**；审批身份/工作流引擎未做 |
| CMS | [Halo (GitHub)](https://github.com/halo-dev/halo)、[Halo (Gitee)](https://gitee.com/halo-dev/halo) | 内容、分类、发布、媒体 | 待建设 |
| HRM | [OrangeHRM (GitHub)](https://github.com/orangehrm/orangehrm) | 员工、假勤、招聘 | 待建设 |
| 工单 | [Zammad (GitHub)](https://github.com/zammad/zammad) | 工单、SLA、分配 | 待建设 |

> 参考仓库的许可证和技术栈各不相同。ai-hub 只采用公开产品思路，代码自写并统一 Vue + Java 21。新增系统需先明确业务实体、状态机、权限边界，再补接口测试和真实页面截图。
