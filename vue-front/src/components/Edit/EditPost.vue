<template>
    <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="editModalLabel">Edit Post</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="submitEdit">
              <div class="mb-3">
                <label for="editContent" class="form-label">Content</label>
                <textarea class="form-control" id="editContent" v-model="edit.content" rows="3" required></textarea>
              </div>
              <div class="mb-3">
                <input class="form-control d-none" type="file" id="editImage" @change="onFileChange" ref="editFileInput">
                <div v-if="edit.image" class="mb-3">
                <img :src="edit.image" alt="Image Preview" class="img-fluid">
              </div>
                <font-awesome-icon :icon="['fas', 'camera-retro']" class="icon-large" @click="triggerEditFileInput" style="cursor: pointer;"/>
                <font-awesome-icon :icon="['fas', 'trash']" class="icon-large" v-if="edit.image" @click="clearImage"/>
              </div>
              
              <button type="submit" class="btn btn-primary">Update</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref , watch } from 'vue';
  import axiosapi from '../../plugins/axios';
  import { Modal } from 'bootstrap';
  
  const props = defineProps(["edit"]);

  const edit = ref({...props.edit });

  const editFileInput = ref(null);
  
  const onFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        edit.value.image = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  };
  
  const triggerEditFileInput = () => {
    editFileInput.value.click();
  };
  //清除圖片按鈕
  const clearImage = () => {
  edit.value.image = null;
};
  const submitEdit = () => {
    const editData = {
      content: edit.value.content,
      permission: edit.value.permission,
    image: edit.value.image || ''
    };
  
    axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
    axiosapi.put(`/ajax/post/editPost/${props.edit.postId}`, editData)
      .then(response => {
        console.log('Post updated successfully:', response.data);
        edit.value = { content: '', image: null };
  
        const editModalElement = document.getElementById('editModal');
        if (editModalElement) {
          const editModal = Modal.getInstance(editModalElement) || new Modal(editModalElement);
          if (editModal) {
            editModal.hide();
            window.location.replace('/home');
          } else {
            console.log('Failed to get the modal instance.');
          }
        } else {
          console.log('Failed to find the modal element.');
        }
      })
      .catch(error => {
        console.error('Error updating post:', error);
      });
  };
 // 监听 props 的变化以更新 edit 对象
watch(() => props.edit, (newValue) => {
  edit.value = { ...newValue };
}, { immediate: true });
</script>
  
  <style scoped>
  /* Optional: Add some custom styles */
  </style>
  