# 仓库管理系统

基于 Spring Boot + Vue 的全栈仓库管理系统，支持商品管理、库存管理、出入库管理、数据看板可视化等功能。

## 功能模块

| 模块 | 说明 |
|------|------|
| **数据看板** | 实时统计、出入库趋势图、仓库库存分布、分类库存饼图，支持深色/浅色双主题 |
| **商品管理** | 商品增删改查、状态管理 |
| **分类管理** | 商品分类树形结构管理 |
| **库存管理** | 库存查询、库存调整、按仓库/商品筛选 |
| **仓库管理** | 仓库信息维护 |
| **库位管理** | 仓库内库位管理 |
| **入库管理** | 采购入库、退货入库，支持审核与执行 |
| **出库管理** | 销售出库、调拨出库，支持审核与执行 |
| **用户管理** | 用户增删改查 |
| **角色管理** | 角色权限管理 |

## 技术栈

### 后端

| 技术 | 版本 |
|------|------|
| Spring Boot | 2.7.18 |
| Java | 11 |
| Maven | 3.6+ |
| MyBatis-Plus | 3.5.3.1 |
| MySQL | 8.0+ |
| Redis | - |
| Knife4j (Swagger) | - |

### 前端

| 技术 | 版本 |
|------|------|
| Vue | 2.6.14 |
| Element UI | 2.15.13 |
| ECharts | 最新 |
| Vue Router | 3.5.1 |
| Vuex | 3.6.2 |
| Axios | 0.27.2 |

## 项目结构

```
仓库管理系统/
├── sql/
│   ├── init.sql                    # 数据库建表脚本
│   └── insert_sample_data.sql      # 示例数据
├── src/main/java/com/warehouse/
│   ├── WarehouseApplication.java   # 启动类
│   ├── common/                     # 通用类 (Result, PageResult, GlobalExceptionHandler)
│   ├── config/                     # 配置类 (Security, Redis, MyBatis, Swagger, CORS)
│   ├── controller/                 # 控制器 (Auth, Dashboard, Product, Inventory, ...)
│   ├── entity/                     # 实体类
│   ├── mapper/                     # 数据访问层
│   ├── security/                   # JWT 安全相关
│   └── service/                    # 服务层
├── src/main/resources/
│   └── application.yml             # 后端配置
├── frontend/
│   ├── src/
│   │   ├── views/                  # 页面组件
│   │   ├── router/                 # 路由配置
│   │   ├── store/                  # Vuex 状态管理
│   │   ├── utils/                  # 工具函数
│   │   ├── App.vue                 # 根组件 (含全局主题)
│   │   └── main.js                 # 入口文件
│   ├── vue.config.js               # 前端配置
│   └── package.json
├── pom.xml                         # Maven 配置
└── README.md
```

## 快速开始

### 环境要求

- JDK 11+
- Maven 3.6+
- MySQL 8.0+
- Redis
- Node.js 14+
- npm 6+

### 1. 初始化数据库

```bash
mysql -u root -p < sql/init.sql
mysql -u root -p warehouse_db < sql/insert_sample_data.sql  # 可选，导入示例数据
```

### 2. 修改后端配置

编辑 `src/main/resources/application.yml`，修改数据库和 Redis 连接：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/warehouse_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
  redis:
    host: localhost
    port: 6379
```

### 3. 启动后端

```bash
mvn clean compile
mvn spring-boot:run
```

后端运行在 `http://localhost:8080`

### 4. 启动前端

```bash
cd frontend
npm install
npm run serve
```

前端运行在 `http://localhost:8081`

### 5. 访问系统

打开浏览器访问 `http://localhost:8081`

- 首页即数据看板，展示实时统计和可视化图表
- 右上角可切换深色/浅色主题
- 无需登录，直接使用

## API 接口

### 数据看板

- `GET /api/v1/dashboard/stats` - 统计概览（商品数、库存量、今日出入库）
- `GET /api/v1/dashboard/inbound-trend` - 近7天入库趋势
- `GET /api/v1/dashboard/outbound-trend` - 近7天出库趋势
- `GET /api/v1/dashboard/warehouse-inventory` - 各仓库库存分布
- `GET /api/v1/dashboard/category-distribution` - 分类库存占比
- `GET /api/v1/dashboard/recent-orders` - 最近出入库单

### 商品管理

- `POST /api/v1/products` - 创建商品
- `GET /api/v1/products` - 商品列表（分页）
- `GET /api/v1/products/{id}` - 商品详情
- `PUT /api/v1/products/{id}` - 更新商品
- `DELETE /api/v1/products/{id}` - 删除商品

### 库存管理

- `GET /api/v1/inventory` - 库存列表（分页）
- `GET /api/v1/inventory/{id}` - 库存详情
- `POST /api/v1/inventory/adjust` - 库存调整
- `GET /api/v1/inventory/warnings` - 库存预警

### 入库管理

- `POST /api/v1/inbound` - 创建入库单
- `GET /api/v1/inbound` - 入库单列表
- `GET /api/v1/inbound/{id}` - 入库单详情
- `PUT /api/v1/inbound/{id}/audit` - 审核
- `PUT /api/v1/inbound/{id}/execute` - 执行入库

### 出库管理

- `POST /api/v1/outbound` - 创建出库单
- `GET /api/v1/outbound` - 出库单列表
- `GET /api/v1/outbound/{id}` - 出库单详情
- `PUT /api/v1/outbound/{id}/audit` - 审核
- `PUT /api/v1/outbound/{id}/execute` - 执行出库

### 仓库管理

- `POST /api/v1/warehouses` - 创建仓库
- `GET /api/v1/warehouses` - 仓库列表
- `PUT /api/v1/warehouses/{id}` - 更新仓库
- `DELETE /api/v1/warehouses/{id}` - 删除仓库

### 库位管理

- `POST /api/v1/locations` - 创建库位
- `GET /api/v1/locations` - 库位列表
- `PUT /api/v1/locations/{id}` - 更新库位
- `DELETE /api/v1/locations/{id}` - 删除库位

### 分类管理

- `POST /api/v1/categories` - 创建分类
- `GET /api/v1/categories` - 分类列表
- `PUT /api/v1/categories/{id}` - 更新分类
- `DELETE /api/v1/categories/{id}` - 删除分类

### 用户 / 角色管理

- `POST /api/v1/users` - 创建用户
- `GET /api/v1/users` - 用户列表
- `POST /api/v1/roles` - 创建角色
- `GET /api/v1/roles` - 角色列表

### API 文档

启动后端后访问：
- http://localhost:8080/doc.html (Knife4j)
- http://localhost:8080/v3/api-docs (OpenAPI)

## 前端主题

系统支持深色和浅色两种主题，通过右上角图标切换。主题选择会持久化到 localStorage。

- **深色主题**：深蓝背景 + 青色霓虹高亮，科技感界面
- **浅色主题**：白灰背景 + 蓝色高亮，清爽简洁

## 配置说明

### 后端端口

默认 8080，在 `application.yml` 中修改：

```yaml
server:
  port: 8080
```

### 前端端口

默认 8081，在 `frontend/vue.config.js` 中修改：

```javascript
devServer: {
  port: 8081,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

### 数据库配置

默认数据库名 `warehouse_db`，在 `application.yml` 中修改连接信息。

## License

MIT
