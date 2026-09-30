# shop · 精选生活好物商城

![商城首页](./screenshots/storefront.png)
![商品列表](./screenshots/catalog.png)

商品搜索/分类、购物车、下单和服务端库存校验；订单与库存重启后仍保留。 Vue 3 + Java 21/Spring Boot；页面截图来自本地实际运行。

## 一键运行

需要 Java 21、Maven、Node.js 20.19+/22.12+ 和 npm；在本目录执行：

```powershell
./run.ps1           # Windows PowerShell
```

```sh
./run.sh            # macOS / Linux
```

首次执行会下载依赖、构建前端、运行后端测试并启动单个 jar。浏览器打开 **http://127.0.0.1:8081**，前后端同一端口，无需另开终端。按 Ctrl+C 停止。Windows 如有旧 Vite 开发服务占用 `frontend/node_modules`，请先关闭再运行。

数据保存在本项目 `data/shop.json`，停止服务后可复制备份；如需恢复默认演示数据，停止后删除该文件并重启。文件损坏时启动会报错，不会自动清空。不要多个进程共用此文件。

## 测试

```sh
mvn -f backend/pom.xml test
npm --prefix frontend ci
npm --prefix frontend run build
```

在仓库根目录执行 `./test-all.ps1`（Windows）或 `./test-all.sh`（macOS / Linux）会验证全部四个项目及 jar 中的页面资源。前端截图参见上方。

**使用边界：**无真实支付、配送、账号体系或分布式库存；仅为演示商城。 默认只监听本机 `127.0.0.1`；这是可本地直接使用的样板，**不适合未经改造部署到公网或生产环境**。
