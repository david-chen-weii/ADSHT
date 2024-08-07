<template>
  <table class="table">
    <thead>
      <tr>
        <th scope="col">商品</th>
        <th scope="col">商品金額</th>
        <th scope="col">商品數量</th>
      </tr>
    </thead>
    <tbody class="table-group-divider">
      <Cart v-for="(cart, index) in carts" :key="index" :cart="cart" @delete-cart="deleteCart"
        @number-change="numberChange" :index="index" ref="cartComps" @check="check"></Cart>
    </tbody>
  </table>
  <div>結帳明細</div>
  <div>${{ totalPrice }}</div>
  <button @click="checkout()"><i class="truck icon"></i></button>
</template>
  
<script setup>
import Cart from '@/components/david-comp/Cart-comp.vue';
import axiosapi from '@/plugins/axios';
import Swal from 'sweetalert2';
import { onMounted, ref } from 'vue';

const cartComps = ref([]);
const carts = ref([]);
const cartChoose = ref({});
const totalPrice = ref(0);

const fetchCart = async () => {
  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
  try {
    const response = await axiosapi.post('/ajax/shop/cart');
    carts.value = response.data.product;
    let price = 0;
    let sessionNumber = JSON.parse(sessionStorage.getItem("products")) || {};

    for (let cart of carts.value) {
      let productId = cart.id;
      if (sessionNumber[productId]) {
        cart.number = sessionNumber[productId].value;
        price += cart.number * cart.price;
      } else {
        cart.number = 1; // Default number if not in session storage
        sessionNumber[productId] = { value: cart.number };
        price += cart.number * cart.price;
      }
    }

    totalPrice.value = price;
    sessionStorage.setItem("products", JSON.stringify(sessionNumber));
  } catch (error) {
    console.error('Error fetching cart:', error);
  }
};

onMounted(fetchCart);

function numberChange(method, productId, cartId, index, stock, isChecked) {
  const productStorage = sessionStorage.getItem("products");
  let products = productStorage ? JSON.parse(productStorage) : {};
  let price = totalPrice.value;
  if (products[`${productId}`]) {
    if (method === "minus") {
      if (products[`${productId}`].value === 1) {
        Swal.fire({
          title: '確定刪除嗎?',
          text: "這個操作無法恢復!",
          icon: 'warning',
          showCancelButton: true,
          confirmButtonText: '是的, 刪除!',
          cancelButtonText: '不, 保留!',
        }).then((result) => {
          if (result.isConfirmed) {
            delete products[`${productId}`];
            sessionStorage.setItem("products", JSON.stringify(products));
            axiosapi.delete(`/ajax/shop/deleteCartProduct/${cartId}`).then((response) => {
              if (response.data.success) {
                carts.value.splice(index, 1);
                fetchCart(); // 刷新购物车
              }
            });
          }
        });
      } else {
        products[`${productId}`].value -= 1;
        carts.value[index].number -= 1;
        if (isChecked) {
          price -= carts.value[index].price;
          totalPrice.value = price;
        }
        sessionStorage.setItem("products", JSON.stringify(products));

      }
    } else if (method === 'plus') {
      if (products[`${productId}`]) {
        if (products[`${productId}`].value < stock) {
          products[`${productId}`].value += 1;
          carts.value[index].number += 1;
          if (isChecked) {
            price += carts.value[index].price;
            totalPrice.value = price;
          }
          sessionStorage.setItem("products", JSON.stringify(products));
        }
      }
    }
  } else {
    console.error('Product not found in session storage');
  }
}
function check(productId, isChecked) {
  console.log(isChecked)
  let products = JSON.parse(sessionStorage.getItem('products')) || {};
  let price = totalPrice.value;

  if (isChecked) {
    price += products[`${productId}`].value * products[`${productId}`].price
  } else {
    price -= products[`${productId}`].value * products[`${productId}`].price
  }

  totalPrice.value = price;
}

function checkout() {
  let data = [];
  let num = [];
<<<<<<< HEAD
=======
  let itemNames = [];

>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
  let products = JSON.parse(sessionStorage.getItem('products')) || {};

  for (let i in cartComps.value) {
    let select = cartComps.value[i].$el.querySelector("input[type='checkbox']");
    if (select && select.checked) {
      let cart = carts.value[i];
      data.push(cart.cartId);
      num.push(products[cart.id] ? products[cart.id].value : 0);
<<<<<<< HEAD
=======

        // 添加商品名称
        itemNames.push(cart.name || '未知商品');
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
      delete products[cart.id];
    }
  }

  sessionStorage.setItem("products", JSON.stringify(products));
  cartChoose.value['carts'] = data;
  cartChoose.value['price'] = num;
<<<<<<< HEAD
  // 发送数据到 Node.js 后端
  axiosapi.get('http://localhost:3000/', {
=======
  cartChoose.value['itemNames'] = itemNames.join('; ');
  // 发送数据到 Node.js 后端
  console.log(import.meta.env.VITE_ECPAY_URL);
  //axiosapi.setBaseURL("http://127.0.0.1:3000");
  axiosapi.get("http://192.168.23.137:3000", {
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
    params: {
      amount: totalPrice.value
    }
  })
    .then((response) => {
      const paymentHtml = response.data.html;
      console.log('Payment HTML:', paymentHtml); // 调试打印 HTML

      // 清空 paymentContainer 内容并插入新的 HTML

      // 创建一个新的窗口并写入支付 HTML
      const paymentWindow = window.open('', '_blank');
      if (paymentWindow) {
        paymentWindow.document.open();
        paymentWindow.document.write(paymentHtml);
        paymentWindow.document.close();
      }

      // 清空购物车的逻辑
      carts.value = [];
      sessionStorage.removeItem("products");

      console.log('Cart cleared:', carts.value);
      console.log('Session storage cleared:', sessionStorage.getItem('products'));

      // 发送数据到 Spring 后端
      axiosapi.post(`/ajax/shop/closeOrder`, cartChoose.value).then((response) => {
        console.log(response);
        location.reload();
      }).catch(error => {
        console.error('Error during checkout:', error);
      });

    }).catch(error => {
      console.error('Error during checkout:', error);
    });

  sessionStorage.setItem("products", JSON.stringify(products));
  cartChoose.value['carts'] = data;
  cartChoose.value['price'] = num;

  axiosapi.post(`/ajax/shop/closeOrder`, cartChoose.value).then((response) => {
    console.log(response);
  }).catch(error => {
    console.error('Error during checkout:', error);
  });
}

function deleteCart(cartId, productId, index) {
  const products = JSON.parse(sessionStorage.getItem("products")) || {};
  Swal.fire({
    title: '確定刪除嗎?',
    text: "這個操作無法恢復!",
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '是的, 刪除!',
    cancelButtonText: '不, 保留!',
  }).then((result) => {
    if (result.isConfirmed) {
      delete products[`${productId}`];
      sessionStorage.setItem("products", JSON.stringify(products));
      axiosapi.delete(`/ajax/shop/deleteCartProduct/${cartId}`).then((response) => {
        if (response.data.success) {
          carts.value.splice(index, 1);
          fetchCart(); // 刷新购物车
        }
      });
    }
  });
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
  