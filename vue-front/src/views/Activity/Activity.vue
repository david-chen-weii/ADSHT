<template>
    <ActivityCard :notifications="notifications"></ActivityCard>
  </template>
  
  <script setup>
  import { ref, onMounted, onBeforeUnmount } from 'vue';
  import ActivityCard from '@/components/Activity/ActivityCard.vue';
  import axiosapi from "@/plugins/axios";
  import notificationStore from '@/store/notificationStore';
  
  const notifications = ref([]);
  
  const fetchNotifications = async () => {
    const userInfoId = sessionStorage.getItem('Session-UserInfoId');
    if (userInfoId) {
      try {
        const response = await axiosapi.post('/api/notification/byUserInfoId', {
          userInfoId: parseInt(userInfoId, 10),
        });
  
        if (response.data && Array.isArray(response.data)) {
          notifications.value = response.data;
        } else if (response.data === "No notifications found for the user") {
          notifications.value = []; // 清空通知列表
        }
      } catch (error) {
        console.error('Error fetching notifications:', error);
      }
    } else {
      console.error('Session-UserInfoId not found in sessionStorage');
    }
  };
  
  const markAllAsRead = async () => {
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
  };
  
  onMounted(() => {
    fetchNotifications();
  });
  
  onBeforeUnmount(() => {
    markAllAsRead();
  });
  </script>
  
  <style>
  /* 样式 */
  </style>
  