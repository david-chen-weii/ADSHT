<template>
    <div class="modal-overlay" @click.self="close">
      <div class="modal-content">
        <button class="close-btn" @click="close">X</button>
  
        <div v-if="type === 'followers'">
          <h3>圖片區</h3>
          <div class="username-container" v-for="follower in followers" :key="follower.userId">
            <div class="profile-container">
              <div class="profile-img">
                <img src="https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQFI_0LSj3Patz1NlO5xtPd5lep2EfY_ynZLnFJHG-iVxpq_jSU" alt="Profile Picture">
              </div>
              <div class="profile-description">
                <a href="#" class="user-title" @click="handleUserClick(follower)">{{ follower.userFirstName }} {{ follower.userLastName }}</a>
                <p class="username">@test</p>
              </div>
            </div>
            <div class="menu-bar">
              <i class="bi bi-three-dots"></i>
              <button type="button" class="btn btn-light">
                {{ follower.isFollowing ? 'Unfollow' : 'Follow' }}
              </button>
            </div>
          </div>
        </div>
  
        <div v-else-if="type === 'following'">
          <h3>关注</h3>
          <div class="username-container" v-for="following in followings" :key="following.userId">
            <div class="profile-container">
              <div class="profile-img">
                <img src="https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQFI_0LSj3Patz1NlO5xtPd5lep2EfY_ynZLnFJHG-iVxpq_jSU" alt="Profile Picture">
              </div>
              <div class="profile-description">
                <a href="#" class="user-title" @click="handleUserClick(following)">{{ following.userFirstName }} {{ following.userLastName }}</a>
                <p class="username">@test</p>
              </div>
            </div>
            <div class="menu-bar">
              <i class="bi bi-three-dots"></i>
              <button type="button" class="btn btn-light">
                {{ following.isFollowing ? 'Unfollow' : 'Follow' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, watch, onMounted } from 'vue';
  import { defineProps, defineEmits } from 'vue';
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
  
  const emits = defineEmits(['close', 'user-clicked']);
  
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
      console.error('获取粉丝列表失败:', error);
    }
  };
  
  const fetchFollowings = async () => {
    try {
      const response = await axiosapi.post('/api/friends/following', { sendId: props.userId });
      followings.value = response.data;
    } catch (error) {
      console.error('获取关注列表失败:', error);
    }
  };
  
  const handleUserClick = (user) => {
  emits('user-clicked', user.email); // 传递用户 email
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