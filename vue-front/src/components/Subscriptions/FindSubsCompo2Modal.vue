<template>

<div ref="exampleRef" class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">


              <div class="modal-header">
                  <h1 class="modal-title fs-5" id="exampleModalLabel">修改你提供的訂閱項目</h1>
                  <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
              </div>
              <div class="modal-body">
                <div class="container group">
                  <!-- <div class="grid-1-5"> -->
                    <div>
                      <img v-if="serviceImage" :src="serviceImage" alt="" width="80px" height="80px" @dblclick="triggerFileInput" style="cursor: pointer">
                      
                      <img v-else-if="modelValue.Image" :src="modelValue.Image" alt="" width="80px" height="80px" @dblclick="triggerFileInput" style="cursor: pointer">
                      
                      <img v-else src="/public/favicon.ico" alt="" width="80px" height="80px" @dblclick="triggerFileInput">
                      <br><br><br>
                      <table>
                      <tr>
                            <td>訂閱名稱 : </td>
                            <td><input type="text" :value="modelValue.ServiceName" @input="doinput('ServiceName', $event)"></td>
                      </tr>
                      <tr>
                            <td>訂閱內容: </td>
                            <td><input type="text" :value="modelValue.Description" @input="doinput('Description', $event)"></td>
                      </tr>
                      <tr>
                            <td>價格 : </td>
                            <td><input type="text" :value="modelValue.Price" @input="doinput('Price', $event)"></td>
                        </tr>
                      </table>
                      <!-- <a class="button2" >訂閱</a>
                      <a class="button1" >取消訂閱</a> -->
                    </div>
                  <!-- </div> -->
                </div>
              </div>
              <div class="modal-footer">
                <div class="mb-3">
                  <span class="icon">
                    <input  class="form-control d-none"  type="file" id="postImage" @change="onFileChange" ref="fileInput">
                  </span>

                  <span v-if="serviceImage != serviceImageTemp ">還原圖片
                  <font-awesome-icon :icon="['fas', 'trash']" class="icon-large" v-if="serviceImage"      @click="clearImage"/></span>
                </div>
                    <button type="button" class="btn btn-primary" @click="emits('customUpdate')">送出修改</button>
              </div>
        </div>
    </div>
</div>
</template>
    
<script setup>
    const props = defineProps(["modelValue","showbtn"]);
    const emits = defineEmits(["customUpdate", "update:modelValue"]);

    import bootstrap from "bootstrap/dist/js/bootstrap.bundle.min.js"
    import { ref, onMounted } from 'vue'

    const exampleRef = ref(null);
    const exampleModal = ref(null);
    const serviceImage = ref("");
    const fileInput = ref(null);
    const serviceImageTemp = ref("");

    onMounted(function() {
        exampleModal.value = new bootstrap.Modal(exampleRef.value);
    });

    function doinput(key, event) {
      console.log("event.target.value",event.target.value);
      console.log("key",key);
        emits('update:modelValue', {
            ...props.modelValue,
            [key]: event.target.value
        });
    }

    function showModal() {
        exampleModal.value.show();

    }
    function hideModal() {
        exampleModal.value.hide();
    }

    defineExpose({
        showModal, hideModal,
    });

    const onFileChange = (e) => {
      const file = e.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          serviceImage.value = e.target.result;
          serviceImageTemp.value = props.modelValue.Image
            emits('update:modelValue', {
            ...props.modelValue,
            Image : serviceImage.value
          });
        console.log("serviceImage.value",serviceImage);
        };
        reader.readAsDataURL(file);
    }
};

const triggerFileInput = () => {
    fileInput.value.click();
};

//清除圖片按鈕
const clearImage = () => {
  serviceImage.value = serviceImageTemp.value;
};


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