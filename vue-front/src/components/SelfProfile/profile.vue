<template>
    {{ data.id }}
    {{ data.gender }}
    {{ data.email }}
    {{ data.phone }}
    {{ data.content }}
    {{ data.firstName }}
    {{ data.lastName }}
    {{ data.birth }}
    {{ data.photo }}
    <img class="ui avatar image" v-show="data.photo != ''" :src="'data:image/png;base64,' + data.photo" alt="Base64 Image">
</template>


<script setup>
const props = defineProps(["data"])
</script>