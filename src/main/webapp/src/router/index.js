import Vue from 'vue'
import Router from 'vue-router'
import population from '@/components/population.vue'
import visit from '@/components/visit.vue'
Vue.use(Router)

export default new Router({
  mode: 'history',
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
    }
  ]
})
