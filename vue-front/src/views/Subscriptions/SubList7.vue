<template>
<<<<<<< HEAD
    <h1>你已訂閱項目</h1>
        <div class="container group">
            <FindSubsCompo2 
                v-for="product in products" 
                :key="product.ServiceId"  :productA="product" :showbtn 
                @customDelete="callRemove" @customAdd="callAdd"  @customCancel="callCancel">
            </FindSubsCompo2>
        </div>
</template>

<script setup>
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

    function callFind(page) {
//計算分頁
        if (page) {
            start.value = (page - 1) * rows.value;
            current.value = page;
        } else {
            start.value = 0;
            current.value = 1;
    }

    let request = {
        "start": start.value,
        "max": rows.value,
        "dir": false,
        "order": "price",
    };

    axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
    axiosapi.post("/ajax/pages/servicesInfo/findMySubsById").then(function (response) {
      console.log("response:", response);
      products.value = response.data.ServList;
      showbtn.value = "findMySubsById";
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
=======
        <h1>你已訂閱項目</h1>
    <!-- <Paginate :first-last-button="true" first-button-text="&lt;&lt;" last-button-text="&gt;&gt;" prev-text="&lt;" next-text="&gt;" :page-count="pages" :initial-page="current" v-model="current"
          :click-handler="callFind">
</Paginate> -->
<div class="container group">
<FindSubsCompo2 v-for="product in products" :key="product.ServiceId" :productA="product" :showbtn @customDelete="callRemove" @customAdd="callAdd" @customCancel="callCancel"></FindSubsCompo2>
</div>
</template>

<script setup>
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

function callFind(page) {
//計算分頁
if (page) {
  start.value = (page - 1) * rows.value;
  current.value = page;
} else {
  start.value = 0;
  current.value = 1;
}

let request = {
  "start": start.value,
  "max": rows.value,
  "dir": false,
  "order": "price",
};
axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
axiosapi.post("/ajax/pages/servicesInfo/findMySubsById").then(function (response) {
  console.log("response:", response);
  products.value = response.data.ServList;
  showbtn.value = "findMySubsById";
  // total.value = response.data.count;
  // pages.value = Math.ceil(total.value / rows.value);
  // lastPageRows.value = total.value % rows.value;
  // setTimeout(function() {
  //     Swal.close();
  // }, 500);
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
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
</script>

<style scoped>

</style>