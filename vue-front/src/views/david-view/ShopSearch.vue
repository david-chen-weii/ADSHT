<template>
  <div class="container">
    <div class="search-section">
      <input type="text" placeholder="Search shop" v-model="searchQuery" @input="searchUser" class="search-input"
        id="searchShop">
      <select class="filter-select" @input="doInput($event.target.value)">
        <option value="shop">shop</option>
        <option value="product">product</option>
        <!-- Add more filter options as needed -->
      </select>
      <!--查詢訂單-->
      <router-link to="/views/orderlist">
        <button class="btn btn-light"><font-awesome-icon :icon="['fas', 'list']" />查看訂單</button>
      </router-link>
    </div>
    <div class="shop-grid">
      <Shop v-for="(shop, index) in shops" :key="index" :shop="shop"></Shop>
    </div>
    <div>
      <button v-show="show" v-if="ownShopId == 0" type="button" class="btn btn-primary post-button" data-bs-toggle="modal"
        data-bs-target="#postModal">
        Create Your Shop
      </button>
      <RouterLink v-else :to="{ name: 'david-shop-link', query: { id: ownShopId } }" class="btn btn-primary post-button">
        Your
        Shop</RouterLink>
    </div>
    <ShopModal></ShopModal>
  </div>
</template>

<script setup>
import ShopModal from '@/components/david-comp/ShopCreateModal.vue';
import Shop from '@/components/david-comp/ShopSearchComp.vue';
import axiosapi from '@/plugins/axios';
import { onMounted, ref } from 'vue';
const ownShopId = ref(0)
const show = ref(true)
const shops = ref([])
const products = ref([])
const searchThing = ref('shop')

// Function to fetch shops
const fetchShops = async () => {
  try {
    let sessionId = sessionStorage.getItem("Session-UserInfoId") || 0;
    axiosapi.defaults.headers["session-user-info-id"] = sessionId;
    const response = await axiosapi.post("/ajax/shop/shop").then((response) => {
      shops.value = response.data.shops;
      if (response.data.ownShop) {
        ownShopId.value = response.data.ownShop;
      }
      if (response.data.user) {
        console.log(response.data.user);
        show.value = false;
      }
      console.log(response.data.shopId);
      console.log("ownShopId.value = ", ownShopId.value)

    });
  } catch (error) {
    console.error('Failed to fetch shops:', error); // Handle error
  }
};

function searchShop(event) {
  if (searchThing.value == "shop") {
    var request = {
      content: event.target.value,
      order: "",
      class: "",
      page: "",
      showNum: "",
    }

    axiosapi.post('ajax/shop/findShops', request).then((response) => {
      shops.value = response.data.shop;
      console.log(shops.value)
    })
  } else {
    var request = {
      content: event.target.value,
      order: "",
      class: "",
      page: "",
      showNum: "",
    }
    axiosapi.post('ajax/shop/findProducts', request).then((response) => {
      products.value = response.data.products;
      console.log("products")
      console.log(products.value)
    })
  }
}
function doInput(thing) {
  console.log(thing);
  searchThing.value = thing
}
// Fetch shops when component is mounted
onMounted(() => {
  fetchShops();
  document.getElementById('searchShop').addEventListener('input', searchShop)
});

</script>
<style>
/* Container for the whole page */
.container {
  padding: 20px;
}

/* Search and filter section */
.search-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-input {
  width: 70%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.filter-select {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

/* Grid layout for shop items */
.shop-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

/* Style for the floating action button */
.post-button {
  position: fixed;
  right: 20px;
  bottom: 20px;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 50px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

/* Adjust button styles */
.btn-primary {
  background-color: #007bff;
  color: #fff;
  border: none;
  transition: background-color 0.3s;
}

.btn-primary:hover {
  background-color: #0056b3;
}
</style>