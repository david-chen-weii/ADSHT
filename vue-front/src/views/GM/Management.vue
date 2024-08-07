<template>
  <div>
    <h1>用戶管理</h1>
    <table class="ui celled table">
      <thead>
        <tr>
          <th>帳號</th>
          <th>姓名</th>
          <th>狀態</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.id" :class="statusClass(user.status)">
          <td>{{ user.email }}</td>
          <td>{{ user.userLastName }}</td>
          <td>
            <i v-if="user.status === 1" class="icon checkmark green"></i>
            <i v-if="user.status === 0" class="icon close red"></i>
            {{ user.status }}
          </td>
          <td>
            <button @click="activateUser(user.id)" 
              class="ui button positive" 
              :disabled="user.status === 1">
              開通</button>
            <button  @click="deleteUser(user.id)" 
              class="ui button negative" 
              :disabled="user.status === 0">
              註銷</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axiosapi from '@/plugins/axios'; // 确保路径正确

const users = ref([]);

// Fetch users from the server
const fetchUsers = async () => {
  try {
    const response = await axiosapi.post('/api/userInfo/lastname');
    users.value = response.data.filter(user => user.status !== 2);
  } catch (error) {
    console.error('取得用戶資料時發生錯誤:', error);
  }
};

// 開通用戶
const activateUser = async (userId) => {
  console.log('開通用戶 ID:', userId);
  if (!userId) {
    console.error('無效的用戶 ID');
    alert('無效的用戶 ID');
    return;
  }
  try {
    const response = await axiosapi.put(`/api/userInfo/activate/${userId}`);
    console.log('用戶成功開通:', response.data);
    alert('用戶成功開通!');
    fetchUsers();
  } catch (error) {
    console.error('開通用戶時發生錯誤:', error.response?.data || error.message);
    alert('開通用戶時發生錯誤: ' + (error.response?.data?.message || error.message));
  }
};

// 註銷用戶
const deleteUser = async (userId) => {
  console.log('註銷用戶 ID:', userId);
  if (!userId) {
    console.error('無效的用戶 ID');
    alert('無效的用戶 ID');
    return;
  }
  try {
    const response = await axiosapi.put(`/api/userInfo/deactivate/${userId}`);
    console.log('用戶成功註銷:', response.data);
    alert('用戶成功註銷!');
    fetchUsers();
  } catch (error) {
    console.error('註銷用戶時發生錯誤:', error.response?.data || error.message);
    alert('註銷用戶時發生錯誤: ' + (error.response?.data?.message || error.message));
  }
};

// 根據狀態返回對應的 CSS 類
const statusClass = (status) => {
  switch (status) {
    case 'Approved':
      return 'positive';
    case 'Unknown':
      return 'negative';
    default:
      return '';
  }
};

// Fetch users when the component is mounted
onMounted(() => {
  fetchUsers();
});
</script>

<style scoped>
/* 调整表格的宽度和字体大小 */
.ui.celled.table {
  width: 100%;
}

/* 调整表头的字体大小 */
.ui.celled.table th {
  font-size: 16px; /* 增大字体大小 */
}

/* 调整单元格的字体大小 */
.ui.celled.table td {
  font-size: 16px; /* 增大字体大小 */
}

/* 调整操作列的宽度 */
.ui.celled.table th:last-child,
.ui.celled.table td:last-child {
  width: 160px; /* 调整操作列的宽度 */
}

/* 调整操作按钮的大小 */
.ui.button.positive,
.ui.button.negative {
  font-size: 14px; /* 增加字体大小 */
  padding: 8px 16px; /* 增加内边距 */
  margin-right: 5px; /* 保留按钮之间的间距 */
}

/* 调整操作列的单元格内边距 */
.ui.celled.table td:last-child {
  padding: 8px; /* 增加内边距 */
}
</style>
