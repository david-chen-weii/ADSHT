<template>
    <div ref="exampleRef" class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel"
        aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <div class="grid-1-5">
                        <img v-if="productA.Image" :src="productA.Image" alt="" width="80px" height="80px">
                        <img v-else src="/public/favicon.ico" alt="" width="80px" height="80px">
                        <h2>{{ productA.ServiceName }}</h2>
                        <h3><span class="uppercase">${{ productA.Price }}</span></h3>
                        <p>{{ productA.Description }}</p>
                        <div>
                            <a v-if="showbtn === '1'" href="#/" class="button3"
                                @click="emits('customAdd', productA.ServiceId)">訂閱</a>

                            <a v-if="showbtn === 'findMyCreate'" href="#" class="button2"
                                @click="emits('customDelete', productA.ServiceId)">修改</a>

                            <a v-if="showbtn === 'findMyCreate'" href="#" class="button1"
                                @click="emits('customDelete', productA.ServiceId)">刪除</a>

                            <a v-if="showbtn === 'findMySubsById'" href="#/" class="button1"
                                @click="emits('customCancel', productA.ServiceId)">取消訂閱</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
const props = defineProps(["productA", "showbtn"]);
const emits = defineEmits(["customDelete", "customAdd", "customCancel"]);



import bootstrap from "bootstrap/dist/js/bootstrap.bundle.min.js"
import { ref, onMounted } from 'vue'
const exampleRef = ref(null);
const exampleModal = ref(null);



onMounted(function () {
    exampleModal.value = new bootstrap.Modal(exampleRef.value);
});

function showModal() {
    exampleModal.value.show();
}
function hideModal() {
    exampleModal.value.hide();
}

defineExpose({
    showModal, hideModal,
});
</script>

<style></style>