<template>
  <div class="ui card large">
    <div class="content">
      <div class="time">{{ formatTime(card.addTime) }}</div>
      <span>
        <RouterLink class="nav-link" :to="'/views/Friend?id=' + card.posterInfoId">
          <img class="ui avatar image" v-show="card.posterPhoto !== '' && card.posterPhoto !== 'undefined'"
            :src="card.posterPhoto" alt="Base64 Image">
          {{ card.posterName }}
        </RouterLink>
      </span>
      <!-- 下拉菜单 -->
      <div v-show="show" class="ui compact menu right floated">
        <div class="ui simple dropdown item ">
          <i class="ellipsis vertical icon"></i>
          <div class="menu">
            <!-- 删除按钮 -->
            <button class="item" @click="deletePost(card.postId)">
              <i class="trash icon"></i> Delete
            </button>
            <!-- 编辑按钮 -->
            <button class="item" @click="editPost(card.postId)">
              <i class="edit icon"></i> Edit
            </button>
          </div>
        </div>
      </div>
    </div>
    <div class="postImg" style="display: flex; align-items: center; justify-content: center; background-color: white;">
      <img v-show="card.image !== ''" :src="card.image || defaultImage" 
      style="
      border-radius: 1%;
      width: 90%;  
      height: auto; /* 根據寬度自動調整高度 */
      object-fit: contain; /* 確保圖片內容不會變形 */
      max-height: 490px;
      object-fit: fill;">
    </div>

    <div class="content">
      <div>{{ card.content }}</div>
      <br>
      <span class="right floated" @click="emits('clickLike')">
        <i class="heart outline like icon"></i>
        {{ card.likeCount }} likes
      </span>
      <RouterLink class="nav-link" :to="'/views/post?id=' + card.postId" @click.native="handleCommentClick">
        <i class="comment icon"></i>
        {{ card.commentNum }} comments
      </RouterLink>
    </div>
    <div class="extra content">
      <div class="ui large transparent left icon input">
        <ReplyBox v-show="showCommentBar" :postId="card.postId" />
      </div>
    </div>
    <!-- Edit Modal -->
    <EditPost :edit="edit" />
  </div>
</template>
  
<script setup>
import axiosapi from '@/plugins/axios';
import { ref } from 'vue';
import EditPost from '../Edit/EditPost.vue';
import { Modal } from 'bootstrap/dist/js/bootstrap.bundle.min';
import { formatDistanceToNow, format, differenceInSeconds, differenceInMinutes, differenceInHours, differenceInDays } from 'date-fns'; // 导入所需的date-fns函数
import ReplyBox from '@/components/Post/ReplyBox.vue';
const props = defineProps(["card", "show", "showCommentBar"]);
const emits = defineEmits(["clickLike", "comment-click"]);
const edit = ref({ content: '', image: null, postId: null, permission: '1' });

axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");//都用這個

const defaultImage = 'https://t4.ftcdn.net/jpg/02/86/37/57/360_F_286375793_tMNCezCxZVJMzXVlunBoKI1JWP72cE9L.jpg';

function formatTime(time) {
  const now = new Date();
  const targetDate = new Date(time);
  const secondsDifference = differenceInSeconds(now, targetDate);
  const minutesDifference = differenceInMinutes(now, targetDate);
  const hoursDifference = differenceInHours(now, targetDate);
  const daysDifference = differenceInDays(now, targetDate);

  if (secondsDifference < 60) {
    return `${secondsDifference}秒前`;
  } else if (minutesDifference < 60) {
    return `${minutesDifference}分鐘前`;
  } else if (hoursDifference < 24) {
    return `${hoursDifference}小時前`;
  } else {
    return `${daysDifference}天前`;
  }
}

function deletePost(postId, onSuccess, onError) {
  axiosapi.delete(`/ajax/post/deletePost/${postId}`)
    .then(response => {
      console.log(`Post ${postId} deleted successfully.`);
      // 调用成功回调函数，更新父组件中的帖子列表或者重新获取数据
      if (onSuccess) {
        onSuccess(response);
      }
      window.location.replace('/home');
    })
    .catch(error => {
      console.error(`Error deleting post ${postId}:`, error);
      // 调用错误回调函数，处理删除失败的情况
      if (onError) {
        onError(error);
      }
    });
}


const editPost = (postId) => {
  const post = props.card;
  edit.value = { content: post.content, image: post.image, postId: postId };

  const editModalElement = document.getElementById('editModal');
  if (editModalElement) {
    const editModal = Modal.getInstance(editModalElement) || new Modal(editModalElement);
    if (editModal) {
      editModal.show();
    } else {
      console.log('Failed to get the modal instance.');
    }
  } else {
    console.log('Failed to find the modal element.');
  }
};
const handleCommentClick = (comment) => {
  emits('comment-click', comment); // Emit comment-click event with the selected comment
};
</script>
  
<style scoped>
/* 如果需要，可以添加局部作用域的樣式 */
.nav-link {
  display: inline;
}

.content {
  width: 100%;
  /* 设置宽度 */
  height: auto;
  /* 设置高度 */
  font-size: 1.3em;
  /* 放大字体 */
}

.time{
  position: absolute;
  right: 80px;
  margin: 0;
  padding: 0px;
}

.ui.card.large {
  /* 确保卡片也被调整大小 */
  width: 470px;
  height: auto;
  font-size: 1.3em;
  /* 放大字体 */
}

.ui.avatar.image {
  width: 50px;
  /* 增大头像宽度 */
  height: 50px;
  /* 增大头像高度 */
  object-fit: cover;
  /* 确保头像内容不会变形 */
}

.date-and-menu {
  position: relative;
  /* 确保下拉菜单绝对定位相对于这个容器 */
}

.ui.compact.menu.right.floated {
  position: absolute;
  top: 0;
  right: 0;
  border: none; /* 移除邊框 */
  box-shadow: none; /* 移除陰影 */
  background: none; /* 使背景透明 */
  padding: 0;
  font-size: 20px;
}

.postImg{
  width: 100%;
  max-width: 100%;
  height: 500px;
  max-height: 100%;
}

</style>
  