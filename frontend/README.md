# 仓库管理系统前端

基于Vue 2 + Element UI的仓库管理系统前端。

## 技术栈

- **框架**：Vue 2.6
- **UI组件库**：Element UI 2.15
- **路由**：Vue Router 3.5
- **状态管理**：Vuex 3.6
- **HTTP客户端**：Axios 0.27
- **构建工具**：Vue CLI 5.0

## 项目结构

```
frontend/
├── public/
│   └── index.html              # HTML模板
├── src/
│   ├── main.js                 # 入口文件
│   ├── App.vue                 # 根组件
│   ├── router/
│   │   └── index.js            # 路由配置
│   ├── store/
│   │   ├── index.js            # Vuex入口
│   │   └── modules/
│   │       └── user.js         # 用户模块
│   ├── utils/
│   │   └── auth.js             # 认证工具
│   └── views/
│       ├── Login.vue           # 登录页
│       ├── Layout.vue          # 布局组件
│       ├── Dashboard.vue       # 仪表盘
│       ├── Product.vue         # 商品管理
│       ├── Inventory.vue       # 库存管理
│       ├── Inbound.vue         # 入库管理
│       ├── Outbound.vue        # 出库管理
│       ├── Warehouse.vue       # 仓库管理
│       ├── Location.vue        # 库位管理
│       ├── Category.vue        # 分类管理
│       ├── User.vue            # 用户管理
│       └── Role.vue            # 角色管理
├── package.json                # 依赖配置
└── vue.config.js               # Vue配置
```

## 快速开始

### 1. 环境要求

- Node.js 14+
- npm 6+ 或 yarn

### 2. 安装依赖

```bash
cd frontend
npm install
```

### 3. 启动开发服务器

```bash
npm run serve
```

启动成功后，访问 http://localhost:8081

### 4. 构建生产版本

```bash
npm run build
```

## 功能模块

### 1. 登录模块
- 用户名密码登录
- Token认证
- 路由守卫

### 2. 首页仪表盘
- 商品总数统计
- 库存总量统计
- 今日入库/出库统计
- 最近单据列表

### 3. 商品管理
- 商品列表查询
- 新增/编辑/删除商品
- 商品状态管理

### 4. 库存管理
- 库存查询
- 库存调整
- 库存预警

### 5. 入库管理
- 入库单列表
- 新建入库单
- 审核/执行入库单

### 6. 出库管理
- 出库单列表
- 新建出库单
- 审核/执行出库单

### 7. 仓库管理
- 仓库列表
- 新增/编辑/删除仓库

### 8. 库位管理
- 库位列表
- 新增/编辑/删除库位

### 9. 分类管理
- 树形分类展示
- 新增/编辑/删除分类

### 10. 用户管理
- 用户列表
- 新增/编辑/删除用户

### 11. 角色管理
- 角色列表
- 新增/编辑/删除角色

## 默认账号

- 用户名: admin
- 密码: admin123

## 配置说明

### 代理配置

在 `vue.config.js` 中配置了开发代理：

```javascript
devServer: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

前端请求 `/api` 开头的接口会自动转发到后端服务。

### 端口配置

默认端口为8081，可在 `vue.config.js` 中修改：

```javascript
devServer: {
  port: 8081
}
```

## 注意事项

1. 首次使用请确保后端服务已启动
2. 确保后端端口为8080，或修改前端代理配置
3. 生产环境部署时需要配置nginx反向代理