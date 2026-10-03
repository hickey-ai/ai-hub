"""Create the first wave of independent industry demos from the proven record app."""
from pathlib import Path
import json, shutil

root = Path(__file__).resolve().parents[1]
source = root/'carcare'
# key, label, type, required, options/ref
SECTORS = [
 ('erp',8101,'进销存 ERP','SUPPLY OS','把采购、库存与销售接成一条清晰的业务线','#5365d9','#edefff',[
   ('products','商品档案','▣',[('sku','商品编码','text',1),('name','商品名称','text',1),('unit','单位','text',1),('stock','库存数量','number',1),('price','参考单价','money',1)],{'sku':'DEMO-101','name':'办公笔记本','unit':'本','stock':120,'price':12.5}),
   ('movements','出入库流水','↔',[('productId','关联商品','relation',1,'products'),('type','业务类型','select',1,['采购入库','销售出库','库存调整']),('quantity','数量','number',1),('date','发生日期','date',1),('counterparty','供应商／客户','text',1),('notes','业务备注','textarea',0)],{'productId':1,'type':'采购入库','quantity':30,'date':'2026-10-01','counterparty':'演示供应商','notes':'虚构入库记录'})]),
 ('manufacturing',8102,'制造执行','FACTORY OS','从物料到工单，给生产现场一个清楚的进度视图','#744fca','#f3edff',[
   ('materials','物料档案','▦',[('code','物料编号','text',1),('name','物料名称','text',1),('unit','单位','text',1),('available','可用数量','number',1),('supplier','供应商','text',1)],{'code':'MAT-101','name':'铝合金壳体','unit':'件','available':320,'supplier':'演示供应商'}),
   ('workorders','生产工单','◈',[('materialId','主要物料','relation',1,'materials'),('orderNo','工单编号','text',1),('product','生产产品','text',1),('quantity','计划数量','number',1),('dueDate','交付日期','date',1),('status','工单状态','select',1,['待排产','生产中','质检中','已完成']),('notes','生产备注','textarea',0)],{'materialId':1,'orderNo':'WO-2026-01','product':'智能控制盒','quantity':80,'dueDate':'2026-10-15','status':'生产中','notes':'演示工单'})]),
 ('logistics',8103,'物流运输','FLEET FLOW','订单、车辆与运单状态在一张工作台上流动','#0c8194','#e9f8fa',[
   ('vehicles','运输车辆','▣',[('plate','车辆编号','text',1),('driver','驾驶员','text',1),('capacity','载重（kg）','number',1),('status','车辆状态','select',1,['待命','运输中','保养中'])],{'plate':'演示货车-01','driver':'张师傅','capacity':3000,'status':'运输中'}),
   ('shipments','运输运单','↗',[('vehicleId','承运车辆','relation',1,'vehicles'),('trackingNo','运单编号','text',1),('origin','始发地','text',1),('destination','目的地','text',1),('dispatchDate','发车日期','date',1),('status','运单状态','select',1,['待发车','在途','已签收','异常']),('notes','运输备注','textarea',0)],{'vehicleId':1,'trackingNo':'DEMO-EXP-01','origin':'上海','destination':'杭州','dispatchDate':'2026-10-01','status':'在途','notes':'虚构演示运单'})]),
]

def java_seed(resource, item):
    pairs=[]
    for k,v in item.items():
        value = '"'+v.replace('"','\\"')+'"' if isinstance(v,str) else str(v)
        pairs.append(f'"{k}", {value}')
    return f'seed("{resource}", Map.of({", ".join(pairs)}));'

def validator(entities):
    blocks=[]
    for key,_,_,fields,_ in entities:
        calls=[]
        for field in fields:
            name,_,type_,required,*extra=field
            if type_ in ('text','textarea'): call=f'text(input, out, "{name}", {str(bool(required)).lower()});'
            elif type_=='select': call=f'choice(input, out, "{name}", {", ".join(json.dumps(x,ensure_ascii=False) for x in extra[0])});'
            elif type_=='relation': call=f'relation(input, out, "{name}", "{extra[0]}");'
            elif type_=='number': call=f'number(input, out, "{name}");'
            elif type_=='money': call=f'money(input, out, "{name}");'
            elif type_=='date': call=f'date(input, out, "{name}");'
            calls.append(call)
        blocks.append(f'case "{key}" -> {{\n                '+ '\n                '.join(calls)+'\n            }')
    return ' '.join(blocks)

from extra_sectors import EXTRA
from new_sectors import NEW
from more_sectors import EXTRA as MORE
from platform_sectors import PLATFORMS
from gap_sectors import GAPS
SECTORS += EXTRA + NEW + MORE + PLATFORMS + GAPS

# Fail before copying a template: duplicate keys would make Java Map.of seeds crash at startup.
if len({s[0] for s in SECTORS}) != len(SECTORS) or len({s[1] for s in SECTORS}) != len(SECTORS):
    raise ValueError("Duplicate sector slug or port")
for sector in SECTORS:
    for entity in sector[7]:
        names = [field[0] for field in entity[3]]
        if len(names) != len(set(names)):
            raise ValueError(f"Duplicate fields in {sector[0]}/{entity[0]}")

