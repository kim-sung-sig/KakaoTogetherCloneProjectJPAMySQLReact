<script setup>
import { RouterLink, RouterView } from 'vue-router'
import HelloWorld from './components/HelloWorld.vue'
import axios from 'axios'
</script>

<template>
  {{ count }}
  <h1 :class = "{active : isActive}"> 5 </h1>

  <div v-for="dreamBoard in dreamBoards" :key="dreamBoard" style="display: block;">
    <h4>{{ dreamBoard.a }} 상품들</h4>
    <p>{{ dreamBoard.b }} 만원</p>
  </div>

</template>
<script>
export default {
  data() {
    return {
      count: 0,
      isActive : false,
      dreamBoards : [
        {
          'a' : 1,
          'b' : 2
        },
        {
          'a' : 3,
          'b' : 4
        },
        {
          'a' : 5,
          'b' : 6
        },
      ]
    }
  }
}
  axios.get("/api/v1/dreamBoards", {
    params : {
      'size' : 3,
    }
  })
  .then(res => {
    console.log(res);
  })
  .catch(e => console.log(e))
</script>

<style scoped>
  .active {
    font-size: 50px;
  }
</style>
