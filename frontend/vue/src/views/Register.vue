<template>
  <div class="RegisterBackground">
    <div class="RegisterForm">
      <div class="form-container">
        <el-form :model="registerForm" ref="registerFormRef" status-icon @submit.native.prevent>
          <!-- 用户名输入框 -->
          <el-form-item prop="username" :rules="registerRules.username">
            <div class="input-group">
              <img src="../assets/username.png" class="input-icon" />
              <el-input v-model="registerForm.username" placeholder="用户名" clearable size="large" />
            </div>
          </el-form-item>

          <!-- 手机号输入框 -->
          <el-form-item prop="phone" :rules="registerRules.phone">
            <div class="input-group">
              <img src="../assets/username.png" class="input-icon" />
              <el-input v-model="registerForm.phone" placeholder="手机号" clearable size="large" />
            </div>
          </el-form-item>

          <!-- 密码输入框 -->
          <el-form-item prop="password" :rules="registerRules.password">
            <div class="input-group">
              <img src="../assets/password.png" class="input-icon" />
              <el-input v-model="registerForm.password" type="password" placeholder="密码" show-password size="large" />
            </div>
          </el-form-item>

          <!-- 确认密码输入框 -->
          <el-form-item prop="confirmPassword" :rules="registerRules.confirmPassword">
            <div class="input-group">
              <img src="../assets/password.png" class="input-icon" />
              <el-input v-model="registerForm.confirmPassword" type="password" placeholder="确认密码" show-password
                size="large" @keydown.enter="handleRegister" />
            </div>
          </el-form-item>

          <!-- 注册按钮 -->
          <el-form-item class="action-group">
            <el-button native-type="submit" class="register-btn" @click="handleRegister">
              立即注册
            </el-button>
            <div class="login-link">
              <el-button type="text" @click="$router.push('/user/login')">
                已有账号？返回登录
              </el-button>
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value !== this.registerForm.password) {
        callback(new Error('两次密码输入不一致'));
      } else {
        callback();
      }
    };

    return {
      registerForm: {
        username: "",
        phone: "",
        password: "",
        confirmPassword: ""
      },
      registerRules: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' },
          { min: 3, max: 16, message: '长度在 3 到 16 个字符', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '手机号不能为空', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { validator: validatePassword, trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    handleRegister() {
      this.$refs.registerFormRef.validate(async (valid) => {
        if (!valid) return;

        try {
          const response = await fetch("http://localhost:8080/api/register", {
            method: 'POST', // 指定请求方法为 POST
            headers: {
              // 必须手动设置 Content-Type 为 application/json
              'Content-Type': 'application/json',
            },
            // 按照后端接口图片，修改字段名并将其序列化为 JSON 字符串
            body: JSON.stringify({
              uname: this.registerForm.username,
              pwd: this.registerForm.password,
              phoneNumber: this.registerForm.phone
            }),
          });

          if (!response.ok) {
            // 如果响应状态码不是 2xx，则抛出错误，进入 catch 块
            throw new Error(`HTTP 错误! 状态码: ${response.status}`);
          }

          const data = await response.json();

          if (data.code === 20000) {
            this.$message.success("注册成功");
            this.$router.push("/user/login");
          } else {
            this.$message.error(data.message || "注册失败");
          }
        } catch (error) {
          // 此处会捕获网络请求失败的错误或上面手动抛出的 HTTP 错误
          this.$message.error("请求发送失败");
          console.error("注册请求错误:", error);
        }
      });
    }
  }
};
</script>

<style scoped>
.form-container {
  margin-top: 350px;
  margin-left: 32px;
}

.RegisterBackground {
  background: url("../assets/RegisterBackground.jpg") no-repeat center/cover;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.RegisterForm {
  background: url("../assets/Register.png") no-repeat center/contain;
  width: 800px;
  height: 600px;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
}

.input-group {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 8px;
  padding: 5px 15px;
  border: 1px solid #2abeb2;
}

.input-icon {
  width: 24px;
  height: 24px;
  margin-right: 10px;
}

:deep(.el-input__inner) {
  background: transparent !important;
  border: none !important;
  height: 40px;
  color: #2abeb2;
  font-size: 16px;
}

:deep(.el-input__inner::placeholder) {
  color: #2abeb2;
}

.action-group {
  margin-top: 30px;
  text-align: center;
}

.register-btn {
  width: 100%;
  height: 45px;
  background: #2abeb2;
  color: white;
  border-radius: 8px;
  font-size: 18px;
  border: none;
  cursor: pointer;
}

.register-btn:hover {
  background: #1da89b;
}

.login-link {
  margin-top: 15px;
}

:deep(.el-button--text) {
  color: #2abeb2;
  font-size: 14px;
}
</style>