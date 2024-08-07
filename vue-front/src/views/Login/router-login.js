import Login from "./Login.vue";
import RegisterPage from "./RegisterPage.vue";
import ForgetPassword from "./ForgetPassword.vue"
export default [
    { name: "login-text-link", path: "/views/login", component: Login },
    { name: "registerPage-text-link", path: "/views/registerPage", component: RegisterPage },
    { name: "forgetPassword-link", path: "/views/forgetPassword", component: ForgetPassword },
]