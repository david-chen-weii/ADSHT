<template>
  <Personal
    :user="user"
    :senderId="senderId"
    :followersCount="followersCount"
    :followingCount="followingCount"
    :PostCount="PostCount"
    @toggleFollow="toggleFollow"
  />
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import axiosapi from '@/plugins/axios';
import Personal from '@/components/Friend/personal.vue';
import Swal from 'sweetalert2';

const user = ref(null);
const senderId = sessionStorage.getItem('Session-Id');
const followersCount = ref(0);
const followingCount = ref(0);
const  PostCount = ref(0);

const route = useRoute();

const fetchUser = async () => {
  try {
    const userInfoId = route.query.id || localStorage.getItem('userInfoId');
    if (userInfoId) {
      const response = await axiosapi.post('/api/friends/searchById', { id: userInfoId });

      if (response.data.success) {
        const userInfo = response.data.userInfos[0];

        const checkResponse = await axiosapi.post('/api/friends/checkRelationship', {
          user1Id: senderId,
          user2Id: userInfo.userId
        }, {
          headers: {
            'session-id': sessionStorage.getItem('Session-Id')
          }
        });

        user.value = {
          ...userInfo,
          isFollowing: checkResponse.data.status === 2 || checkResponse.data.status === 3
        };

        await fetchFollowersCount(userInfo.userId);
        await fetchFollowingCount(userInfo.userId);
        await fetchPostCount(userInfo.userId);
      } else {
        console.error('Failed to fetch user data');
      }
    } else {
      console.error('No id found in query or localStorage');
    }
  } catch (error) {
    console.error('Search failed:', error.message);
  }
};

const fetchFollowersCount = async (userId) => {
  try {
    const response = await axiosapi.post('/api/friends/followers/count', { userId });
    followersCount.value = response.data;
  } catch (error) {
    console.error('獲取粉絲數量失敗:', error);
  }
};

const fetchFollowingCount = async (userId) => {
  try {
    const response = await axiosapi.post('/api/friends/following/count', { userId });
    followingCount.value = response.data;
  } catch (error) {
    console.error('獲取關注數量失敗:', error);
  }
};

const fetchPostCount = async (userId) => {
  try {
    const response = await axiosapi.post('/api/friends/post/count', { userId });
    PostCount.value = response.data;
  } catch (error) {
    console.error('獲取貼文數量失敗:', error);
  }
};

onMounted(() => {
  fetchUser();
});

watch(() => route.query.id, (newId) => {
  if (newId) {
    fetchUser();
  }
});
// 有問題
const toggleFollow = async ({ user, source }) => {
  if (!senderId) {
    // Swal.fire({
    //   icon: "error",
    //   title: "請先登入",
    //   text: "將跳轉至登入頁面",
    // }).then((result) => {
    //   if (result.isConfirmed) {
    //     route.push('/views/login');
    //   }
    // });
    return '/views/login';
  }
  
  try {
    if (user.isFollowing) {
      await axiosapi.delete('/api/friends/delete', {
        data: {
          senderId,
          receiverId: user.userId
        },
        headers: {
          'session-id': sessionStorage.getItem('Session-Id')
        }
      });
      console.log("有呼叫刪除");
      user.isFollowing = false;
      if (source === 'personal') {
        fetchFollowersCount(user.userId);
      } else if (source === 'userListModal') {
        console.log("component2呼叫刪除");
        followingCount
      }
    } else {
      await axiosapi.post('/api/friends/add', {
        senderId,
        receiverId: user.userId
      }, {
        headers: {
          'session-id': sessionStorage.getItem('Session-Id')
        }
      });
      console.log("有呼叫新增");
      user.isFollowing = true;
      if (source === 'personal') {
        followersCount.value += 1;
      } else if (source === 'userListModal') {
        console.log("component2呼叫新增");
      }
    }
  } catch (error) {
    console.error('操作失败:', error);
  }
};
</script>

<style scoped>
/* 這裡是你的樣式代碼 */
</style>
