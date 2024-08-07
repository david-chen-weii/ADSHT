<template>
  <!-- <div class="bg">
      <h1>Shop</h1>
  </div> -->
  <div class="nft">
    <div class='main'>
      <RouterLink :to="`shop?id=${shop.shopId}`">
        <img class='tokenImage' :src="shop.image || defaultImage" alt="NFT" loading="lazy" />
        <h2>{{ shop.shopName }}</h2>
        <p class='description'>描述:{{ shop.description }}</p>
        <div class='tokenInfo'>
          <div class="price">

          </div>
          <div class="duration">

          </div>
        </div>
        <hr />
        <div class='creator'>
          <div class='wrapper'>
            <img :src="userInfo.photo || defaultAvatar" alt="Creator" loading="lazy" />
          </div>
          <p><ins>Creation of</ins> {{ userInfo.userFirstName }} {{ userInfo.userLastName }}</p>
        </div>
      </RouterLink>
    </div>
  </div>
</template>

<script setup>
import { defineProps, ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axiosapi from "@/plugins/axios";

const props = defineProps(["shop"]);
const router = useRouter();

const userInfo = ref({ name: '', image: '' });

const defaultAvatar = 'https://img.freepik.com/premium-vector/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-vector-illustration_561158-3396.jpg?w=360';
const defaultImage = 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR9xah24k1IPo5PF3vksg-gN0503a4yoyuliQ&s';

const fetchUserInfo = async (userId) => {
  try {
    const sessionId = sessionStorage.getItem('Session-Id');
    const response = await axiosapi.post(
      '/api/userInfo/findUserInfoByUserId',
      { userId: userId },
      {
        headers: {
          'Content-Type': 'application/json',
          'session-id': sessionId,
        }
      }
    );

    return response.data;
  } catch (error) {
    console.error('獲取用戶信息時發生錯誤:', error);
    return { success: false };
  }
};

onMounted(async () => {
  const userId = props.shop.owner;
  const data = await fetchUserInfo(userId);
  if (data.success) {
    userInfo.value = data.userInfo;
  }
});
</script>

<style scoped>
@import '@/assets/shopCard.css';

.shopCard {
  position: relative;
  width: 300px;
  height: 300px;
  text-align: center;
  background-color: azure;
  overflow: hidden;
  cursor: pointer;
}

img {
  max-width: 300px;
  max-height: 280px;
  object-fit: cover;
}

.shopDescribe {
  display: none;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  align-items: center;
  justify-content: center;
  padding: 10px;
  box-sizing: border-box;
}

.shopCard:hover .shopDescribe {
  display: flex;
}
</style>
