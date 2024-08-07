<template>
  <div class="modal fade" id="postModal" tabindex="-1" aria-labelledby="postModalLabel">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="postModalLabel">Create Post</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submitPost">
            <div class="mb-3">
              <label for="postContent" class="form-label">Content</label>
              <textarea class="form-control" id="postContent" v-model="post.content" rows="3" required></textarea>
            </div>
            <div class="mb-3">
              <input class="form-control d-none" type="file" id="postImage" @change="onFileChange" ref="fileInput">
              <div v-if="post.image" class="mb-3">
                <img :src="post.image" alt="Image Preview" class="img-fluid">
            </div>
              <font-awesome-icon :icon="['fas', 'camera-retro']" class="icon-large" @click="triggerFileInput" style="cursor: pointer;"/>
              <font-awesome-icon :icon="['fas', 'trash']" class="icon-large" v-if="post.image" @click="clearImage"/>
            </div>
            
            <button type="submit" class="btn btn-primary">Post</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axiosapi from '../../plugins/axios';
import { Modal } from 'bootstrap';
import { useRouter } from 'vue-router';

const router = useRouter();
const post = ref({ content: '', image: null });
const fileInput = ref(null);

const onFileChange = (e) => {
  const file = e.target.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = (e) => {
      post.value.image = e.target.result;
    };
    reader.readAsDataURL(file);
  }
};

  //清除圖片按鈕
  const clearImage = () => {
    post.value.image = null;
};

const triggerFileInput = () => {
  fileInput.value.click();
};

const submitPost = () => {
  const postData = {
    content: post.value.content,
    image: post.value.image
  };

  axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
  console.log(axiosapi.defaults.headers.common);
  axiosapi.post("/ajax/post/newPost", postData)
    .then(response => {
      console.log('Post submitted successfully:', response.data);
      post.value = { content: '', image: null };


      // 尝试获取模态框实例并添加调试日志
      const postModalElement = document.getElementById('postModal');
      console.log('postModalElement:', postModalElement);

      if (postModalElement) {
        const postModal = Modal.getInstance(postModalElement) || new Modal(postModalElement);
        console.log('postModal:', postModal);

        if (postModal) {
          postModal.hide();
          window.location.replace('/home');
          console.log('Modal should be hidden now.');
        } else {
          console.log('Failed to get the modal instance.');
        }
      } else {
        console.log('Failed to find the modal element.');
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
