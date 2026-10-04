"""High-priority local workflow demos; keep stable slugs and ports."""
PRIORITIES = [
 ('parcelstation',8179,'快递驿站','PARCEL STATION','入库、取件码与异常件处理','#286f98','#e8f4fa',[
   ('shelves','货架档案','▤',[('code','货架编号','text',1),('name','货架名称','text',1),('capacity','参考容量','number',1),('location','存放区域','text',1)],{'code':'DEMO-A01','name':'演示A架','capacity':60,'location':'演示门店'}),
   ('parcels','包裹流转','↗',[('shelfId','存放货架','relation',1,'shelves'),('trackingNo','包裹编号','text',1),('recipient','收件人代称','text',1),('pickupCode','取件码','text',1),('eventDate','入库日期','date',1),('status','流转状态','select',1,['待取件','已签收','异常件']),('notes','异常或签收备注','textarea',0)],{'shelfId':1,'trackingNo':'DEMO-P01','recipient':'演示顾客','pickupCode':'DEMO-01','eventDate':'2026-10-04','status':'待取件','notes':'虚构包裹'})]),
 ('recycling',8180,'再生资源回收','RECYCLE DESK','物料、称重与回收结算记录','#218077','#e8f7f2',[
   ('materials','回收物料','♻',[('code','物料编号','text',1),('name','物料名称','text',1),('unit','计量单位','text',1),('price','参考单价','money',1)],{'code':'DEMO-R01','name':'演示纸类','unit':'千克','price':1.20}),
   ('receipts','回收单据','▣',[('materialId','关联物料','relation',1,'materials'),('receiptNo','单据编号','text',1),('source','来源代称','text',1),('weight','重量（千克）','money',1),('amount','应付金额','money',1),('eventDate','回收日期','date',1),('status','单据状态','select',1,['待核对','已结算','已作废']),('notes','分类与流转备注','textarea',0)],{'materialId':1,'receiptNo':'DEMO-RC01','source':'演示住户','weight':10,'amount':12,'eventDate':'2026-10-04','status':'待核对','notes':'虚构称重记录'})]),
 ('childcare',8181,'托育机构','CHILDCARE DESK','班级、接送与日常照护交接','#8066ac','#f4effa',[
   ('groups','托育班级','▣',[('code','班级编号','text',1),('name','班级名称','text',1),('room','活动室','text',1),('staff','责任老师代称','text',1)],{'code':'DEMO-C01','name':'演示托班','room':'演示活动室','staff':'演示老师'}),
   ('handoffs','照护交接','♡',[('groupId','所属班级','relation',1,'groups'),('childAlias','儿童代称','text',1),('guardianAlias','接送人代称','text',1),('eventDate','交接日期','date',1),('type','交接类型','select',1,['入园','离园']),('status','交接状态','select',1,['待交接','已交接','异常待核']),('notes','喂养午睡与交接备注','textarea',0)],{'groupId':1,'childAlias':'演示儿童','guardianAlias':'演示家长','eventDate':'2026-10-04','type':'入园','status':'待交接','notes':'无真实个人资料'})]),
]
