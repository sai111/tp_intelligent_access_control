<template>
  <div class="p-layout">
    <div class="p-layout-topbar clearfix">
      <div class="p-layout-name" :class="{'sider-mini': isCollapse}">
        <router-link class="full" to="/" v-if="!isCollapse">Tpson</router-link>
        <span class="mini" v-else>TP</span>
      </div>
      <div class="p-layout-collapse" @click="toggleSider"><span class="icon-menu"></span></div>
      <div class="p-layout-nav">
        <el-dropdown class="is-user" @command="handleDropdown">
          <img src="./assets/avatar.jpg" class="p-layout-avatar" alt="">
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="logout">退出</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <div class="p-layout-body" :class="{ 'sider-full': !isCollapse,'sider-mini': isCollapse }">
      <aside class="p-layout-sider">
        <el-menu default-active="1-1" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose" :collapse="isCollapse"
                 theme="dark"
                 :unique-opened="true"
                 :default-active="currentRoute"
                 :router="true">
          <el-menu-item index="index" style="width:100%;"><i class="el-icon-menu"></i>主页</el-menu-item>
          <el-submenu
            :index="menu.name"
            v-for="(menu, index) in menus">
            <template slot="title">
              <i v-if="menu.icon" class="fa" :class="'el-icon-' + menu.icon"></i>
              <span class="nav-next">{{menu.text}}</span>
            </template>
            <el-menu-item-group>
              <el-menu-item
                :index="subMenu.name"
                v-if="!subMenu.children"
                v-for="(subMenu, subIndex) in menu.children">{{subMenu.text}}</el-menu-item>
              <el-submenu v-else="!subMenu.children" :index="subMenu.name">
                <span slot="title">{{subMenu.text}}</span>
                <el-menu-item
                  :index="item.name"
                  v-for="(item, index) in subMenu.children">{{item.text}}</el-menu-item>
              </el-submenu>
            </el-menu-item-group>
          </el-submenu>
        </el-menu>
      </aside>
      <div class="p-layout-panel">
        <router-view></router-view>
      </div>

    </div>
    <div class="p-layout-footer"> 版权所有 © 2016</div>
  </div>
