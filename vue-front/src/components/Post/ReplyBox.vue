<template>
    <div class="ui large transparent left icon input input-container">
        <i class="heart outline icon"></i>
        <input v-model="replyContent" type="text" placeholder="Add Comment..." @keydown.enter="handleReply">
    </div>
</template>

<script setup>
import axiosapi from '@/plugins/axios';
import Swal from 'sweetalert2';
import { ref, defineProps } from 'vue';
const props = defineProps(["postId"])

const replyContent = ref('');
const postId = ref('');
axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");

function handleReply() {
    if (replyContent.value.trim() === '') {
        alert('回覆內容不能為空');
        return;
    }

    // 打印 postId 和 replyContent 以检查值
    console.log('Reply content:', replyContent.value);

    const params = new URLSearchParams(window.location.search);
    postId.value = params.get('id');
    console.log("postId", postId.value);

    const replyData = {
        content: replyContent.value
    };
    //確認登入
    if (!sessionStorage.getItem("Session-Id")) {
        console.log("not login");
        //沒登入 跳轉login
        Swal.fire({
            icon: "error",
            text: "請登入",
            showConfirmButton: true,
            confirmButtonText: '確定'
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.replace('/views/login');
            }
        });
        return;
    }
    axiosapi.post(`/ajax/comments/newComment/${postId.value}`, replyData, {
        headers: {
            'Content-Type': 'application/json',
            'session-id': sessionStorage.getItem("Session-Id")
        }
    })
        .then(response => {
            console.log('回覆成功:', response.data);
            replyContent.value = ''; // 清空输入框
            sessionStorage.setItem('newCommentId', response.data.id);
            window.location.reload(); // 刷新頁面
        })
        .catch(error => {
            console.error('回覆失败:', error.response.data);
            alert('提交回覆時出錯，請稍後在試。');
        });
}
</script>

<style scoped>
.input-container {
    display: flex;
    align-items: center;
}
</style>
