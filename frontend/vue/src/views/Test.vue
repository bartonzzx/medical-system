<template>
  <div style="background-color: white; color: white; padding: 20px; text-align: center;">
    <br>
    {{ a }}
    <br>
    {{ data.message }}
    <br>

    <div>
      <input v-model="data.name">
    </div>

    <div>
      <span @click="show(data.name)" style="color: red;" v-if="data.name === '佩奇'">小猪佩奇</span>
      <span @click="show(data.name)" style="color: blue;" v-else-if="data.name === '乔治'">小猪乔治</span>
      <span v-else style=" color: green;">请输入佩奇或乔治</span>
    </div>

    <select name="水果选择" id="">
      <option v-for="item in data.fruit" :key="item.id">{{ item.name }}</option>
    </select>
    
    <div>
      <el-button type="primary" @click="addLuck" style="margin: 30px;">切换</el-button>
    </div>

    <div style="margin:30px">
      <el-icon :size="20" color="red">
        <Edit />
      </el-icon>
    </div>

    <div>
      <img :src="data.img" alt="">
    </div>

    <div>
      <el-icon :size="20" color = "blue">
        <Search />
      </el-icon>
    </div>

  <div style="margin-bottom: 20px">
        <el-button type="primary" @click="router.back()">返回Home 页面</el-button>
  </div>
  </div>
</template>



<script setup>
import { reactive, ref ,onMounted} from "vue";
import router from '@/router/index.js';
const a = ref("Hello World");

const data = reactive({
  //js中获取路由传递的参数
  id:router.currentRoute.value.query.id,
  string:router.currentRoute.value.query.string,

  message: "Hello from Home.vue",
  name: '佩奇',
  fruit:[
    {
      id:1,name:'苹果'
    },
    {
      id:2,name:'香蕉'
    },
    {
      id:3,name:'橘子'
    }
  ],
  img:'	https://www.runoob.com/images/border.png'
});
const show = (name) => {
  alert(name);
};
const addLuck = () => {
  if (data.name === "佩奇")
    data.name = "乔治";
  else
    data.name = "佩奇";
};
console.log('获取到传递过来的id='+data.id);
console.log('获取到传递过来的string='+data.string);
onMounted(() => {
  console.log("页面加载完成！");
});
</script>
