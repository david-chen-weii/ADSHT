<template>
    <div class="container">
        <div class="cards-container">
            <CommentCard :card="post" :comments="comments" :postId="postId" />
            
        </div>
    </div>
</template>
    
<script setup>
import CommentCard from "@/components/Post/CommentCard.vue"
import ReplyBox from "./ReplyBox.vue";

const props = defineProps({
  post: Object,
  comments: Array,
  postId: Number // 确保 postId 被传递
});

</script>
    
<style></style>