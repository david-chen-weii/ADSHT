<template>
    <nav :class="['navbar', 'navbar-expand-lg', 'bg-body-tertiary', 'fixed-top']" :data-bs-theme="darkMode ? 'dark' : null">
        <div class="container-fluid d-flex justify-content-center ">
            <RouterLink class="navbar-brand" to="/">
                <font-awesome-icon :icon="['fab', 'fort-awesome']" class="icon-large" />
            </RouterLink>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown"
                aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-center" id="navbarNavDropdown">
                <ul class="navbar-nav">
                    <li class="nav-item nav-item-spacing">
                        <RouterLink class="nav-link active" to="/home">
                            <font-awesome-icon :icon="['fas', 'house']" class="icon-large" />
                        </RouterLink>
                    </li>
                    <li class="nav-item nav-item-spacing">
                        <RouterLink class="nav-link active" to="/views/search">
                            <font-awesome-icon :icon="['fas', 'magnifying-glass']" class="icon-large" />
                        </RouterLink>
                    </li>
                    <li class="nav-item nav-item-spacing position-relative">
                      <button @click="handleHeartClick" class="nav-link active">
                            <font-awesome-icon :icon="['fas', 'heart']" class="icon-large" />
                            <span v-if="unreadCount > 0" class="badge">{{ unreadCount }}</span>
                        </button>
                    </li>
                    <li class="nav-item nav-item-spacing">
                        <RouterLink class="nav-link active" :to="personalPageLink">
                            <font-awesome-icon :icon="['fas', 'user']" class="icon-large" />
                        </RouterLink>
                    </li>
                    <li class="nav-item nav-item-spacing">
                        <RouterLink class="nav-link active" to="/views/message">
                            <font-awesome-icon :icon="['fas', 'comment']" class="icon-large" />
                        </RouterLink>
                    </li>
                    <li class="nav-item nav-item-spacing">
                        <RouterLink class="nav-link active" :to="{ name: 'david-search-link' }">
                            <font-awesome-icon :icon="['fas', 'cart-shopping']" class="icon-large" />
                        </RouterLink>
                    </li>

                    <li class="nav-item nav-item-spacing">
                        <RouterLink class="nav-link active" to="/views/bomb">
                        <font-awesome-icon icon="fa-solid fa-bomb" class="icon-large" />
                        </RouterLink>
                    </li>
                </ul>
            </div>
            <!-- 登入登出按钮 -->
            <div class="nav justify-content-end">
                <RouterLink v-show="hasProductsInCart()" class="btn btn-secondary" :to="{ name: 'david-cart-link' }">
                    <font-awesome-icon :icon="['fab', 'shopify']" />
                </RouterLink>
                <RouterLink v-show="!isLoggedIn()" class="btn btn-dark" to="/views/login">登入</RouterLink>
                <button @click="logout" v-show="isLoggedIn()" class="btn btn-dark" to="/views/logout">登出</button>
                <!-- 深色模式切换按钮 -->
                <!-- <button @click="toggleDarkMode" class="btn btn-secondary btn-custom-sm">
                    <font-awesome-icon :icon="darkMode ? ['fas', 'sun'] : ['fas', 'moon']" />
                </button> -->
                <RouterLink v-show="isAdmin()" to="/views/management" class="btn btn-dark">後台</RouterLink>
            </div>
        </div>

        <!-- Modal 结构 -->
        <div v-if="showModal" class="modal-overlay" @click="closeModal">
            <div class="modal-content" @click.stop>
            <ActivityCard :notifications="notifications"></ActivityCard>
            <span class="close" @click="closeModal">&times;</span>
            </div>
        </div>


    </nav>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue';
import axiosapi from '@/plugins/axios';
import { useRouter } from 'vue-router';
import notificationStore from '@/store/notificationStore';
import ActivityCard from '@/components/Activity/ActivityCard.vue';

const router = useRouter();
const darkMode = ref(false);
const unreadCount = notificationStore.unreadCount;
const showModal = ref(false);
const notifications = ref([]);
const pollingInterval = ref(null);

