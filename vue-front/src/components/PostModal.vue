<template>
    <div>
      <button type="button" class="btn btn-primary" @click="showModal = true">
        New Post
      </button>
  
      <div v-if="showModal" class="modal fade show" tabindex="-1" role="dialog" aria-labelledby="postModalLabel" style="display: block;" aria-hidden="true">
        <div class="modal-dialog" role="document">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title" id="postModalLabel">Create a new post</h5>
              <button type="button" class="close" @click="showModal = false" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <form @submit.prevent="submitPost">
                <div class="form-group">
                  <label for="postContent">What's on your mind?</label>
                  <textarea v-model="postContent" class="form-control" id="postContent" rows="3"></textarea>
                </div>
              </form>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="showModal = false">Close</button>
              <button type="button" class="btn btn-primary" @click="submitPost">Post</button>
            </div>
          </div>
        </div>
      </div>
      <div v-if="showModal" class="modal-backdrop fade show"></div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        showModal: false,
        postContent: ''
      };
    },
    methods: {
      submitPost() {
        this.$emit('submit-post', this.postContent);
        this.postContent = '';
        this.showModal = false;
      }
    }
  };
  </script>
  
  <style>
  .modal-backdrop {
    z-index: 1040;
  }
  .modal {
    z-index: 1050;
  }
  </style>