</template>
<script>
//  import axios from 'axios'
  import auth from './auth'
  import menus from './nav-config'
  export default {
    name: 'layout',
    data () {
      return {
        loggedIn: auth.loggedIn(),
        menus,
        currentRoute: this.$router.history.current.fullPath,
        isCollapse: false
      }
    },
    created () {
      auth.onChange = (loggedIn) => {
        this.loggedIn = loggedIn
      }
    },

    methods: {
      handleOpen(key, keyPath) {
        console.log(key, keyPath);
      },
      handleClose(key, keyPath) {
        console.log(key, keyPath);
      },
      toggleSider () {
        this.isCollapse = !this.isCollapse
      },
      handleDropdown (cmd) {
        if (cmd === 'logout') {
          auth.logout()
          this.$router.replace({ name: 'login' })
        }
      }
    }
//  data () {
//    return {
//      msg: 'Welcome to Your Vue.js App'
//    }
//  },
//  methods: {
//      add: function () {
//          this.msg = 1;
//        axios.get('/visitor/data_page').then(function (res) {
//          console.log(res)
//        })
//      }
//  }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="less">
  @import 'assets/less/home.less';
  @import '../static/css/style.css';
  @black: #2a323c;
  @light-black: #324057;
  @extra-light-black: #475669;
  @blue: #03a9f4;
  @gray: #d3dce6;
  @light-gray: #e5e9f2;
  @sider-width: 224px;
  @top-height: 70px;
  @sider-collapse-width: 64px;
  @transition: all 0.3s ease;
  @cont-padding: 15px;
  @fff:#fff;
  h1, h2 {
    font-weight: normal;
  }
  ul {
    list-style-type: none;
    padding: 0;
  }

  li {
    display: inline-block;
  }

  a {
    color: #42b983;
  }
  html, body, #app {
    margin: 0;
    height: 100%;
  }
 .fa{
   display: inline-block;
   width: 20px;
   height: 20px;
 }
  .p-layout-topbar {
    position: fixed;
    width: 100%;
    height: @top-height;
    line-height: @top-height;
    background-color: @black;
   // background-color: @blue;-->
    z-index: 101;
    color: @gray;
  }

  .p-layout-topbar a {
    color: @gray;
  }

  .p-layout-name {
    transition: all 0.3s ease;
    width: @sider-width;
    text-align: center;
    float: left;
    background-color: @black;
    font-family: Helvetica;
    font-size: 30px;
    &.sider-mini {
      width: @sider-collapse-width;
    }
    .full {
      text-decoration: none;
    }
    .mini {
    }
  }

  .p-layout-nav {
    float: right;
    padding-right: 10px;
    .nav-item {
      margin-right: 10px;
      .fa {
        font-size: 20px;
      }
      .el-badge__content.is-fixed {
        top: 20px;
      }
    }
    .p-layout-avatar {
      width: 36px;
      height: 36px;
      border: 2px solid @gray;
    }
  }

  .p-layout-nav img.p-layout-avatar {
    border-radius: 50%;
    vertical-align: middle;
    cursor: pointer;
  }

  .p-layout-sider {
    width: @sider-width;
    background-color: @black;
    position: fixed;
    top: @top-height;
    left: 0;
    height: 100%;
    transition: @transition;
    z-index: 102;
  }
  .el-menu-item-group__title{
    padding: 0;

  }
  .p-layout-user {
    padding: 20px;
    text-align: center;
    color: @fff;
    .p-layout-avatar {
      width: 64px;
      height: 64px;
      margin-bottom: 10px;
      border-radius: 50%;
    }
  }

  .p-layout-panel, .p-layout-content {
    position: absolute;
    left: 0;
    top: 0;
    bottom: 0;
    right: 0;
    overflow: hidden;
    background: #f5f5f5;
    transition: @transition;
    width: auto;
  }

  .p-layout-collapse {
    float: left;
    width: @top-height;
    cursor: pointer;
    text-align: center;
    color: @fff;
  }

  .p-layout-body {
    position: absolute;
    width: 100%;
    top: @top-height;
    bottom: 0;
    z-index: 100;
    height: calc(100% - 64px);
  }
  .p-layout-body .p-layout-panel {
    left: @sider-width;
  }
  .p-layout-body {
    &.sider-mini {
      .p-layout-panel {
        left: @sider-collapse-width;
      }
      .p-layout-sider {
        width: @sider-collapse-width;
      }
      .p-layout-user {
        display: none;
      }
      .el-menu {
        .el-submenu__icon-arrow,
        .nav-next {
          display: none;
        }
        &-item {
          padding: 0 !important;
        }
        .el-submenu {
          text-align: center;
        }
      }
    }
  }
  .p-layout-content {
    overflow-y: auto;
    .p-layout-container {
      padding: 15px;
      .p-layout-breadcrumb {
        box-shadow: 0 1px 2px 0 rgba(0, 0, 0, .1);
        padding: 25px 15px;
        background-color: @fff;
        margin: -15px -15px 0 -15px;
      }
      .p-layout-inner {
        padding: 10px;
        background: @fff;
        border-radius: 3px;
        margin-top: 10px;
      }
    }
  }
/*
  .p-layout {
    &-header {
      padding: 20px 10px;
      border-bottom: 1px solid #e9e9e9;
      background-color: @light-black;
      &:before,
      &:after {
        content: '';
        display: table;
      }
      &:after {
        clear: both;
      }
      .is-user {
        float: right;
      }
    }
  }
  */

  .p-layout-footer {
    position:fixed;
    bottom:0;
    left:0;
    height: 64px;
    line-height: 64px;
    text-align: center;
    font-size: 12px;
    color: #000;
    width: 100%;
  }

  .el-menu {
    border-radius: 0;
    background: @black;
  }

  .el-submenu .el-menu {
    background-color: #1f2f3d;
  }

  .el-submenu .el-menu-item:hover, .el-submenu__title:hover {
    background: #475669;
  }
  #index:hover{
    background: #475669;
  }

  .el-submenu__title {
    width: 100%;
    color: #d3dce6;
  }

  .el-submenu .el-menu-item, .el-submenu {
    width: 100%;
    padding: 0;
  }

 li.el-menu-item{
   color:#d3dce6;
 }
</style>

