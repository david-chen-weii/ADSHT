<template>
  <body>
    <div v-if="user" class="plx-card gold">
      <div class="pxc-bg"
        style="background-image:url('https://png.pngtree.com/background/20230410/original/pngtree-powder-smoke-colorful-background-picture-image_2379658.jpg')">
      </div>
<<<<<<< HEAD

      <font-awesome-icon icon="fa-solid fa-chess-king" class="king-icon" @click="showSubsEdit()" v-if="user.userId == senderId"/>
=======
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
      <font-awesome-icon icon="fa-solid fa-gear" class="gear-icon" v-if="user.userId == senderId"
        @click="toggleSettingsModal" />
      <div class="pxc-avatar">
        <div class="image">
          <div class="circle-1"></div>
          <div class="circle-2"></div>
          <!-- 個人圖片 -->
          <div class="avatar-upload">
            <div class="avatar-edit" v-if="user.userId == senderId">
              <input type="file" id="imageUpload" accept=".png, .jpg, .jpeg" @change="previewFile" />
              <label for="imageUpload">
                <font-awesome-icon icon="fa-solid fa-pencil"></font-awesome-icon>
              </label>
            </div>
            <img :src="user.photo || defaultAvatar" width="70" height="70" :alt="noImg">
          </div>
        </div>
      </div>
      <div class="pxc-stopper"></div>
      <div class="pxc-subcard">
        <div class="content">
          <div class="pxc-title">{{ user.userFirstName }} {{ user.userLastName }}</div>
          <div class="pxc-sub"></div>
          <div class="pxc-feats">
            <!-- 測試區 -->
            <span>{{ user.selfContent }}</span>
          </div>
          <div class="pxc-tags">
            <button v-if="user.userId !== senderId" @click="handleFollowClick">
              {{ user.isFollowing ? '已追蹤' : '追蹤' }}
            </button>
<<<<<<< HEAD
            <button v-if="user.userId !== senderId" @click="goToOtherSubs(user.userId,user.userLastName)">訂閱</button>
=======
            <button v-if="user.userId !== senderId">訂閱</button>
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
            <!-- <button v-if="user.userId !== senderId">共同</button> -->
          </div>
          <div class="bottom-row">
            <div class="pxc-info">
              <div class="flags">
                <!-- <span>圖</span>
                <span>圖</span>
                <span>圖</span>
                <span>圖</span> -->
              </div>
            </div>
          </div>
        </div>
        <div class="stats">
          <div class="box no-click">
            <span class="value">{{ PostCount }}</span>
            <span class="parameter">貼文</span>
          </div>
          <div class="box" @click="openFollowersModal">
            <span class="value">{{ followersCount }}</span>
            <span class="parameter">粉絲</span>
          </div>
          <div class="box" @click="openFollowingModal">
            <span class="value">{{ followingCount }}</span>
            <span class="parameter">關注</span>
          </div>
        </div>
      </div>
    </div>
    <div v-else></div>
    <!-- Followers Modal -->
    <UserListModal v-if="showFollowersModal" @close="closeFollowersModal" :type="'followers'" :userId="user.userId"
        @user-clicked="handleUserClicked" @toggleFollow="handleToggleFollowFromModal"></UserListModal>

    <!-- Following Modal -->
    <UserListModal v-if="showFollowingModal" @close="closeFollowingModal" :type="'following'" :userId="user.userId"
    @user-clicked="handleUserClicked" @toggleFollow="handleToggleFollowFromModal"></UserListModal>

    <!-- 分隔區1 -->
    <div class="lineheader">
        <div class="linecontainer">
          <div class="line">
            <a href="#" @click.prevent="showPosts">贴文</a>
            <a href="#" @click.prevent="showShops">商店</a>
          </div>
        </div>
      </div>



    <!-- 分隔區2 -->
    <div v-if="currentView === 'posts'">
    <div>
      <!--image row start-->
      <div class="row">
        <!--貼文 start-->
        <div v-for="post in posts" :key="post.postId" class="imageForPost">
          <!-- 點img會跳至對應貼文 -->
          <img :src="post.image || defaultImage" alt="" loading="lazy">
          <div class="details" @click="openImageModal(post)">
            <h2><span></span></h2>
            <p><font-awesome-icon icon="fa-regular fa-eye" /></p>
            <div class="more">
              <a class="read-more">{{ formatDate(post.addTime) }}</a>
              <div class="icon-links">
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
        <!--貼文 card end-->

