"""Regenerate the two-axis portfolio matrix from the bilingual project catalogs."""
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]
SECTIONS = re.compile(r'<a id="([a-z]+)"></a>\n## ([^\n]+) · (\d+)\n(.*?)(?=<a id=|## How to|## 如何使用)', re.S)
PROJECT = re.compile(r'\[([^\]]+?) · `([a-z0-9]+)`\]\(\.\./\2/README\.md\)')
SOURCE = 'https://www.miit.gov.cn/gxsj/tjfx/rjy/art/2026/art_65a12a560865432bb1548fdddc74f19c.html'


def catalog(language):
    text = (ROOT / ('docs/project-catalog.md' if language == 'zh' else 'docs/project-catalog.en.md')).read_text(encoding='utf-8')
    sections = []
    for anchor, heading, expected, body in SECTIONS.findall(text):
        entries = PROJECT.findall(body)
        if len(entries) != int(expected):
            raise ValueError(f'{language} {anchor}: {len(entries)} != {expected}')
        sections.append((anchor, heading, entries))
    if len(sections) != 12 or sum(len(entries) for _, _, entries in sections) != 101:
        raise ValueError(f'{language}: catalog does not contain 12 categories / 101 projects')
    return sections


def render(language):
    sections = catalog(language)
    is_zh = language == 'zh'
    lines = ([
        '# 产品矩阵：业务场景 × 软件业统计口径', '',
        '**[中文](./product-matrix.md) · [English](./product-matrix.en.md)**', '',
        '这里的 **12 类**是 ai-hub 按客户任务建立的导航；工信部软件业运行情况按 **软件产品、信息技术服务、信息安全产品和服务、嵌入式系统软件** 分项统计业务收入。两者不是同一分类体系，不能直接相加。下表只表示本仓库项目的**产品形态对应方向**，不是企业收入归属、官方认定或产品认证。依据：[工信部《2025年软件业运行情况》](' + SOURCE + ')（访问：2026-10-05）；应以相应时期的统计报表和正式标准为准。', '',
        '现有 101 项都是独立本机应用/工具样板，故仅放在“软件产品方向”；其他三列用“未交付”而不是把一个记录界面硬算为服务、安全产品或固件。**目录链接证明项目存在，不证明生产就绪。**', '',
        '| 客户业务场景 | 软件产品方向（现有项目） | 信息技术服务 | 信息安全产品和服务 | 嵌入式系统软件 |',
        '| :--- | :--- | :---: | :---: | :---: |',
    ] if is_zh else [
        '# Product matrix: customer domains × software-industry reporting', '',
        '**[中文](./product-matrix.md) · [English](./product-matrix.en.md)**', '',
        'The **12 customer domains** are repository navigation, while MIIT software-industry operating reports separately track revenue from **software products, IT services, information-security products and services, and embedded-system software**. These axes cannot be added together. This matrix indicates an **approximate product-form direction**, not official classification, reported revenue or certification. Source: [MIIT 2025 software-industry operating report](' + SOURCE + ') (accessed 2026-10-05). Consult the applicable official reporting rules for any formal filing.', '',
        'The 101 existing projects are independent local applications/tools, so they appear only under the software-product direction. The other columns say “not delivered” rather than mislabeling record screens as services, security products or firmware. **A catalog link proves existence, not production readiness.**', '',
        '| Customer domain | Software-product direction (existing) | IT services | Security products and services | Embedded-system software |',
        '| :--- | :--- | :---: | :---: | :---: |',
    ])
    for anchor, heading, entries in sections:
        lines.append(f'| [{heading}](./project-catalog{"" if is_zh else ".en"}.md#{anchor}) | {len(entries)} ' + ('项' if is_zh else 'projects') + f' · [{"查看项目" if is_zh else "View projects"}](#matrix-{anchor}) | ' + ('待建设 | 待建设 | 待建设 |' if is_zh else 'Not delivered | Not delivered | Not delivered |'))
    lines += ['', '## 逐场景项目入口' if is_zh else '## Projects by customer domain', '']
    for anchor, heading, entries in sections:
        links = ' · '.join(f'[{name}](../{slug}/README.md)' for name, slug in entries)
        lines += [f'<a id="matrix-{anchor}"></a>', f'### {heading} · {len(entries)}', '', links, '']
    lines += (['', '**合计：** 12 个客户场景、101 个不重复项目、4 个软件业统计方向；当前仅“软件产品方向”有样板，另外三个方向均没有通过下述验收的独立交付。四个小程序包含在 101 项内，不额外计数。', '',
        '## 缺失赛道：先定义可验收的产品，再建设', '',
        '| 方向与优先级 | 当前关联项目（不能替代交付） | 必须补齐的能力与最低验收 |', '| :--- | :--- | :--- |',
        '| 信息安全产品和服务 · 优先 | [管理后台](../manage/README.md) 仅有 JWT API 试点；[门禁](../access/README.md) 管理物理通行 | 独立可复用身份与授权能力；服务端按租户/角色/资源拒绝越权（401/403），可验证审计、密钥轮换与异常令牌测试；浏览器和小程序完成安全登录。详见[鉴权边界](./auth-integration.md)。**未交付。** |',
        '| 信息技术服务 · 其次 | [内部工单](../ticketops/README.md)、[运维](../itops/README.md) 是本机软件 | 明确服务对象、服务级别、工单分派/升级/回访、履约度量与多租户隔离；至少一个跨用户端到端场景及自动化测试。只有真实对外服务及运营证据时再讨论收入归类。**未交付。** |',
        '| 嵌入式系统软件 · 硬件条件具备后 | [门禁](../access/README.md)、[通信设施](../telecom/README.md) 是管理页面 | 明确目标设备与固件环境，完成设备侧采集/离线缓冲/安全更新、硬件在环测试、断网恢复及回滚；没有设备测试就不宣称嵌入式交付。**未交付。** |', '',
        '## 维护与验证', '',
        '新增产品先确定**客户场景主分类**，再依据实际交付内容判断**统计方向**；软件交付不能自动推断服务收入。同步更新中英文[分类目录](./project-catalog.md)、截图与本矩阵；运行 `python tools/generate_product_matrix.py --check` 和 `python -m unittest discover -s tools -p "test_*.py"`。逐项目已验证范围见[验证记录](./project-validation-2026-10-03.md)及[质量账本](./quality-loop.md)；本矩阵没有新增后端业务系统或宣称 101 项逐页验收。', ''] if is_zh else ['',
        '**Totals:** 12 customer domains, 101 unique projects, four statistical directions. Only the software-product direction currently contains repository demos. Four mini-program builds are included in the 101, not counted again.', '',
        '## Missing directions: delivery criteria, not placeholder projects', '',
        '| Direction and priority | Related demos (not equivalent) | Minimum acceptance before claiming delivery |', '| :--- | :--- | :--- |',
        '| Security products and services · first | [manage](../manage/README.md) has a JWT API pilot; [access](../access/README.md) records physical access | Shared identity and authorization, server-side 401/403 per tenant/role/resource, verifiable audit, key rotation and invalid-token tests, and secure browser/mini-program login. [Current auth boundary](./auth-integration.md). **Not delivered.** |',
        '| IT services · next | [ticketops](../ticketops/README.md) and [itops](../itops/README.md) are local applications | Defined customers and SLAs, assignment/escalation/follow-up, measured fulfillment and tenant isolation, with an automated multi-user end-to-end scenario. Revenue classification requires actual service evidence. **Not delivered.** |',
        '| Embedded-system software · after hardware is available | [access](../access/README.md) and [telecom](../telecom/README.md) are management screens | Specify a device/firmware target, device-side capture, offline buffering, secure updates, hardware-in-loop testing, recovery and rollback. No embedded claim without device evidence. **Not delivered.** |', '',
        '## Maintenance and verification', '',
        'Assign a primary customer domain and an evidence-based statistical direction independently. Update the bilingual [catalog](./project-catalog.en.md), screenshots and this matrix together. Run `python tools/generate_product_matrix.py --check` and `python -m unittest discover -s tools -p "test_*.py"`. Existing evidence: [project validation](./project-validation-2026-10-03.md) and [quality log](./quality-loop.md). This matrix adds no backend systems or claim of exhaustive 101-project acceptance.', ''])
    return '\n'.join(lines)


def main():
    import argparse
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--check', action='store_true', help='fail if tracked matrices are stale')
    args = parser.parse_args()
    for language in ('zh', 'en'):
        output = ROOT / ('docs/product-matrix.md' if language == 'zh' else 'docs/product-matrix.en.md')
        expected = render(language)
        if args.check:
            if not output.is_file() or output.read_text(encoding='utf-8') != expected:
                raise SystemExit(f'Stale matrix: {output}; run python tools/generate_product_matrix.py')
        else:
            output.write_text(expected, encoding='utf-8')
    print('Product matrices: 12 domains, 101 projects, bilingual, in sync')


if __name__ == '__main__':
    main()
