<template>
  <el-container class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="aside">
      <div class="logo">
        <div class="logo-icon">
          <i class="el-icon-box"></i>
        </div>
        <span v-show="!isCollapse" class="logo-text">仓库管理系统</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        :collapse="isCollapse"
        :collapse-transition="false"
        background-color="transparent"
        text-color="var(--text-secondary)"
        active-text-color="var(--accent-color)"
        router
      >
        <el-menu-item index="/dashboard">
          <i class="el-icon-data-board"></i>
          <span slot="title">数据看板</span>
        </el-menu-item>
        <el-submenu index="1">
          <template slot="title">
            <i class="el-icon-goods"></i>
            <span>商品管理</span>
          </template>
          <el-menu-item index="/products">商品列表</el-menu-item>
          <el-menu-item index="/categories">分类管理</el-menu-item>
        </el-submenu>
        <el-submenu index="2">
          <template slot="title">
            <i class="el-icon-box"></i>
            <span>库存管理</span>
          </template>
          <el-menu-item index="/inventory">库存查询</el-menu-item>
          <el-menu-item index="/warehouses">仓库管理</el-menu-item>
          <el-menu-item index="/locations">库位管理</el-menu-item>
        </el-submenu>
        <el-submenu index="3">
          <template slot="title">
            <i class="el-icon-s-order"></i>
            <span>单据管理</span>
          </template>
          <el-menu-item index="/inbound">入库管理</el-menu-item>
          <el-menu-item index="/outbound">出库管理</el-menu-item>
        </el-submenu>
        <el-submenu index="4">
          <template slot="title">
            <i class="el-icon-s-tools"></i>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/users">用户管理</el-menu-item>
          <el-menu-item index="/roles">角色管理</el-menu-item>
        </el-submenu>
      </el-menu>
      <div class="sidebar-footer" v-show="!isCollapse">
        <div class="version">v1.0.0</div>
      </div>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-left">
          <i :class="isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'" @click="isCollapse = !isCollapse" class="collapse-btn"></i>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="$route.meta.title">{{ $route.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <div class="theme-toggle" @click="toggleTheme" :title="theme === 'dark' ? '切换浅色主题' : '切换深色主题'">
            <i :class="theme === 'dark' ? 'el-icon-sunny' : 'el-icon-moon'"></i>
          </div>
          <div class="header-divider"></div>
          <div class="user-info">
            <div class="user-avatar">
              <i class="el-icon-user"></i>
            </div>
            <span class="user-name">管理员</span>
          </div>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Layout',
  data() {
    return {
      isCollapse: false
    }
  },
  computed: {
    activeMenu() {
      return this.$route.path
    },
    theme() {
      return this.$store.state.theme
    }
  },
  methods: {
    toggleTheme() {
      this.$store.dispatch('toggleTheme')
    }
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.aside {
  background: var(--bg-sidebar);
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  position: relative;
  border-right: 1px solid var(--border-color);
}

.aside::after {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 1px;
  height: 100%;
  background: linear-gradient(180deg, transparent 0%, var(--accent-color) 50%, transparent 100%);
  opacity: 0.3;
}

.logo {
  height: 64px;
  display: flex;
  align-items: center;
  padding: 0 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
  gap: 12px;
}

.logo-icon {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  background: var(--accent-gradient);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  box-shadow: 0 2px 12px var(--accent-glow);
}

.logo-icon i {
  font-size: 20px;
  color: #fff;
}

.logo-text {
  color: #fff;
  font-size: 16px;
  font-weight: 700;
  letter-spacing: 1px;
  white-space: nowrap;
  background: linear-gradient(90deg, #fff 0%, var(--accent-color) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.sidebar-menu {
  border-right: none !important;
  padding: 8px 0;
}

.sidebar-menu .el-menu-item,
.sidebar-menu .el-submenu__title {
  height: 44px;
  line-height: 44px;
  margin: 2px 8px;
  border-radius: 8px;
  transition: all 0.2s ease;
}

.sidebar-menu .el-menu-item:hover,
.sidebar-menu .el-submenu__title:hover {
  background-color: rgba(0, 212, 255, 0.08) !important;
}

.light .sidebar-menu .el-menu-item:hover,
.light .sidebar-menu .el-submenu__title:hover {
  background-color: rgba(255, 255, 255, 0.08) !important;
}

.sidebar-menu .el-menu-item.is-active {
  background: linear-gradient(90deg, rgba(0, 212, 255, 0.15) 0%, rgba(0, 212, 255, 0.05) 100%) !important;
  position: relative;
}

.light .sidebar-menu .el-menu-item.is-active {
  background: linear-gradient(90deg, rgba(255, 255, 255, 0.15) 0%, rgba(255, 255, 255, 0.05) 100%) !important;
}

.sidebar-menu .el-menu-item.is-active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 20px;
  border-radius: 0 3px 3px 0;
  background: var(--accent-gradient);
  box-shadow: 0 0 8px var(--accent-glow);
}

.sidebar-menu .el-menu-item i,
.sidebar-menu .el-submenu__title i {
  color: var(--text-muted);
  transition: color 0.2s;
}

.sidebar-menu .el-menu-item.is-active i,
.sidebar-menu .el-menu-item:hover i {
  color: var(--accent-color);
}

.sidebar-footer {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px;
  text-align: center;
  border-top: 1px solid rgba(255, 255, 255, 0.06);
}

.version {
  color: var(--text-muted);
  font-size: 12px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: var(--bg-header);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-bottom: 1px solid var(--border-color);
  padding: 0 24px;
  height: 64px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  color: var(--text-secondary);
  transition: all 0.2s;
  padding: 8px;
  border-radius: 8px;
}

.collapse-btn:hover {
  color: var(--accent-color);
  background: rgba(0, 212, 255, 0.1);
}

.light .collapse-btn:hover {
  background: rgba(59, 130, 246, 0.1);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.theme-toggle {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  color: var(--text-secondary);
  background: rgba(255, 255, 255, 0.05);
}

.theme-toggle:hover {
  color: var(--accent-color);
  background: rgba(0, 212, 255, 0.1);
  transform: rotate(30deg);
}

.light .theme-toggle:hover {
  background: rgba(59, 130, 246, 0.1);
}

.header-divider {
  width: 1px;
  height: 24px;
  background: var(--border-color);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 12px;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s;
}

.user-info:hover {
  background: rgba(0, 212, 255, 0.08);
}

.light .user-info:hover {
  background: rgba(59, 130, 246, 0.08);
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: var(--accent-gradient);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 8px var(--accent-glow);
}

.user-avatar i {
  color: #fff;
  font-size: 16px;
}

.user-name {
  color: var(--text-primary);
  font-size: 14px;
  font-weight: 500;
}

.main {
  background-color: var(--bg-primary);
  padding: 20px;
  overflow-y: auto;
}
</style>
