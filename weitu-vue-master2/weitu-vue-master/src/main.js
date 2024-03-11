import Vue from 'vue'
import App from './App.vue'
import _ from 'lodash'
import * as Scroll from "./assets/js/seamscroll.min";
import ElementUI from 'element-ui';
import request from "@/utils/request";
import 'element-ui/lib/theme-chalk/index.css';
import VueResource from 'vue-resource';
import router from "@/router";
import VueParticles from 'vue-particles'

Vue.config.productionTip = false
Vue.prototype._ = _
Vue.prototype.Scroll = Scroll
Vue.use(VueResource)
Vue.use(ElementUI, {size:"small"});
Vue.use(VueParticles)

Vue.prototype.request=request

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
