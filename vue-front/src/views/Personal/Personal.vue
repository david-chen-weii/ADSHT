<template>
  <profile :data="data"></profile>
</template>
      
<script setup>
import { ref, onMounted } from "vue"
import profile from '@/components/SelfProfile/profile.vue'
import axiosapi from "@/plugins/axios";
const params = new URLSearchParams(window.location.search);
const id = params.get('id'); // id will be '2'
console.log(id)
const data = ref({})

const fetchData = async () => {
  try {
    const response = await axiosapi.get("/ajax/pages/profile/" + id);
    console.log("data  : ", response.data);
    return response.data; // Return fetched data
  } catch (error) {
    console.error("Error fetching data:", error);
    return null; // Return null or handle error appropriately
  }
};

onMounted(async () => {
  data.value = await fetchData();
  // Now you can use `data` in your component state or display it
});

</script>
      
<style>
.avatar {
  width: 100px;
  /* 調整頭像大小 */
  height: 100px;
  /* 調整頭像大小 */
  border-radius: 50%;
  /* 使圖像成為圓形 */
  cursor: pointer;
  /* 添加指針樣式，表示可以點擊 */
}

.modal-backdrop {
  z-index: 1040;
}

.modal {
  z-index: 1050;
}
</style>
      