<<<<<<< HEAD
  <div v-if="currentView === 'subsEdit'">
    <div>
      <!--image row start-->
      <div class="row">
        <!--貼文 start-->
        <SubListCompo1></SubListCompo1>
      </div>
    </div>
  </div>

=======
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
     <!-- shop區域 -->
     <div v-if="currentView === 'shops'">
      <div class="row">
        <div v-for="shop in shops" :key="shop.shopId" class="shop-card">
          <div v-for="product in shop.products" :key="product.productId" class="product-card">
            <img :src="product.productImages[0]?.image || defaultImage" alt="Product Image" loading="lazy"/>
            <div class="product-details">
              <h4>{{ product.productName }}</h4>
              <p>商品描述:{{ product.productDescribe }}</p>
              <div class="product-buy">
                <div><font-awesome-icon icon="fa-solid fa-dollar-sign" /> {{ product.productPrice }}</div>
                <button class="product-cart" @click="addToCart(product.productId, product.productPrice)"><font-awesome-icon icon="fa-solid fa-bag-shopping" /></button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- shop區域 -->

      
      
      <!--image row end-->
  
    <!--image card layout end-->

    <!-- 修改頁面彈出視窗 -->
    <div v-if="showSettingsModal" class="modal">
      <div class="modal-content">
        <span class="close" @click="toggleSettingsModal">&times;</span>
        <h2 class="textH1">編輯個人檔案</h2>
        <div class="profile-header">
          <img class="profile-image" :src="user.photo || defaultAvatar" :alt="noImg">
          <div class="profile-info">
            <h2>{{ user.userFirstName }} {{ user.userLastName }}</h2>
            <p>Vinyl Records</p>
          </div>
        </div>
        <form>
          <div class="form-group">
            <label for="firstName">姓氏</label>
            <input type="text" id="firstName" name="firstName" v-model="user.userFirstName" placeholder="姓氏">
          </div>
          <div class="form-group">
            <label for="lastName">名字</label>
            <input type="text" id="lastName" name="lastName" v-model="user.userLastName" placeholder="名字">
          </div>
          <div class="form-group">
            <label for="birth">生日</label>
            <input type="date" id="birth" name="birth" v-model="user.birth">
          </div>

          <div class="form-group">
            <label for="phone">聯絡資訊</label>
            <input type="text" id="phone" name="phone" v-model="user.phone" placeholder="phone" maxlength="10"
              minlength="8">
          </div>
          <div class="form-group">
            <label for="selfContent">個人簡介</label>
            <textarea id="selfContent" name="selfContent" maxlength="150" v-model="user.selfContent"
              placeholder="個人簡介"></textarea>
            <p class="char-count">{{ user.selfContent ? user.selfContent.length : 0 }} / 150</p>
          </div>
          <div class="form-group">
            <label for="gender">性別</label>
            <select id="gender" name="gender" v-model="user.gender">
              <option value="1">男性</option>
              <option value="2">女性</option>
              <option value="3">其他</option>
            </select>
            <p class="hint">這不會顯示在你的公開個人檔案中。</p>
          </div>
          <button type="button" class="save-button" @click="saveChanges">儲存變更</button>
        </form>
      </div>
    </div>
    <!-- 修改頁面彈出視窗 -->

    <!-- 個人貼文彈出視窗 -->
    <div v-if="showImageModal" class="image-modal">
      <div class="image-modal-content">
        <span class="image-modal-close" @click="closeImageModal">&times;</span>
        <div class="personalPost-container">
          <div class="image-section">
            <img :src="currentPost?.image || defaultImage" alt="Profile Picture">
          </div>
          <div class="info-section">
            <div class="header">
              <img :src="user.photo || defaultAvatar" alt="Profile Picture">
              <div class="username">{{ user.userFirstName }} {{ user.userLastName }}</div>
            </div>
            <div class="comments">
              <!-- post內容 -->
              <div class="personalLink">
                <img :src="user.photo || defaultAvatar" alt="Profile Picture">
                <div class="description">
                  <div class="user-info">
                    <div class="user-title">{{ user.userFirstName }} {{ user.userLastName }}</div>
                    <p class="user-sub" :title="formatDate(currentPost?.addTime)">{{
                      formatRelativeTime(currentPost?.addTime) }}</p>
                  </div>
                  <div class="greeting">{{ currentPost?.content }}</div>
                </div>
              </div>

              <!-- postCommet內容 -->
              <div v-for="comment in postComments" :key="comment.id" class="personalLink">
                <img :src="comment.userPhoto || defaultAvatar" alt="Profile Picture">
                <div class="description">
                  <div class="user-info">
                    <div class="user-title">{{ comment.userFirstName }} {{ comment.userLastName }}</div>
                    <p class="user-sub" :title="formatDate(comment.addTime)">{{ comment.formattedTime }}</p>

                  </div>
                  <div class="greeting">{{ comment.content }}</div>
                </div>
              </div>
              <!-- postCommet內容結尾 -->
            </div>

            <div class="add-comment">
              <input type="text" v-model="newCommentContent" placeholder="留言...">
              <button @click="addComment">發佈</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 個人貼文彈出視窗 -->

