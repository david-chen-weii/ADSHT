<template>
    <comp1 v-for="a in 3" @do-click="doClick" :a="a"></comp1>
    <testModal ref="modal" ></testModal>
</template>
    
<script setup>
import comp1 from '@/components/david-comp/testComp/test2Comp.vue';
import testModal from '@/components/david-comp/testComp/testModal.vue';
import { ref } from 'vue';

const a = ref(1);
const modal = ref(null);
function doClick(a){
    console.log('yes', a)
    modal.value.show();
}
</script>
    
<style>
    
</style>