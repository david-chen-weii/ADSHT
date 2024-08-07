<template>
  <div class="centered-container">
    <SearchComment 
      :searchQuery="searchQuery"
      :searchResults="searchResults"
      :searchPostRes="searchPostRes"
      @update:selectedTab="selectedTab = $event"
      @update:searchQuery="searchQuery = $event"
      @searchUser="searchUser" 
      @toggleFollow="toggleFollow"
      @posts="posts"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axiosapi from "@/plugins/axios";
import SearchComment from "@/components/Search/SearchComment.vue";

const selectedTab = ref('posts');
const searchQuery = ref('');
const searchResults = ref([]);
const searchPostRes = ref([]);

const searchUser = async () => {
  if (searchQuery.value.trim() === "") {
    searchResults.value = [];
    return;
  }

  try {
    const response = await axiosapi.get('/api/friends/searchUser', {
      params: { name: searchQuery.value },
      headers: {
        'session-id': sessionStorage.getItem('Session-Id')
      }
    });
    if (response.data.success) {
      const senderId = sessionStorage.getItem('Session-Id');

      const userInfos = await Promise.all(response.data.userInfos.map(async (user) => {
        const checkResponse = await axiosapi.post('/api/friends/checkRelationship', {
          user1Id: senderId,
          user2Id: user.userId
        }, {
          headers: {
            'session-id': sessionStorage.getItem('Session-Id')
          }
        });

        return {
          ...user,
          isFollowing: checkResponse.data.status === 2 || checkResponse.data.status === 3
        };
      }));

      searchResults.value = userInfos;
    } else {
      searchResults.value = [];
      console.error('找不到符合的用戶');
    }
  } catch (error) {
    searchResults.value = [];
    console.error('查詢失敗:', error);
  }
};

const toggleFollow = async (user) => {
  const senderId = sessionStorage.getItem('Session-Id');
  try {
    if (user.isFollowing) {
      await axiosapi.delete('/api/friends/delete', {
        data: {
          senderId: senderId,
          receiverId: user.userId
        },
        headers: {
          'session-id': sessionStorage.getItem('Session-Id')
        }
      });
      user.isFollowing = false;
    } else {
      await axiosapi.post('/api/friends/add', {
        senderId: senderId,
        receiverId: user.userId
      }, {
        headers: {
          'session-id': sessionStorage.getItem('Session-Id')
        }
      });
      user.isFollowing = true;
    }
  } catch (error) {
    console.error('操作失敗:', error);
  }
};

function posts() {
  if (searchQuery.value.trim() === "") {
    searchPostRes.value = [];
    return;
  }
  let sentence = { "sentence": searchQuery.value }
  axiosapi.post("ajax/post/searchPost", sentence).then((response) => {
    searchPostRes.value = response.data.posts;
  })
}
</script>

<style scoped>
.centered-container {
  display: flex;
  justify-content: center;
  /* 讓容器鋪滿整個視窗高度 */
}
</style>
