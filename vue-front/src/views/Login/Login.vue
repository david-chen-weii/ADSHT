<template>
    <div class="login-page">
        <div class="login-container">
            <div class="col-12">
                <h1 class="animate__bounceInDown">登入</h1>
                <form @submit.prevent="login">
                    <div class="mb-3">
                        <label for="exampleInputEmail1" class="form-label">帳號</label>
                        <input type="text" v-model="userName" class="form-control" id="exampleInputEmail1">
                    </div>
                    <div class="mb-3">
                        <label for="inputPassword5" class="form-label">密碼</label>
                        <input type="password" class="form-control" v-model="password" id="inputPassword5">
                        <div id="passwordHelpBlock" class="form-text">
                            Your password must be 8-20 characters long, contain letters and numbers, and must not contain
                            spaces, special characters, or emoji.
                        </div>
                    </div>
                    <button type="submit" class="btn btn-dark">送出</button>
                    <button class="btn btn-light">
                        <RouterLink class="nav justify-content-end" to="/views/registerPage">註冊</RouterLink>
                    </button>
                    <button class="btn btn-light">
                        <RouterLink class="nav justify-content-end" :to="{ name: 'forgetPassword-link' }">忘記密碼</RouterLink>
                    </button>
                </form>

                <div>
                    <div>{{ success }}</div>
                    <div>{{ error }}</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import 'animate.css';
import Swal from 'sweetalert2';
import axiosapi from '@/plugins/axios.js';
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const userName = ref('');
const password = ref('');
const router = useRouter();

const success = ref('');
const error = ref('');



function login() {
    if (!userName.value || !password.value) {
        error.value = '請填寫所有必填項目';
        return;
    }

    let request = {
        "userName": userName.value,
        "password": password.value
    };

    axiosapi.post("/ajax/pages/login", request, {
        headers: {
            'Session-Id': sessionStorage.getItem("session"),
        }
    }).then(function (response) {
        if (response.data.success) {
            axiosapi.defaults.headers['session-id'] = response.data.session;
            sessionStorage.setItem('Session-Id', response.data.session);
            sessionStorage.setItem('Session-UserInfoId', response.data.userinfo_id);
            sessionStorage.setItem('user-status', response.data.status); // 存储用户状态
            Swal.fire({
                icon: "success",
                text: response.data.message,
            }).then(function (result) {
                axiosapi.defaults.headers.aaa = response.data.session;
                updateLoggedInStatus();
                window.location.replace('/');
            });
        } else {
            Swal.fire({
                icon: "warning",
                text: "請輸入正確的帳號密碼: " + response.data.message,
            });
        }
    }).catch(function(error) {
        if (error.response && error.response.status === 500) {
            Swal.fire({
                icon: "error",
                text: "此帳號已被封鎖，請聯繫客服人員",
            });
        } else {
            Swal.fire({
                icon: "error",
                text: "登入失敗: " + (error.response?.data?.message || error.message),
            });
        }
    });
}

function updateLoggedInStatus() {
    const loggedIn = sessionStorage.getItem('loginId') !== null;
    document.dispatchEvent(new CustomEvent('login-status-changed', { detail: { loggedIn } }));
}
onMounted(() => {
    const heading = document.querySelector('h1');
    if (heading) {
        heading.classList.add('animate__animated', 'animate__bounceInDown');
    }
});
</script>

<style scoped>
/* 確保背景圖片覆蓋整個頁面，無邊距和內邊距 */
body,
html {
    height: 100%;
    width: 100%;
    margin: 0;
    padding: 0;
}

/* 背景圖片設置 */
.login-page {
    display: flex;
    justify-content: center;
    /* 水平置中 */
    align-items: center;
    /* 垂直置中 */
    min-height: 100vh;
    width: 100%;
    background-image: url('https://images.pexels.com/photos/1939485/pexels-photo-1939485.jpeg');
    background-size: contain;
    background-position: center;
}

/* 登錄容器設置 */
.login-container {
    max-width: 500px;
    /* 設定最大寬度 */
    width: 100%;
    background-color: rgba(255, 255, 255, 0.8);
    /* 添加背景顏色，使其與背景圖片區分開 */
    border-radius: 10px;
    /* 添加圓角 */
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    /* 添加陰影 */
    padding: 20px;
    /* 添加內邊距 */
    margin-top: -50px;
    /* 向上移動 */
}
</style>
