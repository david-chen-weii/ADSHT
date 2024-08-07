<template>
    <div v-show="visible" class="modal">
        <div class="background">
            <div class="modal-content">
                <!-- Modal content goes here -->
                <h3>商品名</h3>
                <input :value="product.productName" @input="doInput('productName', $event.target.value)">
                <h3>商品描述</h3>
                <textarea :value="product.productDescription"
                    @input="doInput('productDescription', $event.target.value)"></textarea>
                <h3>價格</h3>
                <input :value="product.price" type="number" @input="doInput('price', $event.target.value)">
                <h3>庫存</h3>
                <input :value="product.stock" type="number" @input="doInput('stock', $event.target.value)">
                <input type="file" @input="doInput('image', $event)"><img :src="product.image"
                    style="width:300px;height: 200px;">
                <div>
                    <button @click="deleteProduct(product.productId)">刪除商品</button>
                    <button @click="editProduct(product.productId, product)">確認修改</button>
                    <button @click="hide">取消</button>
                </div>
            </div>
        </div>
    </div>
</template>
  
<script setup>
import { ref } from 'vue';
import axiosapi from '@/plugins/axios';
const product = ref({});
const visible = ref(false);
const shopId = new URLSearchParams(window.location.search).get('id');

function show(info) {
    console.log("click")
    product.value = info;
    visible.value = true;
}

function hide() {
    visible.value = false;
}
function editProduct(productId, product) {
    axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
    axiosapi.put(`ajax/shop/${shopId}/editProduct/${productId}`, product).then((response) => {

    });
    hide();
}
function deleteProduct(productId) {
    axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
    axiosapi.delete(`ajax/shop/${shopId}/deleteProduct/${productId}`).then((response) => {

    });
    hide();
}
function doInput(key, value) {
    if (key == "image") {
        console.log(product.value.image)
        console.log(key, value.target.files[0]);
        const reader = new FileReader();
        reader.onload = (e) => {
            product.value = { ...product.value, [key]: e.target.result };
            console.log("data= \n", e.target.result)
        };
        reader.readAsDataURL(value.target.files[0]);
    }

    console.log(key, value);
    product.value = { ...product.value, [key]: value }
    console.log(product.value);
}
defineExpose({
    show,
    hide
});
</script>
  
<style scoped>
.modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
}

.modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 5px;
    text-align: center;
}
</style>