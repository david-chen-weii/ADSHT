<template>
  <div class="profile-page">
    <header>
      <img src="/public/favicon.ico" alt="Signal Icon" class="signal-icon" />
      <img src="/public/favicon.ico" alt="Battery Icon" class="battery-icon" />
    </header>
    <div class="profile-info">
      <img src="/public/favicon.ico" alt="Avatar" class="avatar" />
      <h1>訂閱管理</h1>
    </div>
    <div class="content">
      <div class="item">
        <h2>新增您的訂閱服務</h2>
        <p>讓你的社群更加活耀</p>
        <RouterLink class="nav-link active" :to="{name:'sublist-page-link'}">
          <font-awesome-icon :icon="['fas', 'chalkboard']" size="2xl"/><button>建立</button>
        </RouterLink>
      </div>
      <div class="item">
        <h2>管理您的訂閱服務</h2>
        <p>讓你輕鬆管理訂閱。</p>
        <RouterLink class="nav-link active" :to="{name:'sublist6-page-link'}">
          <font-awesome-icon :icon="['fas', 'chalkboard']" size="2xl"/><button>編輯</button>
        </RouterLink>
      </div>
    </div>
  </div>
</template>
    
<script setup>

function editProfile() {
      // 編輯個人檔案的邏輯
    };
function    shareProfile() {
      // 分享個人檔案的邏輯
    };
function    createPost() {
      // 建立申文的邏輯
    };
function    uploadPhoto() {
      // 上傳大頭貼的邏輯
    };
function    goHome() {
      // 回到主頁的邏輯
    };
function    search() {
      // 搜索的邏輯
    };
function    create() {
      // 創建內容的邏輯
    };
function    likes() {
      // 查看點讚的邏輯
    };
function    profile() {
      // 查看個人資料的邏輯
    };

</script>
    
<style scoped>
    .profile-page {
  background-color: #000;
  color: #fff;
  text-align: center;
<<<<<<< HEAD
  width: 2000px;
=======
  width: 1000px;
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
  height: 550px;
}
header {
  display: flex;
  justify-content: space-between;
  padding: 10px;
}
.signal-icon, .battery-icon {
  width: 20px;
  height: 20px;
}
.profile-info {
  margin: 20px 0;
}
.avatar {
  border-radius: 50%;
  width: 80px;
  height: 80px;
}
.actions {
  display: flex;
  justify-content: center;
  margin: 20px 0;
}
.actions button {
  margin: 0 10px;
}
.content {
  display: flex;
  justify-content: space-around;
}
.item {
  width: 45%;
  background: #333;
  padding: 20px;
  border-radius: 10px;
}
.bottom-nav {
  display: flex;
  justify-content: space-around;
  background: #333;
  padding: 10px 0;
  position: fixed;
  bottom: 0;
  width: 100%;
}
.bottom-nav button {
  color: #fff;
  background: none;
  border: none;
}
</style>