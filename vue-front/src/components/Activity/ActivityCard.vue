<template>
  <div class="container">
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
    </div>
    <div v-else class="notification-container">
      <!-- Notification list header -->
      <div class="notification-list-header">
        <div class="arrow-icon">
          <i class="fa fa-angle-left icon"></i>
        </div>
        <div class="notification-title">通知</div>
        <div class="search-form">
          <!-- 垃圾桶图标 -->
          <font-awesome-icon icon="fa-solid fa-trash" class="fa fa-search icon" @click="confirmDeleteReadNotifications"/>
        </div>
      </div>
      <!-- Notification Options -->
      <div class="notification-option">
        <div>{{ unreadCount }} 則未讀通知</div>
        <div>
          <i class="fa fa-trash icon remove-icon"></i>
          <i class="fa fa-check icon check-icon"></i>
        </div>
      </div>
      <!-- Notification Options -->

      <!-- Notification list container/box -->
      <div class="notification-list-box">
        <ul class="list-group">
          <li 
            class="list-group-item table-content" 
            v-for="notification in notifications" 
            :key="notification.id"
            :class="{'unread-notification': !notification.isRead
            , 'read-notification': notification.isRead}"
          >
            <div class="media-left">
              <img class="media-object img-circle" :src=" notification.photo || defaultAvatar" alt="Profile Picture" />
            </div>
            <div class="media-body">
              <h4 class="media-heading" @click="goToProfile(notification.userInfoId)"> {{ notification.userFirstName }} {{ notification.userLastName }}</h4>
              <span class="notification-location">{{ notification.content }}</span>
            </div>
            <div class="notification-time">{{ formatRelativeTime(notification.createdAt) }}</div>
          </li>
        </ul>
      </div>
      <!-- Notification list container/box -->
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, onMounted } from 'vue';
import axiosapi from "@/plugins/axios";
import { useRouter } from 'vue-router';
import Swal from 'sweetalert2';

const props = defineProps({
  notifications: {
    type: Array,
    required: true
  }
});

const searchText = ref('');
const defaultAvatar = 'https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQFI_0LSj3Patz1NlO5xtPd5lep2EfY_ynZLnFJHG-iVxpq_jSU';
const unreadCount = ref(0);
const loading = ref(true); // 用於控制是否顯示加載畫面
const router = useRouter();

function formatRelativeTime(dateString) {
  const date = new Date(dateString);
  const now = new Date();
  const diff = Math.abs(now - date);
  const diffDays = Math.floor(diff / (1000 * 60 * 60 * 24));

  if (diffDays < 1) {
    const diffHours = Math.floor(diff / (1000 * 60 * 60));
    if (diffHours < 1) {
      const diffMinutes = Math.floor(diff / (1000 * 60));
      return `${diffMinutes} 分鐘前`;
    }
    return `${diffHours} 小時前`;
  } else if (diffDays < 7) {
    return `${diffDays} 天前`;
  } else {
    const diffWeeks = Math.floor(diffDays / 7);
    return `${diffWeeks} 週前`;
  }
}

// 在組件加載時獲取未讀通知數量
onMounted(async () => {
  fetchUnreadCount();
  fetchNotifications();
});

const fetchUnreadCount = async () => {
  const sessionId = sessionStorage.getItem('Session-Id');
  const userId = sessionId;
  try {
    const response = await axiosapi.get(`/api/notification/unreadCount?userId=${userId}`);
    unreadCount.value = response.data;
    loading.value = false; // 數據加載完成後設置 loading 為 false
  } catch (error) {
    console.error('Error fetching unread notifications count:', error);
    loading.value = false; // 即使出錯，也要隱藏加載畫面
  }
};

function goToProfile(userInfoId) {
  router.push({ name: 'user-profile', query: { id: userInfoId } });
}

// 確認刪除已讀通知
function confirmDeleteReadNotifications() {
  Swal.fire({
    title: "確認刪除已讀通知?",
    text: "注意:此動作無法還原",
    icon: "warning",
    showCancelButton: true,
    confirmButtonColor: "#d33",
    cancelButtonColor: "#3085d6",
    confirmButtonText: "刪除",
    cancelButtonText: "取消"
  }).then((result) => {
    if (result.isConfirmed) {
      deleteReadNotifications();
    }
  });
}

// 刪除已讀通知
async function deleteReadNotifications() {
  const sessionId = sessionStorage.getItem('Session-Id');
  const userId = sessionId;
  try {
    const response = await axiosapi.delete('/api/notification/deleteRead', {
      data: { userId }
    });
    if (response.data.success) {
      Swal.fire({
        title: "Deleted!",
        text: "All read notifications have been deleted.",
        icon: "success"
      });
      // 刪除成功後重新獲取通知
      fetchNotifications();
    }
  } catch (error) {
    console.error('Error deleting read notifications:', error);
  }
};

const fetchNotifications = async () => {
  const userInfoId = sessionStorage.getItem('Session-UserInfoId');
  if (userInfoId) {
    try {
      const response = await axiosapi.post('/api/notification/byUserInfoId', {
        userInfoId: parseInt(userInfoId, 10),
      });

      if (response.data && Array.isArray(response.data)) {
        console.log("結果", response.data);
        props.notifications.splice(0, props.notifications.length, ...response.data);
      } else if (response.data === "No notifications found for the user") {
        props.notifications.splice(0, props.notifications.length); // 清空通知列表
      }
    } catch (error) {
      console.error('Error fetching notifications:', error);
    }
  } else {
    console.error('Session-UserInfoId not found in sessionStorage');
  }
};
</script>

<style scoped>
@import '@/assets/notification.css';

.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* 讓 loading 畫面佔滿整個視窗 */
}

.loading-spinner {
  border: 16px solid #f3f3f3; 
  border-top: 16px solid #3498db;
  border-radius: 50%;
  width: 120px;
  height: 120px;
  animation: spin 2s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.unread-notification {
  background-color: rgb(126, 126, 114);
}

.read-notification {
  background-color: rgb(236, 235, 232);
}
</style>
