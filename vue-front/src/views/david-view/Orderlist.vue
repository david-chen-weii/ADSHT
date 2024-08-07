<template>
    <div class="order-list">
      <h2>我的訂單</h2>
      <table class="table table-striped">
        <thead>
          <tr>
            <th>訂單編號</th>
            <th>數量</th>
            <th>價格</th>
            <th>商品</th>
            <th>賣家</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="orders.length === 0">
            <td colspan="4">沒有訂單</td>
          </tr>
          <tr v-else v-for="order in orders" :key="order.cartId">
            <td>{{ order.cartId }}</td>
            <td>{{ order.number }}</td>
            <td>{{ order.price }}</td>
            <td>{{ order.product }}</td>
            <td>{{ order.seller }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import axiosapi from '@/plugins/axios'; // 確保正確引入你的 axios 實例
  
  const orders = ref([]);
  
  const fetchOrders = async () => {
    try {
      const sessionId = sessionStorage.getItem("Session-Id"); // 確保與實際存儲鍵名一致
      if (!sessionId) {
        console.error('Session ID not found.');
        return;
      }
  
      // 設置 session-id 請求頭
      axiosapi.defaults.headers['session-id'] = sessionId;
  
      // 發送 API 請求
      const response = await axiosapi.post('/ajax/cart/orderlist');
      orders.value = response.data;
    } catch (error) {
      console.error('Error fetching orders:', error);
    }
  };
  
  // 當組件掛載時，獲取訂單數據
  onMounted(() => {
    fetchOrders();
  });
  </script>
  
  <style scoped>
  /* 添加一些基本的樣式 */
  .order-list {
    padding: 20px;
  }
  .order-list ul {
    list-style-type: none;
    padding: 0;
  }
  .order-list li {
    border-bottom: 1px solid #ddd;
    padding: 10px 0;
  }
  </style>
  