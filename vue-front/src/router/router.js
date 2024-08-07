import { createRouter, createWebHistory } from "vue-router";
import IntroPage from '@/views/IntroPage.vue'

import Home from "@/views/Home.vue";
import NotFound from "@/views/NotFound.vue";
import routerActivity from "@/views/Activity/router-activity";
import routerPersonal from "@/views/Personal/router-personal";
import routerEdit from "@/views/Edit/router-edit";
import routerSearch from "@/views/Search/router-search";
import routerShopping from "@/views/Shopping/router-shopping";
import routerLogin from "@/views/Login/router-login";
import routerMessage from "@/views/Message/router-message";
import routerPost from "@/views/Post/router-post";
import routerFriendPersonal from '@/views/Friend/router-personal';
import david from "@/views/david-view/david-view"
import routerSubs from '@/views/Subscriptions/router-subscriptions';//(Sean240729)訂閱
import BombPage from '@/views/Friend/BombPage.vue'; // 新增的页面组件
import routerManagement from "@/views/GM/router-management";

const routes = [
    { name: "home-link", path: "/home", component: Home },
    { name: "intropage-link", path: "/", component: IntroPage },
    { name: "notfound-link", path: "/:pathMatch(.*)*", component: NotFound },
    { path: '/views/Bomb', name: 'bomb', component: BombPage },
    ...routerActivity,
    ...routerPersonal,
    ...routerEdit,
    ...routerShopping,
    ...routerSearch,
    ...routerLogin,
    ...routerMessage,
    ...routerPost,
    ...routerFriendPersonal,
    ...david,
    ...routerSubs, //(Sean240729)訂閱
    ...routerManagement,
]

const router = createRouter({
    history: createWebHistory(),
    routes: routes,
})
router.beforeEach((to, from, next) => {
    const requiresStatus2 = to.meta.requiresStatus2;
    const userStatus = sessionStorage.getItem('user-status'); // 从 sessionStorage 获取用户状态

    if (requiresStatus2) {
        if (userStatus === '2') {
            next(); // 用户状态为2，允许访问
        } else {
            next('/notfound'); // 否则重定向到404页面
        }
    } else {
        next(); // 不需要状态2的页面，允许访问
    }
});

export default router;