<template>
    <div class="register-page">
        <div class="register-container">
            <div class="col-12">
                <h1 class="animate__bounceInDown">註冊</h1>

                <form @submit.prevent="register">
                    <div class="mb-3">
                        <label for="exampleInputuserName" class="form-label">email</label>
                        <input type="text" class="form-control" v-model="userName" id="exampleInputuserName"
                            @input="validateEmail">
                    </div>
                    <span id="emailText"></span>
                    <div class="mb-3">
                        <label for="exampleInputPassword1" class="form-label">密碼</label>
                        <input type="password" class="form-control" v-model="password" id="exampleInputPassword1">
                    </div>
                    <div class="mb-3">
                        <label for="exampleInputPassword1" class="form-label">帳號</label>
                        <input type="text" class="form-control" v-model="lastName" id="exampleInputlastName1">
                    </div>
                    <button type="submit" class="btn btn-dark" id="send">送出</button>
                </form>

                <div>
                    <div>{{ success }}</div>
                    <div>{{ error }}</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang>
import Swal from 'sweetalert2';
import axiosapi from '@/plugins/axios.js';
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

const userName = ref('');
const password = ref('');
const lastName = ref('');
const router = useRouter();

const success = ref('');
const error = ref('');

function validateEmail(event) {
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    let btn = document.querySelector("#send")
    let text = document.getElementById("emailText")

    if (emailPattern.test(event.target.value)) {
        btn['disabled'] = "";

        text.innerHTML = ""
    } else {
        btn['disabled'] = "disabled";
        text.innerHTML = "請輸入正確的email"
        text.style.color = "red"
    }
}

function register() {
    if (!userName.value || !lastName.value || !password.value) {
        error.value = '請填寫所有必填項目';
        return;
    }

    let request = {
        "userName": userName.value,
        "password": password.value,
        "lastName": lastName.value
    };

    axiosapi.post("/ajax/pages/users", request)
        .then(function (response) {
            console.log("Response", response);
            Swal.fire({
                icon: 'success',
                text: '註冊成功',
            }).then(function (result) {
                router.push({ name: "login-text-link" }); // 導向登入
                console.log("成功");
            });
        })
        .catch(function (err) {
            error.value = '註冊失敗: ' + err.message;
            console.error("Error", err);
            Swal.fire({
                icon: 'error',
                text: '註冊失敗: ' + err.message,
            });
        });
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
.register-page {
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
.register-container {
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
