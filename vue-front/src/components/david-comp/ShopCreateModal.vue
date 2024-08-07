<template>
    <div class="modal fade" id="postModal" tabindex="-1" aria-labelledby="postModalLabel">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="postModalLabel">建立商店</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form @submit.prevent="submitPost">

                        <div class="mb-3">
                            <label for="shopName" class="form-label">商店名</label>
                            <textarea class="form-control" id="shopName" v-model="newShop.shopName" rows="3"
                                required></textarea>
                        </div>

                        <div class="mb-3">
                            <label for="postContent" class="form-label">商店描述</label>
                            <textarea class="form-control" id="postContent" v-model="newShop.description" rows="3"
                                required></textarea>
                        </div>

                        <div class="mb-3">
                            <input class="form-control d-none" type="file" id="postImage" @change="onFileChange"
                                ref="fileInput">

                            <div v-if="newShop.image" class="mb-3">
                                <img :src="newShop.image" alt="Image Preview" class="img-fluid">
                            </div>
                            <font-awesome-icon :icon="['fas', 'camera-retro']" class="icon-large" @click="triggerFileInput"
                                style="cursor: pointer;" />
                            <font-awesome-icon :icon="['fas', 'trash']" class="icon-large" v-if="newShop.image"
                                @click="clearImage" />
                        </div>

                        <button type="submit" class="btn btn-primary">建立</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</template>
  
<script setup>
import { Modal } from 'bootstrap';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axiosapi from '../../plugins/axios';

const router = useRouter();
const newShop = ref({ shopName: '', image: null, description: "" });
const fileInput = ref(null);

const onFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
            newShop.value.image = e.target.result;
        };
        reader.readAsDataURL(file);
    }
};

//清除圖片按鈕
const clearImage = () => {
    newShop.value.image = null;
};

const triggerFileInput = () => {
    fileInput.value.click();
};

const submitPost = () => {
    const shopData = {
        shopName: newShop.value.shopName,
        image: newShop.value.image,
        description: newShop.value.description,
    };

    axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
    axiosapi.post("/ajax/shop/newShop", shopData)
        .then(response => {
            newShop.value = { shopName: '', image: null, description: "" };

            // 尝试获取模态框实例并添加调试日志
            const postModalElement = document.getElementById('postModal');

            if (postModalElement) {
                const postModal = Modal.getInstance(postModalElement) || new Modal(postModalElement);
                console.log('postModal:', postModal);

                if (postModal) {
                    postModal.hide();
                    window.location.replace('/views/david-search-page');
                }
            }
        })
        .catch(error => {
            console.error('Error submitting post:', error);
        });
};
</script>
  
<style scoped>
/* Optional: Add some custom styles */
</style>
  