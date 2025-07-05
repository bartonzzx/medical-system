import { getMenuList } from "../api/Login";
import Layout from "../layout/index.vue";

function tree(data, arr) {
  data.forEach((datas, index) => {
    arr.push({
      path: datas.path,
      name: datas.name,
      component:
        datas.component == "Layout"
         ? Layout
          : () => import(`../views/${datas.component}/index.vue`),
      meta: {
        title: datas.meta.title,
      },
      //子路由
      children: [],
    });
    if (datas.children) {
      const childArr = tree(datas.children, []);
      arr[index].children = childArr;
    }
  });
  return arr;
}

// export function getMenu() {
//   return new Promise((resolve, reject) => {
//     getMenuList(JSON.parse(localStorage.getItem("userInfo")).type).then(
//       (res) => {
//         if (res.data.code === 20000) {
//           resolve(tree(res.data.data.permissions, []));
//         } else {
//           alert("获取菜单列表失败！");
//           reject();
//         }
//       }
//     );
//   })
// }

console.log("userInfoRaw = ", localStorage.getItem("userInfo"));

export function getMenu() {
  return new Promise((resolve, reject) => {
    const userInfoRaw = localStorage.getItem("userInfo");

    // 🚫 防止为 null 或 undefined 报错
    if (!userInfoRaw) {
      console.error("userInfo 不存在，不能加载菜单！");
      reject(new Error("用户未登录或信息丢失"));
      return;
    }

    let userInfo;
    try {
      userInfo = JSON.parse(userInfoRaw);
    } catch (e) {
      console.error("userInfo JSON 解析失败:", e);
      reject(new Error("用户信息格式错误"));
      return;
    }

    getMenuList(userInfo.utype).then((res) => {
      if (res.data.code === 20000) {
        resolve(tree(res.data.data.permissions, []));
      } else {
        alert("获取菜单列表失败！");
        reject(new Error("菜单接口错误"));
      }
    }).catch(err => {
      console.error("获取菜单接口异常", err);
      reject(err);
    });
  });
}
