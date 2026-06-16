import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: () => import('../views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'products',
        name: 'Products',
        component: () => import('../views/Product.vue'),
        meta: { title: '商品管理' }
      },
      {
        path: 'inventory',
        name: 'Inventory',
        component: () => import('../views/Inventory.vue'),
        meta: { title: '库存管理' }
      },
      {
        path: 'inbound',
        name: 'Inbound',
        component: () => import('../views/Inbound.vue'),
        meta: { title: '入库管理' }
      },
      {
        path: 'outbound',
        name: 'Outbound',
        component: () => import('../views/Outbound.vue'),
        meta: { title: '出库管理' }
      },
      {
        path: 'warehouses',
        name: 'Warehouses',
        component: () => import('../views/Warehouse.vue'),
        meta: { title: '仓库管理' }
      },
      {
        path: 'locations',
        name: 'Locations',
        component: () => import('../views/Location.vue'),
        meta: { title: '库位管理' }
      },
      {
        path: 'categories',
        name: 'Categories',
        component: () => import('../views/Category.vue'),
        meta: { title: '分类管理' }
      },
      {
        path: 'users',
        name: 'Users',
        component: () => import('../views/User.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'roles',
        name: 'Roles',
        component: () => import('../views/Role.vue'),
        meta: { title: '角色管理' }
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router