<template>
  <div class="modal-overlay" @click.self="close">
    <div class="modal-content">
      <button class="close-btn" @click="close">X</button>
      <div v-if="type === 'followers'">
        <h3>粉絲</h3>
        <div class="row">
          <div class="username-container" v-for="follower in followers" :key="follower.userId">
            <div class="profile-container">
              <div class="profile-img">
                <img :src="follower.photo || defaultAvatar" alt="Profile Picture">
              </div>
              <div class="profile-description">
                <a href="#" class="user-title" @click="handleUserClick(follower)">{{ follower.userFirstName }} {{ follower.userLastName }}</a>
                <p class="username">{{ follower.email }}</p>
              </div>
            </div>
            <div class="menu-bar">
              <i class="bi bi-three-dots"></i>
              <button v-if="isOwnerPage" class="btn btn-light" @click="removeFollower(follower)">
                移除
              </button>
              <button v-else class="btn btn-light" v-if="follower.sendId !== senderId" @click="handleToggleFollow(follower)">
                {{ follower.isFollowing ? '已追蹤' : '追蹤' }}
              </button>
            </div>
          </div>
        </div>
      </div>

      <div v-else-if="type === 'following'">
        <h3>關注</h3>
        <div class="row">
          <div class="username-container" v-for="following in followings" :key="following.userId">
            <div class="profile-container">
              <div class="profile-img">
                <img :src="following.photo || defaultAvatar" alt="Profile Picture">
              </div>
              <div class="profile-description">
                <a href="#" class="user-title" @click="handleUserClick(following)">{{ following.userFirstName }} {{ following.userLastName }}</a>
                <p class="username">{{ following.email }}</p>
              </div>
            </div>
            <div class="menu-bar">
              <i class="bi bi-three-dots"></i>
              <button v-if="isOwnerPage" class="btn btn-light" @click="unfollowUser(following)">
                取消追蹤
              </button>
              <button v-else class="btn btn-light" v-if="following.receiveId !== senderId" @click="handleToggleFollow(following)">
                {{ following.isFollowing ? '已追蹤' : '追蹤' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { defineProps, defineEmits } from 'vue';
import { useRouter } from 'vue-router';
import axiosapi from '@/plugins/axios';

const props = defineProps({
  type: {
    type: String,
    required: true
  },
  userId: {
    type: String,
    required: true
  }
});

const emits = defineEmits(['close', 'user-clicked', 'toggleFollow']);
const router = useRouter();
const senderId = sessionStorage.getItem('Session-Id');
const isOwnerPage = senderId === props.userId;
const defaultAvatar = 'https://img.freepik.com/premium-vector/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-vector-illustration_561158-3396.jpg?w=360';

const close = () => {
  emits('close');
};

const followers = ref([]);
const followings = ref([]);

const fetchFollowers = async () => {
  try {
    const response = await axiosapi.post('/api/friends/followers', { receiveId: props.userId });
    followers.value = response.data;
  } catch (error) {
    console.error('取得粉絲清單失敗:', error);
  }
};

const fetchFollowings = async () => {
  try {
    const response = await axiosapi.post('/api/friends/following', { sendId: props.userId });
    followings.value = response.data;
  } catch (error) {
    console.error('取得關注列表失敗:', error);
  }
};

const handleUserClick = (user) => {
  emits('close');
  emits('user-clicked', user.id);
};

const handleToggleFollow = (user) => {
  if (!senderId) {
    router.push({ name: 'login-text-link' });
  } else {
    toggleFollow(user);
  }
};

const toggleFollow = (user) => {
  emits('toggleFollow', user);
};

const removeFollower = async (follower) => {
  try {
    await axiosapi.delete('/api/friends/delete', {
      data: { senderId: follower.userId, receiverId: senderId },
      headers: { 'session-id': sessionStorage.getItem('Session-Id') }
    });
    fetchFollowers();
  } catch (error) {
    console.error('刪除粉絲失敗:', error);
  }
};

const unfollowUser = async (following) => {
  try {
    await axiosapi.delete('/api/friends/delete', {
      data: { senderId: senderId, receiverId: following.userId },
      headers: { 'session-id': sessionStorage.getItem('Session-Id') }
    });
    fetchFollowings();
  } catch (error) {
    console.error('取消追蹤失敗:', error);
  }
};

watch(() => props.type, (newType) => {
  if (newType === 'followers') {
    fetchFollowers();
  } else if (newType === 'following') {
    fetchFollowings();
  }
});

onMounted(() => {
  if (props.type === 'followers') {
    fetchFollowers();
  } else if (props.type === 'following') {
    fetchFollowings();
  }
});
</script>

<style scoped>
@import '@/assets/fansAndFollow.css';

.user-title {
  cursor: pointer;
}

h3 {
  text-align: center;
  border-bottom: 1px solid rgb(34, 10, 1);
  line-height: 35px;
}
</style>
