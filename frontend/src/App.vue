<template>
  <div id="app" :class="theme">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
  computed: {
    theme() {
      return this.$store.state.theme || 'dark'
    }
  },
  created() {
    const saved = localStorage.getItem('warehouse_theme')
    if (saved) {
      this.$store.commit('SET_THEME', saved)
    }
  }
}
</script>

<style>
:root {
  --bg-primary: #0f1923;
  --bg-secondary: #1a2736;
  --bg-card: rgba(26, 39, 54, 0.8);
  --bg-sidebar: linear-gradient(180deg, #0a1628 0%, #0d1f35 100%);
  --bg-header: rgba(15, 25, 35, 0.85);
  --border-color: rgba(64, 158, 255, 0.15);
  --text-primary: #e4e7ed;
  --text-secondary: #a3b1bf;
  --text-muted: #6c7a89;
  --accent-color: #00d4ff;
  --accent-glow: rgba(0, 212, 255, 0.4);
  --accent-gradient: linear-gradient(135deg, #00d4ff 0%, #0099ff 100%);
  --success-color: #00e676;
  --warning-color: #ffab00;
  --danger-color: #ff5252;
  --card-shadow: 0 4px 24px rgba(0, 0, 0, 0.3);
  --card-border: 1px solid rgba(64, 158, 255, 0.1);
  --hover-glow: 0 0 20px rgba(0, 212, 255, 0.2);
}

#app.light {
  --bg-primary: #f0f2f5;
  --bg-secondary: #ffffff;
  --bg-card: #ffffff;
  --bg-sidebar: linear-gradient(180deg, #1a2736 0%, #2c3e50 100%);
  --bg-header: rgba(255, 255, 255, 0.95);
  --border-color: rgba(0, 0, 0, 0.08);
  --text-primary: #1a1a2e;
  --text-secondary: #4a5568;
  --text-muted: #a0aec0;
  --accent-color: #3b82f6;
  --accent-glow: rgba(59, 130, 246, 0.3);
  --accent-gradient: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  --success-color: #10b981;
  --warning-color: #f59e0b;
  --danger-color: #ef4444;
  --card-shadow: 0 4px 16px rgba(0, 0, 0, 0.06);
  --card-border: 1px solid rgba(0, 0, 0, 0.06);
  --hover-glow: 0 4px 20px rgba(59, 130, 246, 0.15);
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'PingFang SC', 'Microsoft YaHei', -apple-system, BlinkMacSystemFont, sans-serif;
  background-color: var(--bg-primary);
  color: var(--text-primary);
  transition: background-color 0.3s, color 0.3s;
}

#app {
  height: 100%;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

/* Scrollbar */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}
::-webkit-scrollbar-track {
  background: transparent;
}
::-webkit-scrollbar-thumb {
  background: rgba(0, 212, 255, 0.3);
  border-radius: 3px;
}
::-webkit-scrollbar-thumb:hover {
  background: rgba(0, 212, 255, 0.5);
}

.light ::-webkit-scrollbar-thumb {
  background: rgba(59, 130, 246, 0.3);
}
.light ::-webkit-scrollbar-thumb:hover {
  background: rgba(59, 130, 246, 0.5);
}

/* Element UI Override */
.el-card {
  background: var(--bg-card) !important;
  border: var(--card-border) !important;
  border-radius: 12px !important;
  box-shadow: var(--card-shadow) !important;
  transition: all 0.3s ease !important;
  color: var(--text-primary) !important;
}

.el-card:hover {
  box-shadow: var(--hover-glow) !important;
}

.el-card__header {
  border-bottom: 1px solid var(--border-color) !important;
  color: var(--text-primary) !important;
  font-weight: 600;
  font-size: 15px;
  padding: 14px 20px !important;
}

.el-table {
  background-color: transparent !important;
  color: var(--text-primary) !important;
}

.el-table th {
  background-color: rgba(0, 212, 255, 0.05) !important;
  color: var(--accent-color) !important;
  font-weight: 600;
}

.light .el-table th {
  background-color: rgba(59, 130, 246, 0.05) !important;
  color: var(--accent-color) !important;
}

.el-table tr {
  background-color: transparent !important;
}

.el-table--enable-row-hover .el-table__body tr:hover > td {
  background-color: rgba(0, 212, 255, 0.05) !important;
}

.light .el-table--enable-row-hover .el-table__body tr:hover > td {
  background-color: rgba(59, 130, 246, 0.05) !important;
}

.el-table td, .el-table th.is-leaf {
  border-bottom: 1px solid var(--border-color) !important;
}

.el-table::before {
  display: none;
}

.el-table--border::after, .el-table--group::after {
  display: none;
}

.el-input__inner {
  background-color: var(--bg-secondary) !important;
  border-color: var(--border-color) !important;
  color: var(--text-primary) !important;
}

.el-input__inner:focus {
  border-color: var(--accent-color) !important;
}

.el-input__inner::placeholder {
  color: var(--text-muted) !important;
}

.el-button--primary {
  background: var(--accent-gradient) !important;
  border: none !important;
  box-shadow: 0 2px 12px var(--accent-glow) !important;
  transition: all 0.3s ease !important;
}

.el-button--primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 20px var(--accent-glow) !important;
}

.el-tag {
  border-radius: 6px !important;
}

.el-pagination {
  color: var(--text-secondary) !important;
}

.el-pagination .btn-prev, .el-pagination .btn-next {
  background-color: var(--bg-card) !important;
  color: var(--text-secondary) !important;
}

.el-pagination .el-pager li {
  background-color: var(--bg-card) !important;
  color: var(--text-secondary) !important;
}

.el-pagination .el-pager li.active {
  color: var(--accent-color) !important;
}

.el-breadcrumb__inner {
  color: var(--text-secondary) !important;
}

.el-breadcrumb__separator {
  color: var(--text-muted) !important;
}

.el-select-dropdown {
  background: var(--bg-secondary) !important;
  border: 1px solid var(--border-color) !important;
}

.el-select-dropdown__item {
  color: var(--text-primary) !important;
}

.el-select-dropdown__item.hover, .el-select-dropdown__item:hover {
  background-color: rgba(0, 212, 255, 0.1) !important;
}

.light .el-select-dropdown__item.hover, .light .el-select-dropdown__item:hover {
  background-color: rgba(59, 130, 246, 0.1) !important;
}

.el-dialog {
  background: var(--bg-secondary) !important;
  border: 1px solid var(--border-color) !important;
  border-radius: 16px !important;
}

.el-dialog__title {
  color: var(--text-primary) !important;
}

.el-form-item__label {
  color: var(--text-secondary) !important;
}

.el-message-box {
  background: var(--bg-secondary) !important;
  border: 1px solid var(--border-color) !important;
}

.el-dropdown-menu {
  background: var(--bg-secondary) !important;
  border: 1px solid var(--border-color) !important;
}

.el-dropdown-menu__item {
  color: var(--text-primary) !important;
}

.el-dropdown-menu__item:hover {
  background-color: rgba(0, 212, 255, 0.1) !important;
  color: var(--accent-color) !important;
}
</style>
