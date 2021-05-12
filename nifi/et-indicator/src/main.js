import Vue from 'vue'
import App from './App.vue'

Vue.config.productionTip = false

import VueNativeSock from 'vue-native-websocket'
Vue.use(VueNativeSock, 'ws://localhost:1113/websocket/',{
  reconnection: true,
})

new Vue({
  render: h => h(App),
}).$mount('#app')
