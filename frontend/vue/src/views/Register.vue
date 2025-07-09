<template>
    <div class="RegisterBackground">
      <div class="RegisterForm">
        <div class="big-contain">
          <el-form :model="registerForm" ref="registerForm" status-icon>
            <div class="form">
  
              <!-- 用户名输入框 -->
              <el-form-item prop="username" :rules="loginRules.usernameRules" class="NameNotNull">
                <div class="UserName">
                  <img src="../assets/username.png" class="Logo" />
                  <el-input
                    v-model="registerForm.username"
                    placeholder="用户名"
                    clearable
                    class="InputFix"
                  />
                </div>
              </el-form-item>
  
              <!-- 密码输入框 -->
              <el-form-item prop="password" :rules="loginRules.passwordRules" class="PasswordNotNull">
                <div class="Password">
                  <img src="../assets/password.png" class="Logo" />
                  <el-input
                    v-model="registerForm.password"
                    type="password"
                    placeholder="密码"
                    show-password
                    @keydown.enter="handleRegister"
                    class="InputFix"
                  />
                </div>
              </el-form-item>
  
              <!-- 注册按钮 -->
              <el-form-item>
                <button class="RegisterBtn" @click="handleRegister"></button>
                <div style="margin-top: 10px; text-align: center;">
                  <el-button type="text" @click="$router.push('/user/login')">已有账号？返回登录</el-button>
                </div>
              </el-form-item>
  
            </div>
          </el-form>
        </div>
      </div>
    </div>
  </template>
  
  <script>
import { loginRules } from "../utils/validator";

  export default {
    data() {
      return {
        registerForm: {
          username: "",
          password: "",
        },
        loginRules,
      };
    },
    methods: {
      handleRegister() {
        this.$refs.registerForm.validate((valid) => {
          if (!valid) return;
          this.$axios
            .post("/user/register", {
              username: this.registerForm.username,
              password: this.registerForm.password,
            })
            .then((res) => {
              if (res.data.code === 20000) {
                this.$message.success("注册成功，请登录");
                this.$router.push("/user/login");
              } else {
                this.$message.error(res.data.message || "注册失败");
              }
            })
            .catch(() => {
              this.$message.error("注册请求失败");
            });
        });
      },
    },
  };
  </script>
  
  <style scoped>
  .RegisterBackground {
    background: url("../assets/RegisterBackground.jpg") no-repeat;
    background-size: cover;
    width: 100%;
    height: 100%;
    overflow: hidden;
  }
  
  .RegisterForm {
    background: url("../assets/Register.png") no-repeat;
    background-size: 800px 509px;
    width: 1000px;
    height: 800px;
    margin-left: 400px;
    margin-top: 100px;
    overflow: hidden;
  }
  
  .UserName {
    overflow: hidden;
    border: 2px solid #2abeb2;
    border-radius: 20px;
    margin-top: 270px;
    margin-left: -60px;
    height: 70px;
    width: 530px;
    display: flex;
    align-items: center;
  }
  
  .Password {
    overflow: hidden;
    border: 2px solid #2abeb2;
    border-radius: 20px;
    margin-top: 20px;
    margin-left: -60px;
    height: 70px;
    width: 530px;
    display: flex;
    align-items: center;
  }
  
  .Logo {
    margin-left: 15px;
    width: 32px;
    height: 32px;
  }
  
  .RegisterBtn {
    overflow: hidden;
    background: url("../assets/RegisterButton.png");
    background-size: 100%;
    width: 220px;
    height: 65px;
    margin-left: 290px;
    margin-top: 10px;
    border: none;
    cursor: pointer;
  }
  
  .NameNotNull {
    margin-left: 200px;
  }
  
  .PasswordNotNull {
    margin-left: 200px;
  }
  
  input::-webkit-input-placeholder {
    color: #2abeb2;
  }
  
  input::-ms-input-placeholder {
    color: #2abeb2;
  }
  
  /* el-input 样式覆盖 */
  ::v-deep(.InputFix .el-input__inner) {
    height: 60px;
    font-size: 24px;
    color: #2abeb2;
    margin-left: 20px;
    border: none;
    box-shadow: none;
  }
  </style>
  