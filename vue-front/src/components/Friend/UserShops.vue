<template>
    <div>
      <div v-for="shop in shops" :key="shop.shopId" class="shop">
        <h3>{{ shop.shopName }}</h3>
        <p>{{ shop.shopDescription }}</p>
        <img :src="shop.shopImage" alt="Shop Image" />
        <div v-for="product in shop.products" :key="product.productId" class="product">
          <h4>{{ product.productName }}</h4>
          <p>{{ product.productDescribe }}</p>
          <p>{{ product.productPrice }}</p>
          <div v-for="image in product.productImages" :key="image.productImageId" class="product-image">
            <img :src="image.image" alt="Product Image" />
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue';
  import axiosapi from '@/plugins/axios';
  import { defineProps } from 'vue';
  
  const props = defineProps({
    userId: {
      type: String,
      required: true
    }
  });
  
  const shops = ref([]);
  
  const fetchShopsAndProducts = async () => {
    console.log("有呼叫商城");
    try {
      const response = await axiosapi.post('/api/userInfo/getUserShopsAndProducts', { userId: props.userId });
      if (response.data.success) {
        shops.value = response.data.shops;
      } else {
        console.error('Failed to fetch shops and products:', response.data.message);
      }
    } catch (error) {
      console.error('Error fetching shops and products:', error);
    }
  };
  
  onMounted(() => {
    fetchShopsAndProducts();
  });
  </script>
  
  <style scoped>
  /* Add styles for shop and product display */
  .shop {
    margin-bottom: 20px;
  }
  
  .product {
    margin-left: 20px;
  }
  
  .product-image img {
    width: 100px;
    height: 100px;
  }
  </style>
  