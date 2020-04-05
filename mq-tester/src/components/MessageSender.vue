<template>
  <div id="message-sender" class="message-container-item">
    <h4 class="title">Message Sending</h4>
    <form>
      <label for="message-sender">Message</label>
      <textarea 
        id="message-sender"
        @input="cleanAlerts"
        v-bind:class="['form-control', 'text-area', hasNoMessage ? 'is-invalid' : '']"
        v-model="message"></textarea>
      <label for="queue-sender">Queue</label>
      <div class="queue-container internal-div">
        <input 
          id="queue-sender"
          @input="cleanAlerts"
          type="text" 
          v-bind:class="['form-control', 'input-text', 'queue-name', hasNoQueue ? 'is-invalid' : '']"
          v-model="queue"/>
        <button 
          type="button" 
          @click="sendMessage"
          class="btn btn-primary queue-button">Send</button>
      </div>
    </form>
    <hr />
    <div class="last-item-container internal-div">
      <div class="internal-div">
        <label for="last-queue-sent">Last Queue</label>
        <input id="last-queue-sent" v-model="lastQueue" type="text" class="form-control input-text last-queue" disabled/>
      </div>
      <div class="internal-div">
        <label for="last-message-sent">Last Message</label>
        <input id="last-message-sent" v-model="lastMessage" type="text" class="form-control input-text last-message" disabled/>
      </div>
    </div>
  </div>
</template>

<script>
import MessageQueueService from '../services/MessageQueueService.js'

export default {
  name: "MessageSender",
  data() {
    return {
      warns: {},
      errors: [],
      infos: [],
      successArray: [],
      message: "",
      queue: "",
      lastQueue: "",
      lastMessage: "",
      attemptToSend: false
    }
  },
  methods: {
    sendMessage() {
      this.attemptToSend=true
      if (this.checkForm()) {
        MessageQueueService.sendMessage(this.queue, this.message, this.setLastMessage, error => {
          if (error && error.response && error.response.data) {
            if (error.response.data.status == 502) {
              this.warns['middleware'] = error.response.data.message
              
              console.warn({'info':error})

              this.emitWarn()
            } else {
              this.emitGenericError()
            }
          } else if (error.message == 'Network Error') {
            this.warns['backend'] = 'The backend server was not found'
              
            console.warn({'info':error})

            this.emitWarn()
          } else {
            this.emitGenericError()
          }
        })
        this.attemptToSend=false
      }
    },

    emitGenericError(error){
      this.errors = []
      this.errors.push(error)
      this.emitError()
    },

    setLastMessage() {
      this.successArray.push('Message sent')
      this.emitSuccess()
      this.lastQueue = this.queue
      this.lastMessage = this.message
      this.queue = ''
      this.message = ''
    },

    checkForm() {
      this.cleanAlerts()

      if (this.message && this.queue) {
        this.emitWarn()

        return true
      }

      if (!this.queue) {
        this.warns['queue'] = 'The queue is required.'
      }

      if (!this.message) {
        this.warns['message'] = 'The message is required.'
      }
      
      this.emitWarn()

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
      this.$emit('warns', Object.values(this.warns))
      this.warns = {}
    },

    emitInfo() {
      this.$emit('infos', this.infos)
      this.infos = []
    },

    cleanAlerts() {
      this.successArray = []
      this.errors = []
      this.infos = []
      this.warns = {}
    }
  },

  computed: {
    hasNoQueue() {
      return this.attemptToSend && !this.queue;
    },

    hasNoMessage() {
      return this.attemptToSend && !this.message
    },
  }
};
</script>