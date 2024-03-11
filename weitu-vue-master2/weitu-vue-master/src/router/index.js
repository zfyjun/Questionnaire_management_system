import Vue from 'vue'
import VueRouter from 'vue-router'
// import store from "@/store";


Vue.use(VueRouter)

const routes = [
    {
        path: '/helloWorld',
        name: 'HelloWorld',
        component: () => import('../components/HelloWorld.vue')
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue')
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('../views/Register.vue')
    },

    {
        path: '/manage',
        name: 'Manage',
        component: () => import('../views/Manage.vue'),
        // redirect:"/student",
        children:[
            {
                path: '/home',
                name: 'Home',
                component: () => import('../views/Home.vue')
            },
            {
                path: '/student',
                name: 'Student',
                component: () => import('../views/Student.vue')
            },
            {
                path: '/file',
                name: 'File',
                component: () => import('../views/File.vue')
            },
            {
                path: '/isDelete',
                name: 'IsDelete',
                component: () => import('../views/IsDelete.vue')
            },
            {
                path: '/listEdit',
                name: 'ListEdit',
                component: () => import('../views/ListEdit.vue')
            },
            {
                path: '/listPublish',
                name: 'ListPublish',
                component: () => import('../views/ListPublish.vue')
            },
            {
                path: '/listManagement',
                name: 'ListManagement',
                component: () => import('../views/ListManagement.vue')
            },
            {
                path: '/analysis',
                name: 'analysis',
                component: () => import('../views/Analysis.vue')
            },
        ]
    },
    {
        path: '/list',
        name: 'list',
        component: () => import('../views/List.vue')
    },
    {
        path: '/registerWeb',
        name: 'RegisterWeb',
        component: () => import('../views/RegisterWeb.vue')
    },
    {
        path: '/front',
        name: 'Front',
        component: () => import('../views/front/Front.vue'),
        // redirect:"/student",
        children:[
            {
                path: 'home',
                name: 'Home',
                component: () => import('../views/front/Home.vue')
            },
            {
                path: 'studentList',
                name: 'StudentList',
                component: () => import('../views/front/StudentList.vue')
            },
            {
                path: 'paper',
                name: 'Paper',
                component: () => import('../views/front/Paper.vue')
            },
            {
                path: 'information',
                name: 'Information',
                component: () => import('../views/front/Information.vue')
            },
            {
                path: 'list',
                name: 'List',
                component: () => import('../views/List.vue')
            },
        ]
    },
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})




export default router
