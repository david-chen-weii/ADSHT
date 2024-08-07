<template>
    <form @submit.prevent="submitPost">
      <div class="form-group">
        <label for="postContent">What's on your mind?</label>
        <textarea v-model="postContent" class="form-control" id="postContent" rows="3"></textarea>
      </div>
      <button type="submit" class="btn btn-primary">Post</button>
    </form>
  </template>
  
  <script>
  export default {
    data() {
      return {
        postContent: ''
      };
    },
    methods: {
      submitPost() {
        this.$emit('submit-post', this.postContent);
        this.postContent = '';
      }
    }
  };
  </script>