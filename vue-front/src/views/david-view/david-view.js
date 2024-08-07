import Cart from "./Cart.vue";
import Shop from "./Shop.vue";
import Search from "./ShopSearch.vue";
import Test from "./testView/test1.vue";
import Test2 from "./testView/test2.vue";
import forget from "./account/Forget.vue"
import Orderlist from "./Orderlist.vue";
export default [
    { name: "david-search-link", path: "/views/david-search-page", component: Search },
    { name: "david-shop-link", path: "/views/shop", component: Shop },
    { name: "david-cart-link", path: "/views/cart", component: Cart },
    { name: "david-test-link", path: "/test", component: Test },
    { name: "david-test2-link", path: "/test2", component: Test2 },
    { name: "david-forget-link", path: "/Reset/:pathMatch(.*)*", component: forget },
    { name: "orderlist-forget-link", path: "/views/orderlist", component: Orderlist },
]