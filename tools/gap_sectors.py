"""Ten additional local-only industry record demos (ports 8169-8178)."""
GAPS = [
 ('transit',8169,'公交地铁运营','TRANSIT DESK','线路与班次登记','#2564a8','#eaf3fc',[
  ('routes','运营线路','▤',[('code','线路编号','text',1),('name','线路名称','text',1),('origin','起点','text',1),('destination','终点','text',1),('mode','交通方式','select',1,['公交','地铁'])],{'code':'DEMO-B01','name':'演示环线','origin':'东站','destination':'西站','mode':'公交'}),
  ('trips','班次记录','↗',[('routeId','关联线路','relation',1,'routes'),('serviceNo','班次编号','text',1),('eventDate','运行日期','date',1),('vehicle','车辆编号','text',1),('status','班次状态','select',1,['计划中','运行中','已完成','已取消']),('notes','交接备注','textarea',0)],{'routeId':1,'serviceNo':'DEMO-01','eventDate':'2026-10-03','vehicle':'演示车辆01','status':'计划中','notes':'虚构班次'})]),
 ('foodsafety',8170,'食品批次溯源','TRACE STUDIO','批次与流转事件手工追踪','#087d77','#e4f7f3',[
  ('batches','食品批次','▦',[('lotNo','批次编号','text',1),('product','产品名称','text',1),('source','来源单位','text',1),('productionDate','生产日期','date',1),('quantity','批次数量','number',1)],{'lotNo':'DEMO-LOT-01','product':'演示苹果','source':'虚构农场','productionDate':'2026-10-01','quantity':120}),
  ('traceevents','流转记录','↔',[('batchId','关联批次','relation',1,'batches'),('checkpoint','流转节点','text',1),('eventDate','记录日期','date',1),('status','处置状态','select',1,['正常','待核查','已隔离','已召回']),('operator','记录人','text',1),('notes','记录说明','textarea',0)],{'batchId':1,'checkpoint':'入库检查','eventDate':'2026-10-03','status':'正常','operator':'演示人员','notes':'虚构记录'})]),
 ('emergency',8171,'应急消防巡检','SAFETY LOG','场所巡检与隐患处理手工台账','#ba604a','#fff1eb',[
  ('sites','巡检场所','▣',[('code','场所编号','text',1),('name','场所名称','text',1),('area','所在区域','text',1),('contact','责任联系人','text',1)],{'code':'DEMO-S01','name':'演示楼宇','area':'东区','contact':'演示值班员'}),
  ('inspections','巡检记录','✓',[('siteId','关联场所','relation',1,'sites'),('eventDate','巡检日期','date',1),('inspector','巡检人','text',1),('finding','发现事项','text',1),('status','处理状态','select',1,['待处理','处理中','已复核']),('notes','处理说明','textarea',0)],{'siteId':1,'eventDate':'2026-10-03','inspector':'演示巡检员','finding':'消防通道检查','status':'待处理','notes':'虚构记录'})]),
 ('events',8172,'演出赛事票务台账','EVENT DESK','场馆与活动场次手工登记','#8353c0','#f5efff',[
  ('venues','活动场馆','▣',[('code','场馆编号','text',1),('name','场馆名称','text',1),('location','场馆位置','text',1),('capacity','参考容量','number',1)],{'code':'DEMO-V01','name':'演示剧场','location':'市民广场','capacity':300}),
  ('shows','活动场次','◇',[('venueId','关联场馆','relation',1,'venues'),('title','活动名称','text',1),('eventDate','活动日期','date',1),('organizer','主办方','text',1),('status','场次状态','select',1,['筹备中','开放登记','已结束','已取消']),('notes','场次说明','textarea',0)],{'venueId':1,'title':'虚构音乐夜','eventDate':'2026-10-10','organizer':'演示团队','status':'筹备中','notes':'不含真实售票'})]),
 ('maritime',8173,'港口航运记录','PORT FLOW','泊位与靠港记录台账','#236f8d','#eaf5fa',[
  ('berths','港口泊位','▣',[('code','泊位编号','text',1),('name','泊位名称','text',1),('terminal','所属码头','text',1),('capacity','参考吨位','number',1)],{'code':'DEMO-B01','name':'演示一号泊位','terminal':'东码头','capacity':5000}),
  ('portcalls','靠港记录','↗',[('berthId','关联泊位','relation',1,'berths'),('vessel','船舶名称','text',1),('eventDate','靠港日期','date',1),('cargo','货物摘要','text',1),('status','登记状态','select',1,['计划中','靠泊中','已离港']),('notes','调度备注','textarea',0)],{'berthId':1,'vessel':'演示货轮','eventDate':'2026-10-03','cargo':'虚构集装箱','status':'计划中','notes':'不控制真实调度'})]),
 ('museum',8174,'博物馆藏品档案','COLLECTIONS','藏品目录与借展记录','#8b6d3c','#faf3e6',[
  ('objects','馆藏档案','▤',[('accessionNo','入藏编号','text',1),('name','藏品名称','text',1),('period','年代说明','text',1),('storage','保存位置','text',1)],{'accessionNo':'DEMO-M01','name':'演示陶器复制品','period':'现代复制','storage':'演示库房'}),
  ('loans','借展登记','↔',[('objectId','关联藏品','relation',1,'objects'),('borrower','借展单位','text',1),('eventDate','登记日期','date',1),('status','借展状态','select',1,['待审核','借出中','已归还']),('notes','借展说明','textarea',0)],{'objectId':1,'borrower':'虚构机构','eventDate':'2026-10-03','status':'待审核','notes':'虚构借展'})]),
 ('autosales',8175,'汽车销售线索','AUTO SHOWROOM','车辆档案与客户意向记录','#4775b6','#edf3fb',[
  ('listings','在售车辆','▣',[('vinAlias','车辆编号','text',1),('model','车型名称','text',1),('mileage','参考里程','number',1),('price','参考售价','money',1),('condition','车辆类别','select',1,['新车','二手车'])],{'vinAlias':'DEMO-A01','model':'演示车型','mileage':12000,'price':68000,'condition':'二手车'}),
  ('leads','客户意向','✦',[('listingId','关联车辆','relation',1,'listings'),('customer','客户称呼','text',1),('eventDate','登记日期','date',1),('status','跟进状态','select',1,['新线索','已联系','已结束']),('notes','跟进说明','textarea',0)],{'listingId':1,'customer':'演示客户','eventDate':'2026-10-03','status':'新线索','notes':'虚构线索'})]),
 ('procurement',8176,'采购招投标台账','BID DESK','采购需求与报价记录','#755fae','#f2effc',[
  ('requests','采购需求','▤',[('requestNo','需求编号','text',1),('title','需求名称','text',1),('department','申请部门','text',1),('budget','参考预算','money',1),('deadline','截止日期','date',1)],{'requestNo':'DEMO-P01','title':'演示设备采购','department':'运营部','budget':20000,'deadline':'2026-10-15'}),
  ('bids','报价记录','◇',[('requestId','关联需求','relation',1,'requests'),('vendor','报价单位','text',1),('amount','报价金额','money',1),('eventDate','报价日期','date',1),('status','记录状态','select',1,['待核对','已核对','已归档']),('notes','报价说明','textarea',0)],{'requestId':1,'vendor':'虚构供应商','amount':18500,'eventDate':'2026-10-03','status':'待核对','notes':'非真实评标'})]),
 ('calibration',8177,'仪器计量校准','CALIBRATION','设备与校准记录追踪','#24867d','#e9f8f4',[
  ('instruments','仪器档案','▣',[('assetNo','资产编号','text',1),('name','仪器名称','text',1),('location','使用位置','text',1),('owner','保管人','text',1)],{'assetNo':'DEMO-C01','name':'演示温度计','location':'实验室A','owner':'演示人员'}),
  ('calibrations','校准记录','✓',[('instrumentId','关联仪器','relation',1,'instruments'),('certificateNo','记录编号','text',1),('eventDate','校准日期','date',1),('dueDate','下次日期','date',1),('status','记录状态','select',1,['待复核','已记录','待复检']),('notes','校准说明','textarea',0)],{'instrumentId':1,'certificateNo':'DEMO-CAL-01','eventDate':'2026-10-03','dueDate':'2027-10-03','status':'待复核','notes':'非正式证书'})]),
 ('auditfirm',8178,'审计事务所项目','AUDIT WORKPAPER','审计项目与工作底稿登记','#526a9f','#edf1fa',[
  ('engagements','审计项目','▤',[('code','项目编号','text',1),('client','客户代称','text',1),('scope','服务范围','text',1),('year','会计年度','number',1)],{'code':'DEMO-AUD-01','client':'虚构公司','scope':'演示审阅','year':2026}),
  ('workpapers','工作底稿','▣',[('engagementId','关联项目','relation',1,'engagements'),('section','底稿章节','text',1),('reviewer','复核人','text',1),('eventDate','记录日期','date',1),('status','复核状态','select',1,['草稿','待复核','已复核']),('notes','底稿摘要','textarea',0)],{'engagementId':1,'section':'收入核对','reviewer':'演示复核人','eventDate':'2026-10-03','status':'草稿','notes':'虚构底稿'})]),
]
