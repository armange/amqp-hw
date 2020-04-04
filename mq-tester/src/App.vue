<template>
  <div id="app">
    <MessageHeader/>
    <MessageContainer/>
  </div>
</template>

<script>
import MessageHeader from './components/MessageHeader.vue'
import MessageContainer from './components/MessageContainer.vue'
import './css/app.css'

export default {
  name: 'App',
  components: {
    MessageHeader,
    MessageContainer,
  }
}
</script>