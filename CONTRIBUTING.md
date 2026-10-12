# 贡献指南

感谢你关注 ai-hub。这个仓库不是把目录数量堆得越多越好，而是把真实业务流程做成**可以运行、可以验证、可以继续改造**的软件起点。

## 先从哪里开始

- 想改善商城、订单或商品流程：从 [`shop`](./shop/README.md) 开始。
- 想完善通用后台、用户和权限：从 [`manage`](./manage/README.md) 开始。
- 想参与公共鉴权能力：从 [`authcenter`](./authcenter/README.md) 和 [`docs/auth-integration.md`](./docs/auth-integration.md) 开始。
- 想补充一个职业场景：先查 [`项目分类目录`](./docs/project-catalog.md)，避免重复建设。

## 提交前检查

```powershell
python -m unittest discover -s tools -p 'test_*.py' -q
python tools/generate_product_matrix.py --check
./test-all.ps1
```

只修改一个项目时，也要运行该项目的前端构建和 `mvn -f <项目>/backend/pom.xml test`。请在合并请求中说明实际执行的命令、结果和未覆盖的边界；“构建通过”不等于“所有页面和真实业务流程验收通过”。

## 代码约定

- 前端保持 Vue 3，后端保持 Java 21/Spring Boot；除非有明确理由，不要引入新的主技术栈。
- 每个项目使用独立目录、独立端口，端口必须在分类目录和测试中保持一致。
- 默认绑定 `127.0.0.1`，不得把演示数据或敏感数据提交到仓库。
- 新项目必须补充 README、运行命令、截图、默认端口和测试；不要只添加空壳目录。
- 改动中文文档时，同步更新对应英文文档或明确说明为什么不需要同步。

## 问题和合并请求

提交问题时请包含：项目目录、操作系统、Java/Node 版本、复现步骤、期望结果、实际结果和日志。安全问题请不要公开创建 Issue，先阅读 [`安全策略`](./SECURITY.md)。

合并请求应尽量只解决一个问题，并在描述中列出验证证据和已知限制。欢迎提交界面优化、可访问性改进、测试、文档和真实业务流程；不鼓励为了增加项目数量而复制空白样板。
