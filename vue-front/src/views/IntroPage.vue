<template>
  <body>
    <div class='wrap'>
      <span>L</span>
      <span>O</span>
      <span>A</span>
      <span>D</span>
      <span>I</span>
      <span>N</span>
      <span>G</span>
    </div>
    <div class='wrap'>
      <span>L</span>
      <span>O</span>
      <span>A</span>
      <span>D</span>
      <span>I</span>
      <span>N</span>
      <span>G</span>
    </div>
  </body>
</template>
  
<script setup>
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

onMounted(() => {
  setTimeout(() => {
    router.push('/home'); // 主页面的路由
  }, 2000); // 3秒后跳转
});
</script>
  
<style scoped>
@import url("https://fonts.googleapis.com/css2?family=JetBrains+Mono:ital,wght@0,100..800;1,100..800&display=swap");

body {
  position: fixed;
  /* 固定背景图片 */
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-size: cover;
  /* 背景图像保持比例并覆盖整个容器 */
  background-position: center;
  /* 背景图像居中 */
  background-attachment: fixed;
  /* 固定背景图像 */
  z-index: -1;
  /* 确保图像在内容后面 */
  background: #1e1f24;
  /* Set up a base gradient background */
  background: radial-gradient(circle at center, #1e1f24, #444444);
}

body:before,
body:after {
  border-radius: 20px 20px 0 0;
  content: "";
  position: absolute;
  width: 100vw;
  background: radial-gradient(circle at center, #58c9ea 1px, transparent 1px, transparent 10px) 50% 50%/20px 20px, radial-gradient(circle at center, #58c9ea 1px, transparent 1px, transparent 10px) calc(50% + 10px) calc(50% + 10px)/20px 20px;
  height: calc(50vh - 10px);
  box-shadow: inset 0 0 0 1px #f84c04;
  left: 0;
  top: calc(50% + 22.5px);
  animation: scroll 2s linear infinite -1.5s;
}

@keyframes scroll {
  to {
    transform: translateX(-200vw);
  }
}

body:after {
  width: 150vw;
  left: 150vw;
}

body .wrap {
  width: 400px;
  height: 400px;
  text-align: center;
  position: absolute;
  transform-origin: 100% 0%;
  top: calc(50% - 200px);
  left: calc(50% - 400px);
  font-family: "JetBrains Mono";
  font-size: 50px;
  animation: wobble 2s linear infinite;
}

body .wrap:nth-of-type(2) {
  animation-delay: -0.05s;
  z-index: -1;
}

body .wrap:nth-of-type(2) span {
  color: #1e1f24;
  animation: run2 2s ease-in-out infinite;
  -webkit-text-stroke: 2px #bb9c30;
}

@keyframes wobble {

  0%,
  20%,
  40%,
  50%,
  80%,
  100% {
    transform: rotate(-5deg);
  }

  10%,
  30%,
  90% {
    transform: rotate(5deg);
  }

  70% {
    transform: rotate(0deg) scaleY(0.8) scaleX(1.1);
  }
}

body .wrap span {
  color: #ebe9dc;
  position: absolute;
  offset-path: path("M 140 200 C 100 80 300 80 260 200 ");
  animation: run 2s ease-in-out infinite;
}

@keyframes run2 {

  0%,
  20%,
  40%,
  80%,
  100% {
    offset-path: path("M 140 200 C 100 80 300 80 260 200 ");
  }

  10%,
  30%,
  90% {
    offset-path: path("M 50 200 C 100 80 300 80 350 200 ");
  }

  50%,
  60%,
  70% {
    offset-path: path("M 0 70 C 160 70 260 70 400 70 ");
  }
}

@keyframes run {

  0%,
  23.5%,
  43.5%,
  83.5%,
  100% {
    offset-path: path("M 140 200 C 100 80 300 80 260 200 ");
  }

  13.5%,
  33.5%,
  93.5% {
    offset-path: path("M 50 200 C 100 80 300 80 350 200 ");
  }

  50%,
  60%,
  70% {
    offset-path: path("M 0 70 C 160 70 260 70 400 70 ");
  }
}

body .wrap span:nth-of-type(1) {
  offset-distance: calc(0 * 1%);
}

body .wrap span:nth-of-type(2) {
  offset-distance: calc(16.6666666667 * 1%);
}

body .wrap span:nth-of-type(3) {
  offset-distance: calc(33.3333333333 * 1%);
}

body .wrap span:nth-of-type(4) {
  offset-distance: calc(50 * 1%);
}

body .wrap span:nth-of-type(5) {
  offset-distance: calc(66.6666666667 * 1%);
}

body .wrap span:nth-of-type(6) {
  offset-distance: calc(83.3333333333 * 1%);
}

body .wrap span:nth-of-type(7) {
  offset-distance: calc(100 * 1%);
}</style>
  