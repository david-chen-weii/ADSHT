<template>
    <div class="testDiv"></div>
    <img src="\public\pic2\00060507.jpg" id="img1" class="myImg">
    <img src="\public\pic2\00079160.jpg" id="img2" class="myImg">
    <test1Comp></test1Comp>
</template>
    
<script setup>
import test1Comp from '@/components/david-comp/testComp/test1Comp.vue';
import { onMounted } from 'vue';
class testA {
    constructor(ele) {
        console.log("ele: ", ele)
    }
}
onMounted(() => {
    console.log('Go')
    const A = new testA(document.getElementById('img1'))

})

</script>
    
<style scoped>
.testDiv {
    margin-top: 300px;
    background-color: black;
    animation-name: aaa;
    animation-duration: 4s;
    width: 300px;
    height: 300px;
}

@keyframes aaa {
    from {
        background-color: black;
    }

    to {
        background-color: yellow;
    }
}

.Myimg {
    animation-name: imga;
    animation-duration: 4s;
    transition: width 4s;
    width: 300px;
    height: 300px;
}

@keyframes imga {
    25% {
        rotate: 180;
    }
}
</style>