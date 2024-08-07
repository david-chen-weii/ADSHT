
import router from "./router/router.js"
import { createApp } from 'vue'
import App from './App.vue'
import FontAwesomeIcon from './plugins/fontawesome.js'
import 'semantic-ui-css/semantic.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import 'animate.css';

createApp(App)
    .use(router)
    .component('font-awesome-icon', FontAwesomeIcon)
    .mount('#app')
