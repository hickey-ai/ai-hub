"""Additional sector-specific local record demos.

Each tuple describes two linked records, not a production domain workflow.
"""

def text(key, label): return (key, label, 'text', 1)
def num(key, label): return (key, label, 'number', 1)
def money(key, label): return (key, label, 'money', 1)
def date(key, label): return (key, label, 'date', 1)
def choice(key, label, options): return (key, label, 'select', 1, options)

# slug, name, parent key/title, parent field/value, child key/title,
# child field/value, state choices, sample state, use-case description
ROWS = [
 ('parking','停车场','lots','停车区域',text('capacity','车位总数'),120,'stays','停车记录',text('plate','车牌编号'),'演示车辆-01',['在场','已离场'],'在场','车位与进出场登记'),
 ('charging','充电站','stations','充电站点',num('piles','充电桩数量'),16,'sessions','充电记录',num('energy','电量（kWh）'),24,['进行中','已完成','异常'],'进行中','充电站点与充电登记'),
 ('parkops','园区运营','parks','园区档案',text('district','所在区域'),'演示园区A','inspections','巡检记录',text('inspector','巡检人员'),'演示巡检员',['待处理','处理中','已完成'],'待处理','园区设施与巡检台账'),
 ('fleet','车队管理','vehicles','车队车辆',text('plate','车辆编号'),'演示车队-01','trips','行车任务',text('driver','驾驶人员'),'演示司机',['待发车','运输中','已归档'],'待发车','车队与行车任务'),
 ('scenic','景区管理','spots','景点档案',num('dailyCapacity','参考日容量'),800,'visits','游览登记',text('visitorGroup','团队名称'),'演示游客团',['已预约','已入园','已结束'],'已预约','景点资源与游览登记'),
 ('clinic','诊所管理','clinics','诊室档案',text('specialty','服务方向'),'全科','appointments','预约登记',text('patientAlias','访客代称'),'演示访客',['待到诊','已到诊','已取消'],'待到诊','诊室与预约登记'),
 ('dental','口腔门诊','chairs','牙椅档案',text('room','诊室编号'),'演示诊室A','visits','就诊登记',text('patientAlias','访客代称'),'演示访客',['预约中','已到诊','已结束'],'预约中','牙椅与就诊排期'),
 ('aesthetics','医美服务','services','服务项目',money('referencePrice','参考价格'),399,'consultations','咨询登记',text('visitorAlias','访客代称'),'演示访客',['待沟通','沟通中','已结束'],'待沟通','项目与咨询跟进'),
 ('rehab','康复训练','programs','训练方案',num('sessions','计划次数'),12,'sessions','训练记录',text('participantAlias','参与者代称'),'演示参与者',['待开始','进行中','已完成'],'待开始','方案与训练记录'),
 ('lis','检验台账','assays','检验项目',text('specimenType','标本类型'),'演示样本','samples','样本登记',text('sampleCode','样本编号'),'DEMO-SAMPLE-01',['待接收','处理中','已记录'],'待接收','项目与样本流转演示'),
 ('kindergarten','幼儿园管理','classes','班级档案',num('capacity','班级容量'),25,'activities','班级活动',text('teacher','带班教师'),'演示老师',['筹备中','进行中','已结束'],'筹备中','班级与活动记录'),
 ('training','培训机构','courses','培训课程',num('hours','课程课时'),24,'enrollments','报名登记',text('learnerAlias','学员代称'),'演示学员',['已咨询','已登记','已结束'],'已咨询','课程与报名记录'),
 ('elearning','在线学习','courses','线上课程',num('lessons','章节数量'),8,'progress','学习进度',text('learnerAlias','学员代称'),'演示学员',['未开始','学习中','已完成'],'学习中','课程与学习记录'),
 ('exam','考试管理','exams','考试场次',num('seatCount','座位数量'),40,'registrations','报考登记',text('candidateAlias','考生代称'),'演示考生',['待确认','已确认','已结束'],'待确认','场次与报考登记'),
 ('library','图书馆','books','馆藏图书',text('isbn','馆藏编号'),'DEMO-BOOK-01','loans','借阅登记',text('readerAlias','读者代称'),'演示读者',['借出','已归还','逾期标记'],'借出','图书与借阅台账'),
 ('petcare','宠物照护','pets','宠物档案',text('species','宠物类别'),'猫','carelogs','照护记录',text('caregiver','照护人员'),'演示店员',['待处理','进行中','已完成'],'待处理','宠物与照护记录'),
 ('petboarding','宠物寄养','rooms','寄养空间',num('capacity','容纳数量'),4,'stays','寄养登记',text('petAlias','宠物昵称'),'团团',['已预约','寄养中','已离店'],'已预约','房间与寄养登记'),
 ('petgrooming','宠物美容','packages','美容服务',money('referencePrice','参考价格'),128,'appointments','美容预约',text('petAlias','宠物昵称'),'团团',['待服务','服务中','已完成'],'待服务','服务与预约登记'),
 ('veterinary','宠物诊所','clinics','诊室档案',text('focus','服务方向'),'日常咨询','visits','到访记录',text('petAlias','宠物昵称'),'团团',['已预约','已到访','已结束'],'已预约','诊室与到访台账'),
 ('pos','门店收银','counters','收银台档案',text('counterArea','门店区域'),'一楼','receipts','收银记录',money('amount','登记金额'),89.9,['待核对','已登记','已作废'],'待核对','收银台与手工交易台账'),
 ('loyalty','会员运营','tiers','会员等级',num('pointsThreshold','积分门槛'),100,'members','会员档案',text('memberAlias','会员代称'),'演示会员',['新建','活跃','暂停'],'新建','等级与会员登记'),
 ('laundry','洗衣门店','machines','洗护设备',text('model','设备型号'),'演示机型A','orders','洗护订单',text('garment','衣物描述'),'演示外套',['待收件','洗护中','待取件'],'待收件','设备与洗护订单'),
 ('gym','健身房','classes','团体课程',num('capacity','课程人数'),20,'bookings','课程预约',text('memberAlias','会员代称'),'演示会员',['待上课','已签到','已结束'],'待上课','课程与预约台账'),
 ('photography','摄影工作室','packages','拍摄套餐',money('referencePrice','参考价格'),599,'shoots','拍摄预约',text('clientAlias','客户代称'),'演示客户',['待拍摄','修片中','已交付'],'待拍摄','套餐与拍摄预约'),
 ('wedding','婚庆策划','plans','婚礼方案',money('budget','参考预算'),12000,'events','婚礼档期',text('clientAlias','客户代称'),'演示客户',['筹备中','执行中','已完成'],'筹备中','方案与婚礼档期'),
 ('accounting','财务记账','ledgers','账簿分类',text('category','账目类别'),'演示经营费用','entries','记账条目',money('amount','记账金额'),230,['待核对','已记录','已作废'],'待核对','账簿与手工记账'),
 ('contracts','合同管理','templates','合同模板',text('version','模板版本'),'v1','contracts','合同登记',text('counterparty','相对方'),'演示客户',['草拟','待签署','已归档'],'草拟','模板与合同台账'),
 ('projectops','项目执行','projects','项目档案',text('owner','负责人'),'演示负责人','milestones','项目里程碑',text('deliverable','交付物'),'演示报告',['待开始','进行中','已完成'],'待开始','项目与里程碑跟踪'),
 ('maintenance','设备维保','assets','设备档案',text('model','设备型号'),'演示设备A','workorders','维保工单',text('technician','维保人员'),'演示工程师',['待处理','处理中','已完成'],'待处理','设备与维保工单'),
 ('qms','质量管理','standards','检验标准',text('revision','标准版本'),'v1','checks','质量检查',text('batchCode','批次编号'),'DEMO-BATCH-01',['待检','检查中','已记录'],'待检','标准与检查记录'),
 ('coldchain','冷链运输','containers','冷链箱档案',num('targetTemp','目标温度（℃）'),4,'transports','运输记录',text('route','运输线路'),'演示线路A',['待发运','在途','已到达'],'待发运','容器与运输登记'),
 ('freshdelivery','生鲜配送','routes','配送线路',text('region','配送区域'),'演示片区','deliveries','配送任务',text('driver','配送人员'),'演示配送员',['待分配','配送中','已送达'],'待分配','线路与配送任务'),
 ('crossborder','跨境业务','products','跨境商品',text('origin','原产地区'),'演示产地','shipments','跨境运单',text('destination','目的地区'),'演示地区',['待准备','运输中','已签收'],'待准备','商品与跨境运单'),
 ('returns','退换货','products','商品档案',text('sku','商品编码'),'DEMO-RETURN-01','requests','退换申请',text('reason','申请原因'),'演示原因',['待审核','处理中','已结束'],'待审核','商品与退换登记'),
 ('realestate','房产交易','listings','房源档案',num('areaSqm','建筑面积（㎡）'),89,'viewings','带看记录',text('clientAlias','客户代称'),'演示客户',['待带看','已带看','已取消'],'待带看','房源与带看台账'),
]

