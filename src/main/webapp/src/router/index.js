import Vue from 'vue'
import Router from 'vue-router'
import population from '@/components/population.vue'
import visit from '@/components/visit.vue'
import card from '@/components/card.vue'
import index from '@/components/index.vue'
Vue.use(Router)

export default new Router({
  mode: 'history',
  // 作用 去掉路由中的 #  h5 history模式
  routes: [
    {
      path: '/population',
      name: 'population',
      component: population
    },
    {
      path: '/visit',
      name: 'visit',
      component: visit
    },
    {
      path: '/card',
      name: 'card',
      component: card
    },
    {
      path: '/index',
      name: 'index',
      component: index
    }
  ]
})
