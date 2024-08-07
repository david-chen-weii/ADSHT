<template>
  <div class="container">
    <div class="cards-container">
      <CommentCard :card="post" :comments="comments" :postId="postId" :show="post.posterId == userId" :userId="userId" />
    </div>
  </div>
</template>
    
<script setup>
import CommentCard from "@/components/Post/CommentCard.vue";
import axiosapi from '@/plugins/axios';
import { differenceInDays, differenceInHours, differenceInMinutes, differenceInSeconds } from 'date-fns';
import { onMounted, ref } from 'vue';
// 初始化
const data = ref({});
const comments = ref([]);
const post = ref({});
const postId = ref("");
const userId = ref('')
//URL確認Id資訊
postId.value = new URLSearchParams(window.location.search).get('id');

// 資料取得
const fetchData = async () => {
  try {
    const response = await axiosapi.get(`/ajax/post/findOnePost/${postId.value}`);
    console.log('Fetched data:', response.data); // 检查 API 返回的数据
    return response.data;
  } catch (error) {
    console.error('Error fetching data:', error);
    return null;
  }
};

// DOM鉤子
onMounted(async () => {
  data.value = await fetchData();
  post.value = data.value.post;
  comments.value = data.value.comments;
  userId.value = sessionStorage.getItem('Session-UserInfoId');
});

</script>
    
<style scoped>
.container {
  display: flex;
  justify-content: center;
  /* 水平居中对齐 */
  align-items: flex-start;
  /* 置顶对齐 */
  height: 100vh;
  /* 使容器的高度为视口高度 */
  padding: 20px;
  /* 添加内边距（可选） */
}

.insidePost {
  width: 100%;
  max-width: 800px;
  /* 根据需要设置最大宽度 */
}
</style>
