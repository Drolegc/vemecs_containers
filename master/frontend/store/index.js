export default {
  state: {
    vemecs: [],
  },
  mutations: {
    mutateVemecs(state, vemecs) {
      state.vemecs = vemecs;
    },
  },
  actions: {
    setVemecs({
      commit
    }, vemecs) {
      commit("mutateVemecs", vemecs);
    }
  },
  getters: {
    vemecs(state) {
      return state.vemecs;
    }
  },
  handler: () => {}
};
