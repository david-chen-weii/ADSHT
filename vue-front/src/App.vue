<template>
    <div class="app-container">
      <Navigation></Navigation>
      <br>
      <br>
      <br>
      <RouterView></RouterView>
      <!-- 这将显示路由视图的内容 -->
    </div>
  </template>
    
  <script setup>
  import Navigation from './views/Navigation.vue';
  import 'bootstrap/dist/css/bootstrap.min.css';
  import 'bootstrap/dist/js/bootstrap.bundle.min.js';
  </script>
    
  <style>
  /* 设置背景图片 */
  .app-container {
    position: relative;
    min-height: 100vh; /* 确保容器占满整个视口高度 */
    overflow: hidden; /* 防止滚动条覆盖背景 */
    padding: 0; /* 去除内边距 */
  }
  
  /* 背景图片设置 */
  body::before {
    content: "";
    position: fixed; /* 固定背景图片 */
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-image: url('/public/background/wall.jpg'); /* 使用实际图片路径 */
    background-size: cover; /* 背景图像保持比例并覆盖整个容器 */
    background-position: center; /* 背景图像居中 */
    background-attachment: fixed; /* 固定背景图像 */
    z-index: -1; /* 确保图像在内容后面 */
  }
  
  body {
    margin: 0;
    padding: 0;
  }
  </style>
  