<!-- 取消追蹤彈出視窗 -->
<div v-if="showUnfollowModal" class="follow-modal" @click.self="closeModal">
  <div class="follow-modal-content">
    <span class="close" @click="toggleUnfollowModal">&times;</span>
    <img :src="user.photo || defaultAvatar" alt="User Image">
    <div class="username">{{ user.userFirstName }} {{ user.userLastName }}</div>
    <div class="follow-linecontainer"></div>

    <div class="follow-modal-operator">
      <!-- <p @click="updateFriendState(isCloseFriend ? 1 : 2)">
        {{ isCloseFriend ? '移除摯友名單' : '加到摯友名單' }}
        <font-awesome-icon :icon="['fab', 'web-awesome']" />
      </p> -->
      <p @click="confirmUnfollow">
        取消追蹤
        <font-awesome-icon icon="fa-solid fa-chevron-right" />
      </p>
    </div>
  </div>
</div>
<!-- 取消追蹤彈出視窗 -->
  </body>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import { defineProps, defineEmits } from 'vue';
import axiosapi from '@/plugins/axios';
import UserListModal from '@/components/Friend/UserListModal.vue';
import { useRouter, useRoute } from 'vue-router';
import Swal from 'sweetalert2';
<<<<<<< HEAD
import SubListCompo1 from '../Subscriptions/SubListCompo1.vue';
=======
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764

const noImg = 'User\'s profile image';

const props = defineProps({
  user: {
    type: Object,
    default: () => ({ userFirstName: '', userLastName: '', photo: '', userId: '', isFollowing: false, state: 0, birth: '', phone: '', selfContent: '', gender: '' })
  },
  senderId: String,
  followersCount: Number,
  followingCount: Number,
  PostCount: Number,
});

const emits = defineEmits(['toggleFollow', 'user-clicked']);
const router = useRouter();
const route = useRoute();

const showFollowersModal = ref(false);
const showFollowingModal = ref(false);
const showUnfollowModal = ref(false);

const posts = ref([]);
const shops = ref([]);
const newCommentContent = ref("");
const currentView = ref('posts');  // State to manage the current view
const defaultAvatar = 'https://img.freepik.com/premium-vector/default-avatar-profile-icon-social-media-user-image-gray-avatar-icon-blank-profile-silhouette-vector-illustration_561158-3396.jpg?w=360';
const defaultImage = 'https://t4.ftcdn.net/jpg/02/86/37/57/360_F_286375793_tMNCezCxZVJMzXVlunBoKI1JWP72cE9L.jpg';


