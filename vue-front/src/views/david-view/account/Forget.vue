<template>
    <Forget v-show="show" v-model="obj" @reset="reset"></Forget>
</template>
    
<script setup>
import Forget from '@/components/david-comp/accountComp/forgetComp.vue';
import axiosapi from '@/plugins/axios';
import Swal from 'sweetalert2';
import { onBeforeMount, onMounted, ref } from 'vue';
const show = ref(false)
const obj = ref({
    password: "",
    passwordCheck: ""
})
onBeforeMount(() => {
    setToken();
})

onMounted(() => {
    check();
})
const setToken = async () => {
    console.log(axiosapi.defaults.headers)
    const url = new URL(window.location.href)
    axiosapi.defaults.headers.authorization = `Bearer ${url.pathname.split("/").pop()}`
}

const check = async () => {
    await axiosapi.post("ajax/pages/resetPasswordCheck"
    ).then((response) => {
        show.value = true
    }).catch((error) => {
        Swal.fire({
            icon: "error",
            text: "沒有權限",
            showConfirmButton: true,
            confirmButtonText: '確認'
        }).then((result) => {
            //router.push('/views/login')
            window.location.replace('/views/login');
        })
    })
}
function reset() {
    const url = new URL(window.location.href)
    console.log("send", axiosapi.defaults.headers.authorization)
    if (obj.value.password != obj.value.passwordCheck) {
        Swal.fire({
            icon: "error",
            text: "密碼不一致"
        }).catch((error) => {
            console.log(error);
        })
    } else {

        axiosapi.post("/ajax/pages/resetPasswordCheck/reset", obj.value
        ).then((response) => {
            console.log(response.data)
            if (response.data.success) {
                Swal.fire({
                    icon: "success",
                    text: response.data.success,
                    showConfirmButton: true,
                    confirmButtonText: '確認'
                }).then((result) => {
                    window.location.replace('/views/login');
                })
            }
        }).catch((error)=>{
            console.log("error");
        })
    }


}
</script>
    
<style></style>