for slug,port,cn,en,subtitle,accent,soft,entities in SECTORS:
    dst=root/slug
    if dst.exists():
        print('skip existing', slug)
        continue
    shutil.copytree(source,dst,ignore=shutil.ignore_patterns('node_modules','target','dist','static','data','screenshots','package-lock.json'))
    (dst/'screenshots').mkdir()
    class_name=slug.capitalize()
    pkg=dst/'backend/src/main/java/dev/aihub'/slug
    pkg.mkdir(parents=True)
    java=(source/'backend/src/main/java/dev/aihub/carcare/CarcareApplication.java').read_text(encoding='utf-8')
    java=java.replace('package dev.aihub.carcare;',f'package dev.aihub.{slug};').replace('CarcareApplication',class_name+'Application')
    java=java.replace('List.of("vehicles", "services")','List.of('+', '.join(f'"{e[0]}"' for e in entities)+')')
    start=java.index('            var car = seed(')
    end=java.index('\n        }',start)
    java=java[:start]+'            '+'\n            '.join(java_seed(e[0],e[4]) for e in entities)+java[end:]
    start=java.index('        String childKey = switch (resource)')
    end=java.index('        var removed = list.remove(index);',start)
    parent,child=entities[0][0],entities[1][0]
    ref=next(f[0] for f in entities[1][3] if f[2]=='relation')
    java=java[:start]+f'''        if (resource.equals("{parent}") && records("{child}").stream()
            .anyMatch(row -> ((Number) row.get("{ref}")).longValue() == id))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "请先删除关联记录");
'''+java[end:]
    start=java.index('        switch(resource) {')
    end=java.index('        return out;',start)
    java=java[:start]+'        switch(resource) { '+validator(entities)+' default -> throw new ResponseStatusException(HttpStatus.NOT_FOUND); }\n'+java[end:]
    (pkg/(class_name+'Application.java')).write_text(java,encoding='utf-8')
    shutil.rmtree(dst/'backend/src/main/java/dev/aihub/carcare')
    pom=dst/'backend/pom.xml'; pom.write_text(pom.read_text(encoding='utf-8').replace('carcare-api',slug+'-api'),encoding='utf-8')
    (dst/'backend/src/main/resources/application.properties').write_text(f'server.port={port}\nserver.address=127.0.0.1\naihub.data-file=data/{slug}.json\n',encoding='utf-8')
    tests=dst/'backend/src/test/java/dev/aihub'
    shutil.rmtree(tests/'carcare')
    (tests/slug).mkdir()
    base=(source/'backend/src/test/java/dev/aihub/carcare/CarcareApplicationTests.java').read_text(encoding='utf-8')
    # Tests are generated explicitly below rather than inheriting carcare assumptions.
    example=json.dumps(entities[1][4],ensure_ascii=False)
    test=f'''package dev.aihub.{slug};
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;
@SpringBootTest @AutoConfigureMockMvc
class {class_name}ApplicationTests {{
    static final Path FILE;
    static {{ try {{ FILE=Files.createTempDirectory("aihub-{slug}-").resolve("data.json"); }} catch(Exception e) {{ throw new ExceptionInInitializerError(e); }} }}
    @DynamicPropertySource static void props(DynamicPropertyRegistry r) {{ r.add("aihub.data-file",()->FILE.toString()); }}
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Test void crudValidationAndPersistence() throws Exception {{
        String resource="{child}";
        mvc.perform(get("/api/{parent}")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").exists());
        mvc.perform(get("/api/unknown")).andExpect(status().isNotFound());
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content("{{}}")).andExpect(status().isBadRequest());
        String payload=mapper.writeValueAsString(mapper.readValue("{example.replace(chr(34),chr(92)+chr(34))}",java.util.Map.class));
        String created=mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long id=mapper.readTree(created).get("id").asLong();
        mvc.perform(delete("/api/{parent}/1")).andExpect(status().isConflict());
        mvc.perform(put("/api/"+resource+"/"+id).contentType(MediaType.APPLICATION_JSON).content(payload)).andExpect(status().isOk());
        assertTrue(Files.exists(FILE));
        assertEquals(2,new RecordsController(mapper,FILE.toString()).list(resource).size());
        mvc.perform(delete("/api/"+resource+"/"+id)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/"+resource+"/"+id)).andExpect(status().isNotFound());
    }}
}}
'''
    # Exercise relation, enum and date validation for every generated sector.
    invalid=f'''        var badRelation=mapper.readTree(payload).deepCopy();
        ((com.fasterxml.jackson.databind.node.ObjectNode)badRelation).put("{ref}", 99999);
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(badRelation))).andExpect(status().isBadRequest());
        var badStatus=mapper.readTree(payload).deepCopy();
        ((com.fasterxml.jackson.databind.node.ObjectNode)badStatus).put("status", "INVALID_STATUS");
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(badStatus))).andExpect(status().isBadRequest());
        var badDate=mapper.readTree(payload).deepCopy();
        ((com.fasterxml.jackson.databind.node.ObjectNode)badDate).put("eventDate", "bad-date");
        mvc.perform(post("/api/"+resource).contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(badDate))).andExpect(status().isBadRequest());
'''
    # Early sectors have varying field names; add this only for the newer schema.
    if any(f[0]=='eventDate' for f in entities[1][3]) and any(f[0]=='status' for f in entities[1][3]):
        test=test.replace('        String created=mvc.perform(',invalid+'        String created=mvc.perform(')
    (tests/slug/(class_name+'ApplicationTests.java')).write_text(test,encoding='utf-8')
    config={'cn':cn,'en':en,'subtitle':subtitle,'accent':accent,'soft':soft,'entities':[{'key':k,'title':title,'singular':title,'icon':icon,'fields':[list(f) for f in fields],'columns':[f[0] for f in fields if f[2]!='textarea']} for k,title,icon,fields,_ in entities]}
    (dst/'frontend/src/config.js').write_text('export default '+json.dumps(config,ensure_ascii=False,indent=2)+'\n',encoding='utf-8')
    app=dst/'frontend/src/App.vue'; content=app.read_text(encoding='utf-8')
    content=content.replace('AI-HUB / LIFE RECORDS','AI-HUB / INDUSTRY STUDIO')
    content=content.replace("const parent = config.entities.find(e => e.key === (key === 'vehicleId' ? 'vehicles' : 'children'))", "const relation = config.entities.flatMap(e => e.fields).find(f => f[0] === key && f[2] === 'relation')\n    const parent = config.entities.find(e => e.key === relation?.[4])")
    content=content.replace('return found?.plate || found?.name || \'已删除档案\'', "return found?.[parent?.fields[0][0]] || '已删除档案'")
    content=content.replace(':value="x.id">{{x.plate||x.name}}', ':value="x.id">{{x[config.entities.find(e=>e.key===field[4]).fields[0][0]]}}')
    content=content.replace('const recent = computed(', '''const activityLabel = item => {
  const entity = config.entities.at(-1)
  const field = entity.fields.find(f => !['relation','date','select','textarea'].includes(f[2]))
  return `${entity.title} · ${item[field?.[0]] ?? item.id}`
}
const recent = computed(''',1)
    content=content.replace('<strong>{{item.title||item.type||item.name||item.plate}}</strong><small>{{item.date||item.startAt||item.birthDate}} · {{item.category||item.model||\'已保存\'}}</small>', '<strong>{{activityLabel(item)}}</strong><small>{{item.eventDate||item.date||item.startAt||\'\'}} · {{item.status||item.category||\'已保存\'}}</small>')
    app.write_text(content,encoding='utf-8')
    html=dst/'frontend/index.html'; html.write_text(html.read_text(encoding='utf-8').replace('汽车维修记录',cn),encoding='utf-8')
    package=dst/'frontend/package.json'; package.write_text(package.read_text(encoding='utf-8').replace('carcare',slug).replace('9095',str(port+1000)),encoding='utf-8')
    vite=dst/'frontend/vite.config.js'; vite.write_text(vite.read_text(encoding='utf-8').replace('8095',str(port)),encoding='utf-8')
    for script in ('run.ps1','run.sh'):
        p=dst/script; content=p.read_text(encoding='utf-8').replace('carcare-api',slug+'-api'); p.write_bytes(content.encode('utf-8')) if script=='run.sh' else p.write_text(content,encoding='utf-8')
    (dst/'README.md').write_text(f'''# {cn} · {slug}\n\n{subtitle}。Vue 3 + Java 21 / Spring Boot 独立本机单用户演示，提供 {entities[0][1]} 和 {entities[1][1]} 两个关联的业务页面，支持检索、新增、修改、删除和本地 JSON 持久化。首次载入虚构数据，后续存储在 `data/{slug}.json`。\n\n## 直接体验\n\n准备 Java 21、Maven、Node.js 20.19+/22.12+、npm。在仓库根目录运行 `./{slug}/run.ps1`（Windows）或 `./{slug}/run.sh`（macOS/Linux），打开 http://127.0.0.1:{port}。首次构建会联网下载依赖，Ctrl+C 停止。\n\n## API 与测试\n\n`GET /api/{{resource}}`、`POST /api/{{resource}}`、`PUT /api/{{resource}}/{{id}}`、`DELETE /api/{{resource}}/{{id}}`。字段、必填、类别、数字、日期、关联关系均在服务端校验。无效输入 400、不存在 404、删除被引用档案 409。执行 `mvn -f {slug}/backend/pom.xml test`，或根目录运行 `./test-all.ps1`。\n\n## 真实页面截图\n\n![{cn}总览](./screenshots/overview.png)\n![{entities[0][1]}](./screenshots/primary.png)\n![{entities[1][1]}](./screenshots/secondary.png)\n![新增表单](./screenshots/editor.png)\n\n## 使用边界\n\n本机单用户样板，不含正式鉴权、权限隔离、数据库、并发写入、外部设备、真实资金或生产流程控制；状态由用户手动维护。请勿录入敏感或真实业务数据，也不要直接部署到公网。\n''',encoding='utf-8')
    print(slug,port)
