<template>
  <div class="card">
    <div class="card-body">
      <h5 class="card-title">{{ post.userName }}</h5>
      <p class="card-text">{{ post.content }}</p>
      <p class="card-text"><small class="text-muted">Posted {{ post.timeAgo }}</small></p>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    post: {
      type: Object,
      required: true
    }
  }
};
</script>

<style scoped>
.card {
  margin-bottom: 0; /* 移除卡片之間的間距 */
}
</style>
