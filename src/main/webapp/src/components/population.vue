<template>
  <div class="p-layout-content">
    <div class="p-layout-container">
      <div class="p-layout-breadcrumb">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>人员管理</el-breadcrumb-item>
          <el-breadcrumb-item>人口管理</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="p-layout-inner">
        <el-form :inline="true" :model="formInline" class="demo-form-inline">
          <el-form-item>
            <el-select v-model="formInline.region">
              <el-option value="shanghai">nihoaaaaa</el-option>
              <el-option value="beijing">sgsagaga</el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <div class="block">
              <span class="demonstration">报警等级</span>
              <span class="wrapper">
          <el-button type="success">全部</el-button>
          <el-button type="warning">高</el-button>
          <el-button type="danger">中</el-button>
          <el-button type="info">低</el-button>
        </span>
            </div>
          </el-form-item>
        </el-form>
      <slot></slot>
      </div>
    </div>
  </div>
</template>
<script>
  export default{
    name: 'PeopleCountManage',
    data() {
      return {
        formInline: {
          user: '',
          region: ''
        }
      }
    },
    methods: {
      onSubmit() {
        console.log('submit!');

      }
    }
  }
</script>
<style>
  .el-scrollbar .el-select-dropdown__item {
    width: 100%;
  }
  .demonstration button{
    width: 70px;
  }

</style>
