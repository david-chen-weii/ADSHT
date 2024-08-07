<template>
  <div class="nft-container">
    <div class="nft">
      <div class='main'>
        <img v-if="product.image" :src="product.image" class='tokenImage' alt="Product Image">

        <h2>{{ product.productName }}</h2>
        <p class='description'>商品描述: {{ product.productDescription }}</p>
        <div class='tokenInfo'>
          <div class="price">
            <ins><font-awesome-icon icon="fa-solid fa-money-bill" class="icon-large" /></ins>
            <p>{{ product.price }} 元</p>
          </div>
          <div class="duration">
            <!-- <ins><font-awesome-icon icon="fa-solid fa-box"  class="icon-large"/></ins>
                    <p>{{ product.stock }}</p> -->
          </div>
        </div>
        <hr />
        <div class='creator'>
          <button class="btn btn-primary" @click="emits('addToCart', product.productId, product.price)"
            v-show="!showEditBtn">加入購物車</button>
          <button class="btn btn-primary" @click="emits('editProduct', product)" v-show="showEditBtn">修改商品</button>

        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps(["product", "showEditBtn"]);
const emits = defineEmits(["addToCart"]);
</script>

<style scoped>
@import '@/assets/shopCard.css';

.card {
  border-radius: 0.5rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.card-img-top {
  border-bottom: 1px solid #ddd;
  object-fit: cover;
  height: 180px;
  /* Adjust as needed */
}

.card-body {
  padding: 1rem;
}

.card-title {
  font-size: 1.25rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.card-text {
  font-size: 0.875rem;
  margin-bottom: 0.5rem;
}

.card hr {
  margin: 0.5rem 0;
}

.btn-primary {
  background-color: #007bff;
  border: none;
  border-radius: 0.25rem;
  font-size: 0.875rem;
  padding: 0.5rem;
}

.btn-primary:hover {
  background-color: #0056b3;
}
</style>
