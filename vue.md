# 1.脚手架安装
见官网https://cn.vuejs.org/v2/guide/installation.html

# 2.less安装
         1. 安装less依赖 
                cnpm install style-loader --save-dev
                cnpm install less less-loader --save-dev
         2. 修改webpack.base.conf.js文件
                {
                test: /\.less$/,
                loader: "style-loader!css-loader!less-loader",
                }
         3 在app.vue 引用
                <style lang="less">
                  @import 'assets/less/header.less';
                </style>

# 3.安装element

官网：http://element.eleme.io/#/zh-CN/component/installation

           1.  npm 安装
            npm i element-ui -S
            引入 Element
           2.  在 main.js 中写入以下内容：
            import Vue from 'vue'
            import ElementUI from 'element-ui'
            import 'element-ui/lib/theme-default/index.css'
            import App from './App.vue'
            
            Vue.use(ElementUI)
            
            new Vue({
              el: '#app',
              render: h => h(App)
            })