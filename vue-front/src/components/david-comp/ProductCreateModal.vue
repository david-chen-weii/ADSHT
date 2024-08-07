<template>
  <div class="modal fade" id="postModal" tabindex="-1" aria-labelledby="postModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="postModalLabel">新商品</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitPost">
            <div class="mb-2">
              <label for="productName" class="form-label">商品名</label>
              <input type="text" class="form-control form-control-sm" id="productName" v-model="newProduct.productName" required>
            </div>

            <div class="mb-2">
              <label for="productPrice" class="form-label">價格</label>
              <input type="number" class="form-control form-control-sm" id="productPrice" v-model="newProduct.price" required>
            </div>

            <div class="mb-2">
              <label for="productDescription" class="form-label">商品描述</label>
              <textarea class="form-control form-control-sm" id="productDescription" v-model="newProduct.description" rows="2" required></textarea>
            </div>

            <div class="mb-2">
              <label for="productStock" class="form-label">庫存</label>
              <input type="number" class="form-control form-control-sm" id="productStock" v-model="newProduct.stock" required>
            </div>

            <div class="mb-3">
              <input class="form-control d-none" type="file" id="postImage" @change="onFileChange" ref="fileInput">

              <div v-if="newProduct.image" class="image-preview mb-3">
                <img :src="newProduct.image" alt="Image Preview" class="img-fluid img-thumbnail">
                <button type="button" class="btn btn-danger btn-sm mt-2" @click="clearImage">Remove Image</button>
              </div>
              <div v-else>
                <button type="button" class="btn btn-secondary btn-sm" @click="triggerFileInput">
                  <font-awesome-icon :icon="['fas', 'camera-retro']" /> Upload Image
                </button>
              </div>
            </div>

            <button type="submit" class="btn btn-primary btn-sm">建立</button>
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
const newProduct = ref({
  productName: '',
  image: null,
  description: '',
  price: '',
  stock: ''
});
const router = useRouter();
const fileInput = ref(null);

const onFileChange = (e) => {
  const file = e.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      newProduct.value.image = e.target.result;
    };
    reader.readAsDataURL(file);
  }
};

const clearImage = () => {
  newProduct.value.image = null;
};

const triggerFileInput = () => {
  fileInput.value.click();
};

const submitPost = async () => {
  const shopData = {
      productName: newProduct.value.productName,
      image: newProduct.value.image,
      description: newProduct.value.description,
      stock: newProduct.value.stock,
      price: newProduct.value.price,
  };
  const id = new URLSearchParams(window.location.search).get('id');
  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");

  try {
      const response = await axiosapi.post(`/ajax/shop/${id}/newProduct`, shopData);
      console.log('Product added successfully:', response.data);
      newProduct.value = { productName: '', image: null, description: '', stock: '', price: '' };

      const postModalElement = document.getElementById('postModal');
      if (postModalElement) {
          const postModal = Modal.getInstance(postModalElement) || new Modal(postModalElement);
          postModal.hide();
      }
      //window.location.reload();
      router.go(0);
  } catch (error) {
  }
};
</script>

<style scoped>
.modal-dialog {
  max-width: 400px; /* Adjust the width of the modal dialog */
}

.form-control-sm {
  font-size: 0.8rem; /* Adjust font size for form controls */
}

.image-preview img {
  max-width: 100%;
  height: auto;
  border-radius: 0.375rem; /* Border radius for image preview */
}

.btn-sm {
  font-size: 0.8rem; /* Adjust font size for buttons */
}

.btn-secondary {
  display: flex;
  align-items: center;
}

.btn-secondary .icon {
  margin-right: 8px;
}
</style>
