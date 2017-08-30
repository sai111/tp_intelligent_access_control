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
        <div class="message">
          <div class="informationEntry"></div><u class="topLine"></u>
          <div class="relevance"></div><u class="topLine"></u>
          <div class="accessCard"></div><u class="topLine"></u>
          <div class="completed"></div>
        </div>
        <hr>
        <el-form ref="form" :model="form" id="form">
          <div class="formTop">
            <div class="formLeft">
              <el-form-item label="姓名：">
                <el-input v-model="form.user" placeholder="请输入姓名" class="widthEdit"></el-input>
              </el-form-item>
              <el-form-item label="国家：">
                <el-select v-model="form.region" placeholder="请选择国家" class="widthEdit">
                  <el-option label="中国" value="shanghai"></el-option>
                  <el-option label="其他" value="beijing"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="出生年月：">
                <el-col :span="11" class="widthEdit">
                  <el-date-picker type="date" placeholder="选择日期" v-model="form.birth"
                                  style="width: 100%; width: 250px;"></el-date-picker>
                </el-col>
              </el-form-item>
              <el-form-item label="证件类型：">
                <el-select v-model="form.papers" class="widthEdit">
                  <el-option label="二代身份证" value="0"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="证件有效期始：">
                <el-date-picker type="date" placeholder="选择日期" v-model="form.dateStart"
                                style="width: 100%; width:250px"></el-date-picker>
              </el-form-item>
            </div>
            <div class="formRight clearfix">
              <div class="rightT">
                <div class="topLeft">
                  <el-form-item label="性别：">
                    <el-radio-group v-model="form.sex" placeholder="">
                      <el-radio label="男"></el-radio>
                      <el-radio label="女"></el-radio>
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item label="民族：">
                    <el-select v-model="form.nation" placeholder="" style="width:160px;">
                      <el-option label="中国" value="shanghai"></el-option>
                      <el-option label="其他" value="beijing"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="手机号码：">
                    <el-input v-model="form.phone" placeholder="请输入手机号码"  style="width:160px;"></el-input>
                  </el-form-item>
                  <el-form-item label="证件号码：">
                    <el-input v-model="form.papersNumber" style="width:160px;" ></el-input>
                  </el-form-item>
                </div>
                <div class="topRight">
                  <el-form-item>
                    <el-button type="primary" style="display: inline-block;" >读卡</el-button>
                  </el-form-item>
                  <el-form-item>
                    <div class="photo"></div>
                  </el-form-item>
                </div>
              </div>
              <div class="bottomRight">
                <el-form-item label="证件有效期止：">
                  <el-date-picker type="date" placeholder="选择日期" v-model="form.dateStart" style="width: 100%;width:250px"></el-date-picker>
                </el-form-item>
              </div>
            </div>
          </div>
          <div class="submitText">
            <el-form-item>
            <el-button type="primary">下一步</el-button>
            </el-form-item>
          </div>
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
        form: {
          user: '',
          sex: '',
          region: '', //国家
          nation: '', // 民族
          papers: '', //证件
          birth: '',
          phone: '',
          papersNumber: '',
          dateStart: '',
          dateEnd: '',

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

  .demonstration button {
    width: 70px;
  }

  .widthEdit {
    width: 250px;
  }
/* */
  .message{
    text-align: center;
    height: 160px;
    display: flex;
    margin:0 230px;
  }
  .informationEntry, .relevance , .accessCard, .completed{
    flex:1;
     margin:0 10px;
   border:1px solid red;
  }
  u.topLine{
    display: inline-block;
    width: 70px;
    background: red;
    height: 2px;
    margin-top: 70px;
  }
  /*表格*/
  #form {
    margin: 40px 230px;
    text-align: right;
  }
  #form .formTop{
    width: 100%;
    display: flex;
  }
  .submitText{
    height: 100px;
  }
  .formLeft, .formRight {
    flex:1;
  }
  .formLeft{
    margin-right: 100px;
  }
  .formRight {
    text-align: left;
  }
  .rightT{
    width: 100%;
    display: flex;
  }
  .topLeft{
    flex:7;
    height: 200px;
  }
  .topRight{
    flex:3;
    text-align: center;
  }
  .bottomRight{

  }
  .photo{
    width: 100px;
    height:120px;
    background: #D6D6D6;
    margin:0 5px;
  }
</style>
