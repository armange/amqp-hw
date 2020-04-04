import axios from 'axios';

export default {
    name: 'MessageQueueService',
    getMessage(queue, success, fail) {
        axios
            .get(this.getBasePath() + queue + '/receive',
                {
                    auth: {
                        username: 'user',
                        password: '123456'
                    }
                })
            .then(success)
            .catch(fail)
    },
    sendMessage(queue, message, success, fail) {
        axios
            .post(this.getBasePath() + queue + '/send',
                message
                , {
                    headers: { 'Content-Type': 'text/plain' },
                    auth: {
                        username: 'user',
                        password: '123456'
                    }
                })
            .then(success)
            .catch(fail)
    },
    getBasePath() {
        return 'http://' + process.env.VUE_APP_MESSAGE_QUEUE_ENDPOINT_HOST + ':' + process.env.VUE_APP_MESSAGE_QUEUE_ENDPOINT_PORT + '/'
    }
}