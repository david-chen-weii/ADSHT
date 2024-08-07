<template>
  <tr>
    <td>
      <div class="cart-item">
        <button type="button" class="btn-close" aria-label="Close"
          @click="emits('deleteCart', cart.cartId, cart.id, index)"></button>
        <input class="form-check-input" type="checkbox" value="" aria-label="Checkbox for item" checked @click="check">
        <div class="image-container">
          <img v-if="cart.image" :src="cart.image" alt="Product Image">
        </div>
      </div>
    </td>

    <td>
      <ul class="list-group list-group-flush">
        <li class="list-group-item cart-item-name">{{ cart.name }}</li>
        <li class="list-group-item cart-item-price">${{ cart.price * cart.number }}</li>
      </ul>
    </td>

    <td>
      <div class="quantity-controls">
        <button class="btn btn-outline-secondary btn-sm" type="button" @click="numberChange('minus')">-</button>
        <input type="text" :value="cart.number" class="quantity-input" aria-label="Quantity">
        <button class="btn btn-outline-secondary btn-sm" type="button" @click="numberChange('plus')">+</button>
      </div>
    </td>
  </tr>
</template>
  
<script setup>
const props = defineProps(["cart", "index"]);
const emits = defineEmits(["numberChange", "deleteCart", "check"]);

function check() {
  let isChecked = document.querySelectorAll("input[type='checkbox']")[props.index].checked
  emits('check', props.cart.id, isChecked)
}

function numberChange(method) {
  let isChecked = document.querySelectorAll("input[type='checkbox']")[props.index].checked
  console.log(props.index);
  emits('numberChange', method, props.cart.id, props.cart.cartId, props.index, props.cart.stock, isChecked)
}
</script>
  
<style scoped>
.cart-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.cart-item .btn-close {
  margin-right: 10px;
}

.image-container {
  width: 80px;
  height: 80px;
  overflow: hidden;
  border-radius: 8px;
}

.image-container img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cart-item-name {
  font-weight: bold;
}

.cart-item-price {
  color: #007bff;
}

.quantity-controls {
  display: flex;
  align-items: center;
  gap: 5px;
}

.quantity-input {
  width: 60px;
  text-align: center;
}
</style>
