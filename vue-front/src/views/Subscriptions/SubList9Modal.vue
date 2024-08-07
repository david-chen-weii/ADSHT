<template>
<<<<<<< HEAD
<div  ref="exampleRef" class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">訂閱項目</h5>
=======
<div class="modal fade" id="otherSubsModal" tabindex="-1" aria-labelledby="otherSubsModalLabel">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="otherSubsModalLabel">訂閱項目</h5>
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
                    <div class="container group">
                        <FindSubsCompo2 v-for="product in products" :key="product.ServiceId" :productA="product" :showbtn @customDelete="callRemove" @customAdd="callAdd" @customCancel="callCancel"></FindSubsCompo2>
                    </div>
                </div>
            </div>
        </div>
    </div>
<<<<<<< HEAD
</template>

<script setup>
import FindSubsCompo2 from '@/components/Subscriptions/FindSubsCompo2.vue'
import axiosapi from '@/plugins/axios.js';
import Swal from 'sweetalert2';
import { ref, onMounted } from 'vue';
import bootstrap from "bootstrap/dist/js/bootstrap.bundle.min.js"
const products = ref([]);
const showbtn = ref("");
const exampleRef = ref(null);
const exampleModal = ref(null);

    onMounted(function() {
        exampleModal.value = new bootstrap.Modal(exampleRef.value);
        callFind();
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


function callFind() {
// let request = {
//   "start": start.value,
//   "max": rows.value,
//   "dir": false,
//   "order": "price",
// };
=======

        <!-- <h1>管理你的訂閱項目</h1>
 <div class="container group">
<FindSubsCompo2 v-for="product in products" :key="product.ServiceId" :productA="product" :showbtn @customDelete="callRemove" @customAdd="callAdd" @customCancel="callCancel"></FindSubsCompo2>
</div>  -->
</template>

<script setup>
// const props = defineProps(["user1"]);
import FindSubsCompo2 from '@/components/Subscriptions/FindSubsCompo2.vue'
import axiosapi from '@/plugins/axios.js';
import Swal from 'sweetalert2';
// import Paginate from 'vuejs-paginate-next';
import { ref, onMounted } from 'vue';
const products = ref([]);
const showbtn = ref("")

//分頁 start
const total = ref(0);   //總資料筆數
const pages = ref(0);   //總共頁數
const current = ref(0); //目前頁碼
const rows = ref(99);    //最多抓幾筆資料
const start = ref(0);   //從哪裡開始抓資料
const lastPageRows = ref(0)
//分頁 end


onMounted(function () {
callFind();
});

function callFind() {
// //計算分頁
// if (page) {
//   start.value = (page - 1) * rows.value;
//   current.value = page;
// } else {
//   start.value = 0;
//   current.value = 1;
// }

let request = {
  "start": start.value,
  "max": rows.value,
  "dir": false,
  "order": "price",
};
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
axiosapi.post("/ajax/pages/servicesInfo/findMyCreate").then(function (response) {
  console.log("response:", response);
  products.value = response.data.ServList;
  showbtn.value = "findMyCreate";
<<<<<<< HEAD
=======
  // total.value = response.data.count;
  // pages.value = Math.ceil(total.value / rows.value);
  // lastPageRows.value = total.value % rows.value;
  // setTimeout(function() {
  //     Swal.close();
  // }, 500);
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
}).catch(function (error) {
  console.log("error", error);
  Swal.fire({
      text: "查詢失敗：" + error.message,
      icon: "error"
  });
});
}

function callRemove(id) {
Swal.fire({
  icon: 'question',
  text: '確定要刪除？',
  showCancelButton: true,
  allowOutsideClick: false
}).then(function (result) {
  if (result.isConfirmed) {
      Swal.fire({
          text: "執行中......",
          allowOutsideClick: false,
          showConfirmButton: false,
      });
      if (id) {
          axiosapi.delete("/ajax/pages/servicesInfo/del/" + id).then(function (response) {
              console.log("response", response);
              if (response.data.success) {
                  Swal.fire({
                      icon: "success",
                      text: response.data.message,
                  }).then(function (result) {
                      if (lastPageRows.value == 1 && current.value > 1) {
                          current.value = current.value - 1;
                      }
                      callFind(current.value);
                  });
              } else {
                  Swal.fire({
                      icon: "warning",
                      text: response.data.message,
                  });
              }
          }).catch(function (error) {
              console.log("error", error);
              Swal.fire({
                  icon: "error",
                  text: "刪除錯誤：" + error.message,
              });
          });
      }
  }
});
}

function callCancel(id) {
Swal.fire({
  icon: 'question',
  text: '確定要取消訂閱嗎？',
  showCancelButton: true,
  allowOutsideClick: false
}).then(function (result) {
  if (result.isConfirmed) {
      Swal.fire({
          text: "執行中......",
          allowOutsideClick: false,
          showConfirmButton: false,
      });
      if (id) {
          axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
          axiosapi.delete("/ajax/pages/SubServices/del/" + id).then(function (response) {
              console.log("response", response);
              if (response.data.success) {
                  Swal.fire({
                      icon: "success",
                      text: response.data.message,
                  }).then(function (result) {
                      if (lastPageRows.value == 1 && current.value > 1) {
                          current.value = current.value - 1;
                      }
                      callFind(current.value);
                  });
              } else {
                  Swal.fire({
                      icon: "warning",
                      text: response.data.message,
                  });
              }
          }).catch(function (error) {
              console.log("error", error);
              Swal.fire({
                  icon: "error",
                  text: "刪除錯誤：" + error.message,
              });
          });
      }
  }
});
}

function callAdd(id) {
    Swal.fire({
        text: "訂閱中......",
        allowOutsideClick: false,
        showConfirmButton: false,
    });
    let request = {
        "servInfoId": id
    };
    axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
    console.log(["id", id],["session-id", axiosapi.defaults.headers['session-id']]);
    axiosapi.post("/ajax/pages/SubServices/add", request).then(function (response) {
        console.log("response", response);
        if (response.data.success) {
            Swal.fire({
                icon: "success",
                text: response.data.message,
            });
        } else {
            Swal.fire({
                icon: "warning",
                text: response.data.message,
            });
        }
    }).catch(function (error) {
        console.log("error", error);
        Swal.fire({
            icon: "error",
            text: "新增錯誤：" + error.message,
        });
    });
}
</script>

<style scoped>

</style>