<template>
  <div class="LoginBackground">
    <div class="LoginForm">
      <div class="big-contain">
        <el-form :model="loginForm" ref="loginForm" status-icon @submit.native.prevent>
          <div class="form">

            <el-form-item prop="username" :rules="loginRules.usernameRules" class="NameNotNull">
              <div class="UserName">
                <img src="../assets/username.png" class="Logo" />
                <el-input v-model="loginForm.username" placeholder="用户名" clearable class="InputFix" />
              </div>
            </el-form-item>

            <el-form-item prop="password" :rules="loginRules.passwordRules" class="PasswordNotNull">
              <div class="Password">
                <img src="../assets/password.png" class="Logo" />
                <el-input v-model="loginForm.password" type="password" placeholder="密码" show-password
                  @keydown.enter="handleLogin('loginForm')" class="InputFix" />
              </div>
            </el-form-item>

            <!-- 登录注册 -->
            <el-form-item>
              <button class="LoginBtn" @click="handleLogin('loginForm')"></button>
              <div style="margin-top: 10px; text-align: center;">
                <el-button type="text" font-size="small"  @click="$router.push('/user/register')">还没有账号？去注册</el-button>
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
      loginForm: {
        username: "",
        password: "",
      },
      loginRules,
    };
  },
  methods: {
    handleLogin(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$store
            .dispatch("app/login", this.loginForm)
            .then(() => {
              this.$store.dispatch("app/setMenuList").then(() => {
                this.$router.replace("/");
              });
            })
            .catch(() => {
              this.$message.error("账号或密码错误");
            });
        } else {
          this.$notify.error({
            title: "错误",
            message: "请输入正确的用户名密码",
          });
          return false;
        }
      });
    },

  },
};
</script>

<style scoped>
.LoginBackground {
  background: url("../assets/LoginBackground.jpg") no-repeat;
  background-size: cover;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.LoginForm {
  background: url("../assets/Login.png") no-repeat;
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

.LoginBtn {
  overflow: hidden;
  background: url("../assets/LoginButton.png");
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
