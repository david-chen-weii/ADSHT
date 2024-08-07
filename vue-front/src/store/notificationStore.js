import { ref } from 'vue';

const unreadCount = ref(0);

const setUnreadCount = (count) => {
    unreadCount.value = count;
};

const incrementUnreadCount = () => {
    unreadCount.value++;
};

const decrementUnreadCount = () => {
    unreadCount.value--;
};

export default {
    unreadCount,
    setUnreadCount,
    incrementUnreadCount,
    decrementUnreadCount,
};