// 弹窗逻辑
const openFollowersModal = () => {
  showFollowersModal.value = true;
};

const closeFollowersModal = () => {
  showFollowersModal.value = false;
};

const openFollowingModal = () => {
  showFollowingModal.value = true;
};

const closeFollowingModal = () => {
  showFollowingModal.value = false;
};


const handleFollowClick = () => {
  if (props.user.isFollowing) {
    toggleUnfollowModal();
  } else {
    handleToggleFollow();
  }
};

const handleToggleFollow = () => {
  if (!props.senderId) {
    router.push('/views/login');
    return;
  }
  emits('toggleFollow', { user: props.user, source: 'personal' });
};

const handleToggleFollowFromModal = (user) => {
  emits('toggleFollow', { user, source: 'userListModal' });
};

const handleUserClicked = (userId) => {
  closeFollowersModal();
  closeFollowingModal();
  router.push({ name: 'user-profile', query: { id: userId } });
};



function previewFile(event) {
  const file = event.target.files[0];
  const reader = new FileReader();

  reader.onload = (e) => {
    props.user.photo = e.target.result;
    uploadImage(e.target.result);
  };

  if (file) {
    reader.readAsDataURL(file);
  }
}

async function uploadImage(base64Image) {
  try {
    const sessionId = sessionStorage.getItem('Session-Id'); // 获取 session-id
    if (!sessionId) {
      throw new Error('Session ID not found');
    }
    const response = await axiosapi.put(
      '/api/userInfo/updatePhoto',
      { image: base64Image },
      {
        headers: {
          'Content-Type': 'application/json',
          'session-id': sessionId,
        },
      }
    );

    if (response.data.success) {
      console.log('圖片更新成功');
    } else {
      console.error('圖片更新失敗:', response.data.message);
    }
  } catch (error) {
    console.error('上傳圖片時發生錯誤:', error);
  }
}

const showSettingsModal = ref(false);

const toggleSettingsModal = () => {
  showSettingsModal.value = !showSettingsModal.value;
};

const toggleUnfollowModal = () => {
  showUnfollowModal.value = !showUnfollowModal.value;
};

const confirmUnfollow = () => {
  toggleUnfollowModal();
  handleToggleFollow();
};

const saveChanges = async () => {
  try {
    const sessionId = sessionStorage.getItem('Session-Id'); // 獲取 session-id
    if (!sessionId) {
      throw new Error('Session ID not found');
    }
    const formattedBirth = props.user.birth ? props.user.birth : '';
    const userData = {
      birth: formattedBirth,
      gender: props.user.gender.toString(),
      phone: props.user.phone,
      selfContent: props.user.selfContent,
      userFirstName: props.user.userFirstName,
      userLastName: props.user.userLastName,
    };
    // console.log('Sending user data:', userData);
    const response = await axiosapi.put(
      '/api/userInfo/updateInfo',
      userData,
      {
        headers: {
          'Content-Type': 'application/json',
          'session-id': sessionId,
        },
      }
    );

    if (response.data.success) {
      console.log('用戶信息更新成功');
    } else {
      console.error('用戶信息更新失敗:', response.data.message);
    }
  } catch (error) {
    console.error('保存變更時發生錯誤:', error);
  }
  toggleSettingsModal();
};


const fetchUserPosts = async () => {
  try {
    const sessionId = props.user.userId;
    if (!sessionId) {
      throw new Error('User ID not found');
    }
    const response = await axiosapi.post(
      '/api/friends/userPostsFind',
      {
        asc: false,
        userId: props.user.userId
      },
      {
        headers: {
          'Content-Type': 'application/json',
          'session-id': sessionId,
        }
      }
    );

    if (response.data.error) {
      console.error('獲取貼文失敗:', response.data.error);
    } else {
      posts.value = response.data.posts.map(post => {
        post.addTime = post.addTime;
        post.formattedAddTime = formatRelativeTime(post.addTime);
        return post;
      });
    }
  } catch (error) {
    console.error('獲取貼文時發生錯誤:', error);
  }
};

