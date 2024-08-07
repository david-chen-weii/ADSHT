import Management from "./Management.vue"

export default [
    {
        name: "account-management-link",
        path: "/views/management",
        component: Management,
        meta: { requiresStatus2: true },
    },
]