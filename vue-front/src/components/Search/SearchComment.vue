<template>
  <div class="ui container">
    <!-- 搜索框 -->
    <div class="ui search custom-search">
      <input id="inputBar" class="prompt" type="text" placeholder="搜尋" :value="searchQuery" @input="updateSearchQuery">
      <div class="results"></div>
    </div>
    <!-- Semantic UI 頂部附加的標籤選項卡 -->
    <div class="ui top attached tabular menu">
      <a class="item" :class="{ active: selectedTab === 'posts' }" @click="selectTab('posts')">貼文</a>
      <a class="item" :class="{ active: selectedTab === 'friends' }" @click="selectTab('friends')">朋友</a>
      <!-- <a class="item" :class="{ active: selectedTab === 'messages' }" @click="selectTab('messages')">消息</a>
      <a class="item" :class="{ active: selectedTab === 'products' }" @click="selectTab('products')">產品</a> -->
    </div>

    <!-- Semantic UI 底部附加的標籤區段 -->
     <!-- 貼文內容 -->
    <div class="ui bottom attached tab segment" :class="{ active: selectedTab === 'posts' }" data-tab="posts">
      <h2>貼文內容</h2>
      <PostRes v-for="(post, index) in searchPostRes" :key="index" :post="post"></PostRes>
    </div>
    <!-- 貼文內容 -->

    <!-- 朋友內容 -->
    <div class="ui bottom attached tab segment" :class="{ active: selectedTab === 'friends' }" data-tab="friends">
      <h2>朋友內容</h2>
      <div v-for="friend in filteredFriends" :key="friend.id" class="no-search">{{ friend.name }}</div>
            <div v-if="searchResults.length > 0">
              <div v-for="user in searchResults" :key="user.userId" class="body">
                <div class="profile">
                <div class="forColor">
                <div class="photo"><img :src="user.photo || defaultAvatar" ></div>
                  <div class="content"  @click="goToProfile(user)">
                    <div class="text">
                      <h3>{{ user.userFirstName }} {{ user.userLastName }}</h3>
                    </div>
                  </div>
                </div>
              </div>
              </div>
          </div>
        
      <!-- 推薦朋友內容 -->
      <div>
        <h2>推薦好友</h2>
            <div v-for="user in recommendedFriends" :key="user.userId" class="body">
              <div class="profile">
                <div class="forColor">
                  <div class="photo"><img :src="user.photo || defaultAvatar"></div>
                    <div class="content"  @click="goToProfile(user)">
                      <div class="text">
                        <h3>{{ user.userFirstName }} {{ user.userLastName }}</h3>
                      </div>
                    </div>
                </div>
              </div>
            </div>
      </div>
    
    </div>
    <!-- 朋友內容 -->
     
    <!-- 消息內容 -->
    <!-- <div class="ui bottom attached tab segment" :class="{ active: selectedTab === 'messages' }" data-tab="messages">
      
      <h2>消息內容</h2>
      <p v-for="message in filteredMessages" :key="message.id">{{ message.text }}</p>
    </div> -->
    <!-- 消息內容 -->
    
    <!-- 產品內容 -->
    <!-- <div class="ui bottom attached tab segment" :class="{ active: selectedTab === 'products' }" data-tab="products">
      
      <h2>產品內容</h2>
      <p v-for="product in filteredProducts" :key="product.id">{{ product.text }}</p>
    </div> -->
    <!-- 產品內容 -->

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axiosapi from "@/plugins/axios";
import PostRes from '@/components/david-comp/search/searchComp.vue';

const props = defineProps({
  searchQuery: String,
  searchResults: Array,
  searchPostRes: Array,
});
const emits = defineEmits(['update:selectedTab', 'update:searchQuery', 'searchUser', 'toggleFollow', "posts"]);

const defaultAvatar = 'https://img.freepik.com/premium-vector/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-vector-illustration_561158-3396.jpg?w=360';

const selectedTab = ref('posts');
const recommendedFriends = ref([]);

const posts = ref(props.searchPostRes || []);
const friends = ref([
  { id: 1, name: '尚無搜尋結果' },
  { id: 2, name: '' },
]);
const messages = ref([
  { id: 1, text: '消息 1' },
  { id: 2, text: '消息 2' },
]);
const products = ref([
  { id: 1, text: '产品 1' },
  { id: 2, text: '产品 2' },
]);

const filteredPosts = computed(() =>
  posts.value.filter(post => post.title.toLowerCase().includes(props.searchQuery.toLowerCase()))
);
const filteredFriends = computed(() =>
  friends.value.filter(friend => friend.name.toLowerCase().includes(props.searchQuery.toLowerCase()))
);
const filteredMessages = computed(() =>
  messages.value.filter(message => message.text.toLowerCase().includes(props.searchQuery.toLowerCase()))
);
const filteredProducts = computed(() =>
  products.value.filter(product => product.text.toLowerCase().includes(props.searchQuery.toLowerCase()))
);

const router = useRouter();

const selectTab = (tab) => {
  selectedTab.value = tab;
  emits('update:selectedTab', tab);
};

const updateSearchQuery = (event) => {
  console.log("呼叫updateSearchQuery");
  emits('update:searchQuery', event.target.value);
  emits('searchUser'); // 發送 searchUser 事件
  emits('posts'); // 發送 posts 事件
};

const goToProfile = (user) => {
  localStorage.setItem('userInfoId', user.id);
  router.push({ name: 'user-profile' });
};

const fetchRecommendedFriends = async () => {
  try {
    const userId = sessionStorage.getItem('Session-Id');
    const response = await axiosapi.post('/api/friends/recommendations', { userId });
    recommendedFriends.value = response.data;
  } catch (error) {
    console.error('获取推荐朋友失败:', error);
  }
};

onMounted(() => {
  fetchRecommendedFriends();
});

</script>

<style scoped>
@import '@/assets/search.css';
/* 菜單版型 */
.ui.tabular.menu {
  margin-bottom: 10px;
}

.ui.bottom.attached.segment {
  padding: 20px;
}

.custom-search input.prompt {
  width: 700px;
}

.no-search{
  font-size: 16px;
  margin-top: 20px;
  margin-bottom: 20px;
}
</style>