const formatDate = (dateString) => {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}年${month}月${day}日`;
};

const formatRelativeTime = (dateString) => {
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
};

// 獲取好友狀態
const fetchFriendState = async () => {
  // console.log("userId", props.user.userId);
  try {
    const sessionId = sessionStorage.getItem('Session-Id');
    // console.log("sessionId", sessionId);
    if (!sessionId) {
      throw new Error('Session ID not found');
    }

    const response = await axiosapi.post('/api/friends/state', {
      sendId: sessionId,
      receiveId: props.user.userId
    }, {
      headers: {
        'Content-Type': 'application/json',
        'session-id': sessionId,
      },
    });

    if (response.data.success) {
      props.user.state = response.data.state; // 更新本地狀態
      // 根據 state 更新顯示文本
      if (response.data.state === 1) {
        console.log('顯示 "加到摯友名單"');
      } else if (response.data.state === 2) {
        console.log('顯示 "移除摯友名單"');
      }
    } else {
      console.error('獲取好友狀態失敗:', response.data.message);
    }
  } catch (error) {
    console.error('獲取好友狀態時發生錯誤:', error);
  }
};


watch(() => props.user, (newUser) => {
  if (newUser && newUser.userId) {
    if (currentView.value === 'posts') {
      fetchUserPosts();
    }
    fetchFriendState();  // 新增這行
  }
}, { immediate: true });

const showImageModal = ref(false);
const currentPost = ref(null);
const postComments = ref([]);


const openImageModal = async (post) => {
  currentPost.value = post;

  try {
    const sessionId = sessionStorage.getItem('Session-Id');
    if (!sessionId) {
      throw new Error('Session ID not found');
    }
    const postCommentsResponse = await axiosapi.post(
      `/ajax/comments/findPostComment/${post.postId}`,
      {},
      {
        headers: {
          'Content-Type': 'application/json',
          'session-id': sessionId,
        }
      }
    );

    if (postCommentsResponse.data.error) {
      console.error('獲取留言失敗:', postCommentsResponse.data.error);
    } else {
      const comments = postCommentsResponse.data.comments;
      const userInfoPromises = comments.map(comment => fetchUserInfo(comment.user));

      const userInfoResponses = await Promise.all(userInfoPromises);

      userInfoResponses.forEach((response, index) => {
        if (response.success) {
          comments[index].userPhoto = response.userInfo.photo;
          comments[index].userFirstName = response.userInfo.userFirstName;
          comments[index].userLastName = response.userInfo.userLastName;
        } else {
          comments[index].userPhoto = defaultAvatar;
          comments[index].userFirstName = "未知";
          comments[index].userLastName = "用戶";
        }
        comments[index].formattedTime = formatRelativeTime(comments[index].addTime);
      });

      postComments.value = comments;
    }
  } catch (error) {
    console.error('獲取留言時發生錯誤:', error);
  }

  currentPost.value.formattedAddTime = formatRelativeTime(currentPost.value.addTime);
  showImageModal.value = true;
};

const closeImageModal = () => {
  showImageModal.value = false;
  currentPost.value = null;
  postComments.value = [];
};

const fetchUserInfo = async (userId) => {
  try {
    const sessionId = sessionStorage.getItem('Session-Id');
    if (!sessionId) {
      throw new Error('Session ID not found');
    }

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

const addComment = async () => {
  if (!newCommentContent.value.trim()) {
    alert("留言內容不能為空");
    return;
  }

  try {
    const sessionId = sessionStorage.getItem('Session-Id');
    if (!sessionId) {
      throw new Error('Session ID not found');
    }

    const response = await axiosapi.post(
      `/ajax/comments/newComment/${currentPost.value.postId}`,
      { content: newCommentContent.value },
      {
        headers: {
          'Content-Type': 'application/json',
          'session-id': sessionId,
        }
      }
    );

    if (response.data.error) {
      console.error('新增留言失敗:', response.data.error);
      alert(response.data.error);
    } else {
      console.log('新增留言成功:', response.data);
      newCommentContent.value = "";
      openImageModal(currentPost.value);
    }
  } catch (error) {
    console.error('新增留言時發生錯誤:', error);
    alert('新增留言時發生錯誤');
  }
};

// 更新好友狀態
const updateFriendState = async (state) => {
  try {
    const sessionId = sessionStorage.getItem('Session-Id');
    if (!sessionId) {
      throw new Error('Session ID not found');
    }
    
    const data = {
      id: {
        sendId: props.senderId,
        receiveId: props.user.userId,
      },
      state: state
    };

    const response = await axiosapi.put('/api/friends/update', data, {
      headers: {
        'Content-Type': 'application/json',
        'session-id': sessionId,
      },
    });

    if (response.data.success) {
      console.log('更新成功');
      props.user.state = state; // 更新本地狀態
    } else {
      console.error('更新失敗:', response.data.message);
    }
  } catch (error) {
    console.error('更新好友狀態時發生錯誤:', error);
  }
};

// Methods to switch views
const showPosts = () => {
  currentView.value = 'posts';
  fetchUserPosts();  // Fetch posts only when posts view is active
};

const showShops = () => {
  console.log("觸發showShops");
  currentView.value = 'shops';
  fetchUserShops();
};

<<<<<<< HEAD
const showSubsEdit = () => {
  currentView.value = 'subsEdit';
};

=======
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
// 當組件掛載時獲取好友狀態
onMounted(() => {
  if (props.user && props.user.userId) {
    fetchFriendState();
    showShops();
    fetchUserShops();
    console.log("觸發props.user.userId", props.user.userId);
  }
});

const closeModal = () => {
  showUnfollowModal.value = false;
};

const fetchUserShops = async () => {
  const owner = props.user.userId;
  if (!owner) {
    throw new Error('User ID not found');
  }
  console.log("有呼叫商城", owner);
  try {
    const response = await axiosapi.post('/api/userInfo/getUserShopsAndProducts', { userId: owner });
    if (response.data.success) {
      console.log("有呼叫shops", shops);
      shops.value = response.data.shops;
    } else {
      console.error('Failed to fetch shops and products:', response.data.message);
    }
  } catch (error) {
    console.error('Error fetching shops and products:', error);
  }
};

const addToCart = async (productId, price) => {
  let storedProduct = sessionStorage.getItem('products');
  let productSession = storedProduct ? JSON.parse(storedProduct) : {};

  if (!productSession[productId]) {
    productSession[productId] = { value: 1, price: price };
    sessionStorage.setItem('products', JSON.stringify(productSession));
    axiosapi.defaults.headers["session-id"] = sessionStorage.getItem("Session-Id");
    try {
      const response = await axiosapi.post(`/ajax/shop/addToCart/${productId}`);
      if (response.data.success) {
        Swal.fire({
          icon: "success",
          title: "商品已加入購物車",
          text: response.data.message,
          timer: 1000,
          showConfirmButton: false
        }).then(() => {
          window.location.reload();
        });
      } else {
        Swal.fire({
          icon: "error",
          title: "添加到購物車失敗",
          text: response.data.message,
          timer: 1000,
          showConfirmButton: false
        });
      }
    } catch (error) {
      console.error('Error adding to cart:', error);
    }
  } else {
    Swal.fire({
      icon: "info",
      title: "商品已在購物車中",
      text: "您已經將此商品加入購物車。",
      timer: 1000,
      showConfirmButton: false
    });
  }
};

<<<<<<< HEAD
function goToEditSubscript(userId,username) {
  router.push({ name: 'sublist3-page-link'});
}

function goToOtherSubs(userId,username) {
  console.log("userId：",userId);
  console.log("userName：",username);
  router.push({ name: 'sublist8-page-link', query: { id: userId, name: username } });
}
=======
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
</script>

<style scoped>
@import '@/assets/personalPage.css';
@import '@/assets/personalUpdate.css';
@import '@/assets/personalPost.css';

</style>