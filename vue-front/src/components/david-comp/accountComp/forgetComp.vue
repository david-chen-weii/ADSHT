<template>
    <div class="login-page">
        <div class="login-container">
            <div class="col-12">
                <h1 class="animate__bounceInDown">變更密碼</h1>
                <form @submit.prevent="reset">

                    <div class="mb-3">
                        <label for="inputPassword5" class="form-label">密碼</label>
                        <input type="password" class="form-control" id="password" @input="doinput('password', $event)">

                        <label for="inputPassword5" class="form-label">確認密碼</label>
                        <input type="password" class="form-control" id="passwordCheck"
                            @input="doinput('passwordCheck', $event)">
                        <div id="passwordHelpBlock" class="form-text">
                            Your password must be 8-20 characters long, contain letters and numbers, and must not contain
                            spaces, special characters, or emoji.
                        </div>
                    </div>
                    <button type="submit" class="btn btn-dark" @click="emits('reset')">送出</button>
                </form>
            </div>
        </div>
    </div>
</template>
    
<script setup>
const props = defineProps(["modelValue"])
const emits = defineEmits(["reset", "update:modelValue"])
//v-model function
function doinput(key, event) {
    emits('update:modelValue', {
        ...props.modelValue,
        [key]: event.target.value
    });
}
</script>
    
<style></style>