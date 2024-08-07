<template>
<<<<<<< HEAD
        <h1>管理建立的訂閱項目</h1>
<div class="container group">
    <FindSubsCompo2 
        v-for="product in products" 
        :key="product.ServiceId" 
        :productA="product" 
        :showbtn 
        @custom-delete="callRemove" @custom-add="callAdd" @custom-cancel="callCancel" @custom-open="openModal">
    </FindSubsCompo2>


    <FindSubsCompo2Modal
        ref="subsModal"
        v-model="product1"
        @custom-update="callModify"
        >
    </FindSubsCompo2Modal>

=======
        <h1>管理你的訂閱項目</h1>
    <!-- <Paginate :first-last-button="true" first-button-text="&lt;&lt;" last-button-text="&gt;&gt;" prev-text="&lt;" next-text="&gt;" :page-count="pages" :initial-page="current" v-model="current"
          :click-handler="callFind">
</Paginate> -->
<div class="container group">
<FindSubsCompo2 v-for="product in products" :key="product.ServiceId" :productA="product" :showbtn @customDelete="callRemove" @customAdd="callAdd" @customCancel="callCancel"></FindSubsCompo2>
>>>>>>> fb2647b3e54d38c49db58fac26fd9c7bd0fb5764
</div>
</template>

<script setup>
<<<<<<< HEAD
    const props = defineProps(["user"]);
    import FindSubsCompo2 from '@/components/Subscriptions/FindSubsCompo2.vue'
    import FindSubsCompo2Modal from '@/components/Subscriptions/FindSubsCompo2Modal.vue';
    import axiosapi from '@/plugins/axios.js';
    import Swal from 'sweetalert2';
    import { useRoute } from 'vue-router';
// import Paginate from 'vuejs-paginate-next';
    import { ref, onMounted } from 'vue';

    const products = ref([]);
    const product1 = ref({});
    const showbtn = ref("")
    const subsModal = ref(null);
    const route = useRoute();

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


    function openModal(id) {
        callFindById(id);
        subsModal.value.showModal();
    }


    function callFind() {
    // //計算分頁
    // if (page) {
    //   start.value = (page - 1) * rows.value;
    //   current.value = page;
    // } else {
    //   start.value = 0;
    //   current.value = 1;
    // }
        console.log("user：",route.query.id);
        console.log("user：",route.query.name);
        let request = {
            "start": start.value,
            "max": rows.value,
            "dir": false,
            "order": "price",
        };
        axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
        axiosapi.post("/ajax/pages/servicesInfo/findMyCreate").then(function (response) {
        console.log("response:", response);
        products.value = response.data.ServList;
        showbtn.value = "findMyCreate";
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
                        window.location.reload();
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


    function callFindById(id) {
        Swal.fire({
            text: "處理中.....",
            allowOutsideClick: false,
            showConfirmButton: false
        });

        axiosapi.get(`/ajax/pages/servicesInfo/findOne/${id}`).then(function (response) {
            product1.value = response.data.list[0];

            setTimeout(function () {
                Swal.close();
            }, 500);
        }).catch(function (error) {
            console.log("error", error);
            Swal.fire({
                text: "查詢失敗：" + error.message,
                icon: "error"
            });
        });
    }



    function callModify() {
        Swal.fire({
            text: "執行中......",
            allowOutsideClick: false,
            showConfirmButton: false,

        });
        console.log(product1.value);
        let request = {
            "serviceName": product1.value.ServiceName,
            "price": product1.value.Price,
            "description": product1.value.Description,
            "image" : product1.value.Image
            };
        //  if (product1.value.ServiceName == "") {
        //      product1.value.ServiceName = null;
        //  }
        //  if (product1.value.Price == "") {
        //      product1.value.Price = null;
        //  }
        //  if (product1.value.Description == "") {
        //      product1.value.Description = null;
        //  }
        //  if (product1.value.Image == "") {
        //      product1.value.Image = null;
        //  }
        axiosapi.put(`/ajax/pages/servicesInfo/update/${product1.value.ServiceId}`, request).then   (function (response) {
            console.log("response", response);
            if (response.data.success) {
                Swal.fire({
                    icon: "success",
                    text: response.data.message,
                }).then(function (result) {
                    subsModal.value.hideModal();
                    window.location.reload();
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
                text: "修改錯誤：" + error.message,
            });
        });

    }

=======
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
axiosapi.defaults.headers['session-id'] = sessionStorage.getItem("Session-Id");
axiosapi.post("/ajax/pages/servicesInfo/findMyCreate").then(function (response) {
  console.log("response:", response);
  products.value = response.data.ServList;
  showbtn.value = "findMyCreate";
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