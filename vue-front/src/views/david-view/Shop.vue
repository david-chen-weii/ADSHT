<template>
    <div class="container-sm">
        <div class="row align-items-center">

            <Prodcut v-for="(product, index) in products" :key="product.productId" :product="product"
                @add-to-cart="addToCart" @edit-product="editProduct" :showEditBtn="showbtn"></Prodcut>

        </div>

    </div>
    <button v-if="showbtn" type="button" class="btn btn-primary post-button" data-bs-toggle="modal"
        data-bs-target="#postModal">
        新增商品
    </button>
    <EditProductModal ref="editProductModal"></EditProductModal>
    <ProductModal></ProductModal>

    <img id="car" src="\public\pic2\3081840.png" alt="Car">
</template>
    
<script setup>
import EditProductModal from "@/components/david-comp/EditProductModal.vue";
import Prodcut from "@/components/david-comp/ProductComp.vue";
import ProductModal from "@/components/david-comp/ProductCreateModal.vue";
import axiosapi from '@/plugins/axios';
import Swal from 'sweetalert2';
import { onMounted, ref } from 'vue';
const productCards = ref([])
const shop = ref({});
const products = ref([]);
const shopId = ref('')
const showbtn = ref("")
const editProductModal = ref("");
//設定  
onMounted(() => {
    fetchDate();
})
//商店資訊
const fetchDate = async () => {
    shopId.value = new URLSearchParams(window.location.search).get('id');
    try {
        const response = await axiosapi.get(`/ajax/shop?shopId=${shopId.value}`);
        shop.value = {
            shopName: response.data.shopName,
            shopId: response.data.shopId,
            shopDescription: response.data.shopDescription,
            shopImage: response.data.shopImage,
            owner: response.data.owner,
        }
        products.value = response.data.products;
        showbtn.value = sessionStorage.getItem('Session-Id') === response.data.owner;
    } catch (error) {
        console.error('Error fetching data:', error);
    }

};

//加入購物車
const addToCart = (key, price) => {
    let storedProduct = sessionStorage.getItem('products');
    let productSession = storedProduct ? JSON.parse(storedProduct) : {};

    if (!productSession[key]) {
        productSession[key] = { value: 1, price: price };
        sessionStorage.setItem('products', JSON.stringify(productSession));
        axiosapi.defaults.headers["session-id"] = sessionStorage.getItem("Session-Id");
        axiosapi.post(`/ajax/shop/addToCart/${key}`)
            .then(response => {
                if (response.data.success) {
                    const car = document.querySelector("#car");
                    car.style.display = "block"; // Make sure the element is visible
                    car.classList.add("animate");

                    car.addEventListener("animationend", function () {
                        car.style.display = "none"; // Hide the element after animation ends
                        car.classList.remove("animate"); // Remove the animation class
                    }, { once: true }); // Ensure the event listener is only run once

                    Swal.fire({
                        icon: "success",
                        title: "商品已加入購物車",
                        text: response.data.message,
                        timer: 1000, // 2 seconds before closing
                        showConfirmButton: false
                    }).then(() => {
                        // Optional: additional actions after the alert is closed

                        window.location.reload();
                    });
                } else {
                    Swal.fire({
                        icon: "error",
                        title: "添加到購物車失敗",
                        text: response.data.message,
                        timer: 1000, // 2 seconds before closing
                        showConfirmButton: false
                    }).then(() => {
                        // Optional: additional actions after the alert is closed
                    });
                }
            })
            .catch(error => {
                console.error('Error adding to cart:', error);
            });
    } else {
        Swal.fire({
            icon: "info",
            title: "商品已在購物車中",
            text: "您已經將此商品加入購物車。",
            timer: 1000, // 1 second before closing
            showConfirmButton: false
        });
    }
};

function editProduct(product) {

    editProductModal.value.show(product);
}
//彈出視窗

</script>
    
<style>
.post-button {
    position: fixed;
    right: 20px;
    bottom: 20px;
}

#car {
    display: none;
    width: 20vw;
    /* Adjust size as needed */
    height: 25vh;
    /* Adjust size as needed */
    position: absolute;
    bottom: 0;
    /* Start at the bottom */
    right: 0;
    /* Start at the right */
    z-index: 9999;
}

.animate {
    animation: car 1s forwards;
    /* Duration of 5 seconds for the animation */
}

@keyframes car {
    0% {
        transform: translate(0, 0) rotate3d(0, 1, 0, 180deg);
        /* Start at the bottom right */
    }

    30% {
        transform: translate(-35vw, -45vh)rotate3d(0, 1, 0, 180deg);
        /* Move to the middle */
    }

    50% {
        width: 10vw;
        height: 15vh;
        transform: translate(-30vw, -45vh);
        /* Turn around at the middle */
    }

    70% {
        transform: translate(0vw, -90vh);
        /* Move to the top right */
    }

    100% {
        width: 5vw;
        height: 5vh;
        transform: translate(40vw, -150vh);
        /* Move to the top right */
    }
}
</style>