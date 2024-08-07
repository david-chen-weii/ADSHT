<template>
    <div ref="exampleRef" class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="exampleModalLabel">Product</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <table>
                        <tr>
                            <td> : </td>
                            <td><input type="text" name="id" :value="modelValue.id" @input="doinput('id', $event)"></td>
                        </tr>
                        <tr>
                            <td>Name : </td>
                            <td><input type="text" name="name" :value="modelValue.name" @input="doinput('name', $event)"></td>
                        </tr>
                        <tr>
                            <td>Price : </td>
                            <td><input type="text" name="price" :value="modelValue.price" @input="doinput('price', $event)"></td>
                        </tr>
                        <tr>
                            <td>Make : </td>
                            <td><input type="text" name="make" :value="modelValue.make" @input="doinput('make', $event)"></td>
                        </tr>
                        <tr>
                            <td>Expire : </td>
                            <td><input type="text" name="expire" :value="modelValue.expire" @input="doinput('expire', $event)"></td>
                        </tr>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                    <button type="button" class="btn btn-primary" v-show="isShowInsertButton" @click="emits('customInsert')">新增</button>
                    <button type="button" class="btn btn-primary" v-show="!isShowInsertButton" @click="emits('customUpdate')">修改</button>

                </div>
            </div>
        </div>
    </div>
</template>
    
<script setup>
    const props = defineProps(["isShowInsertButton", "modelValue"]);
    const emits = defineEmits(["customInsert", "customUpdate", "update:modelValue"])
    function doinput(key, event) {
        emits('update:modelValue', {
            ...props.modelValue,
            [key]: event.target.value
        });
    }

    import bootstrap from "bootstrap/dist/js/bootstrap.bundle.min.js"
    import { ref, onMounted } from 'vue'

    const exampleRef = ref(null);
    const exampleModal = ref(null);
    onMounted(function() {
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
    
<style>
    
</style>