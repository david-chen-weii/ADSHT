<template>
  <div class="ui card large">
    <!-- 卡片头部 -->
    <div class="content">
      <div class="right floated meta">{{ formatTime(card.addTime) }}</div>
      <span>
        <RouterLink class="nav-link" :to="'/views/Friend?id=' + card.posterId">
          <img class="ui avatar image" v-show="card.posterPhoto !== '' && card.posterPhoto !== 'undefined'"
            :src="card.posterPhoto" alt="Base64 Image">
          {{ card.posterName }}
        </RouterLink>
      </span>
      <!-- 下拉菜单 -->
      <div class="ui compact menu right floated" v-show="show || canDelete">
        <div class="ui simple dropdown item">
          <i class="ellipsis vertical icon"></i>
          <div class="menu">
            <!-- 删除按钮 -->
            <button class="item" @click="deletePost(card.postId)">
              <i class="trash icon"></i> Delete
            </button>
            <!-- 编辑按钮 -->
            <button v-show="canEdit" class="item" @click="editPost(card.postId)">
              <i class="edit icon"></i> Edit
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 卡片图片 -->
    <div class="image" style="display: flex; align-items: center; justify-content: center; background-color: white;">
      <img v-show="card.image !== ''" :src="card.image" style="width: 50%; height: auto; object-fit: fill;">
    </div>

    <!-- 卡片内容 -->
    <div class="content">
      <div>{{ card.content }}</div>
      <br>
      <span class="right floated" @click="clickLike">
        <i class="heart outline like icon"></i>
        {{ card.likeCount }} likes
      </span>
    </div>
    <br>
    <hr>
    <ReplyBox :postId="postId" />
    <br>
    <!-- 评论部分 -->

    <div class="comments">
      <commentComp v-for="(comment, index) in comments" :index="index" :comment="comment"
        :show="canDelete ||userId == comment.userInfoId" @delete-comment="deleteComment">
      </commentComp>

    </div>
    <EditPost :edit="edit" />
  </div>
</template>
  
<script setup>
import commentComp from "@/components/Post/Comment.vue";
import axiosapi from '@/plugins/axios';
import { Modal } from 'bootstrap/dist/js/bootstrap.bundle.min';
import { differenceInDays, differenceInHours, differenceInMinutes, differenceInSeconds } from 'date-fns'; // 导入所需的date-fns函数
import { defineEmits, defineProps, ref, onMounted,computed } from 'vue';
import EditPost from '../Edit/EditPost.vue';
import ReplyBox from './ReplyBox.vue';

// Define component props
const props = defineProps(['card', 'comments', "postId", "show", "userId"]); // Adjust as needed
const emits = defineEmits(['clickLike']);
const edit = ref({ content: '', image: null, postId: null, permission: '1' });
// Function to delete a post
const deletePost = (postId) => {
  axiosapi.delete(`/ajax/post/deletePost/${postId}`)
    .then(response => {
      console.log(`Post ${postId} deleted successfully.`);
      window.location.replace('/home'); // Reload the page to update UI
    })
    .catch(error => {
      console.error(`Error deleting post ${postId}:`, error);
    });
};

// Function to edit a post
const editPost = (postId) => {
  const post = props.card;
  edit.value = { content: post.content, image: post.image, postId: postId };

  const editModalElement = document.getElementById('editModal');
  if (editModalElement) {
    const editModal = Modal.getInstance(editModalElement) || new Modal(editModalElement);
    editModal.show();
  } else {
    console.log('Failed to find the modal element.');
  }
};


//評論方法
function deleteComment(id) {
  if (!confirm('確認删除評論?')) return;
  const sessionId = sessionStorage.getItem("Session-Id");
  axiosapi.delete(`/ajax/comments/delete/${props.postId}/${id}`, {
    headers: {
      'session-id': sessionId
    }
  })
    .then(response => {
      console.log('评论删除成功:', response.data);
      window.location.reload();
    })
    .catch(error => {
      console.error('删除评论失败:', error.response.data);
      alert('删除评论时出错，请稍后再试。');
    });
}

function clickLike() {
  console.log("I like this");
  let element = document.querySelector(".icon.outline.like");
  if (element) {
    props.card.likeCount++;
    element.classList.remove("outline");
    element.classList.add("inline");
  } else {
    let element = document.querySelector(".icon.inline.like");
    props.card.likeCount--;
    element.classList.remove("inline");
    element.classList.add("outline");
  }
}
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
const canDelete = computed(() => {
  return sessionStorage.getItem('user-status') === '2';
});

const canEdit = computed(() => {
  return sessionStorage.getItem('user-status') !== '2';
});
</script>
  
<style scoped>
.nav-link {
  display: inline;
}

.comments {
  border-top: 1px solid #ddd;
  padding: 10px;
}

.content {
  width: 450px;
  /* 设置宽度 */
  height: auto;
  /* 设置高度 */
  font-size: 1.3em;
  /* 放大字体 */
}

.ui.card.large {
  /* 确保卡片也被调整大小 */
  width: 500px;
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
  /* 绝对定位 */
  top: 0;
  /* 与日期对齐 */
  right: 0;
  /* 右侧对齐 */
  border: none; /* 移除邊框 */
  box-shadow: none; /* 移除陰影 */
  background: none; /* 使背景透明 */
  padding: 0;
  font-size: 20px;
}

.right.floated.meta {
  font-size: 1em;
}

</style>
  