<template>
  <div class="container">
    <div class="cards-container">
      <HomeCard v-for="(card, index) in cards" :key="index" :card="card" @click-like="clickLike(index)"
        @comment-click="addCommentCard" :show="card.posterInfoId == userInfoId||userStatus===2" :showCommentBar="showCommentBar"
        ref="homeCards" />
      <!-- IntersectionObserver 觀察目標 -->
      <div class="observer-target" ref="observerTarget"></div>
    </div>
    <!-- 固定在右下角的按鈕 -->
    <div class="fixed-button">
      <button type="button" class="btn btn-primary post-button" data-bs-toggle="modal" data-bs-target="#postModal">
        <font-awesome-icon :icon="['fas', 'pen-to-square']" />
      </button>
      <PostModal />
    </div>
    <div>
    </div>
  </div>
</template>

<script setup>
import HomeCard from '@/components/Home/HomeCard.vue'; // Import Card component
import axiosapi from '@/plugins/axios'; // Axios instance for API calls
import { ref, onMounted, onBeforeUnmount } from 'vue';
import PostModal from '@/components/Edit/PostModal.vue';
const cards = ref([]); // Array to hold cards data
const page = ref(1); // Current page of data
const perPage = ref(10); // Number of items per page
const maxPages = ref(50); // Maximum number of pages
const loading = ref(false); // Loading state
const userInfoId = ref('');
const showCommentBar = ref(false)
const userStatus = ref(null); // 用戶狀態
//按讚
const homeCards = ref([]);
function clickLike(index) {
  console.log("I like this");
  console.log(index);
  console.log(homeCards.value[index]);
  let element = homeCards.value[index].$el.querySelector(".icon.outline.like");
  if (element) {
    cards.value[index].likeCount++;
    element.classList.remove("outline");
    element.classList.add("inline");
  } else {
    let element = homeCards.value[index].$el.querySelector(".icon.inline.like");
    cards.value[index].likeCount--;
    element.classList.remove("inline");
    element.classList.add("outline");
  }
}

// Function to fetch more cards
const fetchCards = async (page) => {
  try {
    const response = await axiosapi.post("/ajax/post/allPost", { page });
    console.log(response.data.posts); // 打印返回的数据
    return response.data.posts; // Return fetched posts
  } catch (error) {
    console.error("Error fetching cards:", error);
    return []; // Return empty array on error
  }
};

// Function to load more cards
const loadMoreCards = async () => {
  if (page.value <= maxPages.value && !loading.value) {
    loading.value = true; // Set loading state to true
    const newCards = await fetchCards(page.value); // Fetch new cards
    cards.value = [...cards.value, ...newCards]; // Append new cards
    page.value++; // Increment page number
    loading.value = false; // Set loading state back to false
  }
};

// Intersection Observer callback function
const observerCallback = (entries) => {
  entries.forEach((entry) => {
    if (entry.isIntersecting) {
      loadMoreCards(); // Load more cards when entry is intersecting
    }
  });
};

// Intersection Observer options
const observerOptions = {
  root: null, // Use the viewport as the root
  rootMargin: '0px',
  threshold: 0.5,
};

// Intersection Observer instance
const observer = new IntersectionObserver(observerCallback, observerOptions);

// Hook: When component is mounted
onMounted(() => {
  // Start observing when component is mounted
  const observerTarget = document.querySelector('.cards-container > .observer-target');
  userInfoId.value = sessionStorage.getItem("Session-UserInfoId");
  
  if (observerTarget) {
    observer.observe(observerTarget);
  }

  // Initial load of cards
  loadMoreCards();
  if (sessionStorage.getItem("Session-Id"))
    showCommentBar.value = true;
});

// Hook: Before component is unmounted
onBeforeUnmount(() => {
  // Stop observing and clean up
  observer.disconnect();
});

</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  width: 700vh;
}

.cards-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.observer-target {
  height: 10px;
  /* Adjust height as needed for observer target */
  margin: 20px 0;
  /* Add margin to ensure intersection triggers appropriately */
}

.post-button {
  position: fixed;
  right: 20px;
  bottom: 20px;
}
</style>
