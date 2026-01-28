import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
    let storedUser = {}
    try {
        storedUser = JSON.parse(localStorage.getItem('user') || '{}')
    } catch (e) {
        console.error('Failed to parse user from localStorage', e)
        localStorage.removeItem('user')
    }
    const currentUser = ref(storedUser)
    
    const setUser = (user) => {
        currentUser.value = user
        localStorage.setItem('user', JSON.stringify(user))
    }
    
    const clearUser = () => {
        currentUser.value = {}
        localStorage.removeItem('user')
    }
    
    return { currentUser, setUser, clearUser }
})