// 检查深色模式
function checkDarkMode() {
    if (document.body.classList.contains('dark-mode')) {
        darkMode.value = true;
    }
}
//判斷是否有購物車清單
function hasProductsInCart() {
    let storedProducts = sessionStorage.getItem('products');
    console.log('Stored Products:', storedProducts); // 輸出 storedProducts
    if (storedProducts) {
        let productSession = JSON.parse(storedProducts);
        console.log('Parsed Products:', productSession); // 輸出 productSession
        return Object.keys(productSession).length > 0;
    }
    return false;
}

// 登入
function isLoggedIn() {
    return sessionStorage.getItem('Session-Id') !== null;
}
// 登出
function logout() {
    sessionStorage.removeItem('Session-Id');
    sessionStorage.removeItem('Session-UserInfoId');
    sessionStorage.removeItem('user-status');
    window.location.replace('/views/login');
}

// 切换深色模式
const toggleDarkMode = () => {
    darkMode.value = !darkMode.value;
    if (darkMode.value) {
        document.body.classList.add('dark-mode');
    } else {
        document.body.classList.remove('dark-mode');
    }
};
//個人頁面
const personalPageLink = computed(() => {
    const UserInfoId = sessionStorage.getItem('Session-UserInfoId');
    if (UserInfoId) {
        return { name: 'personal-text-link', query: { id: UserInfoId } };
    } else {
        return '/views/login';
    }
});
//取得未讀通知數量
async function getUnreadNotificationCount() {
  const userId = sessionStorage.getItem('Session-Id');
  if (userId) {
    try {
      const response = await axiosapi.get(`/api/notification/unreadCount`, {
        params: { userId }
      });
      notificationStore.setUnreadCount(response.data);
    } catch (error) {
      console.error('Error fetching unread notification count:', error);
    }
  }
}
//改為已讀取
async function markAllAsRead() {
  const userId = sessionStorage.getItem('Session-Id');
  if (userId) {
    try {
      const response = await axiosapi.put('/api/notification/markAllAsRead', {
        userId: userId
      });
      notificationStore.setUnreadCount(0); // 同步navigation 為0
    } catch (error) {
      console.error('Error marking notifications as read:', error);
    }
  } else {
    console.error('Session-UserInfoId not found in sessionStorage');
  }
}

function closeModal() {
  showModal.value = false;
  markAllAsRead();
}
//通知功能跳轉畫面
function handleHeartClick() {
  if (isLoggedIn()) {
    showModal.value = true;
  } else {
    router.push('/views/login');
  }
}

// 啟動輪詢
function startPolling() {
  console.log("10秒抓取未讀取通知");
  getUnreadNotificationCount(); // 初始化時調用一次
  pollingInterval.value = setInterval(getUnreadNotificationCount, 10000); // 每30秒輪詢一次
}

// 停止輪詢
function stopPolling() {
  if (pollingInterval.value) {
    clearInterval(pollingInterval.value);
    pollingInterval.value = null;
  }
}

// 判断用户是否是管理员（假设状态为2代表管理员）
function isAdmin() {
    const status = sessionStorage.getItem('user-status');
    console.log('User Status:', status); // 输出用户状态
    return status === '2';
}

// 在组件挂载时检查当前的模式并设置相应的状态
onMounted(() => {
  checkDarkMode();
  getUnreadNotificationCount();
  startPolling();
});

onBeforeUnmount(() => {
  stopPolling();
});
</script>

<style>
/* 确保导航栏固定在顶部并且不遮挡内容 */
.navbar {
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 1000;
}

.container-fluid {
  display: flex;
  justify-content: space-between;
}

.navbar-collapse {
  justify-content: center;
}

.icon-large {
  font-size: 20px;
}

.nav-item-spacing {
  margin-right: 20px;
}

.dark-mode {
  background-color: #2b3035;
  color: white;
}

.navbar.dark-mode {
  background-color: #2b3035;
}

.nav-link.dark-mode {
  color: white;
}

.badge {
  background-color: red;
  color: white;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 50%;
  position: absolute;
  top: -5px;
  right: -10px;
}

.modal-overlay {
  z-index: 1001;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 80%;
  max-width: 500px;
}

.close {
    position: absolute;
    color: #daabab;
    top: 10px;
    right: -15px;
    font-size: 40px;
    font-weight: bold;
    cursor: pointer;
}

.close:hover,
.close:focus {
    color: rgb(255, 255, 255);
    text-decoration: none;
    cursor: pointer;
}
</style>