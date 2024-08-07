<template>
    <!--modal show test-->
    <div>
        <button @click="showModal">Show Modal</button>
        <EditComment ref="editCommentModal" />
    </div>
</template>
  
<script setup>
import { ref } from 'vue';
import EditComment from '@/components/david-comp/testComp/testModal.vue'; // Make sure the path is correct

const editCommentModal = ref(null);

function showModal() {
    if (editCommentModal.value) {
        editCommentModal.value.show();
    }
}
</script>