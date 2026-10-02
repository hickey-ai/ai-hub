"""Independent local QA and internal-service record desks.

These are manual single-user demos, not CI runners or multi-user help desks.
"""
PLATFORMS = [
 ('testops', 8165, '测试管理平台', 'TEST STUDIO', '从测试用例到手工执行结果，保留可追溯的测试记录', '#5359d6', '#eff0ff', [
   ('testcases', '测试用例', '▤', [('caseNo','用例编号','text',1),('title','用例标题','text',1),('module','所属模块','text',1),('priority','优先级','select',1,['P0','P1','P2','P3']),('steps','操作步骤','textarea',1),('expected','预期结果','textarea',1)], {'caseNo':'TC-DEMO-01','title':'购物车添加商品','module':'商城','priority':'P1','steps':'打开商品详情，点击加入购物车','expected':'购物车数量增加 1'}),
   ('executions', '执行记录', '✓', [('caseId','关联用例','relation',1,'testcases'),('version','测试版本','text',1),('eventDate','执行日期','date',1),('result','执行结果','select',1,['未执行','通过','失败','阻塞']),('status','复核状态','select',1,['待复核','已复核']),('tester','执行人','text',1),('actual','实际结果','textarea',0)], {'caseId':1,'version':'v0.1.0','eventDate':'2026-10-02','result':'通过','status':'待复核','tester':'演示测试员','actual':'购物车数量增加 1'})
 ]),
 ('ticketops', 8166, '内部工单平台', 'TICKET DESK', '按队列记录需求、指派处理人与工单进度', '#277d9d', '#eaf6fb', [
   ('queues', '工单队列', '▦', [('code','队列编号','text',1),('name','队列名称','text',1),('owner','负责团队','text',1),('status','队列状态','select',1,['启用','停用'])], {'code':'IT-HELP','name':'IT 服务台','owner':'演示支持组','status':'启用'}),
   ('tickets', '内部工单', '✦', [('queueId','所属队列','relation',1,'queues'),('ticketNo','工单编号','text',1),('title','工单标题','text',1),('priority','优先级','select',1,['低','普通','高','紧急']),('status','处理状态','select',1,['待受理','处理中','待确认','已解决','已关闭']),('assignee','处理人','text',1),('eventDate','登记日期','date',1),('notes','处理备注','textarea',0)], {'queueId':1,'ticketNo':'IT-2026-001','title':'演示设备接入申请','priority':'普通','status':'处理中','assignee':'演示处理人','eventDate':'2026-10-02','notes':'虚构内部请求'})
 ]),
 ('bugtrack', 8167, '缺陷跟踪平台', 'BUG TRACKER', '把项目缺陷、复现步骤和修复状态整理在同一工作台', '#9d5b88', '#fbedf5', [
   ('projects', '项目档案', '▣', [('code','项目编号','text',1),('name','项目名称','text',1),('owner','负责人','text',1),('status','项目状态','select',1,['进行中','已归档'])], {'code':'DEMO-WEB','name':'演示网页项目','owner':'演示项目组','status':'进行中'}),
   ('defects', '缺陷记录', '◇', [('projectId','关联项目','relation',1,'projects'),('issueNo','缺陷编号','text',1),('title','问题标题','text',1),('severity','严重程度','select',1,['低','中','高','致命']),('status','缺陷状态','select',1,['待确认','待修复','修复中','待验证','已关闭']),('reporter','报告人','text',1),('eventDate','发现日期','date',1),('steps','复现步骤','textarea',0)], {'projectId':1,'issueNo':'BUG-001','title':'演示按钮状态异常','severity':'中','status':'待验证','reporter':'演示测试员','eventDate':'2026-10-02','steps':'打开详情页后切换状态'})
 ])
]