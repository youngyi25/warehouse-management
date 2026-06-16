import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    user
  },
  state: {
    theme: localStorage.getItem('warehouse_theme') || 'dark'
  },
  mutations: {
    SET_THEME(state, theme) {
      state.theme = theme
      localStorage.setItem('warehouse_theme', theme)
    }
  },
  actions: {
    toggleTheme({ commit, state }) {
      const newTheme = state.theme === 'dark' ? 'light' : 'dark'
      commit('SET_THEME', newTheme)
    }
  }
})
