<template>
          <Paginate :first-last-button="true" first-button-text="&lt;&lt;" last-button-text="&gt;&gt;" prev-text="&lt;" next-text="&gt;" :page-count="pages" :initial-page="current" v-model="current"
                :click-handler="callFind">
    </Paginate>
  <div class="background">
  <div class="container">
    <div class="panel pricing-table">
    <FindSubsCompo v-for="product in products" :key="product.ServiceId" :productA="product" @customDelete="callRemove"></FindSubsCompo>
    </div>
    </div>
  </div>
</template>
    
<script setup>
import FindSubsCompo from '@/components/Subscriptions/FindSubsCompo.vue'
import axiosapi from '@/plugins/axios.js';
import Swal from 'sweetalert2';
import Paginate from 'vuejs-paginate-next';
import { ref, onMounted } from 'vue';
const products = ref([]);

//分頁 start
const total = ref(0);   //總資料筆數
const pages = ref(0);   //總共頁數
const current = ref(0); //目前頁碼
const rows = ref(4);    //最多抓幾筆資料
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

    axiosapi.post("/ajax/pages/servicesInfo/find",request).then(function (response) {
        console.log("response:", response);
        products.value = response.data.ServList;
        total.value = response.data.count;
        pages.value = Math.ceil(total.value / rows.value);
        lastPageRows.value = total.value % rows.value;
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
</script>
    
<style scoped>
template {
  box-sizing: border-box;
  font-family: 'Open Sans', sans-serif;
}

*, *:before, *:after {
  box-sizing: inherit;
}

.background {
  padding: 0 25px 25px;
  position: relative;
  width: 100%;
}

.background::after {
  content: '';
  background: #60a9ff;
  background: -moz-linear-gradient(top, #60a9ff 0%, #4394f4 100%);
  background: -webkit-linear-gradient(top, #60a9ff 0%,#4394f4 100%);
  background: linear-gradient(to bottom, #60a9ff 0%,#4394f4 100%);
  filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#60a9ff', endColorstr='#4394f4',GradientType=0 );
  height: 350px;
  left: 0;
  position: absolute;
  top: 0;
  width: 100%;
  z-index: 1;
}

@media (min-width: 900px) {
  .background {
    padding: 0 0 25px;
  }
}

.container {
  margin: 0 auto;
  padding: 50px 0 0;
  max-width: 960px;
  width: 100%;
}

.panel {
  background-color: #fff;
  border-radius: 10px;
  padding: 15px 25px;
  position: relative;
  width: 100%;
  z-index: 10;
}

.pricing-table {
  box-shadow: 0px 10px 13px -6px rgba(0, 0, 0, 0.08), 0px 20px 31px 3px rgba(0, 0, 0, 0.09), 0px 8px 20px 7px rgba(0, 0, 0, 0.02);
  display: flex;
  flex-direction: column;
}

@media (min-width: 900px) {
  .pricing-table {
    flex-direction: row;
  }
}

.pricing-table * {
  text-align: center;
  text-transform: uppercase;
}

.pricing-plan {
  border-bottom: 1px solid #e1f1ff;
  padding: 25px;
}

.pricing-plan:last-child {
  border-bottom: none;
}

@media (min-width: 900px) {
  .pricing-plan {
    border-bottom: none;
    border-right: 1px solid #e1f1ff;
    flex-basis: 100%;
    padding: 25px 50px;
  }

  .pricing-plan:last-child {
    border-right: none;
  }
}

.pricing-img {
  margin-bottom: 25px;
  max-width: 100%;
}

.pricing-header {
  color: #888;
  font-weight: 600;
  letter-spacing: 1px;
}

.pricing-features {
  color: #016FF9;
  font-weight: 600;
  letter-spacing: 1px;
  margin: 50px 0 25px;
}

.pricing-features-item {
  border-top: 1px solid #e1f1ff;
  font-size: 12px;
  line-height: 1.5;
  padding: 15px 0;
}

.pricing-features-item:last-child {
  border-bottom: 1px solid #e1f1ff;
}

.pricing-price {
  color: #016FF9;
  display: block;
  font-size: 32px;
  font-weight: 700;
}

.pricing-button {
  border: 1px solid #9dd1ff;
  border-radius: 10px;
  color: #348EFE;
  display: inline-block;
  margin: 25px 0;
  padding: 15px 35px;
  text-decoration: none;
  transition: all 150ms ease-in-out;
}

.pricing-button:hover,
.pricing-button:focus {
  background-color: #e1f1ff;
}

.pricing-button.is-featured {
  background-color: #48aaff;
  color: #fff;
}

.pricing-button.is-featured:hover,
.pricing-button.is-featured:active {
  background-color: #269aff;
} 
</style>