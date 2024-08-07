<template>
    <div class="container">
      <div class="cards-container">
        <HomeCard v-for="(card, index) in cards" :key="index" :card="card" />
        <!-- IntersectionObserver 觀察目標 -->
        <div class="observer-target" ref="observerTarget"></div>
      </div>
    </div>
  </template>
  
  <script setup>
  import HomeCard from '@/components/Home/HomeCard.vue';
  import axiosapi from '@/plugins/axios'; // Axios instance for API calls
  import { ref, onMounted, onBeforeUnmount } from 'vue';
  
  const cards = ref([]); // Array to hold cards data
  const page = ref(1); // Current page of data
  const perPage = ref(10); // Number of items per page
  const maxPages = ref(50); // Maximum number of pages
  const loading = ref(false); // Loading state
  
  const SessionId = sessionStorage.getItem('Session-Id'); 
  // 或者使用 sessionStorage
  // const currentUserId = sessionStorage.getItem('currentUserId');
  console.log('SessionId:', SessionId);
  
  // Function to fetch more cards
  const fetchCards = async (page) => {
    try {
      const response = await axiosapi.post("/ajax/post/allPost", { page });
      console.log('Fetched posts:', response.data.posts); // 打印返回的数据
      const allPosts = response.data.posts; // 获取所有帖子
      return allPosts.filter(post => post.posterInfoId == SessionId); // 过滤当前用户的帖子
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
      console.log('New cards:', newCards); // 打印新卡片
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
    const observerTarget = document.querySelector('.cards-container > .observer-target');
    if (observerTarget) {
      observer.observe(observerTarget);
    } else {
      console.error('Observer target not found');
    }
    loadMoreCards();
  });
  
  // Hook: Before component is unmounted
  onBeforeUnmount(() => {
    observer.disconnect(); // Stop observing and clean up
  });
  
  </script>
  
  <style scoped>
  .container {
    display: flex;
    justify-content: center;
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
  