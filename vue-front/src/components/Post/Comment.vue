<template>
  <div class="comment">
    <div class="header">
      <a class="avatar">
        <RouterLink class="nav-link" :to="'/views/Friend?id=' + comment.userInfoId">
          <img :src="comment.posterPhoto" alt="Avatar">
        </RouterLink>
        {{ comment.avatar }}
      </a>
      <div class="info">
        <div class="metadata">
          <RouterLink class="nav-link" :to="'/views/Friend?id=' + comment.userInfoId">
            <a class="author">{{ comment.posterName }}</a>
          </RouterLink>
          <div class="date">{{ formatTime(comment.addTime) }}</div>
          <div class="ui compact menu">
            <div v-show="show" class="ui simple dropdown item">
              <i class="ellipsis vertical icon"></i>
              <div class="menu">
                <button class="item" @click="emits('deleteComment', comment.commentId)">
                  <i class="trash icon"></i> 删除
                </button>
                <!--
                <button class="item" @click="showModal">
                  <i class="edit icon"></i> 编辑
                </button>
                -->
              </div>
            </div>
          </div>
        </div>
        <div class="text">
          {{ comment.content || 'No content available' }}
        </div>
      </div>
    </div>
    <br>
    <div class="divider"></div>
    <!-- 引入新的模态框组件 -->
    <EditModal ref="editCommentModal" />
  </div>
</template>

<script setup>
import axiosapi from '@/plugins/axios';
import { differenceInDays, differenceInHours, differenceInMinutes, differenceInSeconds } from 'date-fns';
import { defineProps, ref, onMounted } from 'vue';
import EditModal from './CommentEditModal.vue';


axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
const props = defineProps(["index", "comment", "show"])
const emits = defineEmits(["deleteComment"])
const postId = ref('');
const params = new URLSearchParams(window.location.search);
postId.value = params.get('id');


function formatTime(time) {
  if (!time) return '时间未知';

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
    return `${hoursDifference}小时前`;
  } else {
    return `${daysDifference}天前`;
  }
}

// 创建一个 ref 来引用 EditComment 组件实例
const editCommentModal = ref(null);

function showModal() {
  if (editCommentModal.value) {
    editCommentModal.value.show();
  }
}
</script>

<style scoped>
.comment {
  margin-top: 5px;
  width: 100%;
  /* 确保评论容器占满宽度 */
  padding: 0;
  /* 去除内边距以确保宽度 */
}

.header {
  display: flex;
  align-items: flex-start;
  /* 对齐到顶部 */
  width: 100%;
  /* 确保 header 占满宽度 */
}

.avatar {
  margin-right: 30px;
  /* 头像和信息之间的间距 */
  width: 30px;
  /* 设置图像宽度 */
  height: 30px;
  /* 设置图像高度 */
  border-radius: 50%;
  /* 使图像成为圆形 */
}

.info {
  display: flex;
  flex-direction: column;
  /* 垂直排列元数据和内容 */
  width: 100%;
  /* 确保 info 占满宽度 */
}

.metadata {
  display: flex;
  flex-direction: row;
  /* 横向排列日期、作者和下拉菜单 */
  margin-bottom: 2px;
  /* 减少元数据和内容之间的空间 */
  align-items: center;
}

.date {
  position: relative;
  margin-left: 300px;
  font-size: 0.8em;
  color: #888;
}

.author {
  font-weight: bold;
  margin-bottom: 2px;
  /* 减少作者和文本之间的空间 */
}

.text {
  font-size: 0.9em;
  text-align: left;
  /* 文本左对齐 */
  margin-bottom: 2px;
  /* 减少文本下方的空间 */
}

.divider {
  border-top: 1px solid #ccc;
  /* 添加顶部边框作为分隔符 */
  width: calc(100% + 20px);
  /* 扩展宽度以覆盖外边距/内边距 */
  margin-left: -10px;
  /* 调整左外边距以对齐内容 */
}

.reply-box {
  margin-top: 5px;
}

.ui.compact.menu {
  border: none; /* 移除邊框 */
  box-shadow: none; /* 移除陰影 */
  background: none; /* 使背景透明 */
  margin-left: auto;
}

/* 模态框样式 */
.modal.fade .modal-dialog {
  transition: transform .3s ease-out;
}

.modal.fade.show .modal-dialog {
  transform: translateY(0);
}
</style>