EXTRA = []
for i, (slug, cn, pk, pt, pf, pv, ck, ct, cf, cv, states, state, desc) in enumerate(ROWS):
    parent_fields = [text('name', pt + '名称'), pf, text('location', '位置／备注')]
    parent_seed = {'name': f'演示{cn}A', pf[0]: pv, 'location': '虚构演示地址'}
    if slug == 'realestate':
        parent_fields.append(money('askingPrice', '参考挂牌价（元）'))
        parent_seed['askingPrice'] = 1500000
    if slug == 'scenic':
        parent_fields.append(text('openingHours', '开放时间说明'))
        parent_seed['openingHours'] = '09:00–17:00（演示）'
    relation = (pk[:-1] + 'Id', '关联' + pt, 'relation', 1, pk)
    child_fields = [relation, cf, date('eventDate', '登记日期'), choice('status', '状态', states), ('notes', '备注', 'textarea', 0)]
    child_seed = {relation[0]: 1, cf[0]: cv, 'eventDate': '2026-10-01', 'status': state, 'notes': '虚构演示记录'}
    EXTRA.append((slug, 8130+i, cn, slug.upper() + ' OS', desc, '#217c9a', '#e8f5f9', [
        (pk, pt, '▣', parent_fields, parent_seed), (ck, ct, '◇', child_fields, child_seed)
    ]))
