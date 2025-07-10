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

            <el-form-item>
              <button class="LoginBtn" @click="handleLogin('loginForm')"></button>
            </el-form-item>
            <div class="register-link">
              <el-button type="text" @click="$router.push('/user/register')">
                没有账号？前往注册
              </el-button>
            </div>
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
              console.log("登录成功");
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
/* 核心优化：使用Flex布局实现完美居中 */
.LoginBackground {
  background: url("../assets/LoginBackground.jpg") no-repeat center center;
  background-size: cover;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  width: 100%;
}

.LoginForm {
  background: url("../assets/Login.png") no-repeat center center;
  background-size: contain;
  width: 800px;
  height: 509px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.big-contain {
  width: 100%;
  display: flex;
  justify-content: center;
}

.form {
  margin-top: 90px;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  transform: translateY(-15px);
  /* 微调垂直位置 */
}

/* 表单元素优化 */
.UserName,
.Password {
  border: 2px solid #2abeb2;
  border-radius: 20px;
  height: 70px;
  width: 530px;
  display: flex;
  align-items: center;
  margin-top: 20px;
}

.UserName {
  margin-top: 270px;
}

.Logo {
  margin-left: 15px;
  width: 32px;
  height: 32px;
}

.LoginBtn {
  background: url("../assets/LoginButton.png") center center;
  background-size: contain;
  width: 220px;
  height: 65px;
  margin-top: 30px;
  border: none;
  cursor: pointer;
}

/* 移除不必要的定位 */
.NameNotNull,
.PasswordNotNull {
  display: flex;
  justify-content: center;
  width: 100%;
}

/* 输入框样式优化 */
::v-deep(.InputFix .el-input__inner) {
  height: 60px;
  font-size: 24px;
  color: #2abeb2;
  margin-left: 20px;
  border: none;
  box-shadow: none;
  width: calc(100% - 70px);
  /* 防止内容溢出 */
}

/* 响应式优化 */
@media (max-width: 900px) {
  .LoginForm {
    width: 90%;
    background-size: cover;
  }

  .UserName,
  .Password {
    width: 90%;
    max-width: 530px;
  }
}
</style>