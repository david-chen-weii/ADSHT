<template>
    <div v-if="visible" class="modal">
        <div class="modal-content">
            <!-- Modal content goes here -->
            <p>This is the edit comment modal!</p>
            <button @click="hide">Close</button>
        </div>
    </div>
</template>
  
<script setup>
import { ref } from 'vue';

const visible = ref(false);

function show() {
    visible.value = true;
}

function hide() {
    visible.value = false;
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