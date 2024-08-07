<template>
    <div class="login-page">
        <div class="login-container">
            <div class="col-12">
                <h1 class="animate__bounceInDown">忘記密碼</h1>
                <form @submit.prevent="send">
                    <div class="mb-3">
                        <label for="exampleInputEmail1" class="form-label">請輸入帳號email</label>
                        <input type="text" v-model="email" class="form-control" id="exampleInputEmail1">
                    </div>
                    <button type="submit" class="btn btn-dark" id="send" disabled>送出</button>

                    <button class="btn btn-light">
                        <RouterLink class="nav justify-content-end" :to="{ name: 'login-text-link' }">登入</RouterLink>
                    </button>
                    <button class="btn btn-light">
                        <RouterLink class="nav justify-content-end" to="/views/registerPage">註冊</RouterLink>
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
import axiosapi from '@/plugins/axios.js';
import 'animate.css';
import Swal from 'sweetalert2';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const email = ref('');
const password = ref('');
const router = useRouter();

const success = ref('');
const error = ref('');

function validateEmail(event) {
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    let a = document.querySelector("#send")
    if (emailPattern.test(event.target.value)) {
        a['disabled'] = "";
    } else {
        a['disabled'] = "disabled";
    }
}

function send() {
    sessionStorage.removeItem('user-status');
    sessionStorage.removeItem('Session-Id');
    sessionStorage.removeItem('Session-UserInfoId');
    if (!email.value) {
        error.value = '請填寫所有必填項目';
        return;
    }
    let request = {
        "email": email.value,
    };
    axiosapi.post("/ajax/mail/forget", request
    ).then(function (response) {
        console.log(response.data.token)
        if (response.data.success) {
            Swal.fire({
                icon: "success",
                text: response.data.success,
            }).then(function (result) {
                updateLoggedInStatus();
                window.location.replace('/');
            });
        } else {
            Swal.fire({
                icon: "warning",
                text: "Error " + response.data.error,
            });
        }
    }).catch(function (error) {
        Swal.fire({
            icon: "error",
            text: "異常錯誤: " + error.message,
        });
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
    document.querySelector("#exampleInputEmail1").addEventListener("input", validateEmail);
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
