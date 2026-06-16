import { getToken, setToken, removeToken } from '@/utils/auth'
import axios from 'axios'

const state = {
  token: getToken(),
  userInfo: {}
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_USER_INFO: (state, userInfo) => {
    state.userInfo = userInfo
  }
}

const actions = {
  login({ commit }, userInfo) {
    return new Promise((resolve, reject) => {
      axios.post('/auth/login', userInfo).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.data.token)
        setToken(data.data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  getUserInfo({ commit }) {
    return new Promise((resolve, reject) => {
      axios.get('/auth/info').then(response => {
        const { data } = response
        commit('SET_USER_INFO', data.data)
        resolve(data.data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  logout({ commit }) {
    commit('SET_TOKEN', '')
    commit('SET_USER_INFO', {})
    removeToken()
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}