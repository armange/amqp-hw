<template>
  <div id="message-receiver" class="message-container-item">
    <h4 class="title">Message Receiving</h4>
    <form>
      <label for="queue">Queue</label>
      <div class="queue-container internal-div">
        <input 
          id="queue"
          type="text"
          v-bind:class="['form-control', 'input-text', 'queue-name', hasNoQueue ? 'is-invalid' : '']"
          @input="cleanAlerts"
          v-model="queue"
          required/>
        <button 
          @click="getMessage"
          type="button"
          class="btn btn-primary queue-button">Receive</button>
      </div>
    </form>
    <label for="message-sender">Message</label>
    <textarea 
        id="message-sender"
        style="margin-bottom: 26px;"
        class="form-control text-area"
        disabled
        v-model="message"></textarea>
    <hr />
    <h5>Last received</h5>
    <div class="last-item-container internal-div">
      <input v-model="lastQueue" type="text" class="form-control input-text last-queue" disabled/>
      <input v-model="lastMessage" type="text" class="form-control input-text last-message" disabled/>
    </div>
  </div>
</template>

<script>
import MessageQueueService from '../services/MessageQueueService.js'

export default {
  name: "MessageReceiver",
  data() {
    return {
      errors: [],
      warns: [],
      infos: [],
      successArray: [],
      message: "",
      queue: "",
      requestedQueue: "",
      lastQueue: "",
      lastMessage: ""
    }
  },
  methods: {
    getMessage() {
      if (this.checkForm()) {
        MessageQueueService.getMessage(this.queue, 
        this.setLastMessage, 
        error => {
          if (error && error.response && error.response.data) {
            if (error.response.data.status == 400) {
              this.infos.push(error.response.data.message)

              console.info({'info':error})

              this.emitInfo()
            } else if (error.response.data.status == 502) {
              this.warns.push(error.response.data.message)
              
              console.warn({'info':error})

              this.emitWarn()
            } else {
              this.errors = []
              this.errors.push(error)
              this.emitError()
            }
          } else if (error.message == 'Network Error') {
            this.warns.push('The backend server was not found')
              
            console.warn({'info':error})

            this.emitWarn()
          } else {
            this.errors = []
            this.errors.push(error)
            this.emitError()
          }
        })
      }
    },

    setLastMessage(response) {
      this.successArray.push('Message received')
      this.emitSuccess()
      this.lastQueue = this.requestedQueue
      this.lastMessage = this.message
      this.requestedQueue = this.queue
      this.message = response.data.message
    },

    checkForm() {
      this.cleanAlerts()

      if (this.queue) {
        return true;
      }

      this.warns.push('The queue is required.');
      this.emitWarn();

      return false;
    },

    emitSuccess() {
      this.$emit('success', this.successArray)
      this.successArray = []
    },

    emitError() {
      this.$emit('errors', this.errors)
      this.errors = []
    },

    emitWarn() {
      this.$emit('warns', this.warns)
      this.warns = []
    },

    emitInfo() {
      this.$emit('infos', this.infos)
      this.infos = []
    },

    cleanAlerts() {
      this.successArray = []
      this.warns = []
      this.errors = []
      this.infos = []
    }
  },
  computed: {
    hasNoQueue() {
      return this.warns.length && !this.queue;
    },
  }
};
</script>