<template>
  <div class="grid-1-5">
    <img v-if="productA.Image" :src="productA.Image" alt="" width="80px" height="80px">
    <img v-else src="/public/favicon.ico" alt="" width="80px" height="80px">
    <h2>{{productA.ServiceName}}</h2>
<<<<<<< HEAD
    <p>{{productA.Description}}</p>
    <h3><span class="uppercase">${{productA.Price}}</span></h3>
    <div>
        <a v-if="showbtn === 'findSubs'" href="#/" class="button1" @click="emits('customAdd', productA.ServiceId)">訂閱</a>
        <a v-if="showbtn === 'findMyCreate'" href="#" class="button2" @click="emits('customOpen', productA.ServiceId)">修改</a>
        <a v-if="showbtn === 'findMyCreate'" href="#" class="button1" @click="emits('customDelete', productA.ServiceId)">刪除</a>
=======
    <h3><span class="uppercase">${{productA.Price}}</span></h3>
    <p>{{productA.Description}}</p>
    <div>
        <a v-if="showbtn === '1'" href="#/" class="button3" @click="emits('customAdd', productA.ServiceId)">訂閱</a>

        <a v-if="showbtn === 'findMyCreate'" href="#" class="button2" @click="emits('customDelete', productA.ServiceId)">修改</a>

        <a v-if="showbtn === 'findMyCreate'" href="#" class="button1" @click="emits('customDelete', productA.ServiceId)">刪除</a>

>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
        <a v-if="showbtn === 'findMySubsById'" href="#/" class="button1" @click="emits('customCancel', productA.ServiceId)">取消訂閱</a>
    </div>
  </div>
</template>
    
<script setup>
<<<<<<< HEAD
import { onMounted } from 'vue';

    const props = defineProps(["productA","showbtn"]);
    const emits = defineEmits(["customDelete","customAdd","customCancel","customOpen"]);
    
=======
    const props = defineProps(["productA","showbtn"]);
    const emits = defineEmits(["customDelete","customAdd","customCancel"]);
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
</script>
    
<style scoped>
@import url(https://fonts.googleapis.com/css?family=Lato:400,700,900);

html {
  box-sizing: border-box;
  height: 100%;
}
*,
*:before,
*:after {
  box-sizing: inherit;
}
body {
  background-color: rgb(33, 31, 35);
  background-repeat: no-repeat;
  color: #fff;
  font-family: "Lato", sans-serif;
  font-size: 100%;
  min-height: 100%;
  line-height: 1.5;
  padding: 2.5em 0;
}
.container {
  margin: 0 auto;
  width: 90%;
  max-width: 1200px;
}
.group:after {
  content: "";
  display: table;
  clear: both;
}
.grid-1-5 {
  border: 2px solid #5d4e65;
  min-height: 400px;
  padding: 1.25em;
  position: relative;
  text-align: center;
  transition: all 0.2s ease-in-out;

  @media screen and (min-width: 700px) {
    & {
      float: left;
      width: 50%;
    }
    &:nth-child(odd) {
      clear: left;
    }
  }

  @media screen and (min-width: 800px) {
    & {
      width: 66.3333333%;
    }
    &:nth-child(3n + 1) {
      clear: left;
    }
    &:nth-child(odd) {
      clear: none;
    }
  }

  @media screen and (min-width: 1120px) {
    & {
      width: 20%;
    }
    &:nth-child(odd),
    &:nth-child(3n + 1) {
      clear: none;
    }
  }
}
.grid-1-5:hover {
  background-color: rgb(83, 69, 91);
  border-top: 2px solid #ec7a37;
  border-bottom: 2px solid #ff4f69;
  box-shadow: 0px 0px 10px 0px rgba(50, 50, 50, 1);
  transform: scale(1.025);
  z-index: 2;

  &:before,
  &:after {
    content: "";
    position: absolute;
    background-color: rgb(246, 125, 53);
    top: -2px;
    bottom: -2px;
    width: 2px;
  }

  &:before {
    left: -2px;
  }
  &:after {
    right: -2px;
  }

  & .button {
    background-color: rgb(238, 122, 54);
  }
}
h2,
h3,
p,
ul {
  margin: 0;
}
h2 {
  font-size: 1em;
  font-weight: 400;
  margin: 0 0 0.5em;
}
h3 {
  font-size: 1.5em;
  letter-spacing: 0.0625em;
  margin: 0 0 0.3333333333333333em;
}
p {
  font-size: 0.875em;
}
p,
ul {
  margin: 0 0 1.5em;
}
ul {
  color: #796583;
  font-size: 0.75em;
  list-style-type: none;
  padding: 0;

  li {
    margin: 0 0 0.8333333333333333em;
  }
}

.button1 {
  background-color: #9c83aa;
  border-radius: 20px;
  color: #fff;
  font-size: 1em;
  font-weight: 700;
  padding: 0.75em 1.5em;
  position: absolute;
  bottom: 1.25em;
  left: 50%;
  margin-left: -60px;
  text-decoration: none;
  width: 120px;
}

.button2 {
  background-color: #9c83aa;
  border-radius: 20px;
  color: #fff;
  font-size: 1em;
  font-weight: 700;
  padding: 0.75em 1.5em;
  position: absolute;
  bottom: 5em;
  left: 50%;
  margin-left: -60px;
  text-decoration: none;
  width: 120px;
}

.button3 {
  background-color: #9c83aa;
  border-radius: 20px;
  color: #fff;
  font-size: 1em;
  font-weight: 700;
  padding: 0.75em 1.5em;
  position: absolute;
  bottom: 8.75em;
  left: 50%;
  margin-left: -60px;
  text-decoration: none;
  width: 120px;
}
.uppercase,
.button,
h2 {
  text-transform: uppercase;
}
sup,
.small {
  font-size: 0.6125em;
}

.pricing-button {
  border: 1px solid #9dd1ff;
  border-radius: 10px;
  color: #348EFE;
  display: inline-block;
  margin: 5px 0;
  padding: 15px 35px;
  text-decoration: none;
  transition: all 150ms ease-in-out;
}

</style>