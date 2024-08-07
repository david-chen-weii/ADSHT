<template>
    <div class="profile-header">
      <div class="profile-picture">
        <img :src="user.avatar" alt="Profile Picture">
      </div>
      <div class="profile-info">
        <h2>{{ user.name }}</h2>
        <p>{{ user.bio }}</p>
        <div class="profile-details">
          <p><strong>Location:</strong> {{ user.location }}</p>
          <!-- Add other details like join date, interests, etc. -->
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  const user = ref({
    name: 'User Name',
    avatar: 'path/to/avatar.jpg',
    bio: 'User bio description.',
    location: 'User location',
    // Add more details as needed
  });
  </script>
  
  <style scoped>
  .profile-header {
    display: flex;
    align-items: center;
  }
  
  .profile-picture img {
    width: 100px; /* Adjust size as needed */
    height: 100px; /* Adjust size as needed */
    border-radius: 50%; /* Rounded profile picture */
    object-fit: cover; /* Ensure the image covers the entire area */
    margin-right: 20px;
  }
  
  .profile-info {
    flex: 1; /* Take remaining space */
  }
  
  .profile-info h2 {
    margin-bottom: 10px;
  }
  
  .profile-details {
    margin-top: 10px;
    font-size: 14px;
  }
  </style>
  