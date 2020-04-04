<template>
    <div id="message-container" class="message-container">
        <div id="warn-alert" v-show="hasWarn" class="alert alert-warning issue-alert" role="alert">
          <p>
            <b>Please, fix the following issue(s):</b>
            <ul>
              <li v-bind:key="warn" v-for="warn in warns">{{ warn }}</li>
            </ul>
          </p>
        </div>
        <div id="error-alert" v-show="hasError" class="alert alert-danger issue-alert" role="alert">
          <p>
            <b>Please, fix the following error(s):</b>
            <ul>
              <li v-bind:key="error.message" v-for="error in errors">{{ error.message }}</li>
            </ul>
            <i>See the console log for more details.</i>
          </p>
        </div>
        <div id="info-alert" v-show="hasInfo" class="alert alert-info issue-alert" role="alert">
          <p>
            <b>Atention:</b>
            <ul>
              <li v-bind:key="info" v-for="info in infos">{{ info }}</li>
            </ul>
            <i>See the console log for more details.</i>
          </p>
        </div>
        <div id="success-alert" v-show="hasSuccess" class="alert alert-success issue-alert" role="alert">
          <p>
            <b>Success:</b>
            <ul>
              <li v-bind:key="success" v-for="success in successArray">{{ success }}</li>
            </ul>
          </p>
        </div>
        <MessageSender 
          v-on:errors="readErrors" 
          v-on:warns="readWarns" 
          v-on:infos="readInfos"
          v-on:success="readSuccessArray"/>
        <MessageReceiver 
          v-on:errors="readErrors" 
          v-on:warns="readWarns" 
          v-on:infos="readInfos"
          v-on:success="readSuccessArray"/>
    </div>
</template>

<script>
import MessageSender from './MessageSender.vue'
import MessageReceiver from './MessageReceiver.vue'

export default {
  name: 'MessageContainer',
  
  data() {
    return {
      errors: [],
      warns: [],
      infos: [],
      successArray: [],
      errorTimeout: undefined,
      warnTimeout: undefined,
      infoTimeout: undefined,
      successTimeout: undefined,
    }
  },

  components: {
    MessageSender,
    MessageReceiver,
  },

  methods: {
    readErrors(errorsParam) {
      this.hideAll();

      this.errors = errorsParam;

      if (this.hasAlert(this.errors)) {
        this.errors.forEach(element => console.error({'error' : element}));

        this.showAlert('#error-alert')

        clearTimeout(this.errorTimeout)

        this.errorTimeout = setTimeout(()=>{
          this.hideAlert('#error-alert')
          this.errors = []
        }, 5000);
      } else if (!this.hasError) {
        this.hideAlert('#error-alert')
      }
    },
    readWarns(warnsParam) {
      this.hideAll();

      this.warns = warnsParam;

      if (this.hasAlert(this.warns)) {
        this.showAlert('#warn-alert')
        
        clearTimeout(this.warnTimeout)

        this.warnTimeout = setTimeout(()=>{
          this.hideAlert('#warn-alert')
          this.warns = []
        }, 5000);
      } else if (!this.hasAlert(this.warns)) {
        this.hideAlert('#warn-alert')
      }
    },
    readInfos(infosParam) {
      this.hideAll();

      this.infos = infosParam;

      if (this.hasAlert(this.infos)) {
        this.showAlert('#info-alert')
        
        clearTimeout(this.infoTimeout)

        this.infoTimeout = setTimeout(()=>{
          this.hideAlert('#info-alert')

          this.infos = []
        }, 5000);
      } else if (!this.hasAlert(this.infos)) {
        this.hideAlert('#info-alert')
      }
    },
    readSuccessArray(successParam) {
      this.hideAll();

      this.successArray = successParam;

      if (this.hasAlert(this.successArray)) {
        this.showAlert('#success-alert')

        clearTimeout(this.successTimeout)

        this.successTimeout = setTimeout(()=>{
          this.hideAlert('#success-alert')

          this.successTimeout = []
        }, 2500);
      } else if (!this.hasAlert(this.successArray)) {
        this.hideAlert('#success-alert')
      }
    },
    showAlert(id) {
      let alertComponent = document.querySelector(id)
      if (alertComponent) alertComponent.style.display = ''
    },
    hideAll() {
      let alertComponents = document.querySelectorAll('.alert.issue-alert')
      if (alertComponents) alertComponents.forEach(item => item.style.display = 'none')
    },
    hideAlert(id) {
      let alertComponent = document.querySelector(id)
      if (alertComponent) alertComponent.style.display = 'none'
    },
    hasAlert(sourceArray) {
      return sourceArray.length
    },
    clearAlerts() {
      clearTimeout(this.alertTimeout)
      this.hideAll();
      this.errors= []
      this.warns= []
      this.infos= []
      this.successArray= []
    }
  },
  computed: {
    hasWarn() {
      return this.warns.length
    },
    hasError() {
      return this.errors.length
    },
    hasInfo() {
      return this.infos.length
    },
    hasSuccess() {
      return this.successArray.length
    }
  }
}
</script>