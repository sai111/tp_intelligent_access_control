const nav =[
  {
    name: 'index',
    text: '主页',
    icon: 'menu',
    path: '/index'
  },
  {
    name: 'people',
    text: '人员管理',
    icon: 'setting',
    path: '/people',
    children: [
      {
        name: 'population',
        path: '/population',
        icon: 'population',
        text: '人口管理'
      }, {
        name: 'visit',
        path: '/visit',
        icon: 'visit',
        text: '访客管理',
        children: [
          {
            name: 'card',
            path: '/card',
            icon: 'build',
            text: '门禁卡管理'
          }, {
            name: 'user',
            path: '/user',
            icon: 'user',
            text: '用户管理'
          },
          {
            name: 'role',
            path: '/role',
            icon: 'role',
            text: '角色管理'
          }
        ]
      },
      {
        name: 'entry',
        path: '/entry',
        icon: 'entry',
        text: '出入信息'
      }, {
        name: 'resident',
        path: '/resident',
        icon: 'resident',
        text: '居民信息管理'
      }, {
        name: 'p_statistics',
        path: '/P_statistics',
        icon: 'P_statistics',
        text: '居民信息统计'
      }
    ]
  },  {
    name: 'home',
    text: '房屋管理',
    icon: 'message',
    path: '/home',
    children: [
      {
        name: 'build',
        path: '/build',
        icon: 'build',
        text: '房屋管理'
      }, {
        name: 'monitor',
        path: '/monitor',
        icon: 'monitor',
        text: '房屋监测'
      },
      {
        name: 'h_statistics',
        path: '/h_statistics',
        icon: 'h_statistics',
        text: '房屋信息统计'
      }
    ]
  },  {
    name: 'alarm',
    text: '报警中心',
    icon: 'menu',
    path: '/alarm',
    children: [
      {
        name: 'synthesized',
        path: '/synthesized',
        icon: 'home',
        text: '综合报警'
      }
    ]
  },  {
    name: 'home2',
    text: '房屋管理2',
    icon: 'setting',
    path: '/home',
    children: [
      {
        name: 'build',
        path: '/build',
        icon: 'build',
        text: '房屋管理'
      }, {
        name: 'monitor',
        path: '/monitor',
        icon: 'monitor',
        text: '房屋监测'
      },
      {
        name: 'h_statistics',
        path: '/h_statistics',
        icon: 'h_statistics',
        text: '房屋信息统计'
      }
    ]
  },  {
    name: 'config',
    text: '基础配置',
    icon: 'message',
    path: '/config',
    children: [
      {
        name: 'card',
        path: '/card',
        icon: 'build',
        text: '门禁卡管理'
      }, {
        name: 'user',
        path: '/user',
        icon: 'user',
        text: '用户管理'
      },
      {
        name: 'role',
        path: '/role',
        icon: 'role',
        text: '角色管理'
      }, {
        name: 'log',
        path: '/log',
        icon: 'log',
        text: '日志管理'
      },
      {
        name: 'system',
        path: '/system',
        icon: 'system',
        text: '系统管理'
      }
    ]
  }
  ]
export default nav


