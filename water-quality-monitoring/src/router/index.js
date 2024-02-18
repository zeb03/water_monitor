import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      //title:侧边栏文字
      meta: {title: '首页', icon: 'dashboard'}
    }]
  },

  {
    path: '/user',
    component: Layout,
    name: "User",
    meta: {title: '用户管理', icon: 'el-icon-user'},
    children: [
      {
        path: 'info',
        name: 'Info',
        component: () => import('@/views/user/Info'),
        //title:侧边栏文字
        meta: {title: '普通用户'}
      },
      {
        path: 'root',
        name: 'Root',
        component: () => import('@/views/user/root'),
        //title:侧边栏文字
        meta: {title: '管理员'}
      },

    ]
  },
  {
    path: '/stick',
    component: Layout,
    name: "Stick",
    meta: {title: '设备管理', icon: 'el-icon-location-outline'},
    children: [
      {
        path: 'binding',
        name: 'Binding',
        component: () => import('@/views/station/index'),
        //title:侧边栏文字
        meta: {title: '监测站管理'}
      },
    ]
  },
  {
    path: '/map/ship',
    name: "Ship",
    component: () => import('@/views/map/index'),
  },
  {
    path: '/data',
    component: Layout,
    name: "Data",
    meta: {title: '水质数据管理', icon: 'el-icon-date'},
    children: [
      {
        path: 'monitor',
        name: 'monitor',
        component: () => import('@/views/water/monitor'),
        //title:侧边栏文字
        meta: {title: '近期监测'}
      },
      {
        path: 'history',
        name: 'History',
        component: () => import('@/views/water/historyData'),
        //title:侧边栏文字
        meta: {title: '历史数据'}
      },
      {
        path: 'trend',
        name: 'Trend',
        component: () => import('@/views/water/dataTrend'),
        //title:侧边栏文字
        meta: {title: '数据趋势'}
      },
    ]
  },
  {
    path: '/trend',
    component: Layout,
    name: "Trend",
    meta: {title: '水质数据预测', icon: 'el-icon-data-line'},
    children: [
      {
        path: 'feature',
        name: 'Feature',
        component: () => import('@/views/trend/feature'),
        //title:侧边栏文字
        meta: {title: '下月预测'}
      },
      {
        path: 'model',
        name: 'Domel',
        component: () => import('@/views/trend/model'),
        //title:侧边栏文字
        meta: {title: '训练模型'}
      },

    ]
  },
  // 404 page must be placed at the end !!!
  {path: '*', redirect: '/404', hidden: true}
]

const createRouter = () => new Router({
  mode: 'hash', // require service support
  scrollBehavior: () => ({y: 0}),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
