import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/en' // lang i18n

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon
import '@/permission' // permission control

/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online ! ! !
 */
if (process.env.NODE_ENV === 'production') {
  const {mockXHR} = require('../mock')
  mockXHR()
}

// set ElementUI lang to EN
Vue.use(ElementUI, {locale})
// 如果想要中文版 element-ui，按如下方式声明
// Vue.use(ElementUI)

Vue.config.productionTip = false

//引入相关API请求接口
import API from '@/api'
import CategorySelect from '@/components/CategorySelect'
//注册为全局组件
Vue.component(CategorySelect.name, CategorySelect)
Vue.prototype.$API = API
Vue.prototype.getNowFormatDate = function () {
  var date = new Date();
  var seperator1 = "-";
  var year = date.getFullYear();
  var month = date.getMonth() + 1;
  var strDate = date.getDate();
  if (month >= 1 && month <= 9) {
    month = "0" + month;
  }
  if (strDate >= 0 && strDate <= 9) {
    strDate = "0" + strDate;
  }
  var currentdate = year + seperator1 + month + seperator1 + strDate;
  return currentdate;
};
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
